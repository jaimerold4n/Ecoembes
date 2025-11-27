package com.ecoembes.remoto;

import java.io.IOException;
import java.net.Socket;

public interface FabricaSockets {
    Socket crearSockets(String anfitrion, int puerto) throws IOException;
}
