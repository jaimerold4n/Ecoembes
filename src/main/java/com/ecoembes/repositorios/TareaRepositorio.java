package com.ecoembes.repositorios;
import com.ecoembes.domain.Tarea;
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
}