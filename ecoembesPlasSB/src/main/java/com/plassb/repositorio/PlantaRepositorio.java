package com.plassb.repositorio;

import com.plassb.domain.Planta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlantaRepositorio extends JpaRepository<Planta, String> {
    
    Optional<Planta> findByIdPlanta(String idPlanta);
    
    List<Planta> findByActiva(Boolean activa);
    
    List<Planta> findByTipo(String tipo);
}