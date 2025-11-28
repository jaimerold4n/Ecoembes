package com.ecoembes.service;

import com.ecoembes.domain.Contenedor;
import com.ecoembes.domain.Uso;
import com.ecoembes.dto.EstadoContenedorDTO;
import com.ecoembes.dto.UsoContenedorDTO;
import com.ecoembes.repositorios.ContenedorRepositorio;
import com.ecoembes.repositorios.UsoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class ServicioContenedor {

    private final ContenedorRepositorio repositorioContenedor;
    private final UsoRepositorio repositorioUso;

    public ServicioContenedor(ContenedorRepositorio repositorioContenedor, UsoRepositorio repositorioUso) {
        this.repositorioContenedor = repositorioContenedor;
        this.repositorioUso = repositorioUso;
    }


    @Transactional
    public EstadoContenedorDTO crearNuevoContenedor(String localizacion, Double capacidad) {
        String nuevoId = "D-" + UUID.randomUUID().toString().substring(0, 8);
        String codigoPostal = extractPostalCode(localizacion);

        Contenedor contenedor = new Contenedor(nuevoId, localizacion, codigoPostal, capacidad);
        contenedor = repositorioContenedor.save(contenedor);

        System.out.println("Contenedor creado: " + nuevoId + " en " + localizacion + " con esta capacidad: " + capacidad);

        return new EstadoContenedorDTO(
        		contenedor.getContenedorId(),
        		contenedor.getLocalizacion(),
        		contenedor.getNivelDeLlenado(),
        		contenedor.getNumeroContenedor()
        );
    }


    @Transactional(readOnly = true)
    public List<EstadoContenedorDTO> getEstadoContenedor(String codigoPostal, LocalDate fecha) {
        System.out.println("=== OBTENER ESTADO CONTENEDOR ===");
        System.out.println("Filtro codigo postal: " + codigoPostal);
        System.out.println("Filtro fecha: " + fecha);

        List<Contenedor> contenedores;
        if (codigoPostal != null && !codigoPostal.isEmpty()) {
        	contenedores = repositorioContenedor.encontrarCodigoPostal(codigoPostal);
        } else {
        	contenedores = repositorioContenedor.findAll();
        }

        List<EstadoContenedorDTO> resultado = contenedores.stream()
                .map(d -> new EstadoContenedorDTO(
                        d.getContenedorId(),
                        d.getLocalizacion(),
                        d.getNivelDeLlenado(),
                        d.getNumeroContenedor()
                ))
                .collect(Collectors.toList());

        System.out.println("Devolviendo " + resultado.size() + " contenedores");
        return resultado;
    }

    /**
     * Queries usage history for dumpsters within a date range.
     */
    @Transactional(readOnly = true)
    public List<UsoContenedorDTO> consultaUsoContenedor(LocalDate fechaInicio, LocalDate fechaFin) {
        System.out.println("=== OBTENER USO CONTENEDOR ===");
        System.out.println("Rango de fecha: " + fechaInicio + " to " + fechaFin);

        List<Uso> usos = repositorioUso.findByDateBetween(fechaInicio, fechaFin);

        List<UsoContenedorDTO> resultado = usos.stream()
                .map(u -> new UsoContenedorDTO(
                        u.getContenedor().getContenedorId(),
                        u.getFecha(),
                        u.getNivelDeLlenado(),
                        u.getNumeroDeContenedores()
                ))
                .collect(Collectors.toList());

        System.out.println("Econtrados " + resultado.size() + " registro de usos en el rango de fechas");
        return resultado;
    }

    /**
     * Updates dumpster status (for testing/simulation purposes)
     */
    @Transactional
    public EstadoContenedorDTO actualizarEstadoContenedor(String IdContenedor, String nivelDeLlenado, Integer numeroContenedor) {
        Contenedor contenedor = repositorioContenedor.findById(IdContenedor)
                .orElseThrow(() -> new RuntimeException("Conetenedor no encotrado: " + IdContenedor));

        contenedor.actualizarEstado(nivelDeLlenado, numeroContenedor);
        contenedor = repositorioContenedor.save(contenedor);

        Uso uso = new Uso(contenedor, LocalDate.now(), nivelDeLlenado, numeroContenedor);
        repositorioUso.save(uso);

        System.out.println("Actualizar contenedor " + IdContenedor + ": " + nivelDeLlenado + ", " + numeroContenedor + " contenedores");

        return new EstadoContenedorDTO(
        		contenedor.getContenedorId(),
                contenedor.getLocalizacion(),
                contenedor.getNivelDeLlenado(),
                contenedor.getNumeroContenedor()
        );
    }

    private String extractPostalCode(String localizacion) {
        if (localizacion != null && localizacion.matches(".*\\b\\d{5}\\b.*")) {
            String[] partes = localizacion.split("\\s+");
            for (String parte : partes) {
                if (parte.matches("\\d{5}")) {
                    return parte;
                }
            }
        }
        return "00000";
    }
}