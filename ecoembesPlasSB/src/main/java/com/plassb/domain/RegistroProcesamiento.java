package com.plassb.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "REGISTRO_PROCESAMIENTO")
public class RegistroProcesamiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "ID_PLANTA", nullable = false)
    private Planta planta;
    
    @Column(name = "ID_CONTENEDOR", length = 50, nullable = false)
    private String idContenedor;
    
    @Column(name = "PESO_KG")
    private Double pesoKg;
    
    @Column(name = "FECHA_PROCESAMIENTO", nullable = false)
    private LocalDateTime fechaProcesamiento;
    
    @Column(name = "ESTADO", length = 50)
    private String estado;
    
    @Column(name = "OBSERVACIONES", length = 500)
    private String observaciones;

    public RegistroProcesamiento() {
        this.fechaProcesamiento = LocalDateTime.now();
        this.estado = "PROCESADO";
    }
    
    public RegistroProcesamiento(Planta planta, String idContenedor, Double pesoKg) {
        this.planta = planta;
        this.idContenedor = idContenedor;
        this.pesoKg = pesoKg;
        this.fechaProcesamiento = LocalDateTime.now();
        this.estado = "PROCESADO";
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
    
    public String getIdContenedor() {
        return idContenedor;
    }
    
    public void setIdContenedor(String idContenedor) {
        this.idContenedor = idContenedor;
    }
    
    public Double getPesoKg() {
        return pesoKg;
    }
    
    public void setPesoKg(Double pesoKg) {
        this.pesoKg = pesoKg;
    }
    
    public LocalDateTime getFechaProcesamiento() {
        return fechaProcesamiento;
    }
    
    public void setFechaProcesamiento(LocalDateTime fechaProcesamiento) {
        this.fechaProcesamiento = fechaProcesamiento;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    @Override
    public String toString() {
        return "RegistroProcesamiento{" +
                "id=" + id +
                ", idContenedor='" + idContenedor + '\'' +
                ", pesoKg=" + pesoKg +
                ", fechaProcesamiento=" + fechaProcesamiento +
                ", estado='" + estado + '\'' +
                '}';
    }
}