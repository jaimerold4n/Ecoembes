package com.ecoembes.plasb.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Planta {

    @Id
    private String id;
    private Double capacidad;

    public Planta() {
    }

    public Planta(String id, Double capacidad) {
        this.id = id;
        this.capacidad = capacidad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Double capacidad) {
        this.capacidad = capacidad;
    }
}
