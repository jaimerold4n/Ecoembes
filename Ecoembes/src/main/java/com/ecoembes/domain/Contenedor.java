package com.ecoembes.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Contenedor")
public class Contenedor {
    @Id
    private String contenedorId;

    @Column(nullable = false)
    private String localizacion;

    @Column(nullable = false)
    private String codigoPostal;

    @Column(nullable = false)
    private Double capacidad;

    @Column(nullable = false)
    private String nivelDeLlenado;

    @Column(nullable = false)
    private Integer numeroContenedor;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "contenedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Uso> historialDeUso = new ArrayList<>();

    @OneToMany(mappedBy = "contenedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tarea> tareas = new ArrayList<>();

    protected Contenedor() {}

    public Contenedor(String contenedorId, String localizacion, String codigoPostal, Double capacidad) {
		super();
		this.contenedorId = contenedorId;
		this.localizacion = localizacion;
		this.codigoPostal = codigoPostal;
		this.capacidad = capacidad;
		this.nivelDeLlenado = "verde";
		this.numeroContenedor = 0;
		this.fechaCreacion = LocalDateTime.now();
	}

	public void actualizarEstado(String nivelDeLlenado, Integer numeroContenedor) {
        this.nivelDeLlenado = nivelDeLlenado;
        this.numeroContenedor = numeroContenedor;
    }

	public String getContenedorId() {
		return contenedorId;
	}

	public void setContenedorId(String contenedorId) {
		this.contenedorId = contenedorId;
	}

	public String getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public Double getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(Double capacidad) {
		this.capacidad = capacidad;
	}

	public String getNivelDeLlenado() {
		return nivelDeLlenado;
	}

	public void setNivelDeLlenado(String nivelDeLlenado) {
		this.nivelDeLlenado = nivelDeLlenado;
	}

	public Integer getNumeroContenedor() {
		return numeroContenedor;
	}

	public void setNumeroContenedor(Integer numeroContenedor) {
		this.numeroContenedor = numeroContenedor;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public List<Uso> getHistorialDeUso() {
		return historialDeUso;
	}
	
	public void setHistorialDeUso(List<Uso> historialDeUso) {
		this.historialDeUso = historialDeUso;
	}

	public List<Tarea> getTareas() {
		return tareas;
	}

	public void setTareas(List<Tarea> tareas) {
		this.tareas = tareas;
	}
}

