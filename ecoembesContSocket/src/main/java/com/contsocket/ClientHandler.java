package com.contsocket;

import com.consocket.protocol.SocketProtocol;
import com.consocket.services.PlantaServiceSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    
    private Socket clientSocket;
    private PlantaServiceSocket plantaService;
    private int clientId;
    
    public ClientHandler(Socket socket, PlantaServiceSocket plantaService, int clientId) {
        this.clientSocket = socket;
        this.plantaService = plantaService;
        this.clientId = clientId;
    }
    
    @Override
    public void run() {
        System.out.println("🔌 Cliente #" + clientId + " conectado desde: " + 
                          clientSocket.getInetAddress().getHostAddress());
        
        try (
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream())
            );
            PrintWriter out = new PrintWriter(
                clientSocket.getOutputStream(), true
            )
        ) {
            String mensaje;
            
            while ((mensaje = in.readLine()) != null) {
                System.out.println("📨 Cliente #" + clientId + " envió: " + mensaje);
                
                // Validar mensaje
                if (!SocketProtocol.validarMensaje(mensaje)) {
                    out.println(SocketProtocol.crearRespuestaError("Mensaje inválido"));
                    continue;
                }
                
                // Procesar comando
                String respuesta = procesarComando(mensaje);
                out.println(respuesta);
                System.out.println("📤 Respuesta a cliente #" + clientId + ": " + respuesta);
                
                // Si es EXIT, cerrar conexión
                if (mensaje.startsWith(SocketProtocol.CMD_EXIT)) {
                    break;
                }
            }
            
        } catch (IOException e) {
            System.err.println("❌ Error con cliente #" + clientId + ": " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("🔌 Cliente #" + clientId + " desconectado");
            } catch (IOException e) {
                System.err.println("❌ Error cerrando conexión: " + e.getMessage());
            }
        }
    }
    
    /**
     * Procesa los diferentes comandos
     */
    private String procesarComando(String mensaje) {
        String[] partes = SocketProtocol.parsearMensaje(mensaje);
        
        if (partes.length == 0) {
            return SocketProtocol.crearRespuestaError("Comando vacío");
        }
        
        String comando = partes[0];
        
        switch (comando) {
            case SocketProtocol.CMD_PROCESAR:
                return procesarContenedores(partes);
                
            case SocketProtocol.CMD_CAPACIDAD:
                return obtenerCapacidad(partes);
                
            case SocketProtocol.CMD_ESTADO:
                return obtenerEstado(partes);
                
            case SocketProtocol.CMD_EXIT:
                return SocketProtocol.crearRespuestaOK("Desconexión exitosa");
                
            default:
                return SocketProtocol.crearRespuestaError("Comando desconocido: " + comando);
        }
    }
    
    /**
     * Procesa el comando PROCESAR
     * Formato: PROCESAR:idPlanta:idCont1,idCont2,idCont3
     */
    private String procesarContenedores(String[] partes) {
        if (partes.length < 3) {
            return SocketProtocol.crearRespuestaError(
                "Formato incorrecto. Uso: PROCESAR:idPlanta:idCont1,idCont2,..."
            );
        }
        
        String idPlanta = partes[1];
        String[] idsContenedores = partes[2].split(SocketProtocol.SEPARADOR_LISTA);
        
        return plantaService.procesarContenedores(idPlanta, idsContenedores);
    }
    
    /**
     * Procesa el comando CAPACIDAD
     * Formato: CAPACIDAD:idPlanta
     */
    private String obtenerCapacidad(String[] partes) {
        if (partes.length < 2) {
            return SocketProtocol.crearRespuestaError(
                "Formato incorrecto. Uso: CAPACIDAD:idPlanta"
            );
        }
        
        String idPlanta = partes[1];
        return plantaService.obtenerCapacidad(idPlanta);
    }
    
    /**
     * Procesa el comando ESTADO
     * Formato: ESTADO:idPlanta
     */
    private String obtenerEstado(String[] partes) {
        if (partes.length < 2) {
            return SocketProtocol.crearRespuestaError(
                "Formato incorrecto. Uso: ESTADO:idPlanta"
            );
        }
        
        String idPlanta = partes[1];
        return plantaService.obtenerEstado(idPlanta);
    }
}