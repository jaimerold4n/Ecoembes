package com.ecoembes.plasb.dto;

public class CapacidadPlantaDTO {

    private String id;
    private Double capacidad;

    public CapacidadPlantaDTO(String id, Double capacidad) {
        this.id = id;
        this.capacidad = capacidad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getCapacida() {
        return capacidad;
    }

    public void setCapacidad(Double capacidad) {
        this.capacidad = capacidad;
    }
}
