package com.ecoembes.service.remoto;

import java.io.IOException;
import java.net.Socket;

public class FabricaPredeterminadaSockets implements FabricaSockets {
    @Override
    public Socket crearSockets(String anfitrion, int puerto) throws IOException {
        return new Socket(anfitrion, puerto);
    }
}
