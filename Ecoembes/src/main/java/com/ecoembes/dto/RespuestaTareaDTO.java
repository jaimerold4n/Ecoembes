package com.ecoembes.dto;

import java.util.List;

public record RespuestaTareaDTO(
        String empleadoId,
        String nombreEmpleado,
        String plantaId,
        List<String> idsContenedor,
        String fechaTarea,
        String estado
) {}