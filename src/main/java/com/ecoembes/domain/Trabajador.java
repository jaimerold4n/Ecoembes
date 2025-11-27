package com.ecoembes.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "empleados")
public class Empleado {
	
	@Id
	private String IdEmpleado;

	@Column(nullable = false)
	private String nombre;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String contrasena;

	protected Empleado() {}

	public Empleado(String IdEmpleado, String nombre, String email, String contrasena) {
		super();
		this.IdEmpleado = IdEmpleado;
		this.nombre = nombre;
		this.email = email;
		this.contrasena = contrasena;
	}

	public String getIdEmpleado() {
		return IdEmpleado;
	}

	public void setEmpleadoID(String IdEmpleado) {
		this.IdEmpleado = IdEmpleado;
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
