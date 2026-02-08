package gob.cnr.expedientes.payload.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpedienteRequest {

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(max = 200, message = "El nombre completo no puede exceder 200 caracteres")
    private String nombreCompleto;

    @NotBlank(message = "El DUI es obligatorio")
    @Pattern(regexp = "^\\d{8}-\\d$", message = "El formato del DUI debe ser 00000000-0")
    private String duiDocumento;

    @Size(max = 500, message = "La dirección no puede exceder 500 caracteres")
    private String direccion;

    @Size(max = 9, message = "El teléfono no puede exceder 9 caracteres")
    @Pattern(regexp = "^(\\d{4}-\\d{4})?$", message = "El formato del teléfono debe ser 0000-0000")
    private String telefono;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El estado del expediente es obligatorio")
    @Pattern(regexp = "^(ACTIVO|INACTIVO|EN_REVISION|ARCHIVADO)$",
            message = "El estado debe ser ACTIVO, INACTIVO, EN_REVISION o ARCHIVADO")
    private String estadoExpediente;
}
