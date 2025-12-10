package com.plassb.controlador;

import com.plassb.domain.Planta;
import com.plassb.dto.CapacidadPlantaDTO;
import com.plassb.dto.ProcesarContenedoresRequest;
import com.plassb.dto.RespuestaProcesamiento;
import com.plassb.service.ServicioPlanta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planta")
public class ControladorPlanta {
    
    private final ServicioPlanta servicioPlanta;
    
    public ControladorPlanta(ServicioPlanta servicioPlanta) {
        this.servicioPlanta = servicioPlanta;
    }
    
    /**
     * Procesar contenedores
     * POST /api/planta/procesar
     */
    @PostMapping("/procesar")
    public ResponseEntity<RespuestaProcesamiento> procesarContenedores(
        @RequestBody ProcesarContenedoresRequest request
    ) {
        try {
            RespuestaProcesamiento respuesta = servicioPlanta.procesarContenedores(
                request.getIdPlanta(),
                request.getIdsContenedores()
            );
            
            if (respuesta.isExitoso()) {
                return ResponseEntity.ok(respuesta);
            } else {
                return ResponseEntity.status(HttpStatus.INSUFFICIENT_STORAGE).body(respuesta);
            }
            
        } catch (Exception e) {
            RespuestaProcesamiento error = new RespuestaProcesamiento(
                "Error: " + e.getMessage(),
                0,
                0.0,
                0.0,
                false
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Obtener capacidad de una planta
     * GET /api/planta/{idPlanta}/capacidad
     */
    @GetMapping("/{idPlanta}/capacidad")
    public ResponseEntity<CapacidadPlantaDTO> obtenerCapacidad(
        @PathVariable String idPlanta
    ) {
        try {
            CapacidadPlantaDTO capacidad = servicioPlanta.obtenerCapacidad(idPlanta);
            return ResponseEntity.ok(capacidad);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Obtener todas las plantas
     * GET /api/planta
     */
    @GetMapping
    public ResponseEntity<List<Planta>> obtenerTodasLasPlantas() {
        List<Planta> plantas = servicioPlanta.obtenerTodasLasPlantas();
        return ResponseEntity.ok(plantas);
    }
    
    /**
     * Obtener estadísticas de una planta
     * GET /api/planta/{idPlanta}/estadisticas
     */
    @GetMapping("/{idPlanta}/estadisticas")
    public ResponseEntity<String> obtenerEstadisticas(
        @PathVariable String idPlanta
    ) {
        try {
            String estadisticas = servicioPlanta.obtenerEstadisticas(idPlanta);
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Planta no encontrada");
        }
    }
}