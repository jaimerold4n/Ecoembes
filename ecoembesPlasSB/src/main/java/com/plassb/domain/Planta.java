package com.plassb.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PLANTA")
public class Planta {
    
    @Id
    @Column(name = "ID_PLANTA", length = 50)
    private String idPlanta;
    
    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "CAPACIDAD_TOTAL", nullable = false)
    private Double capacidadTotal;
    
    @Column(name = "TIPO", length = 50)
    private String tipo;
    
    @Column(name = "UBICACION", length = 100)
    private String ubicacion;
    
    @Column(name = "ACTIVA")
    private Boolean activa;
    
    @Column(name = "FECHA_CREACION")
    private LocalDate fechaCreacion;
    
    @OneToMany(mappedBy = "planta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RegistroProcesamiento> registros = new ArrayList<>();
    
    // Constructores
    public Planta() {
        this.activa = true;
        this.fechaCreacion = LocalDate.now();
    }
    
    public Planta(String idPlanta, String nombre, Double capacidadTotal, String tipo, String ubicacion) {
        this.idPlanta = idPlanta;
        this.nombre = nombre;
        this.capacidadTotal = capacidadTotal;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
        this.activa = true;
        this.fechaCreacion = LocalDate.now();
    }
    
    // Getters y Setters
    public String getIdPlanta() {
        return idPlanta;
    }
    
    public void setIdPlanta(String idPlanta) {
        this.idPlanta = idPlanta;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public Double getCapacidadTotal() {
        return capacidadTotal;
    }
    
    public void setCapacidadTotal(Double capacidadTotal) {
        this.capacidadTotal = capacidadTotal;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getUbicacion() {
        return ubicacion;
    }
    
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
    
    public Boolean getActiva() {
        return activa;
    }
    
    public void setActiva(Boolean activa) {
        this.activa = activa;
    }
    
    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public List<RegistroProcesamiento> getRegistros() {
        return registros;
    }
    
    public void setRegistros(List<RegistroProcesamiento> registros) {
        this.registros = registros;
    }
    
    @Override
    public String toString() {
        return "Planta{" +
                "idPlanta='" + idPlanta + '\'' +
                ", nombre='" + nombre + '\'' +
                ", capacidadTotal=" + capacidadTotal +
                ", tipo='" + tipo + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", activa=" + activa +
                '}';
    }
}