package com.ecoembes.dto;

/**
 * DTO for returning the available capacity of a recycling plant.
 */
public record CapacidadPlantaDTO(
        String idPlanta,
        String nombrePlanta,
        Double toneladadCapacidadDePlanta
) {}