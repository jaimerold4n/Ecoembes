package com.ecoembes.remoto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ServicioFrabicaPuerta {

    private final ApplicationContext contexto;

    @Autowired
    public ServicioFrabicaPuerta(ApplicationContext contexto) {
        this.contexto = contexto;
    }

    public ServicioPuertas getServicioPuertas(String tipo) {
        return contexto.getBean(tipo, ServicioPuertas.class);
    }
}

