package com.ecoembes.webclient.controller;

import com.ecoembes.webclient.model.Model;
import com.ecoembes.webclient.proxy.IServiceProxy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Web Client Controller - handles HTTP requests from the browser
 * and delegates business logic to the Service Proxy.
 */
@Controller
public class WebClientController {

    private final IServiceProxy serviceProxy;
    private final Model sessionModel;

    public WebClientController(IServiceProxy serviceProxy, Model sessionModel) {
        this.serviceProxy = serviceProxy;
        this.sessionModel = sessionModel;
    }



    @GetMapping("/")
    public String index() {
        if (sessionModel.isAuthenticated()) {
            return "redirect:/home";
        }
        return "login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                       @RequestParam String password,
                       ModelMap model) {
        try {
            System.out.println("[CONTROLLER] Intentando logearse para: " + email);
            String token = serviceProxy.login(email, password);
            System.out.println("[CONTROLLER] Token recibido: " + token);

            if (token == null || token.isEmpty()) {
                System.err.println("[CONTROLLER] El token está vacío o es nulo!");
                model.addAttribute("error", "Email o contraseña inválidos");
                return "login";
            }

            sessionModel.setToken(token);
            sessionModel.setCurrentURL("/home");
            System.out.println("[CONTROLLER] Logeado correctamente, redirigiendo a /home");
            return "redirect:/home";
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Fallo en el login con excepcion: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Email o contraseña inválidos: " + e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        try {
            if (sessionModel.isAuthenticated()) {
                serviceProxy.logout(sessionModel.getToken());
            }
        } catch (Exception e) {
            // Log error but continue with logout
        }
        sessionModel.setToken(null);
        sessionModel.setCurrentURL(null);
        return "redirect:/login";
    }


    @GetMapping("/home")
    public String home(ModelMap model) {
        if (!sessionModel.isAuthenticated()) {
            return "redirect:/login";
        }
        sessionModel.setCurrentURL("/home");
        return "index";
    }



    @GetMapping("/dumpsters/create")
    public String createDumpsterPage(ModelMap model) {
        if (!sessionModel.isAuthenticated()) {
            return "redirect:/login";
        }
        sessionModel.setCurrentURL("/dumpsters/create");
        return "dumpsters/create";
    }

    @PostMapping("/dumpsters/create")
    public String createDumpster(@RequestParam String location,
                                 @RequestParam Double initialCapacity,
                                 ModelMap model) {
        if (!sessionModel.isAuthenticated()) {
            return "redirect:/login";
        }

        try {
            Map<String, Object> dumpsterData = new HashMap<>();
            dumpsterData.put("location", location);
            dumpsterData.put("initialCapacity", initialCapacity);

            Map<String, Object> result = serviceProxy.createDumpster(sessionModel.getToken(), dumpsterData);
            model.addAttribute("success", "Dumpster created successfully!");
            model.addAttribute("dumpster", result);
            return "dumpsters/create-success";
        } catch (Exception e) {
            model.addAttribute("error", "Fallo al crear contenedores: " + e.getMessage());
            return "dumpsters/create";
        }
    }

    @GetMapping("/dumpsters/query")
    public String queryDumpstersPage(ModelMap model) {
        if (!sessionModel.isAuthenticated()) {
            return "redirect:/login";
        }
        sessionModel.setCurrentURL("/dumpsters/query");
        return "dumpsters/query";
    }

