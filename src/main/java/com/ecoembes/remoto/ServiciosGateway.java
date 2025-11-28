package com.ecoembes.remoto;

import com.ecoembes.domain.Planta;

public interface ServiciosGateway {
    Double getCapacidadPlanta(Planta planta) throws Exception;
}
