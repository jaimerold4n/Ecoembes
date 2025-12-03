package com.ecoembes.controler;

import com.ecoembes.dto.*;
import com.ecoembes.exception.InvalidTokenException;
import com.ecoembes.service.ServicioPlanta;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para gestión de plantas de reciclaje
 */
@RestController
@RequestMapping("/api/v1/plantas")
@Tag(name = "Plantas", description = "Endpoints para gestión de plantas de reciclaje")
public class PlantaController {

    private final ServicioPlanta servicioPlanta;
    private final ServicioManager sesionManager;

    public PlantaController(ServicioPlanta servicioPlanta, ServicioManager sesionManager) {
        this.servicioPlanta = servicioPlanta;
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

    @Operation(summary = "Obtener todas las plantas de reciclaje")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todas las plantas han sido recuperadas correctamente"),
            @ApiResponse(responseCode = "401", description = "Token inválido")
    })
    @GetMapping
    public ResponseEntity<List<CapacidadPlantaDTO>> obtenerTodasLasPlantas(
            @Parameter(description = "Token de sesión recibido en el login") 
            @RequestHeader("Autorizacion") String token
    ) {
        validate(token);
        List<CapacidadPlantaDTO> plantas = servicioPlanta.getTodasLasPlantas();
        return ResponseEntity.ok(plantas);
    }

    @Operation(summary = "Obtener la capacidad disponible de las plantas de reciclaje")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Capacidad de las plantas recibida correctamente"),
            @ApiResponse(responseCode = "401", description = "Token inválido"),
            @ApiResponse(responseCode = "404", description = "Planta no encontrada")
    })
    @GetMapping("/capacidad")
    public ResponseEntity<List<CapacidadPlantaDTO>> obtenerCapacidadPlantas(
            @Parameter(description = "Token de sesión recibido en el login") 
            @RequestHeader("Autorizacion") String token,
            @Parameter(description = "Fecha para obtener la capacidad (YYYY-MM-DD)", required = true) 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @Parameter(description = "ID de planta opcional para filtrar planta específica") 
            @RequestParam(required = false) String IdPlanta
    ) {
        validate(token);
        List<CapacidadPlantaDTO> listaCapacidades = servicioPlanta.getCapacidadPlantaPorFecha(fecha, IdPlanta);
        return ResponseEntity.ok(listaCapacidades);
    }

    @Operation(summary = "Obtener la capacidad disponible de una planta específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Capacidad de la planta obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "Token inválido"),
            @ApiResponse(responseCode = "404", description = "Planta no encontrada")
    })
    @GetMapping("/{plantaId}/capacidad")
    public ResponseEntity<Double> obtenerCapacidadPlanta(
            @Parameter(description = "Token de sesión recibido en el login") 
            @RequestHeader("Autorizacion") String token,
            @Parameter(description = "ID de la planta", required = true) 
            @PathVariable String plantaId
    ) throws Exception {
        validate(token);
        Double capacidad = servicioPlanta.getCapacidadPlanta(plantaId);
        return ResponseEntity.ok(capacidad);
    }

    @Operation(summary = "Asignar uno o más contenedores a una planta de reciclaje")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenedores asignados correctamente", 
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = RespuestaTareaDTO.class))),
            @ApiResponse(responseCode = "401", description = "Token inválido")
    })
    @PostMapping("/asignacion")
    public ResponseEntity<RespuestaTareaDTO> asignarContenedoresAPlanta(
            @Parameter(description = "Token de sesión recibido en el login") 
            @RequestHeader("Autorizacion") String token,
            @Valid @RequestBody AsignarContenedorDTO asignacion
    ) {
        DatosEmpleadoDTO datosEmpleado = validate(token);
        RespuestaTareaDTO respuesta = servicioPlanta.asignarContenedores(
                datosEmpleado.idEmpleado(),
                asignacion.idPlanta(),
                asignacion.idContenedores()
        );
        return ResponseEntity.ok(respuesta);
    }
}