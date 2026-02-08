package gob.cnr.expedientes.payload.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "EXPEDIENTE_CIUDADANO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpedienteCiudadano {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_expediente")
    @SequenceGenerator(name = "seq_expediente", sequenceName = "SEQ_EXPEDIENTE", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(max = 200, message = "El nombre completo no puede exceder 200 caracteres")
    @Column(name = "nombre_completo", nullable = false, length = 200)
    private String nombreCompleto;

    @NotBlank(message = "El DUI es obligatorio")
    @Size(max = 10, message = "El DUI no puede exceder 10 caracteres")
    @Pattern(regexp = "^\\d{8}-\\d$", message = "El formato del DUI debe ser 00000000-0")
    @Column(name = "dui_documento", nullable = false, unique = true, length = 10)
    private String duiDocumento;

    @Size(max = 500, message = "La dirección no puede exceder 500 caracteres")
    @Column(name = "direccion", length = 500)
    private String direccion;

    @Size(max = 9, message = "El teléfono no puede exceder 9 caracteres")
    @Pattern(regexp = "^(\\d{4}-\\d{4})?$", message = "El formato del teléfono debe ser 0000-0000")
    @Column(name = "telefono", length = 9)
    private String telefono;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El estado del expediente es obligatorio")
    @Pattern(regexp = "^(ACTIVO|INACTIVO|EN_REVISION|ARCHIVADO)$",
            message = "El estado debe ser ACTIVO, INACTIVO, EN_REVISION o ARCHIVADO")
    @Column(name = "estado_expediente", nullable = false, length = 20)
    private String estadoExpediente;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    // --- Campos de auditoría ---

    @Column(name = "usuario_creacion", nullable = false, updatable = false, length = 100)
    private String usuarioCreacion;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "usuario_modificacion", length = 100)
    private String usuarioModificacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuario_eliminacion", length = 100)
    private String usuarioEliminacion;

    @Column(name = "fecha_eliminacion")
    private LocalDateTime fechaEliminacion;

    // --- Soft Delete ---

    @Column(name = "eliminado", nullable = false)
    @Builder.Default
    private Boolean eliminado = false;

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
        this.fechaCreacion = LocalDateTime.now();
        if (this.estadoExpediente == null) {
            this.estadoExpediente = "ACTIVO";
        }
        if (this.eliminado == null) {
            this.eliminado = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }
}
