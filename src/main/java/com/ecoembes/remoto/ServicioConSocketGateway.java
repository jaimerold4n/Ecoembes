package com.ecoembes.remoto;

import com.ecoembes.domain.Planta;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Service("ContSocket")
public class ServicioConSocketGateway implements ServiciosGateway {

    private FabricaSockets fabricaSockets;

    public ServicioConSocketGateway() {
        this.fabricaSockets = new FabricaPredeterminadaSockets();
    }

    public ServicioConSocketGateway(FabricaSockets fabricaSockets) {
        this.fabricaSockets = fabricaSockets;
    }

    @Override
    public Double getCapacidadPlanta(Planta planta) throws Exception {
        try (
            Socket socket = fabricaSockets.crearSockets(planta.getAnfitrion(), planta.getPuerto());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            out.println("OBTENER_CAPACIDAD " + planta.getPlantaId());
            String response = in.readLine();
            if (response != null && !response.startsWith("ERROR")) {
                return Double.parseDouble(response);
            }
        }
        return null;
    }
}
