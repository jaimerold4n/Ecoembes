package com.ecoembes.contsocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServidorContSocket {

    private static final Map<String, Double> capacidadDePlantaa = new HashMap<>();

    public static void main(String[] args) throws IOException {
    	capacidadDePlantaa.put("CONSO-01", 75.0);
    	capacidadDePlantaa.put("CONSO-02", 90.0);

        int numeroPuerto = 4444;

        try (ServerSocket serverSocket = new ServerSocket(numeroPuerto)) {
            System.out.println("Escuchando ContSocketServer en el puerto" + numeroPuerto);
            while (true) {
                new ContSocketThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("No ha podido escuchar en el puerto: " + numeroPuerto);
            System.exit(-1);
        }
    }

    private static class ContSocketThread extends Thread {
        private Socket socket = null;

        public ContSocketThread(Socket socket) {
            super("HiloConSocket");
            this.socket = socket;
        }

        public void run() {
            try (
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    String[] tokens = inputLine.split(" ");
                    if (tokens.length == 2 && tokens[0].equals("OBTENER_CAPACIDAD")) {
                        String plantId = tokens[1];
                        Double capacidad = capacidadDePlantaa.get(plantId);
                        if (capacidad != null) {
                            out.println(capacidad);
                        } else {
                            out.println("ERROR: No se ha encontrado la planta");
                        }
                    } else {
                        out.println("ERROR: Comando invalido");
                    }
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
