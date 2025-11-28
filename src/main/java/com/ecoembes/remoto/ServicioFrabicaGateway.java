package com.ecoembes.remoto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ServicioFrabicaGateway {

    private final ApplicationContext contexto;

    @Autowired
    public ServicioFrabicaGateway(ApplicationContext contexto) {
        this.contexto = contexto;
    }

    public ServiciosGateway getServiciosGatway(String tipo) {
        return contexto.getBean(tipo, ServiciosGateway.class);
    }
}

