package com.ecoembes.dto;

public record EstadoContenedorDTO(
        String idContenedor,
        String localidad,
        String nivelDeLlenado,
        int numeroDeContenedor
) {}