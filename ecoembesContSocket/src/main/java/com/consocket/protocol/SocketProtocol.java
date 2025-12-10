package com.consocket.protocol;

public class SocketProtocol {
    
    // Comandos del protocolo
    public static final String CMD_PROCESAR = "PROCESAR";
    public static final String CMD_CAPACIDAD = "CAPACIDAD";
    public static final String CMD_ESTADO = "ESTADO";
    public static final String CMD_EXIT = "EXIT";
    
    // Respuestas
    public static final String RESP_OK = "OK";
    public static final String RESP_ERROR = "ERROR";
    public static final String RESP_CAPACIDAD_INSUFICIENTE = "CAPACIDAD_INSUFICIENTE";
    
    // Separadores
    public static final String SEPARADOR_CAMPO = ":";
    public static final String SEPARADOR_LISTA = ",";
    
    /**
     * Parsea un mensaje del cliente
     * Formato: COMANDO:parametro1,parametro2,...
     */
    public static String[] parsearMensaje(String mensaje) {
        if (mensaje == null || mensaje.trim().isEmpty()) {
            return new String[0];
        }
        return mensaje.split(SEPARADOR_CAMPO);
    }
    
    /**
     * Crea un mensaje de respuesta exitosa
     */
    public static String crearRespuestaOK(String datos) {
        return RESP_OK + SEPARADOR_CAMPO + datos;
    }
    
    /**
     * Crea un mensaje de error
     */
    public static String crearRespuestaError(String mensaje) {
        return RESP_ERROR + SEPARADOR_CAMPO + mensaje;
    }
    
    /**
     * Valida si un mensaje tiene el formato correcto
     */
    public static boolean validarMensaje(String mensaje) {
        if (mensaje == null || mensaje.trim().isEmpty()) {
            return false;
        }
        
        String[] partes = mensaje.split(SEPARADOR_CAMPO);
        if (partes.length < 1) {
            return false;
        }
        
        String comando = partes[0];
        return comando.equals(CMD_PROCESAR) || 
               comando.equals(CMD_CAPACIDAD) || 
               comando.equals(CMD_ESTADO) || 
               comando.equals(CMD_EXIT);
    }
    
    /**
     * Crea un mensaje para procesar contenedores
     * Formato: PROCESAR:idPlanta:idCont1,idCont2,idCont3
     */
    public static String crearMensajeProcesar(String idPlanta, String[] idsContenedores) {
        StringBuilder sb = new StringBuilder();
        sb.append(CMD_PROCESAR).append(SEPARADOR_CAMPO);
        sb.append(idPlanta).append(SEPARADOR_CAMPO);
        sb.append(String.join(SEPARADOR_LISTA, idsContenedores));
        return sb.toString();
    }
}