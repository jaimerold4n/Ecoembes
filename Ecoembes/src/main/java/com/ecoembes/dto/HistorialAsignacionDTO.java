package com.ecoembes.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;


public record HistorialAsignacionDTO(
    Long id,
    LocalDateTime fechaCreacion,
    LocalDate fechaTarea,
    String plantaId,
    String nombrePlanta,
    String trabajadorId,
    String nombreTrabajador,
    String contenedorId,
    String numeroContenedor,
    String status
) {
}