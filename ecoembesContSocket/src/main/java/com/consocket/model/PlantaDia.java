package com.consocket.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlantaDia {
    private String idPlanta;
    private LocalDate fecha;
    private List<String> contenedoresProcesados;
    private double capacidadUsada;
    private double capacidadTotal;

    // Constructor
    public PlantaDia(String idPlanta, LocalDate fecha, double capacidadTotal) {
        this.idPlanta = idPlanta;
        this.fecha = fecha;
        this.capacidadTotal = capacidadTotal;
        this.capacidadUsada = 0.0;
        this.contenedoresProcesados = new ArrayList<>();
    }

    // Método para agregar contenedor procesado
    public boolean agregarContenedor(String idContenedor, double peso) {
        if (capacidadUsada + peso <= capacidadTotal) {
            contenedoresProcesados.add(idContenedor);
            capacidadUsada += peso;
            return true;
        }
        return false;
    }

    // Getters y Setters
    public String getIdPlanta() {
        return idPlanta;
    }

    public void setIdPlanta(String idPlanta) {
        this.idPlanta = idPlanta;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public List<String> getContenedoresProcesados() {
        return contenedoresProcesados;
    }

    public void setContenedoresProcesados(List<String> contenedoresProcesados) {
        this.contenedoresProcesados = contenedoresProcesados;
    }

    public double getCapacidadUsada() {
        return capacidadUsada;
    }

    public void setCapacidadUsada(double capacidadUsada) {
        this.capacidadUsada = capacidadUsada;
    }

    public double getCapacidadTotal() {
        return capacidadTotal;
    }

    public void setCapacidadTotal(double capacidadTotal) {
        this.capacidadTotal = capacidadTotal;
    }

    public double getCapacidadDisponible() {
        return capacidadTotal - capacidadUsada;
    }

    @Override
    public String toString() {
        return "PlantaDia{" +
                "idPlanta='" + idPlanta + '\'' +
                ", fecha=" + fecha +
                ", contenedoresProcesados=" + contenedoresProcesados.size() +
                ", capacidadUsada=" + capacidadUsada +
                ", capacidadTotal=" + capacidadTotal +
                '}';
    }
}