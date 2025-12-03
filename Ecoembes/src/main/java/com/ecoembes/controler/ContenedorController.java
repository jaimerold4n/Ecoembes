package com.ecoembes.controler;

import com.ecoembes.dto.*;
import com.ecoembes.exception.InvalidTokenException;
import com.ecoembes.service.ServicioContenedor;
import com.ecoembes.manejoestado.ServicioManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para gestión de contenedores
 */
@RestController
@RequestMapping("/api/v1/contenedores")
@Tag(name = "Contenedores", description = "Endpoints para gestión de contenedores de reciclaje")
public class ContenedorController {

    private final ServicioContenedor servicioContenedor;
    private final ServicioManager sesionManager;

    public ContenedorController(ServicioContenedor servicioContenedor, ServicioManager sesionManager) {
        this.servicioContenedor = servicioContenedor;
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

    @Operation(summary = "Crear nuevo contenedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contenedor creado correctamente", 
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstadoContenedorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Token inválido")
    })
    @PostMapping
    public ResponseEntity<EstadoContenedorDTO> crearContenedor(
            @Parameter(description = "Token de sesión recibido en el login") 
            @RequestHeader("Autorizacion") String token,
            @Valid @RequestBody NuevoContenedorDTO nuevoContenedor
    ) {
        validate(token);
        EstadoContenedorDTO contenedorCreado = servicioContenedor.crearNuevoContenedor(
                nuevoContenedor.localidad(), 
                nuevoContenedor.capacidadInicial()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(contenedorCreado);
    }

    @Operation(summary = "Actualizar contenedor existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenedor actualizado correctamente", 
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstadoContenedorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Token inválido"),
            @ApiResponse(responseCode = "404", description = "Contenedor no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EstadoContenedorDTO> actualizarContenedor(
            @Parameter(description = "Token de sesión recibido en el login") 
            @RequestHeader("Autorizacion") String token,
            @Parameter(description = "ID del contenedor", required = true) 
            @PathVariable String id,
            @Valid @RequestBody ActualizarContenedorDTO actualizarDatos
    ) {
        validate(token);
        EstadoContenedorDTO contenedorActualizado = servicioContenedor.actualizarEstadoContenedor(
                id,
                actualizarDatos.nivelDeLlenado(),
                actualizarDatos.numeroContenedor()
        );
        return ResponseEntity.ok(contenedorActualizado);
    }

    @Operation(summary = "Obtener estado de contenedores en un área específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado de contenedores recuperado correctamente"),
            @ApiResponse(responseCode = "401", description = "Token inválido")
    })
    @GetMapping("/estado")
    public ResponseEntity<List<EstadoContenedorDTO>> obtenerEstadoContenedores(
            @Parameter(description = "Token de sesión recibido en el login") 
            @RequestHeader("Autorizacion") String token,
            @Parameter(description = "Código postal del área", required = true) 
            @RequestParam String codigoPostal,
            @Parameter(description = "Fecha del estado (YYYY-MM-DD)", required = true) 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
        validate(token);
        List<EstadoContenedorDTO> listaEstados = servicioContenedor.getEstadoContenedor(codigoPostal, fecha);
        return ResponseEntity.ok(listaEstados);
    }

    @Operation(summary = "Consultar el uso de contenedores durante un período de tiempo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uso de contenedores recuperado correctamente"),
            @ApiResponse(responseCode = "401", description = "Token inválido")
    })
    @GetMapping("/uso")
    public ResponseEntity<List<UsoContenedorDTO>> obtenerUsoContenedores(
            @Parameter(description = "Token de sesión recibido en el login") 
            @RequestHeader("Autorizacion") String token,
            @Parameter(description = "Fecha de inicio del período (YYYY-MM-DD)", required = true) 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @Parameter(description = "Fecha fin del período (YYYY-MM-DD)", required = true) 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        validate(token);
        List<UsoContenedorDTO> listaUsos = servicioContenedor.consultaUsoContenedor(fechaInicio, fechaFin);
        return ResponseEntity.ok(listaUsos);
    }
}