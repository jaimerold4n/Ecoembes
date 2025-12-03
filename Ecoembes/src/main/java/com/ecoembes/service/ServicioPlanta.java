package com.ecoembes.service;

import com.ecoembes.domain.Tarea;
import com.ecoembes.domain.Contenedor;
import com.ecoembes.domain.Empleado;
import com.ecoembes.domain.Planta;
import com.ecoembes.dto.RespuestaTareaDTO;
import com.ecoembes.dto.CapacidadPlantaDTO;
import com.ecoembes.repositorios.ContenedorRepositorio;
import com.ecoembes.repositorios.EmpleadoRepositorio;
import com.ecoembes.repositorios.PlantaRepositorio;
import com.ecoembes.repositorios.TareaRepositorio;
import com.ecoembes.service.remoto.GatewayServicio;
import com.ecoembes.service.remoto.GatewayServicioFabrica;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ServicioPlanta {

	private final PlantaRepositorio plantaRepositorio;
	private final ContenedorRepositorio contenedorRepositorio;
	private final EmpleadoRepositorio empleadoRepositorio;
	private final TareaRepositorio tareaRepositorio;
	private final GatewayServicioFabrica puertaDeEnlaceServicioFabrica;

	public ServicioPlanta(PlantaRepositorio plantaRepositorio, ContenedorRepositorio contenedorRepositorio,
			EmpleadoRepositorio empleadoRepositorio, TareaRepositorio tareaRepositorio,
			GatewayServicioFabrica puertaDeEnlaceServicioFabrica) {
		super();
		this.plantaRepositorio = plantaRepositorio;
		this.contenedorRepositorio = contenedorRepositorio;
		this.empleadoRepositorio = empleadoRepositorio;
		this.tareaRepositorio = tareaRepositorio;
		this.puertaDeEnlaceServicioFabrica = puertaDeEnlaceServicioFabrica;
	}

	@Transactional(readOnly = true)
	public List<CapacidadPlantaDTO> getTodasLasPlantas() {
		System.out.println("Obteniendo todas las plantas");

		List<Planta> plantas = plantaRepositorio.findAll();

		return plantas.stream()
				.map(p -> new CapacidadPlantaDTO(p.getPlantaId(), p.getNombre(), p.getCapacidadDisponible()))
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<CapacidadPlantaDTO> getCapacidadPlantaPorFecha(LocalDate fecha, String plantaId) {
		LocalDate fechaEnVigor = fecha != null ? fecha : LocalDate.now();
		System.out.println("Obteniendo capacidad de planta por fecha: " + fecha
				+ (plantaId != null ? " y plantaId: " + plantaId : ""));

		Stream<Planta> plantaStream;
		if (plantaId != null && !plantaId.isEmpty()) {
			Planta planta = plantaRepositorio.findById(plantaId)
					.orElseThrow(() -> new RuntimeException("Planta no encontrada: " + plantaId));
			plantaStream = Stream.of(planta);
		} else {
			plantaStream = plantaRepositorio.findAll().stream();
		}

		return plantaStream
				.map(p -> new CapacidadPlantaDTO(p.getPlantaId(), p.getNombre(), resolveCapacidad(p, fechaEnVigor)))
				.collect(Collectors.toList());
	}

	@Transactional
	public Double getCapacidadPlanta(String plantId) throws Exception {
		return getCapacidadPlanta(plantId, LocalDate.now());
	}

	@Transactional
	public Double getCapacidadPlanta(String plantaId, LocalDate fecha) throws Exception {
		LocalDate fechaEnVigor = fecha != null ? fecha : LocalDate.now();
		Optional<Planta> planta = plantaRepositorio.findById(plantaId);
		if (planta.isPresent()) {
			GatewayServicio puertaDeEnlaceservicio = puertaDeEnlaceServicioFabrica
					.getPuertaDeEnlaceServicio(planta.get().getTipoPuertaDeEnlace());
			return puertaDeEnlaceservicio.getCapacidadPlanta(planta.get(), fechaEnVigor);
		}
		return null;
	}

	@Transactional
	public RespuestaTareaDTO asignarContenedores(String idEmpleado, String plantaId, List<String> idsContenedor) {
		System.out.println("---ASIGNACIÓN DE CONTENEDOR ---");
		System.out.println("Empleado '" + idEmpleado + "' asignando " + idsContenedor.size() + " contenedor a planta '"
				+ plantaId + "'.");

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

		return new RespuestaTareaDTO(empleado.getIdEmpleado(), empleado.getNombre(), planta.getPlantaId(),
				idsDeContenedorAsignados, fechaTarea.toString(), "Pendiente");
	}

	private Double resolveCapacidad(Planta planta, LocalDate fecha) {
		String tipoPuertaDeEnlace = planta.getTipoPuertaDeEnlace();
		if (tipoPuertaDeEnlace == null || tipoPuertaDeEnlace.isBlank()) {
			return planta.getCapacidadDisponible();
		}
		try {
			GatewayServicio puertaDeEnlaceServicio = puertaDeEnlaceServicioFabrica.getPuertaDeEnlaceServicio(tipoPuertaDeEnlace);
			if (puertaDeEnlaceServicio == null) {
				System.out.println("No hay ninguna puerta de enlace de servicio registrada para el tipo " + tipoPuertaDeEnlace
						+ ". Usando capacidad almacenada para la planta " + planta.getPlantaId());
				return planta.getCapacidadDisponible();
			}
			Double capacidadRemota = puertaDeEnlaceServicio.getCapacidadPlanta(planta, fecha);
			if (capacidadRemota != null) {
				return capacidadRemota;
			}
			System.out.println("Puerta de enlace " + tipoPuertaDeEnlace + " returned null capacity for plant " + planta.getPlantaId()
					+ ". Using stored capacity value.");
		} catch (Exception ex) {
			System.out.println("Failed to retrieve live capacity for plant " + planta.getPlantaId() + " via gateway "
					+ tipoPuertaDeEnlace + ": " + ex.getMessage());
		}
		return planta.getCapacidadDisponible();
	}
}
