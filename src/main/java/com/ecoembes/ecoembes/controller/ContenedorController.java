package com.ecoembes.ecoembes.controller;

import com.ecoembes.ecoembes.state.ServerState;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/contenedores")
public class ContenedorController {

    private final ServerState state;

    public ContenedorController(ServerState state) {
        this.state = state;
    }

    // 1️⃣ Crear un contenedor nuevo
    @PostMapping
    public ResponseEntity<String> crearContenedor(@RequestParam String id) {
        state.addContenedor(id, "VERDE"); // Nivel inicial = VERDE
        return ResponseEntity.ok("Contenedor " + id + " creado correctamente ✅");
    }

    // 2️⃣ Simular lectura del sensor
    @PutMapping("/{id}/sensor")
    public ResponseEntity<String> actualizarNivel(
            @PathVariable String id,
            @RequestParam String nivel
    ) {
        if (!state.getContenedores().containsKey(id)) {
            return ResponseEntity.badRequest().body("Error ❌: contenedor no encontrado");
        }
        state.addContenedor(id, nivel);
        return ResponseEntity.ok("Nivel del contenedor " + id + " actualizado a " + nivel);
    }

    // 3️⃣ Consultar todos los contenedores
    @GetMapping
    public ResponseEntity<Map<String, String>> listarContenedores() {
        return ResponseEntity.ok(state.getContenedores());
    }
}
