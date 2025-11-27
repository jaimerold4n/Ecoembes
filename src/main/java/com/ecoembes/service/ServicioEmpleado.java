package com.ecoembes.service;

import com.ecoembes.domain.Empleado;
import com.ecoembes.dto.AuthTokenDTO;
import com.ecoembes.dto.DatosEmpleadoDTO;
import com.ecoembes.exception.LoginException;
import com.ecoembes.repositorios.EmpleadoRepositorio;
import com.ecoembes.manejoestado.ServicioManager;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Handles employee authentication and session management.
 */
@Service
public class ServicioEmpleado {

    private final ServicioManager servicioManager;
    private final EmpleadoRepositorio empleadoRepositorio;

    public ServicioEmpleado(ServicioManager servicioManager, EmpleadoRepositorio empleadoRepositorio) {
        this.servicioManager = servicioManager;
        this.empleadoRepositorio = empleadoRepositorio;
    }

    /**
     * Validates credentials and creates a new session.
     * Token is just a timestamp for simplicity.
     */
    public AuthTokenDTO login(String email, String contrasena) {
        System.out.println("Intentando iniciar sesion para obtener email: " + email);

        Empleado empleado = empleadoRepositorio.encontrarPorEmail(email)
                .orElseThrow(() -> new LoginException("Email o contraña invalidos"));

        if (!empleado.getContrasena().equals(contrasena)) {
            System.out.println("Login failed for email: " + email);
            throw new LoginException("Email o contraña invalidos");
        }

        DatosEmpleadoDTO datosEmpleado = new DatosEmpleadoDTO(
                empleado.getIdEmpleado(),
                empleado.getNombre(),
                empleado.getEmail()
        );

        long timestamp = Instant.now().toEpochMilli();
        String token = String.valueOf(timestamp);

        servicioManager.storeToken(token, datosEmpleado);

        System.out.println("Logeado correctamente para " + datosEmpleado.nombre() + ". Token creado: " + token);
        return new AuthTokenDTO(token, timestamp);
    }

    /**
     * Ends the user session by removing their token.
     */
    public void logout(String token) {
        servicioManager.removeToken(token);
        System.out.println("Logout successful for token: " + token);
    }
}