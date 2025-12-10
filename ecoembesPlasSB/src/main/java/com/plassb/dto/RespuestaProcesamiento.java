package com.plassb.dto;

public class RespuestaProcesamiento {
    private String mensaje;
    private int contenedoresProcesados;
    private Double pesoTotal;
    private Double capacidadRestante;
    private boolean exitoso;
    
    
    public RespuestaProcesamiento() {}
    
    public RespuestaProcesamiento(String mensaje, int contenedoresProcesados, 
                                  Double pesoTotal, Double capacidadRestante, 
                                  boolean exitoso) {
        this.mensaje = mensaje;
        this.contenedoresProcesados = contenedoresProcesados;
        this.pesoTotal = pesoTotal;
        this.capacidadRestante = capacidadRestante;
        this.exitoso = exitoso;
    }
    
    public String getMensaje() {
        return mensaje;
    }
    
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    public int getContenedoresProcesados() {
        return contenedoresProcesados;
    }
    
    public void setContenedoresProcesados(int contenedoresProcesados) {
        this.contenedoresProcesados = contenedoresProcesados;
    }
    
    public Double getPesoTotal() {
        return pesoTotal;
    }
    
    public void setPesoTotal(Double pesoTotal) {
        this.pesoTotal = pesoTotal;
    }
    
    public Double getCapacidadRestante() {
        return capacidadRestante;
    }
    
    public void setCapacidadRestante(Double capacidadRestante) {
        this.capacidadRestante = capacidadRestante;
    }
    
    public boolean isExitoso() {
        return exitoso;
    }
    
    public void setExitoso(boolean exitoso) {
        this.exitoso = exitoso;
    }
}