package gob.cnr.expedientes.utils;

public final class Constants {

    private Constants() {
        // Clase de utilidad, no instanciable
    }

    // Soft delete
    public static final Boolean NO_ELIMINADO = false;
    public static final Boolean ELIMINADO = true;

    // Paginación por defecto
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;

    // Mensajes comunes
    public static final String MSG_CREATED = "Expediente creado exitosamente";
    public static final String MSG_UPDATED = "Expediente actualizado exitosamente";
    public static final String MSG_DELETED = "Expediente eliminado exitosamente";
    public static final String MSG_NOT_FOUND = "Expediente no encontrado";

    // Roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
}
