package com.ecoembes.service.remoto;

import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class GatewayServicioFabrica {


    private final Map<String, GatewayServicio> puertasDeEnlace;

    public GatewayServicioFabrica(Map<String, GatewayServicio> puertasDeEnlace) {
        this.puertasDeEnlace = puertasDeEnlace;
    }

    public GatewayServicio getPuertaDeEnlaceServicio(String tipoPuertaDeEnlace) {
    	GatewayServicio puertaDeEnlaceServicio = puertasDeEnlace.get(tipoPuertaDeEnlace);
        if (puertaDeEnlaceServicio == null) {
            throw new IllegalArgumentException("No se ha encontrado ninguna puerta de enlace de servicio para el tipo: " + tipoPuertaDeEnlace);
        }
        return puertaDeEnlaceServicio;
    }
}

