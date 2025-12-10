package com.plassb.applicatio;

import com.plassb.domain.Planta;
import com.plassb.repositorio.PlantaRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InicializadorDatosPlasSB implements CommandLineRunner {
    
    private final PlantaRepositorio plantaRepositorio;
    
    public InicializadorDatosPlasSB(PlantaRepositorio plantaRepositorio) {
        this.plantaRepositorio = plantaRepositorio;
    }
    
    @Override
    public void run(String... args) {
        System.out.println("\n Inicializando datos de PlasSB...");
        
        // Crear planta principal
        Planta plassb = new Planta(
            "PLASSB-01",
            "PlasSB Ltd.",
            150.0,
            "PLASTICO",
            "Bilbao"
        );
        
        plantaRepositorio.save(plassb);
        
        System.out.println("✅ Planta creada: " + plassb.getNombre());
    }
}
