package com.ecoembes.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

public record AsignarContenedorDTO(
        @NotEmpty(message = "El id de la planta no puede estar vacío")
        String idPlanta,

        @NotEmpty(message = "Tiene que ser añadido el id de un contenedor al menos")
        List<String> idContenedores,

        @FutureOrPresent(message = "La fecha de la tarea debe ser hoy o en un futuro")
        LocalDate fecha
) {}