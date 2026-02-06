package com.digiSchool.digiSchool.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration OpenAPI/Swagger pour la documentation de l'API
 *
 * Swagger UI accessible a: http://localhost:8080/swagger-ui.html
 * OpenAPI JSON: http://localhost:8080/v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
            .info(new Info()
                .title("DigiSchool API")
                .version("1.0.0")
                .description("""
                    API REST pour la gestion d'ecoles DigiSchool.

                    ## Authentification
                    La plupart des endpoints necessitent un token JWT.
                    1. Utilisez `POST /api/auth/login` pour obtenir un token
                    2. Cliquez sur le bouton "Authorize" et entrez: `Bearer <votre_token>`

                    ## Multi-tenancy
                    Le systeme est multi-tenant base sur l'ecole (ecoleId).
                    L'ecoleId est automatiquement extrait du token JWT.

                    ## Comptes de test
                    | Role | Email | Telephone | Password |
                    |------|-------|-----------|----------|
                    | Admin | admin@digischool.cm | +237600000000 | Admin@2025 |
                    | Directeur | smbarga@lavictoire.cm | +237677123456 | Directeur@2025 |
                    | Enseignant | jpkamga@lavictoire.cm | +237670111222 | Enseignant@2025 |
                    | Secretaire | catangana@lavictoire.cm | +237660555666 | Secretaire@2025 |
                    | Parent | fnkoulou@gmail.com | +237691666777 | Parent@2025 |
                    """)
                .contact(new Contact()
                    .name("DigiSchool Support")
                    .email("support@digischool.cm")
                    .url("https://digischool.cm"))
                .license(new License()
                    .name("Proprietary")
                    .url("https://digischool.cm/license")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:" + serverPort)
                    .description("Serveur de developpement local"),
                new Server()
                    .url("https://api.digischool.cm")
                    .description("Serveur de production")))
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                    .name(securitySchemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("Token JWT obtenu via POST /api/auth/login. Entrez: Bearer <token>")));
    }
}
