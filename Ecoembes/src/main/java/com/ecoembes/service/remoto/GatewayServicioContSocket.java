package com.ecoembes.service.remoto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.ecoembes.domain.Planta;

@Service("ContSocket")
public class GatewayServicioContSocket implements GatewayServicio {

    @Override
    public Double getCapacidadPlanta(Planta planta, LocalDate fecha) throws Exception {
        try (
            Socket socket = new Socket(planta.getAnfitrion(), planta.getPuerto());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            String fechaFormateada = fecha != null ? fecha.format(DateTimeFormatter.ISO_DATE) : "";
            out.println("OBTENER_CAPACIDAD " + planta.getPlantaId() + 
                       (fechaFormateada.isEmpty() ? "" : " " + fechaFormateada));
            String response = in.readLine();
            if (response != null && !response.startsWith("ERROR")) {
                return Double.parseDouble(response);
            }
        }
        return null;
    }
}
