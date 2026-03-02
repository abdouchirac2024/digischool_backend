package com.digiSchool.digiSchool.academic.organisation.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digiSchool.digiSchool.academic.organisation.dto.BrandingRequest;
import com.digiSchool.digiSchool.academic.organisation.dto.EcoleDto;
import com.digiSchool.digiSchool.academic.organisation.dto.InscriptionEcoleRequest;
import com.digiSchool.digiSchool.academic.organisation.dto.ValidationEcoleRequest;
import com.digiSchool.digiSchool.academic.organisation.model.StatutEcole;
import com.digiSchool.digiSchool.academic.organisation.service.EcoleService;
import com.digiSchool.digiSchool.auth.model.RoleType;
import com.digiSchool.digiSchool.auth.security.RequireRole;
import com.digiSchool.digiSchool.auth.service.UserContextService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/schools")
@CrossOrigin
@Tag(name = "Écoles", description = "Gestion des écoles (inscription, validation, branding). Les endpoints publics permettent l'inscription sans auth.")
public class EcoleController {

    private final EcoleService ecoleService;
    private final UserContextService userContextService;

    public EcoleController(EcoleService ecoleService, UserContextService userContextService) {
        this.ecoleService = ecoleService;
        this.userContextService = userContextService;
    }

    // =============================================
    // ENDPOINTS PUBLICS (pas d'auth requise)
    // =============================================

    @Operation(
        summary = "Inscrire une école (public)",
        description = "Endpoint public — aucune authentification requise. Crée un compte école en statut `EN_ATTENTE`. "
            + "Un Super Admin devra valider l'école via `PUT /api/schools/{id}/validate`."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "École créée en attente de validation"),
        @ApiResponse(responseCode = "400", description = "Données invalides (nom, email, region, etc.)", content = @Content),
        @ApiResponse(responseCode = "409", description = "Email ou slug déjà utilisé", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<EcoleDto> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Informations de l'école à inscrire", required = true)
            @Valid @RequestBody InscriptionEcoleRequest request) {
        return ResponseEntity.ok(ecoleService.inscrireEcole(request));
    }

