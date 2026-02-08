package gob.cnr.expedientes.service.service;

import gob.cnr.expedientes.context.UserContext;
import gob.cnr.expedientes.payload.entities.ExpedienteCiudadano;
import gob.cnr.expedientes.payload.mapper.ExpedienteMapper;
import gob.cnr.expedientes.payload.request.ExpedienteRequest;
import gob.cnr.expedientes.payload.response.ExpedienteResponse;
import gob.cnr.expedientes.service.repository.ExpedienteRepository;
import gob.cnr.expedientes.utils.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ExpedienteServiceImpl implements ExpedienteService {

    private final ExpedienteRepository expedienteRepository;
    private final ExpedienteMapper expedienteMapper;
    private final UserContext userContext;

    public ExpedienteServiceImpl(ExpedienteRepository expedienteRepository,
                                  ExpedienteMapper expedienteMapper,
                                  UserContext userContext) {
        this.expedienteRepository = expedienteRepository;
        this.expedienteMapper = expedienteMapper;
        this.userContext = userContext;
    }

    @Override
    public ExpedienteResponse crear(ExpedienteRequest request) {
        // Validar DUI único
        if (expedienteRepository.existsByDuiDocumentoIgnoreCaseAndEliminado(
                request.getDuiDocumento(), Constants.NO_ELIMINADO)) {
            throw new IllegalArgumentException("Ya existe un expediente con el DUI: " + request.getDuiDocumento());
        }

        ExpedienteCiudadano entity = expedienteMapper.toEntity(request);
        entity.setUsuarioCreacion(userContext.getCurrentUsername());
        entity.setEliminado(Constants.NO_ELIMINADO);

        ExpedienteCiudadano guardado = expedienteRepository.save(entity);
        return expedienteMapper.toResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public ExpedienteResponse obtenerPorId(Long id) {
        ExpedienteCiudadano expediente = expedienteRepository.findByIdAndEliminado(id, Constants.NO_ELIMINADO)
                .orElseThrow(() -> new ResourceNotFoundException("Expediente no encontrado con ID: " + id));
        return expedienteMapper.toResponse(expediente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpedienteResponse> obtenerTodos() {
        List<ExpedienteCiudadano> expedientes = expedienteRepository
                .findByEliminadoOrderByIdDesc(Constants.NO_ELIMINADO);
        return expedienteMapper.toResponseList(expedientes);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExpedienteResponse> obtenerPaginado(String nombre, String dui, Pageable pageable) {
        return expedienteRepository.findWithFilters(nombre, dui, pageable)
                .map(expedienteMapper::toResponse);
    }

    @Override
    public ExpedienteResponse actualizar(Long id, ExpedienteRequest request) {
        ExpedienteCiudadano expediente = expedienteRepository.findByIdAndEliminado(id, Constants.NO_ELIMINADO)
                .orElseThrow(() -> new ResourceNotFoundException("Expediente no encontrado con ID: " + id));

        // Validar DUI único (excluyendo el actual)
        if (expedienteRepository.existsByDuiDocumentoAndNotId(request.getDuiDocumento(), id)) {
            throw new IllegalArgumentException("Ya existe otro expediente con el DUI: " + request.getDuiDocumento());
        }

        expedienteMapper.updateEntity(expediente, request);
        expediente.setUsuarioModificacion(userContext.getCurrentUsername());
        expediente.setFechaModificacion(LocalDateTime.now());

        ExpedienteCiudadano actualizado = expedienteRepository.save(expediente);
        return expedienteMapper.toResponse(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        ExpedienteCiudadano expediente = expedienteRepository.findByIdAndEliminado(id, Constants.NO_ELIMINADO)
                .orElseThrow(() -> new ResourceNotFoundException("Expediente no encontrado con ID: " + id));

        // Soft delete
        expediente.setEliminado(Constants.ELIMINADO);
        expediente.setUsuarioEliminacion(userContext.getCurrentUsername());
        expediente.setFechaEliminacion(LocalDateTime.now());

        expedienteRepository.save(expediente);
    }

    // Excepción interna
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }
}
