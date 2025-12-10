package com.consocket.services;

import com.consocket.model.PlantaDia;
import com.consocket.model.PlantaInfo;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class PlantaServiceSocket {
    
    private Map<String, PlantaInfo> plantas;
    private Map<String, PlantaDia> registrosDiarios;
    
    public PlantaServiceSocket() {
        this.plantas = new HashMap<>();
        this.registrosDiarios = new HashMap<>();
        inicializarPlantas();
    }
    
    /**
     * Inicializa las plantas de ejemplo
     */
    private void inicializarPlantas() {
        // Planta ContSocket principal
        PlantaInfo contSocket = new PlantaInfo(
            "CONTSO-01",
            "ContSocket Ltd.",
            80.5,
            "GENERAL",
            "Bilbao"
        );
        plantas.put(contSocket.getIdPlanta(), contSocket);
        
        System.out.println("✅ Planta inicializada: " + contSocket.getNombre());
    }
    
    /**
     * Procesa contenedores para una planta
     */
    public String procesarContenedores(String idPlanta, String[] idsContenedores) {
        PlantaInfo planta = plantas.get(idPlanta);
        
        if (planta == null) {
            return "ERROR: Planta no encontrada - " + idPlanta;
        }
        
        if (!planta.isActiva()) {
            return "ERROR: Planta no activa - " + idPlanta;
        }
        
        // Obtener o crear registro diario
        String keyRegistro = idPlanta + "_" + LocalDate.now();
        PlantaDia registro = registrosDiarios.get(keyRegistro);
        
        if (registro == null) {
            registro = new PlantaDia(idPlanta, LocalDate.now(), planta.getCapacidadTotal());
            registrosDiarios.put(keyRegistro, registro);
        }
        
        // Simular peso de cada contenedor (entre 5 y 15 kg)
        double pesoTotal = 0;
        for (String idContenedor : idsContenedores) {
            double peso = 5 + (Math.random() * 10); // 5-15 kg
            pesoTotal += peso;
        }
        
        // Verificar capacidad
        if (registro.getCapacidadDisponible() < pesoTotal) {
            return "ERROR: Capacidad insuficiente. Disponible: " + 
                   String.format("%.2f", registro.getCapacidadDisponible()) + 
                   " kg, Necesario: " + String.format("%.2f", pesoTotal) + " kg";
        }
        
        // Procesar contenedores
        for (String idContenedor : idsContenedores) {
            double peso = 5 + (Math.random() * 10);
            registro.agregarContenedor(idContenedor, peso);
        }
        
        String resultado = String.format(
            "Procesados %d contenedores en planta %s. " +
            "Peso total: %.2f kg. " +
            "Capacidad restante: %.2f kg",
            idsContenedores.length,
            planta.getNombre(),
            pesoTotal,
            registro.getCapacidadDisponible()
        );
        
        System.out.println("✅ " + resultado);
        return "OK:" + resultado;
    }
    
    /**
     * Obtiene la capacidad disponible de una planta
     */
    public String obtenerCapacidad(String idPlanta) {
        PlantaInfo planta = plantas.get(idPlanta);
        
        if (planta == null) {
            return "ERROR: Planta no encontrada";
        }
        
        String keyRegistro = idPlanta + "_" + LocalDate.now();
        PlantaDia registro = registrosDiarios.get(keyRegistro);
        
        double capacidadDisponible = planta.getCapacidadTotal();
        if (registro != null) {
            capacidadDisponible = registro.getCapacidadDisponible();
        }
        
        return String.format("OK:%.2f", capacidadDisponible);
    }
    
    /**
     * Obtiene el estado de una planta
     */
    public String obtenerEstado(String idPlanta) {
        PlantaInfo planta = plantas.get(idPlanta);
        
        if (planta == null) {
            return "ERROR: Planta no encontrada";
        }
        
        String keyRegistro = idPlanta + "_" + LocalDate.now();
        PlantaDia registro = registrosDiarios.get(keyRegistro);
        
        if (registro == null) {
            return String.format("OK:Planta %s - Sin actividad hoy", planta.getNombre());
        }
        
        return String.format(
            "OK:Planta %s - Contenedores procesados: %d, Capacidad usada: %.2f/%.2f kg",
            planta.getNombre(),
            registro.getContenedoresProcesados().size(),
            registro.getCapacidadUsada(),
            registro.getCapacidadTotal()
        );
    }
}