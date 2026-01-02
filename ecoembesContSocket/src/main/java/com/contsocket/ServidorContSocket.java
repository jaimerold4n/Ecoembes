package com.contsocket;

import com.consocket.services.PlantaServiceSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorContSocket {
    
    private static final int PUERTO = 4444;
    private static PlantaServiceSocket plantaService;
    private static int clienteCounter = 0;
    
    public static void main(String[] args) {
        System.out.println("    SERVIDOR CONTSOCKET INICIADO");
        System.out.println("Puerto: " + PUERTO);
        System.out.println("Fecha: " + java.time.LocalDateTime.now());
        
        // Inicializar servicio de plantas
        plantaService = new PlantaServiceSocket();
        
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println(" Servidor escuchando en puerto " + PUERTO);
            System.out.println(" Esperando conexiones...\n");
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clienteCounter++;
                
                
                Thread clientThread = new Thread(
                    new ClientHandler(clientSocket, plantaService, clienteCounter)
                );
                clientThread.start();
            }
            
        } catch (IOException e) {
            System.err.println(" Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}