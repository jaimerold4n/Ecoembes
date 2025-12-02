package com.ecoembes.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ActualizarContenedorDTO(
        @NotBlank(message = "El nivel de llenado no puede estar vacío")
        String nivelDeLlenado,

        @NotNull(message = "El número del contenedor es obligatorio")
        @Min(value = 0, message = "El número no puede ser negativo")
        Integer numeroContenedor
) {}


