package com.ecoembes.repositorios;


import com.ecoembes.domain.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TareaRespositorio extends JpaRepository<Tarea, Long> {
    List<Tarea> encontrarPlantaPorId(String plantaId);
    List<Tarea> encontrarPorFechaTarea(LocalDate fecha);
    List<Tarea> encontrarPorIdEmpleado(String idEmpleado);
}
