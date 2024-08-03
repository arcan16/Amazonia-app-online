package com.example.Amazonia_Online_Store.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SpringDocConfiguration {

        /**
         * Configuracion inicial para OpenAPI
         * @return Configuraciones para nuestra documentacion
         */

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JJWT")))
                .info(new Info()
                        .title("API Amazonia")
                        .description("API Rest de la aplicación Amazonia-Online-Store que contiene las funcionalidades " +
                                "CRUD para los usuarios, productos y pedidos")
                        .contact(new Contact()
                                .name("Equipo Backend")
                                .email("backend@amazonia.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://amazomia-online-store.com/api/licencia")));

    }

}
