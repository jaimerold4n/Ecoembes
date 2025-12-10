package com.consocket.model;

public class ContenedorCarga {
    private String idContenedor;
    private double pesoKg;
    private String tipoMaterial;
    private String estado;


    public ContenedorCarga() {
    }


    public ContenedorCarga(String idContenedor, double pesoKg, String tipoMaterial, String estado) {
        this.idContenedor = idContenedor;
        this.pesoKg = pesoKg;
        this.tipoMaterial = tipoMaterial;
        this.estado = estado;
    }

    public String getIdContenedor() {
        return idContenedor;
    }

    public void setIdContenedor(String idContenedor) {
        this.idContenedor = idContenedor;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(double pesoKg) {
        this.pesoKg = pesoKg;
    }

    public String getTipoMaterial() {
        return tipoMaterial;
    }

    public void setTipoMaterial(String tipoMaterial) {
        this.tipoMaterial = tipoMaterial;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "ContenedorCarga{" +
                "idContenedor='" + idContenedor + '\'' +
                ", pesoKg=" + pesoKg +
                ", tipoMaterial='" + tipoMaterial + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}