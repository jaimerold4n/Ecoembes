package com.ecoembes.dto;


public record EstadisticasAsignacionDTO(
    String plantaId,
    String nombrePlanta,
    Long totalAsignaciones,
    Long asignacionesCompletadas,
    Long asignacionesPendientes,
    Double porcentajeCompletado
) {
}