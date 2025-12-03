package com.ecoembes.dto;

/**
 * DTO for storing essential employee data in the session.
 */
public record DatosEmpleadoDTO(
        String idEmpleado,
        String nombre,
        String email
) {}