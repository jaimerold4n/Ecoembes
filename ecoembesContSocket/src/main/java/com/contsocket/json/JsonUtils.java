package com.contsocket.json;

public class JsonUtils {
    
    /**
     * Convierte un objeto simple a JSON
     * (Implementación básica sin librerías externas)
     */
    public static String toJson(String key, Object value) {
        if (value == null) {
            return "{\"" + key + "\": null}";
        }
        
        if (value instanceof String) {
            return "{\"" + key + "\": \"" + value + "\"}";
        }
        
        if (value instanceof Number || value instanceof Boolean) {
            return "{\"" + key + "\": " + value + "}";
        }
        
        return "{\"" + key + "\": \"" + value.toString() + "\"}";
    }
    
    /**
     * Parsea un valor simple de JSON
     */
    public static String parseValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int startIndex = json.indexOf(searchKey);
        
        if (startIndex == -1) {
            return null;
        }
        
        startIndex += searchKey.length();
        int endIndex = json.indexOf(",", startIndex);
        if (endIndex == -1) {
            endIndex = json.indexOf("}", startIndex);
        }
        
        String value = json.substring(startIndex, endIndex).trim();
        
        // Remover comillas si es string
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        
        return value;
    }
}