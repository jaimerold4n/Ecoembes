package com.ecoembes.plasb.controlador;

import com.ecoembes.plasb.domain.Planta;
import com.ecoembes.plasb.dto.CapacidadPlantaDTO;
import com.ecoembes.plasb.service.ServicioPlanta;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/plantas")
public class ControladorPlanta {

    private final ServicioPlanta servicioPlanta;

    public ControladorPlanta(ServicioPlanta servicioPlanta) {
        this.servicioPlanta = servicioPlanta;
    }

    @GetMapping("/{id}/capacidad")
    public ResponseEntity<CapacidadPlantaDTO> getCapacidadPlanta(@PathVariable String id) {
        Optional<Planta> planta = servicioPlanta.getPlantaPorId(id);
        return planta.map(p -> ResponseEntity.ok(new CapacidadPlantaDTO(p.getId(), p.getCapacidad())))
                .orElse(ResponseEntity.notFound().build());
    }
}
