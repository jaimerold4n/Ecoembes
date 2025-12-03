package com.ecoembes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

/**
 * DTO for receiving login credentials.
 */
public record CredencialLoginDTO(
        @NotEmpty(message = "El email no puede estar vacío")
        @Email(message = "El email debe ser válido")
        String email,

        @NotEmpty(message = "La contraseña no puede estar vacía")
        String contrasena
) {}