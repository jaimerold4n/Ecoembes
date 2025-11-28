package com.ecoembes.service.remoto;

import java.time.LocalDate;
import com.ecoembes.domain.Planta;

public interface PuertaDeEnlaceServicio {
    Double getCapacidadPlanta(Planta planta, LocalDate fecha) throws Exception;
}
