package com.ecoembes.configuracion;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracionSwagger {

    /**
     * Configures the general OpenAPI definition for the Ecoembes API.
     * This includes title, version, description, and license information.
     */
    @Bean
    public OpenAPI ecoembesOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Ecoembes Central Servidor API - Prototip TW1 (Java)")
                        .description("Documentacion API para el poryecto de Ecoembes, primer prototipo. Utiliza informacion simulada y manejo de sesión")
                        .version("v1.0.0")
                        .license(new License().name("Licencia Deusto").url("https://www.deusto.es"))
                );
    }
}