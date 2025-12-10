package com.plassb.dto;

public record CapacidadPlantaDTO(
    String idPlanta,
    String nombre,
    Double capacidadTotal,
    Double capacidadDisponible,
    Double capacidadUsada,
    String tipo,
    Boolean activa
) {
    public static CapacidadPlantaDTO fromPlanta(
        com.plassb.domain.Planta planta, 
        Double capacidadUsada
    ) {
        Double disponible = planta.getCapacidadTotal() - capacidadUsada;
        
        return new CapacidadPlantaDTO(
            planta.getIdPlanta(),
            planta.getNombre(),
            planta.getCapacidadTotal(),
            disponible,
            capacidadUsada,
            planta.getTipo(),
            planta.getActiva()
        );
    }
}