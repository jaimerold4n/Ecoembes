package com.ecoembes.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tarea")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planta_id", nullable = false)
    private Planta planta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contenedor_id", nullable = false)
    private Contenedor contenedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trabajador_id", nullable = false)
    private Empleado trabajador;

    @Column(nullable = false)
    private LocalDate fechaTarea;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private String status;

    protected Tarea() {}

    public Tarea(Planta planta, Contenedor contenedor, Empleado trabajador, LocalDate fechaTarea) {
		super();
		this.planta = planta;
		this.contenedor = contenedor;
		this.trabajador = trabajador;
		this.fechaTarea = fechaTarea;
		this.fechaCreacion = LocalDateTime.now();
		this.status = "Pendiente";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Planta getPlanta() {
		return planta;
	}

	public void setPlanta(Planta planta) {
		this.planta = planta;
	}

	public Contenedor getContenedor() {
		return contenedor;
	}

	public void setContenedor(Contenedor contenedor) {
		this.contenedor = contenedor;
	}

	public Empleado getTrabajador() {
		return trabajador;
	}

	public void setTrabajador(Empleado trabajador) {
		this.trabajador = trabajador;
	}

	public LocalDate getFechaTarea() {
		return fechaTarea;
	}

	public void setFechaTarea(LocalDate fechaTarea) {
		this.fechaTarea = fechaTarea;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
