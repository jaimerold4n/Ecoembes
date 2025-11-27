package com.ecoembes.remoto;

import com.ecoembes.domain.Planta;
import com.ecoembes.dto.CapacidadPlantaRemotaDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("PlasSB")
public class PlasSBServicioPuerta implements ServicioPuertas {

    private final RestTemplate restTemplate;

    public PlasSBServicioPuerta() {
        this.restTemplate = new RestTemplate();
    }

    public PlasSBServicioPuerta(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Double getCapacidadPlanta(Planta planta) throws Exception {
        String url = "http://" + planta.getAnfitrion() + ":" + planta.getPuerto() + "/api/plantas/" + planta.getPlantaId() + "/capacidad";
        CapacidadPlantaRemotaDTO respuesta = restTemplate.getForObject(url, CapacidadPlantaRemotaDTO.class);
        if (respuesta != null) {
            return respuesta.getCapacidad();
        }
        return null;
    }
}
