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
    
    @Query("SELECT u FROM Uso u WHERE u.fecha BETWEEN :startDate AND :endDate ORDER BY u.fecha DESC")
    List<Uso> findByDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT u FROM Uso u WHERE u.contenedor.contenedorId = :contenedorId")
    List<Uso> findByContenedorId(@Param("contenedorId") String contenedorId);
}

