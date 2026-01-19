package com.ecoembes.webclient.proxy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTP-based Service Proxy implementation.
 * Comunicación con el backend API de Ecoembes usando REST calls.
 * ADAPTADO A LOS ENDPOINTS REALES DEL SERVIDOR ECOEMBES
 */
@Component
public class HTTPServiceProxy implements IServiceProxy {

    private final WebClient webClient;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public HTTPServiceProxy(@Value("${ecoembes.api.base-url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public String login(String email, String password) {
        try {
            Map<String, String> credentials = new HashMap<>();
            credentials.put("email", email);
            credentials.put("contrasena", password); // ⭐ Cambio: "contrasena" en lugar de "password"

            System.out.println("[PROXY] Intentando login para: " + email);
            System.out.println("[PROXY] Enviando petición a: /api/v1/login");

            Map<String, String> response = webClient.post()
                    .uri("/api/v1/login")
                    .bodyValue(credentials)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                    .block();

            System.out.println("[PROXY] Respuesta recibida: " + response);
            String token = response != null ? response.get("token") : null;
            System.out.println("[PROXY] Token extraído: " + token);

            return token;
        } catch (WebClientResponseException e) {
            System.err.println("[PROXY] Login fallido - Estado: " + e.getStatusCode());
            System.err.println("[PROXY] Cuerpo de respuesta: " + e.getResponseBodyAsString());
            throw new RuntimeException("Login fallido: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("[PROXY] Error inesperado durante login: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Login fallido: " + e.getMessage(), e);
        }
    }

    @Override
    public void logout(String token) {
        try {
            webClient.post()
                    .uri("/api/v1/logout")
                    .header("Autorizacion", token) 
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Logout fallido: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> createDumpster(String token, Map<String, Object> dumpsterData) {
        try {
            // ⭐ Cambio: Adaptar nombres de campos
            Map<String, Object> adaptedData = new HashMap<>();
            adaptedData.put("localidad", dumpsterData.get("location"));
            adaptedData.put("capacidadInicial", dumpsterData.get("initialCapacity"));

            return webClient.post()
                    .uri("/api/v1/contenedores") 
                    .header("Autorizacion", token) 
                    .bodyValue(adaptedData)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Fallo al crear contenedor: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> queryDumpsterUsage(String token, LocalDate startDate, LocalDate endDate) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/contenedores/uso")
                            .queryParam("fechaInicio", startDate.format(DATE_FORMATTER)) 
                            .queryParam("fechaFin", endDate.format(DATE_FORMATTER)) 
                            .build())
                    .header("Autorizacion", token)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Fallo al consultar uso de contenedores: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getDumpsterStatus(String token, String postalCode, LocalDate date) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/contenedores/estado") 
                            .queryParam("codigoPostal", postalCode) 
                            .queryParam("fecha", date.format(DATE_FORMATTER))
                            .build())
                    .header("Autorizacion", token) 
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Fallo al obtener estado de contenedores: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getAllPlants(String token) {
        try {
            return webClient.get()
                    .uri("/api/v1/plantas") 
                    .header("Autorizacion", token) 
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Fallo al obtener plantas: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getPlantCapacity(String token, LocalDate date, String plantId) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> {
                        var builder = uriBuilder
                                .path("/api/v1/plantas/capacidad") 
                                .queryParam("fecha", date.format(DATE_FORMATTER));
                        if (plantId != null && !plantId.isEmpty()) {
                            builder.queryParam("IdPlanta", plantId); 
                        }
                        return builder.build();
                    })
                    .header("Autorizacion", token) 
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Fallo al obtener capacidad de plantas: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> assignDumpstersToPlant(String token, Map<String, Object> assignmentData) {
        try {
            
            Map<String, Object> adaptedData = new HashMap<>();
            adaptedData.put("idPlanta", assignmentData.get("plantID"));
            adaptedData.put("idContenedores", assignmentData.get("dumpsterIDs"));

            return webClient.post()
                    .uri("/api/v1/plantas/asignacion") 
                    .header("Autorizacion", token)
                    .bodyValue(adaptedData)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Fallo al asignar contenedores: " + e.getMessage(), e);
        }
    }
}