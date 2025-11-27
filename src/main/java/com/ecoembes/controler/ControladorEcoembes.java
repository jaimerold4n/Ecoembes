package com.ecoembes.controler;

import com.ecoembes.dto.*;
import com.ecoembes.exception.InvalidTokenException;
import com.ecoembes.service.ServicioContenedor;
import com.ecoembes.service.ServicioEmpleado;
import com.ecoembes.service.ServicioPlanta;
import com.ecoembes.service.ServicioContenedor;
import com.ecoembes.service.ServicioEmpleado;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Main API controller - routes all requests to appropriate services.
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Ecoembes API", description = "Main API para Servidor Ecoembres ")
public class ControladorEcoembes {

    private final ServicioEmpleado servicioEmpleado;
    private final ServicioContenedor servicioContenedor;
    private final ServicioPlanta servicioPlanta;
    private final ServicioManager sesionManager;

    public ControladorEcoembes(ServicioEmpleado servicioEmpleado, ServicioContenedor servicioContenedor, ServicioPlanta servicioPlanta, ServicioManager sesionManager) {
        this.servicioEmpleado = servicioEmpleado;
        this.servicioContenedor = servicioContenedor;
        this.servicioPlanta = servicioPlanta;
        this.sesionManager = sesionManager;
    }

    /**
     * Validates token and returns employee data if valid.
     * Throws exception if token is invalid/expired.
     */
    private DatosEmpleadoDTO validate(String token) {
        if (!sesionManager.validateToken(token)) {
            throw new InvalidTokenException("Token inválido");
        }
        DatosEmpleadoDTO datosEmpleado = sesionManager.obtenerDatosEmpleado(token);
        if (datosEmpleado == null) {
            throw new InvalidTokenException("El token es valido pero no se han encontrado datos del empleado.");
        }
        return datosEmpleado;
    }

    // --- Employee & Session Endpoints ---

    @Operation(summary = "Logeando empleado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logeado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthTokenDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales no válidas", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<AuthTokenDTO> login(@Valid @RequestBody CredencialLoginDTO credenciales) {
        AuthTokenDTO authToken = servicioEmpleado.login(credenciales.email(), credenciales.contrasena());
        return ResponseEntity.ok(authToken);
    }

    @Operation(summary = "Deslogueando empleado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deslogueado correctamente"),
            @ApiResponse(responseCode = "401", description = "Token inválido")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Parameter(description = "Sesion del token recibien en el logout") @RequestHeader("Autorización") String token) {
        validate(token);
        servicioEmpleado.logout(token);
        return ResponseEntity.ok().build();
    }

    // --- Dumpster Endpoints ---

