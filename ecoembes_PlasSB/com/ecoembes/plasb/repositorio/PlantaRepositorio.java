package com.ecoembes.plasb.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecoembes.plasb.domain.Planta;

public interface PlantaRepositorio extends JpaRepository<Planta, String> {
}
