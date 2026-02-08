package gob.cnr.expedientes.payload.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpedienteResponse {

    private Long id;
    private String nombreCompleto;
    private String duiDocumento;
    private String direccion;
    private String telefono;
    private LocalDate fechaNacimiento;
    private String estadoExpediente;
    private LocalDateTime fechaRegistro;
    private String usuarioCreacion;
    private LocalDateTime fechaCreacion;
    private String usuarioModificacion;
    private LocalDateTime fechaModificacion;
}