    @Operation(summary = "Crear nuevo contenedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contenedot creado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstadoContenedorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Token inválido")
    })
    @PostMapping("/contenedores")
    public ResponseEntity<EstadoContenedorDTO> createNewDumpster(
            @Parameter(description = "Sesion del token recibida en el login") @RequestHeader("Autorización") String token,
            @Valid @RequestBody NuevoContenedorDTO nuevoContenedor
    ) {
        validate(token);
        EstadoContenedorDTO contenedorCreado = servicioContenedor.crearNuevoContenedor(nuevoContenedor.localidad(), nuevoContenedor.capacidadInicial());
        return ResponseEntity.status(HttpStatus.CREATED).body(contenedorCreado);
    }

    @Operation(summary = "Actualizar contenedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenedor actualizado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstadoContenedorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Token inválido"),
            @ApiResponse(responseCode = "404", description = "Conetenedor no encontrado")
    })
    @PutMapping("/dumpsters/{id}")
    public ResponseEntity<EstadoContenedorDTO> updateDumpster(
            @Parameter(description = "Sesion del token recivida en el login") @RequestHeader("Autorización") String token,
            @Parameter(description = "ID Contenedor", required = true) @PathVariable String id,
            @Valid @RequestBody ActualizarContenedorDTO actualizarDatos
    ) {
        validate(token);
        EstadoContenedorDTO actualizarContenedor = servicioContenedor.actualizarEstadoContenedor(
                id,
                actualizarDatos.nivelDeLlenado(),
                actualizarDatos.numeroContenedor()
        );
        return ResponseEntity.ok(actualizarContenedor);
    }

    @Operation(summary = "Obtener estado del contenedor en un área específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El estado del contenedor ha sido recuperado correctamente"),
            @ApiResponse(responseCode = "401", description = "Token inválido")
    })
    @GetMapping("/contenedores/estado")
    public ResponseEntity<List<EstadoContenedorDTO>> getDumpsterStatus(
            @Parameter(description = "Sesion del token recivida en el login") @RequestHeader("Autorización") String token,
            @Parameter(description = "Mirar el código postal de la área", required = true) @RequestParam String codigoPostal,
            @Parameter(description = "Mirar la fecha del estado para (YYYY-MM-DD)", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
        validate(token);
        List<EstadoContenedorDTO> listaEstados = servicioContenedor.getEstadoContenedor(codigoPostal, fecha);
        return ResponseEntity.ok(listaEstados);
    }

    @Operation(summary = "\r\n"
    		+ "Consultar el uso del contenedor de basura durante un período de tiempo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El uso del contenedor ha sido recuperado correctamente"),
            @ApiResponse(responseCode = "401", description = "Token inválido")
    })
    @GetMapping("/contenedores/uso")
    public ResponseEntity<List<UsoContenedorDTO>> obtenerUsoContenedor(
            @Parameter(description = "Sesion del token recibida en el login") @RequestHeader("Autorización") String token,
            @Parameter(description = "Fecha de inicio de la obtención (YYYY-MM-DD)", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @Parameter(description = "Fecha fin de la obtención (YYYY-MM-DD)", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        validate(token);
        List<UsoContenedorDTO> listUsos = servicioContenedor.getUsoContenedor(fechaInicio, fechaFin);
        return ResponseEntity.ok(listUsos);
    }

    // --- Recycling Plant Endpoints ---

    @Operation(summary = "Obteneer todas las plantas de reciclaje")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todas la plantas han sido recuperadas correctamente"),
            @ApiResponse(responseCode = "401", description = "Token inválido")
    })
    @GetMapping("/plantas")
    public ResponseEntity<List<CapacidadPlantaDTO>> obtenerTodasLasPlantas(
            @Parameter(description = "Sesion del token recibida en el login") @RequestHeader("Autorización") String token
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
    @GetMapping("/plants/capacity")
    public ResponseEntity<List<CapacidadPlantaDTO>> obtenerCapacidadPlanta(
            @Parameter(description = "Sesion del token recibida en el login") @RequestHeader("Autorización") String token,
            @Parameter(description = "Fecha para obtener la capacidad (YYYY-MM-DD)", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @Parameter(description = "Opcional ID de planta para filtrar planta específica") @RequestParam(required = false) String IdPlanta
    ) {
        validate(token);
        List<CapacidadPlantaDTO> capacityList = servicioPlanta.getCapacidadPlantaPorFecha(fecha, IdPlanta);
        return ResponseEntity.ok(capacityList);
    }

    @Operation(summary = "Obtener la capacidad disponible de una planta en específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cpacidad de la planta obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "Token inválido"),
            @ApiResponse(responseCode = "404", description = "Planta no encontrada")
    })
    @GetMapping("/plantas/{IdPlanta}/capacidad")
    public ResponseEntity<Double> getPlantCapacity(
            @Parameter(description = "Sesion del token recibida en el login") @RequestHeader("Autorización") String token,
            @Parameter(description = "Obtener Id Planta para obtener capacidad de ", required = true) @PathVariable String plantaID
    ) throws Exception {
        validate(token);
        Double capacidad = servicioPlanta.getCapacidadPlanta(plantaID);
        return ResponseEntity.ok(capacidad);
    }

    @Operation(summary = "Asignar uno o más contenedores a una planta de reciclaje")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenedor asignado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AssignmentResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Token inválido")
    })
    @PostMapping("/plantas/asignación")
    public ResponseEntity<AsignarContenedorDTO> asignarContenedorAPlanta(
            @Parameter(description = "Sesion del token recibida en el login") @RequestHeader("Autorización") String token,
            @Valid @RequestBody AsignarContenedorDTO assignment
    ) {
        DatosEmpleadoDTO employeeData = validate(token);
        AssignmentResponseDTO response = servicioPlanta.assignDumpsters(
                employeeData.employeeID(),
                assignment.plantaID(),
                assignment.dumpsterIDs()
        );
        return ResponseEntity.ok(response);
    }
}