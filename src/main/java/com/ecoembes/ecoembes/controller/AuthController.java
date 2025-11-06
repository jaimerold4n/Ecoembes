package com.ecoembes.ecoembes.controller;

import com.ecoembes.ecoembes.state.ServerState;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final ServerState state;

    // Constructor: Spring inyecta automáticamente la instancia de ServerState
    public AuthController(ServerState state) {
        this.state = state;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody String email) {
        String token = state.login(email);
        return ResponseEntity.ok("Login correcto ✅ Token: " + token);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("X-Auth-Token") String token) {
        state.logout(token);
        return ResponseEntity.noContent().build();
    }
}
