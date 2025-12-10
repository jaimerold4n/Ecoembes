package com.plassb.repositorio;

import com.plassb.domain.RegistroProcesamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RegistroProcesamientoRepositorio extends JpaRepository<RegistroProcesamiento, Long> {
    
    List<RegistroProcesamiento> findByPlantaIdPlanta(String idPlanta);
    
    List<RegistroProcesamiento> findByIdContenedor(String idContenedor);
    
    List<RegistroProcesamiento> findByFechaProcesamientoBetween(
        LocalDateTime inicio, 
        LocalDateTime fin
    );
    
    @Query("SELECT SUM(r.pesoKg) FROM RegistroProcesamiento r " +
           "WHERE r.planta.idPlanta = :idPlanta " +
           "AND DATE(r.fechaProcesamiento) = :fecha")
    Double calcularPesoTotalDia(
        @Param("idPlanta") String idPlanta, 
        @Param("fecha") LocalDate fecha
    );
    
    @Query("SELECT COUNT(r) FROM RegistroProcesamiento r " +
           "WHERE r.planta.idPlanta = :idPlanta " +
           "AND DATE(r.fechaProcesamiento) = :fecha")
    Long contarContenedoresProcesadosHoy(
        @Param("idPlanta") String idPlanta, 
        @Param("fecha") LocalDate fecha
    );
}