package com.ecoembes.repositorios;

import com.ecoembes.domain.Contenedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContenedorRepositorio extends JpaRepository<Contenedor, String> {
    List<Contenedor> encontrarCodigoPostal(String codigoPostal);
}

