package com.ecoembes.repositorios;

import com.ecoembes.domain.Tarea;
import com.ecoembes.dto.HistorialAsignacionDTO;
import com.ecoembes.dto.EstadisticasAsignacionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TareaRepositorio extends JpaRepository<Tarea, Long> {
    
    @Query("SELECT t FROM Tarea t WHERE t.planta.plantaId = :plantaId")
    List<Tarea> encontrarPlantaPorId(@Param("plantaId") String plantaId);
    
    @Query("SELECT t FROM Tarea t WHERE t.fechaTarea = :fecha")
    List<Tarea> encontrarPorFechaTarea(@Param("fecha") LocalDate fecha);
    
    @Query("SELECT t FROM Tarea t WHERE t.trabajador.idEmpleado = :empleadoId")
    List<Tarea> encontrarPorIdEmpleado(@Param("empleadoId") String empleadoId);
    
    /**
     * Obtiene el historial de asignaciones con filtros opcionales
     */
    @Query("""
        SELECT new com.ecoembes.dto.HistorialAsignacionDTO(
            t.id,
            t.fechaCreacion,
            t.fechaTarea,
            t.planta.plantaId,
            t.planta.nombre,
            t.trabajador.idEmpleado,
            t.trabajador.nombre,
            t.contenedor.contenedorId,
            CAST(t.contenedor.numeroContenedor AS string),
            t.status
        )
        FROM Tarea t
        WHERE (:fechaInicio IS NULL OR t.fechaTarea >= :fechaInicio)
        AND (:fechaFin IS NULL OR t.fechaTarea <= :fechaFin)
        AND (:plantaId IS NULL OR t.planta.plantaId = :plantaId)
        AND (:trabajadorId IS NULL OR t.trabajador.idEmpleado = :trabajadorId)
        ORDER BY t.fechaTarea DESC
        """)
    List<HistorialAsignacionDTO> findHistorialAsignaciones(
        @Param("fechaInicio") LocalDate fechaInicio,
        @Param("fechaFin") LocalDate fechaFin,
        @Param("plantaId") String plantaId,
        @Param("trabajadorId") String trabajadorId
    );
    
    /**
     * Obtiene estadísticas agregadas de asignaciones por planta
     */
    @Query("""
        SELECT new com.ecoembes.dto.EstadisticasAsignacionDTO(
            t.planta.plantaId,
            t.planta.nombre,
            COUNT(t.id),
            SUM(CASE WHEN t.status = 'COMPLETADA' THEN 1 ELSE 0 END),
            SUM(CASE WHEN t.status = 'PENDIENTE' THEN 1 ELSE 0 END),
            (SUM(CASE WHEN t.status = 'COMPLETADA' THEN 1 ELSE 0 END) * 100.0 / COUNT(t.id))
        )
        FROM Tarea t
        WHERE t.fechaCreacion BETWEEN :fechaInicio AND :fechaFin
        GROUP BY t.planta.plantaId, t.planta.nombre
        ORDER BY COUNT(t.id) DESC
        """)
    List<EstadisticasAsignacionDTO> findEstadisticasAsignaciones(
        @Param("fechaInicio") LocalDate fechaInicio,
        @Param("fechaFin") LocalDate fechaFin
    );
}