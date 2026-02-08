package gob.cnr.expedientes.payload.mapper;

import gob.cnr.expedientes.payload.entities.ExpedienteCiudadano;
import gob.cnr.expedientes.payload.request.ExpedienteRequest;
import gob.cnr.expedientes.payload.response.ExpedienteResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExpedienteMapper {

    /**
     * Convierte entidad a response
     */
    ExpedienteResponse toResponse(ExpedienteCiudadano entity);

    /**
     * Convierte lista de entidades a lista de responses
     */
    List<ExpedienteResponse> toResponseList(List<ExpedienteCiudadano> entities);

    /**
     * Convierte request a entidad (para crear)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "usuarioCreacion", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "usuarioModificacion", ignore = true)
    @Mapping(target = "fechaModificacion", ignore = true)
    @Mapping(target = "usuarioEliminacion", ignore = true)
    @Mapping(target = "fechaEliminacion", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    ExpedienteCiudadano toEntity(ExpedienteRequest request);

    /**
     * Actualiza entidad existente con datos del request
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "usuarioCreacion", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "usuarioModificacion", ignore = true)
    @Mapping(target = "fechaModificacion", ignore = true)
    @Mapping(target = "usuarioEliminacion", ignore = true)
    @Mapping(target = "fechaEliminacion", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    void updateEntity(@MappingTarget ExpedienteCiudadano entity, ExpedienteRequest request);
}
