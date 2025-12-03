package com.ecoembes.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Historial-uso")
public class Uso {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contenedor_id", nullable = false)
    private Contenedor contenedor;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private String nivelDeLlenado;

    @Column(nullable = false)
    private Integer numeroDeContenedores;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    protected Uso() {}

    public Uso(Contenedor contenedor, LocalDate fecha, String nivelDeLlenado, Integer numeroDeContenedor) {
		super();
		this.contenedor = contenedor;
		this.fecha = fecha;
		this.nivelDeLlenado = nivelDeLlenado;
		this.numeroDeContenedores = numeroDeContenedor;
		this.fechaRegistro = LocalDateTime.now();
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public Contenedor getContenedor() {
		return contenedor;
	}

	public void setContenedor(Contenedor contenedor) {
		this.contenedor = contenedor;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getNivelDeLlenado() {
		return nivelDeLlenado;
	}

	public void setNivelDeLlenado(String nivelDeLlenado) {
		this.nivelDeLlenado = nivelDeLlenado;
	}

	public Integer getNumeroDeContenedores() {
		return numeroDeContenedores;
	}

	public void setNumeroDeContenedores(Integer numeroDeContenedor) {
		this.numeroDeContenedores = numeroDeContenedor;
	}

	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
}
