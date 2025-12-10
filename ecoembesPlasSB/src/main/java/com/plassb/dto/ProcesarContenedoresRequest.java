package com.plassb.dto;

import java.util.List;

public class ProcesarContenedoresRequest {

    private String idPlanta;
    private List<String> idsContenedores;

    // Getters y Setters
    public String getIdPlanta() {
        return idPlanta;
    }

    public void setIdPlanta(String idPlanta) {
        this.idPlanta = idPlanta;
    }

    public List<String> getIdsContenedores() {
        return idsContenedores;
    }

    public void setIdsContenedores(List<String> idsContenedores) {
        this.idsContenedores = idsContenedores;
    }
}
