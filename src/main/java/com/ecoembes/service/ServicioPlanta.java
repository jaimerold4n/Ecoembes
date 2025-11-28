package com.ecoembes.service;

import com.ecoembes.domain.Tarea;
import com.ecoembes.domain.Contenedor;
import com.ecoembes.domain.Empleado;
import com.ecoembes.domain.Planta;
import com.ecoembes.dto.RespuestaTareaDTO;
import com.ecoembes.dto.CapacidadPlantaDTO;
import com.ecoembes.repositorios.TareaRespositorio;
import com.ecoembes.repositorios.ContenedorRepositorio;
import com.ecoembes.repositorios.EmpleadoRepositorio;
import com.ecoembes.repositorios.PlantaRepositorio;
import com.ecoembes.remoto.ServiciosGateway;
import com.ecoembes.remoto.ServicioFrabicaGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServicioPlanta {

    private final PlantaRepositorio plantaRepositorio;
    private final ContenedorRepositorio contenedorRepositorio;
    private final EmpleadoRepositorio empleadoRepositorio;
    private final TareaRespositorio tareaRepositorio;
    private final ServicioFrabicaGateway servicioFabricaPuerta;

    public ServicioPlanta(PlantaRepositorio plantaRepositorio, ContenedorRepositorio contenedorRepositorio,
			EmpleadoRepositorio empleadoRepositorio, TareaRespositorio tareaRepositorio,
			ServicioFrabicaGateway servicioFabricaPuerta) {
		super();
		this.plantaRepositorio = plantaRepositorio;
		this.contenedorRepositorio = contenedorRepositorio;
		this.empleadoRepositorio = empleadoRepositorio;
		this.tareaRepositorio = tareaRepositorio;
		this.servicioFabricaPuerta = servicioFabricaPuerta;
	}

	@Transactional(readOnly = true)
    public List<CapacidadPlantaDTO> getAllPlants() {
        System.out.println("Obteniendo todas las plantas");

        List<Planta> plantas = plantaRepositorio.findAll();

        return plantas.stream()
                .map(p -> new CapacidadPlantaDTO(
                        p.getPlantaId(),
                        p.getNombre(),
                        p.getCapacidadDisponible()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CapacidadPlantaDTO> getCapacidadPlantaPorFecha(LocalDate fecha, String plantaId) {
        System.out.println("Obteniendo capacidad de planta por fecha: " + fecha + (plantaId != null ? " y plantaId: " + plantaId : ""));

        List<Planta> plantas;
        if (plantaId != null && !plantaId.isEmpty()) {
            Planta planta = plantaRepositorio.findById(plantaId)
                    .orElseThrow(() -> new RuntimeException("Planta no encontrada: " + plantaId));
            plantas = List.of(planta);
        } else {
            plantas = plantaRepositorio.findAll();
        }

        return plantas.stream()
                .map(p -> new CapacidadPlantaDTO(
                		p.getPlantaId(),
                        p.getNombre(),
                        p.getCapacidadDisponible()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public Double getCapacidadPlanta(String plantaId) throws Exception {
        Optional<Planta> planta = plantaRepositorio.enocntrarPorId(plantaId);
        if (planta.isPresent()) {
            ServiciosGateway servicioPuerta = servicioFabricaPuerta.getServicioPuerta(planta.get().getTipoPuerta());
            return servicioPuerta.getCapacidadPlanta(planta.get());
        }
        return null;
    }

    @Transactional
    public RespuestaTareaDTO asignarContenedores(String idEmpleado, String plantaId, List<String> idsContenedor) {
        System.out.println("---ASIGNACIÓN DE CONTENEDOR ---");
        System.out.println("Empleado '" + idEmpleado + "' asignando " + idsContenedor.size() + " contenedor a planta '" + plantaId + "'.");

        Empleado empleado = empleadoRepositorio.findById(idEmpleado)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado: " + idEmpleado));

        Planta planta = plantaRepositorio.findById(plantaId)
                .orElseThrow(() -> new RuntimeException("Planta no encontrada: " + plantaId));

        LocalDate fechaTarea = LocalDate.now();
        List<String> idsDeContenedorAsignados = new ArrayList<>();

        for (String contenedorId : idsContenedor) {
            Contenedor contenedor = contenedorRepositorio.findById(contenedorId)
                    .orElseThrow(() -> new RuntimeException("Dumpster not found: " + contenedorId));

            Tarea tarea = new Tarea(planta, contenedor, empleado, fechaTarea);
            tareaRepositorio.save(tarea);
            idsDeContenedorAsignados.add(contenedorId);
        }

        System.out.println("Contenedores asignados: " + String.join(", ", idsDeContenedorAsignados));
        System.out.println("Tarea guardada en la base de datos.");
        System.out.println("---------------------------");

        return new RespuestaTareaDTO(
                empleado.getIdEmpleado(),
                empleado.getNombre(),
                planta.getPlantaId(),
                idsDeContenedorAsignados,
                fechaTarea.toString(),
                "Pendiente"
        );
    }
}
