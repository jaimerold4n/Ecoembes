package com.ecoembes.repositorios;

import com.ecoembes.domain.Planta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantaRepositorio extends JpaRepository<Planta, String> {
}