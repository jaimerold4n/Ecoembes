package com.ecoembes.manejoestado;

import com.ecoembes.dto.DatosEmpleadoDTO;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory session storage for logged-in users.
 * Thread-safe using ConcurrentHashMap.
 * Note: All sessions lost on server restart.
 */
@Component
public class ServicioManager {

    private final Map<String, DatosEmpleadoDTO> sesionesActivas = new ConcurrentHashMap<>();

    /**
     * Saves a new session token with employee info.
     */
    public void tokenAlmacenado(String token, DatosEmpleadoDTO employee) {
        System.out.println("Almacenamiento de tokens para empleados: " + employee.email());
        sesionesActivas.put(token, employee);
    }

    /**
     * Removes a session token (logout).
     */
    public void removeToken(String token) {
        System.out.println("Quitando token: " + token);
        sesionesActivas.remove(token);
    }

    /**
     * Checks if a token is valid (exists in active sessions).
     */
    public boolean validateToken(String token) {
        boolean esValido = sesionesActivas.containsKey(token);
        System.out.println("Validando token " + token + ": " + esValido);
        return esValido;
    }

    /**
     * Gets employee data for a valid token.
     * Returns null if token doesn't exist.
     */
    public DatosEmpleadoDTO obtenerDatosEmpleado(String token) {
        return sesionesActivas.get(token);
    }
}