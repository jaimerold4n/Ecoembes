package com.consocket.model;

public class PlantaInfo {
    private String idPlanta;
    private String nombre;
    private double capacidadTotal;
    private String tipo;
    private String ubicacion;
    private boolean activa;


    public PlantaInfo(String idPlanta, String nombre, double capacidadTotal, String tipo, String ubicacion) {
        this.idPlanta = idPlanta;
        this.nombre = nombre;
        this.capacidadTotal = capacidadTotal;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
        this.activa = true;
    }


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

    public double getCapacidadTotal() {
        return capacidadTotal;
    }

    public void setCapacidadTotal(double capacidadTotal) {
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

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    @Override
    public String toString() {
        return "PlantaInfo{" +
                "idPlanta='" + idPlanta + '\'' +
                ", nombre='" + nombre + '\'' +
                ", capacidadTotal=" + capacidadTotal +
                ", tipo='" + tipo + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", activa=" + activa +
                '}';
    }
}