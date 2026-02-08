package gob.cnr.expedientes.service.repository;

import gob.cnr.expedientes.payload.entities.ExpedienteCiudadano;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpedienteRepository extends JpaRepository<ExpedienteCiudadano, Long> {

    /**
     * Buscar expedientes no eliminados ordenados por ID descendente
     */
    List<ExpedienteCiudadano> findByEliminadoOrderByIdDesc(Boolean eliminado);

    /**
     * Buscar expediente no eliminado por ID
     */
    Optional<ExpedienteCiudadano> findByIdAndEliminado(Long id, Boolean eliminado);

    /**
     * Búsqueda paginada con filtros opcionales (solo no eliminados)
     */
    @Query("SELECT e FROM ExpedienteCiudadano e WHERE e.eliminado = false " +
            "AND (:nombre IS NULL OR UPPER(e.nombreCompleto) LIKE UPPER(CONCAT('%', :nombre, '%'))) " +
            "AND (:dui IS NULL OR e.duiDocumento LIKE CONCAT('%', :dui, '%'))")
    Page<ExpedienteCiudadano> findWithFilters(
            @Param("nombre") String nombre,
            @Param("dui") String dui,
            Pageable pageable);

    /**
     * Verificar si existe expediente con DUI (solo no eliminados)
     */
    boolean existsByDuiDocumentoIgnoreCaseAndEliminado(String duiDocumento, Boolean eliminado);

    /**
     * Verificar si existe expediente con DUI excluyendo un ID específico
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM ExpedienteCiudadano e WHERE UPPER(e.duiDocumento) = UPPER(:dui) " +
            "AND e.eliminado = false AND e.id != :id")
    boolean existsByDuiDocumentoAndNotId(@Param("dui") String duiDocumento, @Param("id") Long id);
}
