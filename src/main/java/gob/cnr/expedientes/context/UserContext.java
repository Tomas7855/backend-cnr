package gob.cnr.expedientes.context;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserContext {

    /**
     * Obtener el nombre de usuario autenticado actualmente
     */
    public String getCurrentUsername() {
        Authentication authentication = getAuthentication();
        return (authentication != null && authentication.isAuthenticated())
                ? authentication.getName()
                : "anonymous";
    }

    /**
     * Verificar si el usuario actual está autenticado
     */
    public boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        return authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());
    }

    /**
     * Obtener la autenticación actual del contexto de seguridad
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Verificar si el usuario actual tiene un rol específico
     */
    public boolean hasRole(String role) {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role));
    }
}
