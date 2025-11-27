package com.ecoembes.dto;

import java.time.LocalDate;


public record UsoContenedorDTO(
        String idContendor,
        LocalDate fecha,
        String nivelDeLlenado,
        int cuentaDeContenedores
) {}