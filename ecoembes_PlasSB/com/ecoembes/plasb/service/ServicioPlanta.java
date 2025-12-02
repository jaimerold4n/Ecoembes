package com.ecoembes.plasb.service;

import com.ecoembes.plasb.domain.Planta;
import com.ecoembes.plasb.repositorio.PlantaRepositorio;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServicioPlanta {

    private final PlantaRepositorio plantaRepositorio;

    public ServicioPlanta(PlantaRepositorio plantaRepositorio) {
        this.plantaRepositorio = plantaRepositorio;
    }

    public Optional<Planta> getPlantaPorId(String id) {
        return plantaRepositorio.findById(id);
    }
}