    @PostMapping("/dumpsters/query")
    public String queryDumpsters(@RequestParam String startDate,
                                 @RequestParam String endDate,
                                 ModelMap model) {
        if (!sessionModel.isAuthenticated()) {
            return "redirect:/login";
        }

        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<Map<String, Object>> usage = serviceProxy.queryDumpsterUsage(sessionModel.getToken(), start, end);
            model.addAttribute("usageData", usage);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            return "dumpsters/query-results";
        } catch (Exception e) {
            model.addAttribute("error", "Fallo al obtener contenedores: " + e.getMessage());
            return "dumpsters/query";
        }
    }

    @GetMapping("/dumpsters/status")
    public String dumpsterStatusPage(ModelMap model) {
        if (!sessionModel.isAuthenticated()) {
            return "redirect:/login";
        }
        sessionModel.setCurrentURL("/dumpsters/status");
        return "dumpsters/status";
    }

    @PostMapping("/dumpsters/status")
    public String dumpsterStatus(@RequestParam String postalCode,
                                 @RequestParam String date,
                                 ModelMap model) {
        if (!sessionModel.isAuthenticated()) {
            return "redirect:/login";
        }

        try {
            LocalDate queryDate = LocalDate.parse(date);
            List<Map<String, Object>> status = serviceProxy.getDumpsterStatus(
                    sessionModel.getToken(), postalCode, queryDate);
            model.addAttribute("statusData", status);
            model.addAttribute("postalCode", postalCode);
            model.addAttribute("date", date);
            return "dumpsters/status-results";
        } catch (Exception e) {
            model.addAttribute("error", "Fallo al obtener estado de contenedores: " + e.getMessage());
            return "dumpsters/status";
        }
    }

    // ========== Plants ==========

    @GetMapping("/plants")
    public String plantsPage(ModelMap model) {
        if (!sessionModel.isAuthenticated()) {
            return "redirect:/login";
        }

        try {
            List<Map<String, Object>> plants = serviceProxy.getAllPlants(sessionModel.getToken());
            model.addAttribute("plants", plants);
            sessionModel.setCurrentURL("/plants");
            return "plants/list";
        } catch (Exception e) {
            model.addAttribute("error", "Fallo al crear plantas: " + e.getMessage());
            return "plants/list";
        }
    }

    @GetMapping("/plants/capacity")
    public String plantCapacityPage(ModelMap model) {
        if (!sessionModel.isAuthenticated()) {
            return "redirect:/login";
        }

        try {
            List<Map<String, Object>> plants = serviceProxy.getAllPlants(sessionModel.getToken());
            model.addAttribute("plants", plants);
            sessionModel.setCurrentURL("/plants/capacity");
            return "plants/capacity";
        } catch (Exception e) {
            model.addAttribute("error", "Fallo al crear plantas: " + e.getMessage());
            return "plants/capacity";
        }
    }

    @PostMapping("/plants/capacity")
    public String plantCapacity(@RequestParam String date,
                                @RequestParam(required = false) String plantId,
                                ModelMap model) {
        if (!sessionModel.isAuthenticated()) {
            return "redirect:/login";
        }

        try {
            LocalDate queryDate = LocalDate.parse(date);
            List<Map<String, Object>> capacities = serviceProxy.getPlantCapacity(
                    sessionModel.getToken(), queryDate, plantId);
            model.addAttribute("capacities", capacities);
            model.addAttribute("date", date);
            model.addAttribute("plantId", plantId);
            return "plants/capacity-results";
        } catch (Exception e) {
            model.addAttribute("error", "Fallo al obtener capacidad de las plantas: " + e.getMessage());
            return "plants/capacity";
        }
    }


    @GetMapping("/assignments/create")
    public String createAssignmentPage(ModelMap model) {
        if (!sessionModel.isAuthenticated()) {
            return "redirect:/login";
        }

        try {
            List<Map<String, Object>> plants = serviceProxy.getAllPlants(sessionModel.getToken());
            model.addAttribute("plants", plants);
            sessionModel.setCurrentURL("/assignments/create");
            return "assignments/create";
        } catch (Exception e) {
            model.addAttribute("error", "Fallo al cargar todas las plantas: " + e.getMessage());
            return "assignments/create";
        }
    }

    @PostMapping("/assignments/create")
    public String createAssignment(@RequestParam String plantId,
                                   @RequestParam String dumpsterIds,
                                   @RequestParam String date,
                                   ModelMap model) {
        if (!sessionModel.isAuthenticated()) {
            return "redirect:/login";
        }

        try {
            // Dividir y recortar ID de contenedores
            List<String> dumpsterIdList = List.of(dumpsterIds.split(","))
                    .stream()
                    .map(String::trim)
                    .toList();

            Map<String, Object> assignmentData = new HashMap<>();
            assignmentData.put("plantID", plantId);  
            assignmentData.put("dumpsterIDs", dumpsterIdList); 
            assignmentData.put("date", date);

            Map<String, Object> result = serviceProxy.assignDumpstersToPlant(
                    sessionModel.getToken(), assignmentData);
            model.addAttribute("success", "Tarea creada correctamente!");
            model.addAttribute("assignment", result);
            return "assignments/create-success";
        } catch (Exception e) {
            model.addAttribute("error", "Fallo al crear tarea: " + e.getMessage());
            List<Map<String, Object>> plants = serviceProxy.getAllPlants(sessionModel.getToken());
            model.addAttribute("plants", plants);
            return "assignments/create";
        }
    }
}

