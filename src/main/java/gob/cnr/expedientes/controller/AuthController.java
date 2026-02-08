package gob.cnr.expedientes.controller;

import gob.cnr.expedientes.payload.request.LoginRequest;
import gob.cnr.expedientes.payload.response.ApiResponse;
import gob.cnr.expedientes.payload.response.LoginResponse;
import gob.cnr.expedientes.security.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "API para autenticación de usuarios")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario con sus credenciales")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login exitoso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generar JWT
        String jwt = jwtUtils.generateJwtToken(authentication);

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        LoginResponse loginResponse = new LoginResponse(
                jwt,
                authentication.getName(),
                roles,
                true
        );

        return ResponseEntity.ok(ApiResponse.success("Login exitoso", loginResponse));
    }

    @Operation(summary = "Cerrar sesión", description = "Cierra la sesión del usuario actual")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(ApiResponse.success("Logout exitoso"));
    }

    @Operation(summary = "Obtener usuario actual", description = "Obtiene la información del usuario autenticado")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<LoginResponse>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.ok(ApiResponse.error("No autenticado"));
        }

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        LoginResponse response = new LoginResponse(
                null,
                authentication.getName(),
                roles,
                true
        );

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
