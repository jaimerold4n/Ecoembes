package com.ecoembes.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;


public record NuevoContenedorDTO(
        @NotEmpty(message = "La localidad no puede estar vacía")
        String localidad,

        @Min(value = 1, message = "La capaciad inicial debe ser una al menos")
        Double capacidadInicial
) {}