package com.ecoembes.repositorios;

import com.ecoembes.domain.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpleadoRepositorio extends JpaRepository<Empleado, String> {
    Optional<Empleado> findByEmail(String email);
}
