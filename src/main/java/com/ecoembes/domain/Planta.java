package com.ecoembes.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "plantas")

public class Planta {

    @Id
    private String plantaId;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Double capacidadDisponible;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = true)
    private String anfitrion;

    @Column(nullable = true)
    private int puerto;

    @Column(nullable = true)
    private String tipoPuertaDeEnlace;

    @OneToMany(mappedBy = "planta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tarea> tareas = new ArrayList<>();

    protected Planta() {}

    public Planta(String plantaId, String nombre, Double capacidadDisponible, String tipo, String tipoPuertaDeEnlace) {
        this.plantaId = plantaId;
        this.nombre = nombre;
        this.capacidadDisponible = capacidadDisponible;
        this.tipo = tipo;
        this.tipoPuertaDeEnlace = tipoPuertaDeEnlace;
    }

	public String getPlantaId() {
		return plantaId;
	}

	public void setPlantaId(String plantaId) {
		this.plantaId = plantaId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getCapacidadDisponible() {
		return capacidadDisponible;
	}

	public void setCapacidadDisponible(Double capacidadDisponible) {
		this.capacidadDisponible = capacidadDisponible;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getAnfitrion() {
		return anfitrion;
	}

	public void setAnfitrion(String anfitrion) {
		this.anfitrion = anfitrion;
	}

	public int getPuerto() {
		return puerto;
	}

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}

	public String getTipoPuertaDeEnlace() {
		return tipoPuertaDeEnlace;
	}

	public void setTipoPuerta(String tipoPuertaDeEnlace ) {
		this.tipoPuertaDeEnlace = tipoPuertaDeEnlace;
	}

	public List<Tarea> getTareas() {
		return tareas;
	}

	public void setTareas(List<Tarea> tareas) {
		this.tareas = tareas;
	}
   
}
