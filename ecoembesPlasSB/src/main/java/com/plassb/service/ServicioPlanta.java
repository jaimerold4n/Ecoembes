package com.plassb.service;

import com.plassb.domain.Planta;
import com.plassb.domain.RegistroProcesamiento;
import com.plassb.dto.CapacidadPlantaDTO;
import com.plassb.dto.RespuestaProcesamiento;
import com.plassb.repositorio.PlantaRepositorio;
import com.plassb.repositorio.RegistroProcesamientoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ServicioPlanta {
    
    private final PlantaRepositorio plantaRepositorio;
    private final RegistroProcesamientoRepositorio registroRepositorio;
    
    public ServicioPlanta(PlantaRepositorio plantaRepositorio, 
                         RegistroProcesamientoRepositorio registroRepositorio) {
        this.plantaRepositorio = plantaRepositorio;
        this.registroRepositorio = registroRepositorio;
    }
    
    /**
     * Procesa contenedores en una planta
     */
    @Transactional
    public RespuestaProcesamiento procesarContenedores(String idPlanta, List<String> idsContenedores) {

        Planta planta = plantaRepositorio.findByIdPlanta(idPlanta)
            .orElseThrow(() -> new RuntimeException("Planta no encontrada: " + idPlanta));
        
        if (!planta.getActiva()) {
            throw new RuntimeException("La planta no está activa");
        }
        

        Double pesoUsadoHoy = registroRepositorio.calcularPesoTotalDia(idPlanta, LocalDate.now());
        if (pesoUsadoHoy == null) {
            pesoUsadoHoy = 0.0;
        }
        

        double pesoTotal = 0.0;
        for (String idContenedor : idsContenedores) {
            double peso = 10 + (Math.random() * 10); // 10-20 kg
            pesoTotal += peso;
        }
        

        double capacidadDisponible = planta.getCapacidadTotal() - pesoUsadoHoy;
        if (capacidadDisponible < pesoTotal) {
            return new RespuestaProcesamiento(
                "Capacidad insuficiente. Disponible: " + 
                String.format("%.2f", capacidadDisponible) + 
                " kg, Necesario: " + String.format("%.2f", pesoTotal) + " kg",
                0,
                0.0,
                capacidadDisponible,
                false
            );
        }
        
        // Guardar registros de procesamiento
        for (String idContenedor : idsContenedores) {
            double peso = 10 + (Math.random() * 10);
            RegistroProcesamiento registro = new RegistroProcesamiento(
                planta, 
                idContenedor, 
                peso
            );
            registroRepositorio.save(registro);
        }
        
        capacidadDisponible -= pesoTotal;
        
        String mensaje = String.format(
            "Procesados %d contenedores en %s. Peso total: %.2f kg",
            idsContenedores.size(),
            planta.getNombre(),
            pesoTotal
        );
        
        System.out.println("✅ " + mensaje);
        
        return new RespuestaProcesamiento(
            mensaje,
            idsContenedores.size(),
            pesoTotal,
            capacidadDisponible,
            true
        );
    }
    
    /**
     * Obtiene la capacidad de una planta
     */
    public CapacidadPlantaDTO obtenerCapacidad(String idPlanta) {
        Planta planta = plantaRepositorio.findByIdPlanta(idPlanta)
            .orElseThrow(() -> new RuntimeException("Planta no encontrada"));
        
        Double pesoUsadoHoy = registroRepositorio.calcularPesoTotalDia(idPlanta, LocalDate.now());
        if (pesoUsadoHoy == null) {
            pesoUsadoHoy = 0.0;
        }
        
        return CapacidadPlantaDTO.fromPlanta(planta, pesoUsadoHoy);
    }
    
    /**
     * Obtiene todas las plantas
     */
    public List<Planta> obtenerTodasLasPlantas() {
        return plantaRepositorio.findAll();
    }
    
    /**
     * Obtiene estadísticas de una planta
     */
    public String obtenerEstadisticas(String idPlanta) {
        Planta planta = plantaRepositorio.findByIdPlanta(idPlanta)
            .orElseThrow(() -> new RuntimeException("Planta no encontrada"));
        
        Long contenedoresProcesados = registroRepositorio.contarContenedoresProcesadosHoy(
            idPlanta, 
            LocalDate.now()
        );
        
        Double pesoUsado = registroRepositorio.calcularPesoTotalDia(idPlanta, LocalDate.now());
        if (pesoUsado == null) {
            pesoUsado = 0.0;
        }
        
        return String.format(
            "Planta %s - Contenedores procesados hoy: %d, Peso total: %.2f kg, Capacidad restante: %.2f kg",
            planta.getNombre(),
            contenedoresProcesados != null ? contenedoresProcesados : 0,
            pesoUsado,
            planta.getCapacidadTotal() - pesoUsado
        );
    }
}