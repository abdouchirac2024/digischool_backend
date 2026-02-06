# Documentation API DigiSchool

## Swagger UI (Interface Interactive)

Une fois le backend demarre, accedez a Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

### Comment utiliser Swagger UI:

1. **Se connecter:**
   - Cliquez sur `POST /api/auth/login`
   - Cliquez sur "Try it out"
   - Entrez les identifiants (voir comptes de test ci-dessous)
   - Cliquez "Execute"
   - Copiez le `token` de la reponse

2. **Autoriser:**
   - Cliquez sur le bouton "Authorize" (cadenas en haut a droite)
   - Entrez: `Bearer <votre_token>`
   - Cliquez "Authorize"

3. **Tester les endpoints:**
   - Tous les endpoints sont maintenant accessibles
   - Cliquez sur un endpoint, puis "Try it out"

## Postman

### Importer la collection:

1. Ouvrez Postman
2. Cliquez sur "Import"
3. Selectionnez le fichier `DigiSchool_API.postman_collection.json`
4. La collection apparait dans votre sidebar

### Configuration:

La collection utilise des variables:
- `{{base_url}}` = `http://localhost:8080/api`
- `{{access_token}}` = Auto-rempli apres login
- `{{refresh_token}}` = Auto-rempli apres login

### Utilisation:

1. Executez d'abord "Login (Email)" dans le dossier Authentication
2. Le token est automatiquement sauvegarde
3. Toutes les autres requetes utilisent ce token automatiquement

## OpenAPI Specification

Les fichiers de specification OpenAPI sont disponibles:

- **YAML (statique):** `docs/openapi.yaml`
- **JSON (dynamique):** `http://localhost:8080/v3/api-docs`
- **YAML (dynamique):** `http://localhost:8080/v3/api-docs.yaml`

## Comptes de Test

| Role | Email | Telephone | Password | ecoleId |
|------|-------|-----------|----------|---------|
| Admin SaaS | admin@digischool.cm | +237600000000 | Admin@2025 | null |
| Directeur | smbarga@lavictoire.cm | +237677123456 | Directeur@2025 | 1 |
| Enseignant | jpkamga@lavictoire.cm | +237670111222 | Enseignant@2025 | 1 |
| Secretaire | catangana@lavictoire.cm | +237660555666 | Secretaire@2025 | 1 |
| Parent | fnkoulou@gmail.com | +237691666777 | Parent@2025 | 1 |
| En Attente | enattente@test.cm | +237699000111 | Test@2025 | 1 |
| Inactif | inactif@test.cm | +237699000222 | Test@2025 | 1 |

## Endpoints Disponibles

### Authentication (`/api/auth`)

| Methode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| POST | `/api/auth/login` | Connexion (email ou telephone) | Non |
| GET | `/api/auth/me` | Profil utilisateur connecte | Oui |
| POST | `/api/auth/refresh` | Rafraichir le token | Non |
| POST | `/api/auth/logout` | Deconnexion | Oui |

### Roles (`/api/roles`)

| Methode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| GET | `/api/roles` | Liste de tous les roles | Oui |

### Regions (`/api/regions`)

| Methode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| GET | `/api/regions` | Liste des regions | Oui |
| GET | `/api/regions/{id}` | Region par ID | Oui |
| POST | `/api/regions` | Creer une region | Oui |
| PUT | `/api/regions/{id}` | Modifier une region | Oui |
| DELETE | `/api/regions/{id}` | Supprimer une region | Oui |

### Classes (`/api/classes`)

| Methode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| GET | `/api/classes` | Classes de mon ecole | Oui |
| GET | `/api/classes/{id}` | Classe par ID | Oui |
| GET | `/api/classes/ecole/{ecoleId}` | Classes par ecole | Oui |
| POST | `/api/classes` | Creer une classe | Oui |
| PUT | `/api/classes/{id}` | Modifier une classe | Oui |
| DELETE | `/api/classes/{id}` | Supprimer une classe | Oui |

## Multi-tenancy

Le systeme utilise le multi-tenancy base sur `ecoleId`:

- L'`ecoleId` est extrait automatiquement du token JWT
- Les utilisateurs ne peuvent acceder qu'aux ressources de leur ecole
- Les admins SaaS (`ecoleId = null`) peuvent voir toutes les ressources

## Codes de Statut HTTP

| Code | Description |
|------|-------------|
| 200 | Succes |
| 201 | Cree avec succes |
| 204 | Supprime avec succes (pas de contenu) |
| 400 | Requete invalide |
| 401 | Non authentifie (token manquant/invalide) |
| 403 | Acces refuse (permissions insuffisantes) |
| 404 | Ressource non trouvee |
| 500 | Erreur serveur |

## Exemples cURL

### Login:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"identifier": "admin@digischool.cm", "password": "Admin@2025"}'
```

### Get Current User:
```bash
curl http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer <votre_token>"
```

### Get Classes:
```bash
curl http://localhost:8080/api/classes \
  -H "Authorization: Bearer <votre_token>"
```

### Create Class:
```bash
curl -X POST http://localhost:8080/api/classes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <votre_token>" \
  -d '{
    "nomClasse": "6eme A",
    "niveau": "COLLEGE",
    "sousSysteme": "FRANCOPHONE",
    "section": "A",
    "capacite": 50,
    "statut": "ACTIVE"
  }'
```
