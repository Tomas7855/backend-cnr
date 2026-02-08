package gob.cnr.expedientes.service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import gob.cnr.expedientes.payload.request.ExpedienteRequest;
import gob.cnr.expedientes.payload.response.ExpedienteResponse;

import java.util.List;

public interface ExpedienteService {

    ExpedienteResponse crear(ExpedienteRequest request);

    ExpedienteResponse obtenerPorId(Long id);

    List<ExpedienteResponse> obtenerTodos();

    Page<ExpedienteResponse> obtenerPaginado(String nombre, String dui, Pageable pageable);

    ExpedienteResponse actualizar(Long id, ExpedienteRequest request);

    void eliminar(Long id);
}
