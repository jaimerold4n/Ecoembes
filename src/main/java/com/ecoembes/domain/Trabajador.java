package com.ecoembes.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "trabajadores")
public class Trabajador {
	
	@Id
	private String trabajadorID;

	@Column(nullable = false)
	private String nombre;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String contrasena;

	protected Trabajador() {}

	public Trabajador(String trabajadorID, String nombre, String email, String contrasena) {
		super();
		this.trabajadorID = trabajadorID;
		this.nombre = nombre;
		this.email = email;
		this.contrasena = contrasena;
	}

	public String getTrabajadorID() {
		return trabajadorID;
	}

	public void setTrabajadorID(String trabajadorID) {
		this.trabajadorID = trabajadorID;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
}
