package gob.cnr.expedientes.controller;

import gob.cnr.expedientes.payload.request.ExpedienteRequest;
import gob.cnr.expedientes.payload.response.ApiResponse;
import gob.cnr.expedientes.payload.response.ExpedienteResponse;
import gob.cnr.expedientes.service.service.ExpedienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expedientes")
@Tag(name = "Expedientes", description = "API para la gestión de expedientes de ciudadanos")
public class ExpedienteController {

    private final ExpedienteService expedienteService;

    public ExpedienteController(ExpedienteService expedienteService) {
        this.expedienteService = expedienteService;
    }

    @PostMapping
    @Operation(summary = "Crear expediente", description = "Crea un nuevo expediente de ciudadano")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Expediente creado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Ya existe un expediente con ese DUI")
    })
    public ResponseEntity<ApiResponse<ExpedienteResponse>> crear(
            @Valid @RequestBody ExpedienteRequest request) {
        ExpedienteResponse response = expedienteService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Expediente creado exitosamente", response));
    }

    @GetMapping
    @Operation(summary = "Listar expedientes paginados", description = "Obtiene una lista paginada de expedientes con filtros opcionales")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de expedientes obtenida exitosamente")
    })
    public ResponseEntity<ApiResponse<Page<ExpedienteResponse>>> obtenerPaginado(
            @Parameter(description = "Filtrar por nombre del ciudadano") @RequestParam(required = false) String nombre,
            @Parameter(description = "Filtrar por DUI del ciudadano") @RequestParam(required = false) String dui,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<ExpedienteResponse> response = expedienteService.obtenerPaginado(nombre, dui, pageable);
        return ResponseEntity.ok(ApiResponse.success("Expedientes obtenidos exitosamente", response));
    }

    @GetMapping("/todos")
    @Operation(summary = "Listar todos los expedientes", description = "Obtiene la lista completa de expedientes activos")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista completa obtenida exitosamente")
    })
    public ResponseEntity<ApiResponse<List<ExpedienteResponse>>> obtenerTodos() {
        List<ExpedienteResponse> response = expedienteService.obtenerTodos();
        return ResponseEntity.ok(ApiResponse.success("Expedientes obtenidos exitosamente", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener expediente por ID", description = "Obtiene un expediente específico por su identificador")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Expediente encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Expediente no encontrado")
    })
    public ResponseEntity<ApiResponse<ExpedienteResponse>> obtenerPorId(
            @Parameter(description = "ID del expediente", required = true) @PathVariable Long id) {
        ExpedienteResponse response = expedienteService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success("Expediente obtenido exitosamente", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar expediente", description = "Actualiza los datos de un expediente existente")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Expediente actualizado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Expediente no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Ya existe un expediente con ese DUI")
    })
    public ResponseEntity<ApiResponse<ExpedienteResponse>> actualizar(
            @Parameter(description = "ID del expediente", required = true) 
            @PathVariable Long id,
            @Valid @RequestBody ExpedienteRequest request) {
        ExpedienteResponse response = expedienteService.actualizar(id, request);
        return ResponseEntity.ok(ApiResponse.success("Expediente actualizado exitosamente", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar expediente", description = "Realiza la eliminación lógica de un expediente")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Expediente eliminado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Expediente no encontrado")
    })
    public ResponseEntity<ApiResponse<Void>> eliminar(
            @Parameter(description = "ID del expediente", required = true) @PathVariable Long id) {
        expedienteService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success("Expediente eliminado exitosamente"));
    }
}
