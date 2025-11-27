package com.ecoembes.repositorios;

import com.ecoembes.domain.Uso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UsoRepositorio extends JpaRepository<Uso, Long> {

    @Query("SELECT u FROM Usage u WHERE u.date BETWEEN :startDate AND :endDate ORDER BY u.date DESC")
    List<Uso> findByDateBetween(@Param("fechaInicio") LocalDate startDate, @Param("fechaFin") LocalDate endDate);

    List<Uso> findByDumpsterDumpsterId(String dumpsterId);
}

