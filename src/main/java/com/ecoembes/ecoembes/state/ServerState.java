package com.ecoembes.ecoembes.state;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// Esta clase simula la "memoria" del servidor
public class ServerState {

    // Tokens activos: token -> usuario
    private final Map<String, String> tokens = new ConcurrentHashMap<>();

    // Contenedores: id -> nivel de llenado (ejemplo simple)
    private final Map<String, String> contenedores = new ConcurrentHashMap<>();

    // Capacidad de plantas (fecha -> lista de capacidades)
    private final Map<LocalDate, Map<String, Double>> capacidadesPlantas = new ConcurrentHashMap<>();

    // Constructor: inicializa datos de ejemplo
    public ServerState() {
        capacidadesPlantas.put(LocalDate.now(), new HashMap<>(Map.of(
                "PlasSB Ltd.", 25.0,
                "ContSocket Ltd.", 18.5
        )));
    }

    // ==== TOKENS ====
    public String login(String email) {
        String token = String.valueOf(Instant.now().toEpochMilli());
        tokens.put(token, email);
        return token;
    }

    public void logout(String token) {
        tokens.remove(token);
    }

    public boolean isValidToken(String token) {
        return tokens.containsKey(token);
    }

    // ==== CONTENEDORES ====
    public void addContenedor(String id, String nivel) {
        contenedores.put(id, nivel);
    }

    public Map<String, String> getContenedores() {
        return contenedores;
    }

    // ==== PLANTAS ====
    public Map<LocalDate, Map<String, Double>> getCapacidadesPlantas() {
        return capacidadesPlantas;
    }
}
