package com.ecoembes.configuracion;

import com.ecoembes.domain.Contenedor;
import com.ecoembes.domain.Empleado;
import com.ecoembes.domain.Planta;
import com.ecoembes.domain.Uso;
import com.ecoembes.repositorios.ContenedorRepositorio;
import com.ecoembes.repositorios.EmpleadoRepositorio;
import com.ecoembes.repositorios.PlantaRepositorio;
import com.ecoembes.repositorios.UsoRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class InicializadorDatos implements CommandLineRunner {

    private final EmpleadoRepositorio empleadoRepositorio;
    private final PlantaRepositorio plantaRepositorio;
    private final ContenedorRepositorio contenedorRepositorio;
    private final UsoRepositorio usoRepositorio;

    public InicializadorDatos(EmpleadoRepositorio empleadoRepositorio, PlantaRepositorio plantaRepositorio,
    		ContenedorRepositorio contenedorRepositorio, UsoRepositorio usoRepositorio) {
        this.empleadoRepositorio = empleadoRepositorio;
        this.plantaRepositorio = plantaRepositorio;
        this.contenedorRepositorio = contenedorRepositorio;
        this.usoRepositorio = usoRepositorio;
    }

    @Override
    public void run(String... args) {
        inicializarEmpleados();
        inicializarPlantas();
        inicializarContenedores();
        inicializarUsos();
        System.out.println("=== Base de datos inicializada con datos de ejemplo ===");
    }

    private void inicializarEmpleados() {
        Empleado admin = new Empleado("E001", "Usuario Admin", "admin@ecoembes.com", "contrasena123");
        Empleado empleado = new Empleado("E002", "Jose Luis", "empleado@ecoembes.com", "pass");

        empleadoRepositorio.save(admin);
        empleadoRepositorio.save(empleado);

        System.out.println("Inicializar 2 empleados");
    }

    private void inicializarPlantas() {
        Planta plassb = new Planta("PLASSB-01", "PlasSB Ltd.", 150.0, "PLASTICO", "PlasSB");
        plassb.setAnfitrion("localhost");
        plassb.setPuerto(8080);
        Planta contsocket = new Planta("CONTSO-01", "ContSocket Ltd.", 80.5, "GENERAL", "ContSocket");
        contsocket.setAnfitrion("localhost");
        contsocket.setPuerto(4444);

        plantaRepositorio.save(plassb);
        plantaRepositorio.save(contsocket);

        System.out.println("Inicializar 2 plantas");
    }

    private void inicializarContenedores() {
        Contenedor c1 = new Contenedor("C-123", "Deusto, Bilbao 48010", "48010", 5200.0);
        c1.actualizarEstado("verde", 10);

        Contenedor c2 = new Contenedor("C-456", "Indautxu, Bilbao 48012", "48012", 4550.0);
        c2.actualizarEstado("naranja", 400);

        Contenedor c3 = new Contenedor("C-789", "Zabalburu, Bilbao 48014", "48014", 6300.0);
        c3.actualizarEstado("verde", 5);

        contenedorRepositorio.save(c1);
        contenedorRepositorio.save(c2);
        contenedorRepositorio.save(c3);

        System.out.println("Inicializar 3 contenedores");
    }

    private void inicializarUsos() {
    	Contenedor d1 = contenedorRepositorio.findById("C-123").orElseThrow();
    	Contenedor d2 = contenedorRepositorio.findById("C-456").orElseThrow();

    	usoRepositorio.save(new Uso(d1, LocalDate.of(2025, 11, 6), "verde", 10));
        usoRepositorio.save(new Uso(d1, LocalDate.of(2025, 11, 7), "verde", 20));
        usoRepositorio.save(new Uso(d1, LocalDate.of(2025, 11, 8), "naranja", 300));

        usoRepositorio.save(new Uso(d2, LocalDate.of(2025, 11, 6), "naranja", 400));
        usoRepositorio.save(new Uso(d2, LocalDate.of(2025, 11, 7), "rojo", 1000));

        System.out.println("Historial de uso inicializado con 5 guardados");
    }
}
