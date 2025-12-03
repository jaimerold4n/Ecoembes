package com.ecoembes.service.remoto;

import com.ecoembes.domain.Planta;
import com.ecoembes.dto.CapacidadPlantaRemotaDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("PlasSB")
public class GatewayServicioPlasSB implements GatewayServicio {

    private final RestTemplate restTemplate;

    public GatewayServicioPlasSB() {
        this.restTemplate = new RestTemplate();
    }

    public GatewayServicioPlasSB(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

	@Override
	public Double getCapacidadPlanta(Planta planta, LocalDate fecha) throws Exception {
		String fechaFormateada = fecha != null ? fecha.format(DateTimeFormatter.ISO_DATE) : "";
        String url = "http://" + planta.getAnfitrion() + ":" + planta.getPuerto()
                + "/api/plantas/" + planta.getPlantaId() + "/capacidad";
        if (!fechaFormateada.isEmpty()) {
            url += "?fecha=" + fechaFormateada;
        }
        CapacidadPlantaRemotaDTO response = restTemplate.getForObject(url, CapacidadPlantaRemotaDTO.class);
        if (response != null) {
            return response.getCapacidad();
        }
		return null;
	}
}
