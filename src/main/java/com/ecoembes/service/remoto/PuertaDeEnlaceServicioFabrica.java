package com.ecoembes.service.remoto;

import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class PuertaDeEnlaceServicioFabrica {


    private final Map<String, PuertaDeEnlaceServicio> puertasDeEnlace;

    public PuertaDeEnlaceServicioFabrica(Map<String, PuertaDeEnlaceServicio> puertasDeEnlace) {
        this.puertasDeEnlace = puertasDeEnlace;
    }

    public PuertaDeEnlaceServicio getPuertaDeEnlaceServicio(String tipoPuertaDeEnlace) {
    	PuertaDeEnlaceServicio puertaDeEnlaceServicio = puertasDeEnlace.get(tipoPuertaDeEnlace);
        if (puertaDeEnlaceServicio == null) {
            throw new IllegalArgumentException("No se ha encontrado ninguna puerta de enlace de servicio para el tipo: " + tipoPuertaDeEnlace);
        }
        return puertaDeEnlaceServicio;
    }
}

