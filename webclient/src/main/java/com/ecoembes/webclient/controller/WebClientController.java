package com.ecoembes.webclient.controller;

import com.ecoembes.webclient.proxy.IServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/web")
public class WebClientController {

    private final IServiceProxy serviceProxy;

    private String token; // Token de sesión del usuario logueado

    @Autowired
    public WebClientController(IServiceProxy serviceProxy) {
        this.serviceProxy = serviceProxy;
    }

    /* ===================== LOGIN ===================== */

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                          @RequestParam String password,
                          Model model) {

        try {
            token = serviceProxy.login(email, password);
            return "redirect:/web/home";
        } catch (Exception e) {
            model.addAttribute("error", "Credenciales incorrectas");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        if (token != null) {
            serviceProxy.logout(token);
            token = null;
        }
        return "redirect:/web/login";
    }

    /* ===================== MENÚ ===================== */

    @GetMapping("/home")
    public String showHome() {
        if (token == null) {
            return "redirect:/web/login";
        }
        return "index";
    }

    /* ===================== CONTENEDORES ===================== */

    @GetMapping("/dumpsters/create")
    public String showCreateDumpster() {
        if (token == null) {
            return "redirect:/web/login";
        }
        return "dumpsters/create";
    }

    @PostMapping("/dumpsters/create")
    public String createDumpster(@RequestParam String location,
                                 @RequestParam Double initialCapacity,
                                 Model model) {
        try {
            Map<String, Object> dumpsterData = new java.util.HashMap<>();
            dumpsterData.put("location", location);
            dumpsterData.put("initialCapacity", initialCapacity);
            
            Map<String, Object> result = serviceProxy.createDumpster(token, dumpsterData);
            model.addAttribute("contenedor", result);
            return "dumpsters/create-success";
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear contenedor: " + e.getMessage());
            return "dumpsters/create";
        }
    }

    @GetMapping("/dumpsters/status")
    public String showDumpsterStatus() {
        if (token == null) {
            return "redirect:/web/login";
        }
        return "dumpsters/status";
    }

    @PostMapping("/dumpsters/status")
    public String getDumpsterStatus(@RequestParam String postalCode,
                                    @RequestParam String date,
                                    Model model) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            List<Map<String, Object>> status =
                    serviceProxy.getDumpsterStatus(token, postalCode, localDate);

            model.addAttribute("contenedores", status);
            model.addAttribute("codigoPostal", postalCode);
            model.addAttribute("fecha", date);
            return "dumpsters/status-results";
        } catch (Exception e) {
            model.addAttribute("error", "Error al obtener estado: " + e.getMessage());
            return "dumpsters/status";
        }
    }

    @GetMapping("/dumpsters/query")
    public String showDumpsterQuery() {
        if (token == null) {
            return "redirect:/web/login";
        }
        return "dumpsters/query";
    }

    @PostMapping("/dumpsters/query")
    public String queryDumpsterUsage(@RequestParam String startDate,
                                     @RequestParam String endDate,
                                     Model model) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<Map<String, Object>> usage =
                    serviceProxy.queryDumpsterUsage(token, start, end);

            model.addAttribute("usoContenedores", usage);
            model.addAttribute("fechaInicio", startDate);
            model.addAttribute("fechaFin", endDate);
            return "dumpsters/query-results";
        } catch (Exception e) {
            model.addAttribute("error", "Error al consultar uso: " + e.getMessage());
            return "dumpsters/query";
        }
    }

    /* ===================== PLANTAS ===================== */

    @GetMapping("/plants")
    public String getPlants(Model model) {
        try {
            if (token == null) {
                return "redirect:/web/login";
            }
            List<Map<String, Object>> plants = serviceProxy.getAllPlants(token);
            model.addAttribute("plantas", plants);
            return "plants/list";
        } catch (Exception e) {
            model.addAttribute("error", "Error al obtener plantas: " + e.getMessage());
            return "plants/list";
        }
    }

    @GetMapping("/plants/capacity")
    public String showPlantCapacity(Model model) {
        if (token == null) {
            return "redirect:/web/login";
        }
        return "plants/capacity";
    }

    @PostMapping("/plants/capacity")
    public String getPlantCapacity(@RequestParam(required = false) String plantId,
                                   @RequestParam String date,
                                   Model model) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            List<Map<String, Object>> capacity =
                    serviceProxy.getPlantCapacity(token, localDate, plantId);

            model.addAttribute("capacidades", capacity);
            model.addAttribute("fecha", date);
            model.addAttribute("plantaId", plantId);
            return "plants/capacity-results";
        } catch (Exception e) {
            model.addAttribute("error", "Error al obtener capacidad: " + e.getMessage());
            return "plants/capacity";
        }
    }

    /* ===================== ASIGNACIÓN ===================== */

    @GetMapping("/assignments/create")
    public String showCreateAssignment(Model model) {
        try {
            if (token == null) {
                return "redirect:/web/login";
            }
            List<Map<String, Object>> plants = serviceProxy.getAllPlants(token);
            model.addAttribute("plants", plants);
            return "assignments/create";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar plantas: " + e.getMessage());
            return "assignments/create";
        }
    }

    @PostMapping("/assignments/create")
    public String assignDumpsters(@RequestParam String plantId,
                                  @RequestParam String dumpsterIds,
                                  Model model) {
        try {
            // Convertir la cadena de IDs separados por comas en una lista
            List<String> idList = java.util.Arrays.stream(dumpsterIds.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(java.util.stream.Collectors.toList());

            Map<String, Object> assignmentData = new java.util.HashMap<>();
            assignmentData.put("plantID", plantId);
            assignmentData.put("dumpsterIDs", idList);

            Map<String, Object> result = serviceProxy.assignDumpstersToPlant(token, assignmentData);
            model.addAttribute("resultado", result);
            return "assignments/create-success";
        } catch (Exception e) {
            model.addAttribute("error", "Error al asignar contenedores: " + e.getMessage());
            List<Map<String, Object>> plants = serviceProxy.getAllPlants(token);
            model.addAttribute("plants", plants);
            return "assignments/create";
        }
    }
}