package com.digiSchool.digiSchool.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
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
                .version("2.0.0")
                .description("""
                    API REST pour la plateforme de gestion scolaire **DigiSchool**.

                    ## Authentification
                    La plupart des endpoints necessitent un token JWT Bearer.
                    1. Utilisez **POST** `/api/auth/login` pour obtenir un token
                    2. Cliquez sur le bouton **Authorize** ci-dessus et entrez votre token

                    ## Multi-tenancy
                    Le systeme est multi-tenant base sur le **tenant** (identifiant unique de l'ecole). \
                    Le tenant est automatiquement extrait du token JWT. \
                    Format: `CM-{REGION}-ECOLE-{ID}` (ex: `CM-CENTRE-ECOLE-001`)

                    ## Comptes de test
                    | Role | Email | Telephone | Mot de passe |
                    |------|-------|-----------|--------------|
                    | Super Admin | admin@digischool.cm | +237600000000 | Admin@2025 |
                    | Directeur | smbarga@lavictoire.cm | +237677123456 | Directeur@2025 |
                    | Enseignant | jpkamga@lavictoire.cm | +237670111222 | Enseignant@2025 |
                    | Secretaire | catangana@lavictoire.cm | +237660555666 | Secretaire@2025 |
                    | Parent | fnkoulou@gmail.com | +237691666777 | Parent@2025 |

                    ## Codes de reponse
                    | Code | Description |
                    |------|-------------|
                    | `200` | Succes |
                    | `201` | Ressource creee |
                    | `204` | Suppression reussie |
                    | `400` | Requete invalide |
                    | `401` | Non authentifie |
                    | `403` | Acces interdit (role insuffisant) |
                    | `404` | Ressource introuvable |
                    | `409` | Conflit (doublon) |
                    | `429` | Trop de tentatives |
                    """)
                .contact(new Contact()
                    .name("DigiSchool Support")
                    .email("support@digischool.cm")
                    .url("https://digischool.cm"))
                .license(new License()
                    .name("Proprietary")
                    .url("https://digischool.cm/license")))
            .externalDocs(new ExternalDocumentation()
                .description("Documentation complete DigiSchool")
                .url("https://docs.digischool.cm"))
            .servers(List.of(
                new Server()
                    .url("http://localhost:" + serverPort)
                    .description("Serveur de developpement local"),
                new Server()
                    .url("https://api.digischool.cm")
                    .description("Serveur de production")))
            // Tag definitions – order here controls display order in Swagger UI
            .tags(List.of(
                new Tag().name("Authentification")
                    .description("Login, logout, refresh token, mot de passe oublie. Supporte email et telephone."),
                new Tag().name("Utilisateurs")
                    .description("Gestion des comptes utilisateurs (CRUD). Reserve aux Super Admin et Admin Ecole."),
                new Tag().name("Classes")
                    .description("Gestion des classes scolaires. Filtrage automatique par ecole via le JWT."),
                new Tag().name("Eleves")
                    .description("Gestion des eleves : inscription, recherche, mise a jour. Filtre par tenant."),
                new Tag().name("Enseignants")
                    .description("Gestion des enseignants de l'ecole."),
                new Tag().name("Parents")
                    .description("Gestion des parents d'eleves."),
                new Tag().name("Relations Eleve-Parent")
                    .description("Association entre eleves et parents : responsable legal, contact d'urgence, principal."),
                new Tag().name("Roles")
                    .description("Liste des roles disponibles dans le systeme."),
                new Tag().name("Regions")
                    .description("Localisation - Gestion des regions du Cameroun."),
                new Tag().name("Departements")
                    .description("Localisation - Gestion des departements (lie aux regions)."),
                new Tag().name("Arrondissements")
                    .description("Localisation - Gestion des arrondissements (lie aux departements)."),
                new Tag().name("Villes")
                    .description("Localisation - Gestion des villes (lie aux arrondissements)."),
                new Tag().name("Quartiers")
                    .description("Localisation - Gestion des quartiers (lie aux villes)."),
                new Tag().name("Adresses")
                    .description("Localisation - Gestion des adresses physiques (lie aux quartiers).")
            ))
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                    .name(securitySchemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("Token JWT obtenu via POST /api/auth/login. Format: Bearer {token}")));
    }
}
