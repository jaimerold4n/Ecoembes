package com.ecoembes.controler;

import com.ecoembes.dto.AuthTokenDTO;
import com.ecoembes.dto.CredencialLoginDTO;
import com.ecoembes.dto.DatosEmpleadoDTO;
import com.ecoembes.exception.InvalidTokenException;
import com.ecoembes.service.ServicioEmpleado;
import com.ecoembes.manejoestado.ServicioManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para autenticación de empleados
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Autenticación", description = "Endpoints para login y logout de empleados")
public class AuthController {

    private final ServicioEmpleado servicioEmpleado;
    private final ServicioManager sesionManager;

    public AuthController(ServicioEmpleado servicioEmpleado, ServicioManager sesionManager) {
        this.servicioEmpleado = servicioEmpleado;
        this.sesionManager = sesionManager;
    }

    /**
     * Valida el token y retorna los datos del empleado si es válido.
     * Lanza excepción si el token es inválido o expirado.
     */
    private DatosEmpleadoDTO validate(String token) {
        if (!sesionManager.validateToken(token)) {
            throw new InvalidTokenException("Token inválido");
        }
        DatosEmpleadoDTO datosEmpleado = sesionManager.obtenerDatosEmpleado(token);
        if (datosEmpleado == null) {
            throw new InvalidTokenException("El token es válido pero no se han encontrado datos del empleado.");
        }
        return datosEmpleado;
    }

    @Operation(summary = "Iniciar sesión de empleado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logeado correctamente", 
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthTokenDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales no válidas", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<AuthTokenDTO> login(@Valid @RequestBody CredencialLoginDTO credenciales) {
        AuthTokenDTO authToken = servicioEmpleado.login(credenciales.email(), credenciales.contrasena());
        return ResponseEntity.ok(authToken);
    }

    @Operation(summary = "Cerrar sesión de empleado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deslogueado correctamente"),
            @ApiResponse(responseCode = "401", description = "Token inválido")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Parameter(description = "Token de sesión recibido en el login") 
            @RequestHeader("Autorizacion") String token
    ) {
        validate(token);
        servicioEmpleado.logout(token);
        return ResponseEntity.ok().build();
    }
}