    @Operation(
        summary = "Trouver une école par son slug (public)",
        description = "Endpoint public — aucune authentification requise. Utile pour la page de connexion d'une école spécifique."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "École trouvée"),
        @ApiResponse(responseCode = "404", description = "Slug introuvable", content = @Content)
    })
    @GetMapping("/slug/{slug}")
    public ResponseEntity<EcoleDto> getBySlug(
            @Parameter(description = "Slug unique de l'école (ex: ecole-la-victoire)", required = true, example = "ecole-la-victoire")
            @PathVariable String slug) {
        return ResponseEntity.ok(ecoleService.getBySlug(slug));
    }

    // =============================================
    // ENDPOINTS UTILISATEUR CONNECTE (selon role)
    // =============================================

    @Operation(
        summary = "Lister les écoles",
        description = "SUPER_ADMIN : retourne toutes les écoles. ADMIN_ECOLE / autres : retourne uniquement leur propre école."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste des écoles selon le rôle"),
        @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<EcoleDto>> getAll() {
        String role = userContextService.getCurrentUserRole();
        if ("SUPER_ADMIN".equals(role)) {
            return ResponseEntity.ok(ecoleService.getAll());
        }
        Long ecoleId = userContextService.getCurrentUserEcoleId();
        if (ecoleId != null) {
            return ResponseEntity.ok(List.of(ecoleService.getById(ecoleId)));
        }
        return ResponseEntity.ok(List.of());
    }

    @Operation(summary = "Détail d'une école", description = "Retourne les informations complètes d'une école par son ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Détail de l'école"),
        @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content),
        @ApiResponse(responseCode = "403", description = "Accès interdit à cette école", content = @Content),
        @ApiResponse(responseCode = "404", description = "École introuvable", content = @Content)
    })
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<EcoleDto> getById(
            @Parameter(description = "ID de l'école", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ecoleService.getById(id));
    }

    @Operation(
        summary = "Personnaliser le branding de son école",
        description = "Permet à un ADMIN_ECOLE de personnaliser le logo, les couleurs et les informations de contact de son école."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Branding mis à jour"),
        @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content),
        @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content),
        @ApiResponse(responseCode = "403", description = "Accès interdit (cette école ne vous appartient pas)", content = @Content),
        @ApiResponse(responseCode = "404", description = "École introuvable", content = @Content)
    })
    @PutMapping("/{id:\\d+}/branding")
    public ResponseEntity<EcoleDto> updateBranding(
            @Parameter(description = "ID de l'école", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Données de branding (logo URL, couleurs, contacts)", required = true)
            @RequestBody BrandingRequest request) {
        return ResponseEntity.ok(ecoleService.updateBranding(id, request));
    }

    // =============================================
    // ENDPOINTS ADMIN SAAS (SUPER_ADMIN uniquement)
    // =============================================

    @Operation(summary = "Toutes les écoles — Admin SaaS", description = "Retourne toutes les écoles sans pagination. Réservé au SUPER_ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste complète de toutes les écoles"),
        @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content),
        @ApiResponse(responseCode = "403", description = "Rôle SUPER_ADMIN requis", content = @Content)
    })
    @GetMapping("/all")
    @RequireRole(RoleType.SUPER_ADMIN)
    public ResponseEntity<List<EcoleDto>> getAllAdmin() {
        return ResponseEntity.ok(ecoleService.getAll());
    }

    @Operation(summary = "Écoles paginées — Admin SaaS", description = "Retourne les écoles avec pagination et tri. Réservé au SUPER_ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page d'écoles"),
        @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content),
        @ApiResponse(responseCode = "403", description = "Rôle SUPER_ADMIN requis", content = @Content)
    })
    @GetMapping("/page")
    @RequireRole(RoleType.SUPER_ADMIN)
    public ResponseEntity<Page<EcoleDto>> getAllPaged(
            @Parameter(description = "Numéro de page (commence à 0)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Champ de tri", example = "createdAt") @RequestParam(defaultValue = "createdAt") String sort,
            @Parameter(description = "Direction du tri : asc ou desc", example = "desc") @RequestParam(defaultValue = "desc") String direction) {
        Sort sortOrder = direction.equalsIgnoreCase("desc") ? Sort.by(sort).descending() : Sort.by(sort).ascending();
        return ResponseEntity.ok(ecoleService.getAllPaged(PageRequest.of(page, size, sortOrder)));
    }

    @Operation(summary = "Statistiques globales — Admin SaaS", description = "Retourne le nombre total d'écoles par statut. Réservé au SUPER_ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Map de statistiques ex: {\"total\": 5, \"actives\": 3, \"en_attente\": 2}"),
        @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content),
        @ApiResponse(responseCode = "403", description = "Rôle SUPER_ADMIN requis", content = @Content)
    })
    @GetMapping("/stats")
    @RequireRole(RoleType.SUPER_ADMIN)
    public ResponseEntity<Map<String, Long>> getStats() {
        return ResponseEntity.ok(ecoleService.getStats());
    }

    @Operation(summary = "Écoles en attente de validation — Admin SaaS", description = "Retourne toutes les écoles en statut EN_ATTENTE. Réservé au SUPER_ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste des écoles en attente"),
        @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content),
        @ApiResponse(responseCode = "403", description = "Rôle SUPER_ADMIN requis", content = @Content)
    })
    @GetMapping("/pending")
    @RequireRole(RoleType.SUPER_ADMIN)
    public ResponseEntity<List<EcoleDto>> getPending() {
        return ResponseEntity.ok(ecoleService.getByStatut(StatutEcole.EN_ATTENTE));
    }

    @Operation(
        summary = "Valider ou rejeter une école — Admin SaaS",
        description = "Change le statut d'une école : `ACTIVE` (validation) ou `REJETEE` (rejet). "
            + "L'école recevra une notification. Réservé au SUPER_ADMIN."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "École validée ou rejetée"),
        @ApiResponse(responseCode = "400", description = "Statut invalide ou données manquantes", content = @Content),
        @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content),
        @ApiResponse(responseCode = "403", description = "Rôle SUPER_ADMIN requis", content = @Content),
        @ApiResponse(responseCode = "404", description = "École introuvable", content = @Content)
    })
    @PutMapping("/{id:\\d+}/validate")
    @RequireRole(RoleType.SUPER_ADMIN)
    public ResponseEntity<EcoleDto> validate(
            @Parameter(description = "ID de l'école à valider/rejeter", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Décision de validation (statut + commentaire optionnel)", required = true)
            @Valid @RequestBody ValidationEcoleRequest request) {
        return ResponseEntity.ok(ecoleService.validerEcole(id, request));
    }
}