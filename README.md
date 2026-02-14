<p align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white" />
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white" />
  <img src="https://img.shields.io/badge/JWT-Auth-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white" />
  <img src="https://img.shields.io/badge/Swagger-OpenAPI_3-85EA2D?style=for-the-badge&logo=swagger&logoColor=black" />
</p>

# DigiSchool Backend

> Plateforme de gestion scolaire complete pour le Cameroun -- concue avec **Spring Boot 3**, **MySQL 8** et **Docker**.

---

## Table des matieres

- [Stack technique](#stack-technique)
- [Architecture du projet](#architecture-du-projet)
- [Data Seeders](#data-seeders)
- [Prerequis](#prerequis)
- [Demarrage rapide](#demarrage-rapide)
- [Acces aux services](#acces-aux-services)
- [Documentation API](#documentation-api)
- [Authentification](#authentification)
- [Comptes de test](#comptes-de-test)
- [Configuration](#configuration)
- [Multi-tenancy](#multi-tenancy)
- [Securite](#securite)
- [Modele de donnees](#modele-de-donnees)
- [Tester l'API](#tester-lapi)
- [Monitoring](#monitoring)
- [Commandes utiles](#commandes-utiles)
- [Evolutions prevues](#evolutions-prevues)

---

## Stack technique

| Technologie | Role |
|:---|:---|
| **Java 21** | Langage principal |
| **Spring Boot 3.5** | Framework applicatif |
| **Spring Data JPA** | Persistance / ORM Hibernate |
| **Spring Security** | Authentification & autorisation |
| **JWT (jjwt 0.11)** | Tokens d'authentification stateless |
| **SpringDoc OpenAPI** | Documentation Swagger UI |
| **Redis 7** | Cache et stockage en memoire |
| **WebSocket** | Communication temps reel |
| **MySQL 8** | Base de donnees relationnelle |
| **Lombok** | Reduction du code repetitif |
| **Docker & Compose** | Conteneurisation & orchestration |
| **phpMyAdmin** | Interface d'administration BDD |
| **Redis Commander** | Interface d'administration Redis |

---

## Architecture du projet

### Vue d'ensemble

```
digischool_backend/
│
├── src/main/java/...              Code source Java
├── src/main/resources/            Configuration (application.properties)
├── docs/                          Documentation API (Postman, OpenAPI)
├── docker-compose.yml             Orchestration des conteneurs
├── Dockerfile                     Image Docker du backend
├── pom.xml                        Dependances Maven
├── ARCHITECTURE.md                Guide detaille de l'architecture
└── README.md                      Ce fichier
```

### Structure du code source

```
src/main/java/com/digiSchool/digiSchool/
│
├── DigiSchoolApplication.java        # Point d'entree de l'application
│
├── config/                           # CONFIGURATION
│   ├── SecurityConfig.java           # Securite Spring (routes, CORS, etc.)
│   ├── OpenApiConfig.java            # Configuration Swagger UI
│   │
│   └── seeder/                       # DONNEES DE TEST (voir section suivante)
│       ├── DataSeeder.java           # Orchestrateur principal
│       ├── RoleSeeder.java           # Roles (ADMIN, DIRECTEUR, etc.)
│       ├── RegionSeeder.java         # Geographie du Cameroun
│       ├── EcoleSeeder.java          # Ecoles de demonstration
│       ├── AnneeScolaireSeeder.java  # Annees scolaires (par ecole)
│       ├── UserSeeder.java           # Utilisateurs de test
│       └── ClasseSeeder.java         # Classes
│
├── auth/                             # AUTHENTIFICATION
│   ├── controller/
│   │   └── AuthController.java       # POST /api/auth/login, GET /me
│   ├── dto/
│   │   ├── LoginRequest.java         # { login, password } (email ou telephone)
│   │   ├── AuthResponse.java         # { accessToken, refreshToken, user }
│   │   └── RefreshTokenRequest.java  # { refreshToken }
│   ├── service/
│   │   ├── AuthService.java          # Logique d'authentification
│   │   ├── JwtService.java           # Creation/validation tokens JWT
│   │   └── UserContextService.java   # Recupere l'utilisateur courant
│   ├── filter/
│   │   └── JwtAuthenticationFilter.java  # Verifie le token a chaque requete
│   └── exception/
│       └── AuthenticationException.java  # Erreurs d'authentification
│
├── user/                             # UTILISATEURS
│   ├── controller/
│   │   └── RoleController.java       # GET /api/roles
│   ├── model/
│   │   ├── Utilisateur.java          # Entite utilisateur
│   │   ├── Role.java                 # Enum des roles
│   │   ├── StatutUtilisateur.java    # ACTIF, INACTIF, EN_ATTENTE
│   │   ├── Eleve.java                # Entite eleve
│   │   └── StatutEleve.java          # Statuts des eleves
│   └── repository/
│       ├── UtilisateurRepository.java
│       └── RoleRepository.java
│
├── academic/                         # GESTION SCOLAIRE
│   │
│   ├── organisation/                 # Ecoles, Classes, Inscriptions
│   │   ├── controller/
│   │   │   ├── RegionController.java
│   │   │   └── ClasseController.java
│   │   ├── dto/
│   │   │   ├── RegionDto.java
│   │   │   └── ClasseDto.java
│   │   ├── model/
│   │   │   ├── Ecole.java
│   │   │   ├── Classe.java
│   │   │   ├── Inscription.java
│   │   │   ├── Anneescolaire.java
│   │   │   ├── EmploiDuTemps.java
│   │   │   ├── Niveau.java           # MATERNELLE, PRIMAIRE, COLLEGE, LYCEE
│   │   │   ├── SousSysteme.java      # FRANCOPHONE, ANGLOPHONE
│   │   │   └── StatutClasse.java     # ACTIVE, INACTIVE, ARCHIVEE
│   │   ├── service/
│   │   │   ├── RegionService.java
│   │   │   └── ClasseService.java
│   │   ├── serviceimp/
│   │   │   ├── RegionServiceImpl.java
│   │   │   └── ClasseServiceImpl.java
│   │   └── repository/
│   │       ├── RegionRepository.java
│   │       ├── EcoleRepository.java
│   │       ├── ClasseRepository.java
│   │       └── AnneescolaireRepository.java
│   │
│   ├── evaluation/                   # Notes et Evaluations
│   │   └── model/
│   │       ├── Evaluation.java
│   │       └── Note.java
│   │
│   └── pedagogique/                  # Bulletins et Disciplines
│       └── model/
│           ├── Discipline.java
│           ├── Periode.java
│           ├── Bulletin.java
│           └── AppreciationBulletin.java
│
└── Exceptionconfig/                  # GEOGRAPHIE CAMEROUN
    ├── model/
    │   ├── Region.java               # 10 regions du Cameroun
    │   ├── Departement.java          # 58 departements
    │   ├── Arrondissement.java
    │   ├── Ville.java
    │   ├── Quartier.java
    │   └── TenantEntity.java         # Classe de base multi-tenant
    └── repository/
        ├── DepartementRepository.java
        ├── ArrondissementRepository.java
        ├── VilleRepository.java
        └── QuartierRepository.java
```

### Pattern MVC

Le projet suit le pattern **Model-View-Controller** :

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   Client    │───>│  Controller │───>│   Service   │───>│ Repository  │
│ (Frontend)  │<───│   (API)     │<───│  (Logique)  │<───│   (BDD)     │
└─────────────┘    └─────────────┘    └─────────────┘    └─────────────┘
```

| Couche | Role | Exemple |
|:-------|:-----|:--------|
| **Controller** | Recoit les requetes HTTP, valide les entrees | `AuthController.java` |
| **Service** | Contient la logique metier | `AuthService.java` |
| **Repository** | Communique avec la base de donnees | `UtilisateurRepository.java` |
| **Model** | Represente les tables de la BDD (entites JPA) | `Utilisateur.java` |
| **DTO** | Donnees transferees via l'API (input/output) | `LoginRequest.java` |

---

## Data Seeders

Les **seeders** creent automatiquement des donnees de demonstration au demarrage de l'application.

### Structure des seeders

```
config/seeder/
│
├── DataSeeder.java           # Orchestrateur - appelle les autres dans l'ordre
├── RoleSeeder.java           # Cree les 5 roles
├── RegionSeeder.java         # Cree les 10 regions du Cameroun + villes + quartiers
├── EcoleSeeder.java          # Cree 3 ecoles de demonstration
├── AnneeScolaireSeeder.java  # Cree 6 annees scolaires (2 par ecole)
├── UserSeeder.java           # Cree 14 utilisateurs de test
└── ClasseSeeder.java         # Cree 20 classes
```

### Ordre d'execution

Les seeders sont appeles dans un ordre precis pour respecter les dependances :

```
1. RoleSeeder           Cree: ADMIN, DIRECTEUR, ENSEIGNANT, SECRETAIRE, PARENT
        │
        ▼
2. RegionSeeder         Cree: 10 regions, departements, villes, quartiers
        │
        ▼
3. EcoleSeeder          Cree: 3 ecoles (Yaounde, Douala, Bafoussam)
        │
        ▼
4. AnneeScolaireSeeder  Cree: 6 annees (2 par ecole, tenant = ecole.getTenant())
        │
        ▼
5. UserSeeder           Cree: 14 utilisateurs (tenant = ecole.getTenant())
        │
        ▼
6. ClasseSeeder         Cree: 20 classes (tenant = ecole.getTenant())
```

### Donnees creees

| Seeder | Nombre | Tenant | Details |
|:-------|:------:|:------:|:--------|
| **RoleSeeder** | 5 | - | ADMIN, DIRECTEUR, ENSEIGNANT, SECRETAIRE, PARENT |
| **RegionSeeder** | ~200 | - | 10 regions, 58 departements, villes, quartiers |
| **EcoleSeeder** | 3 | `CM-{REGION}-ECOLE-{ID}` | Ecoles a Yaounde, Douala, Bafoussam |
| **AnneeScolaireSeeder** | 6 | `ecole.getTenant()` | 2 annees x 3 ecoles |
| **UserSeeder** | 14 | `ecole.getTenant()` | Admin, directeurs, enseignants, parents, etc. |
| **ClasseSeeder** | 20 | `ecole.getTenant()` | Maternelle, primaire, college, lycee |

### Comportement

- Les seeders verifient si les donnees existent deja avant de les creer
- Pas de doublons si on redemarre l'application
- Pour reinitialiser : `docker compose down -v` (supprime les volumes)

### Logs au demarrage

```
╔══════════════════════════════════════════════════════════════╗
║              DIGISCHOOL - INITIALISATION DES DONNEES         ║
╚══════════════════════════════════════════════════════════════╝

  -> Roles : 5 roles crees (ADMIN, DIRECTEUR, ENSEIGNANT, SECRETAIRE, PARENT)
  -> Geographie : 10 regions du Cameroun creees
  -> Ecoles : 3 ecoles creees
  -> Annees scolaires : 6 annees creees (2 par ecole)
  -> Utilisateurs : 14 utilisateurs crees
  -> Classes : 20 classes creees (19 actives + 1 archivee)

╔══════════════════════════════════════════════════════════════╗
║                    INITIALISATION TERMINEE                   ║
╚══════════════════════════════════════════════════════════════╝
```

---

## Prerequis

| Outil | Version minimale |
|:---|:---|
| **Docker** | >= 20.x |
| **Docker Compose** | >= 2.x |
| **Java 21** | Uniquement pour le build Maven (optionnel avec Docker) |

### Installation de Redis en local (optionnel)

> **Note**: Redis est inclus dans Docker Compose. Cette installation n'est necessaire que pour le developpement local sans Docker.

**Ubuntu / Debian:**
```bash
sudo apt update
sudo apt install redis-server
sudo systemctl start redis-server
sudo systemctl enable redis-server

# Verifier l'installation
redis-cli ping  # Doit retourner PONG
```

**macOS (Homebrew):**
```bash
brew install redis
brew services start redis

# Verifier l'installation
redis-cli ping  # Doit retourner PONG
```

**Windows:**
```bash
# Option 1: WSL2 (recommande)
wsl --install
# Puis suivre les instructions Ubuntu ci-dessus

# Option 2: Chocolatey
choco install redis-64
redis-server

# Verifier l'installation
redis-cli ping  # Doit retourner PONG
```

---

## Demarrage rapide

```bash
# 1. Cloner le projet
git clone <URL_DU_DEPOT>
cd digischool_backend

# 2. Lancer avec Docker
docker compose up --build -d

# 3. Verifier que tout fonctionne
curl http://localhost:8080/actuator/health
```

> Le Dockerfile utilise un **build multi-stage Maven** : plus besoin de `mvn package` en local.

Docker Compose demarre automatiquement **5 conteneurs** :

| Conteneur | Service | Port |
|:---|:---|:---:|
| `mysql-db` | Base de donnees MySQL 8 | `3306` |
| `redis` | Cache Redis 7 | `6380` |
| `phpmyadmin` | Interface admin BDD | `8081` |
| `redis-commander` | Interface admin Redis | `8084` |
| `digischool-backend` | API Spring Boot | `8080` |

---

## Acces aux services

| Service | URL | Description |
|:---|:---|:---|
| **API Backend** | http://localhost:8080 | API REST |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | Documentation interactive |
| **OpenAPI JSON** | http://localhost:8080/v3/api-docs | Specification OpenAPI |
| **phpMyAdmin** | http://localhost:8081 | Interface BDD (`root` / `1234`) |
| **Redis Commander** | http://localhost:8084 | Interface Redis |
| **MySQL** | `localhost:3306` | Connexion directe (`root` / `1234`) |
| **Redis** | `localhost:6380` | Connexion directe |

---

## Documentation API

### Swagger UI (Recommande)

Accedez a l'interface interactive Swagger UI :

```
http://localhost:8080/swagger-ui.html
```

**Comment utiliser :**

1. Cliquez sur `POST /api/auth/login` → "Try it out"
2. Entrez : `{"login": "admin@digischool.cm", "password": "Admin@2025"}`
3. Copiez le `accessToken` de la reponse
4. Cliquez sur "Authorize" (cadenas) → Entrez : `Bearer <token>`
5. Testez tous les endpoints

### Postman

Une collection Postman complete est disponible dans `docs/` :

```
docs/
├── DigiSchool_API.postman_collection.json    # Collection complete
├── DigiSchool_Local.postman_environment.json # Variables d'environnement
├── openapi.yaml                               # Specification OpenAPI
└── README.md                                  # Guide d'utilisation
```

---

## Authentification

### Login avec Email OU Telephone

Le champ `login` accepte un email ou un numero de telephone.

```bash
# Login avec email
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login": "admin@digischool.cm", "password": "Admin@2025"}'

# Login avec telephone
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login": "+237677123456", "password": "Directeur@2025"}'
```

### Reponse

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "user": {
    "id": 1,
    "email": "admin@digischool.cm",
    "nom": "Admin",
    "prenom": "Super",
    "telephone": "+237600000000",
    "role": "SUPER_ADMIN",
    "tenantId": "CM-CENTRE-ECOLE-001",
    "ecoleId": 1,
    "ecoleNom": "Ecole Bilingue La Victoire",
    "codeEcole": "ECB-001"
  }
}
```

### Endpoints d'authentification

| Methode | Endpoint | Description | Auth |
|:---|:---|:---|:---:|
| POST | `/api/auth/login` | Connexion (email ou telephone) | Non |
| GET | `/api/auth/me` | Profil utilisateur connecte | Oui |
| POST | `/api/auth/refresh-token` | Rafraichir le token | Non |
| POST | `/api/auth/forgot-password` | Demander reinitialisation | Non |
| POST | `/api/auth/reset-password` | Reinitialiser le mot de passe | Non |
| POST | `/api/auth/logout` | Deconnexion | Oui |
| POST | `/api/auth/logout-all` | Deconnexion de tous les appareils | Oui |

### Tous les endpoints API (81 routes)

<details>
<summary>Cliquer pour voir la liste complete</summary>

#### Authentification (7 routes)
| Methode | Endpoint |
|:---|:---|
| POST | `/api/auth/login` |
| POST | `/api/auth/refresh-token` |
| POST | `/api/auth/forgot-password` |
| POST | `/api/auth/reset-password` |
| POST | `/api/auth/logout` |
| POST | `/api/auth/logout-all` |
| GET | `/api/auth/me` |

#### Classes (6 routes)
| Methode | Endpoint |
|:---|:---|
| GET | `/api/classes` |
| POST | `/api/classes` |
| GET | `/api/classes/{id}` |
| PUT | `/api/classes/{id}` |
| DELETE | `/api/classes/{id}` |
| GET | `/api/classes/ecole/{ecoleId}` |

#### Eleves (7 routes)
| Methode | Endpoint |
|:---|:---|
| GET | `/api/students` |
| POST | `/api/students` |
| GET | `/api/students/{id}` |
| PUT | `/api/students/{id}` |
| DELETE | `/api/students/{id}` |
| GET | `/api/students/matricule/{matricule}` |
| GET | `/api/students/search` |

#### Enseignants (5 routes)
| Methode | Endpoint |
|:---|:---|
| GET | `/api/teachers` |
| POST | `/api/teachers` |
| GET | `/api/teachers/{id}` |
| PUT | `/api/teachers/{id}` |
| DELETE | `/api/teachers/{id}` |

#### Parents (7 routes)
| Methode | Endpoint |
|:---|:---|
| GET | `/api/parents` |
| POST | `/api/parents` |
| GET | `/api/parents/{id}` |
| PUT | `/api/parents/{id}` |
| DELETE | `/api/parents/{id}` |
| GET | `/api/parents/matricule/{matricule}` |
| GET | `/api/parents/search` |

#### Relations Eleve-Parent (10 routes)
| Methode | Endpoint |
|:---|:---|
| GET | `/api/eleve-parents` |
| POST | `/api/eleve-parents` |
| GET | `/api/eleve-parents/{id}` |
| PUT | `/api/eleve-parents/{id}` |
| DELETE | `/api/eleve-parents/{id}` |
| GET | `/api/eleve-parents/eleve/{eleveId}` |
| GET | `/api/eleve-parents/eleve/{eleveId}/principal` |
| GET | `/api/eleve-parents/eleve/{eleveId}/contacts-urgence` |
| GET | `/api/eleve-parents/eleve/{eleveId}/responsables-legaux` |
| GET | `/api/eleve-parents/parent/{parentId}` |

#### Utilisateurs (5 routes)
| Methode | Endpoint |
|:---|:---|
| GET | `/api/users` |
| POST | `/api/users` |
| GET | `/api/users/{id}` |
| DELETE | `/api/users/{id}` |
| PUT | `/api/users/{id}/status` |

#### Roles (1 route)
| Methode | Endpoint |
|:---|:---|
| GET | `/api/roles` |

#### Geographie - Regions (4 routes)
| Methode | Endpoint |
|:---|:---|
| GET | `/api/regions` |
| POST | `/api/regions` |
| GET | `/api/regions/{id}` |
| DELETE | `/api/regions/{id}` |

#### Geographie - Departements (5 routes)
| Methode | Endpoint |
|:---|:---|
| GET | `/api/departements` |
| POST | `/api/departements` |
| GET | `/api/departements/{id}` |
| PUT | `/api/departements/{id}` |
| DELETE | `/api/departements/{id}` |
| GET | `/api/departements/region/{regionId}` |

#### Geographie - Arrondissements (5 routes)
| Methode | Endpoint |
|:---|:---|
| GET | `/api/arrondissements` |
| POST | `/api/arrondissements` |
| GET | `/api/arrondissements/{id}` |
| PUT | `/api/arrondissements/{id}` |
| DELETE | `/api/arrondissements/{id}` |
| GET | `/api/arrondissements/departement/{departementId}` |

#### Geographie - Villes (5 routes)
| Methode | Endpoint |
|:---|:---|
| GET | `/api/villes` |
| POST | `/api/villes` |
| GET | `/api/villes/{id}` |
| PUT | `/api/villes/{id}` |
| DELETE | `/api/villes/{id}` |
| GET | `/api/villes/arrondissement/{arrondissementId}` |

#### Geographie - Quartiers (5 routes)
| Methode | Endpoint |
|:---|:---|
| GET | `/api/quartiers` |
| POST | `/api/quartiers` |
| GET | `/api/quartiers/{id}` |
| PUT | `/api/quartiers/{id}` |
| DELETE | `/api/quartiers/{id}` |
| GET | `/api/quartiers/ville/{villeId}` |

#### Adresses (5 routes)
| Methode | Endpoint |
|:---|:---|
| GET | `/api/adresses` |
| POST | `/api/adresses` |
| GET | `/api/adresses/{id}` |
| PUT | `/api/adresses/{id}` |
| DELETE | `/api/adresses/{id}` |
| GET | `/api/adresses/quartier/{quartierId}` |

</details>

---

## Comptes de test

Les comptes suivants sont crees automatiquement par les seeders :

| Role | Email | Telephone | Tenant | Password |
|:---|:---|:---|:---|:---|
| **Admin SaaS** | admin@digischool.cm | +237600000000 | `CM-CENTRE-ECOLE-001` | `Admin@2025` |
| **Directeur** | smbarga@lavictoire.cm | +237677123456 | `CM-CENTRE-ECOLE-001` | `Directeur@2025` |
| **Enseignant** | jpkamga@lavictoire.cm | +237670111222 | `CM-CENTRE-ECOLE-001` | `Enseignant@2025` |
| **Secretaire** | catangana@lavictoire.cm | +237660555666 | `CM-CENTRE-ECOLE-001` | `Secretaire@2025` |
| **Parent** | fnkoulou@gmail.com | +237691666777 | `CM-CENTRE-ECOLE-001` | `Parent@2025` |
| **En Attente** | enattente@test.cm | +237699000111 | `CM-CENTRE-ECOLE-001` | `Test@2025` |
| **Inactif** | inactif@test.cm | +237699000222 | `CM-CENTRE-ECOLE-001` | `Test@2025` |

> **Note :** L'Admin SaaS peut acceder a toutes les ressources de toutes les ecoles.

---

## Configuration

### Profils Spring

| Profil | Fichier | Utilisation |
|:---|:---|:---|
| **default** | `application.properties` | Developpement local |
| **docker** | `application-docker.properties` | Docker Compose |

### Variables d'environnement

```properties
# Base de donnees
spring.datasource.url=jdbc:mysql://mysql-db:3306/school_db
spring.datasource.username=root
spring.datasource.password=1234

# JWT
jwt.secret=<votre_secret_256_bits>
jwt.expiration=86400000          # 24h
jwt.refresh-expiration=604800000 # 7 jours

# Swagger
springdoc.swagger-ui.path=/swagger-ui.html
```

---

## Multi-tenancy

### Nomenclature Tenant ID

Le tenant suit le format hierarchique `CM-{REGION}-ECOLE-{ID}` :

| Ecole | Ville | Tenant ID |
|:------|:------|:----------|
| Ecole Bilingue La Victoire | Yaounde | `CM-CENTRE-ECOLE-001` |
| Progressive Comprehensive College | Douala | `CM-LITTORAL-ECOLE-002` |
| Groupe Scolaire Les Champions | Bafoussam | `CM-OUEST-ECOLE-003` |

Le tenant est genere automatiquement par `EcoleSeeder.generateTenantId()` a partir de la hierarchie geographique (Quartier → Ville → Region).

### Entites multi-tenant

Toutes les entites d'une ecole partagent le meme tenant au format `CM-{REGION}-ECOLE-{ID}` :

| Entite | Champ | Format |
|:-------|:------|:-------|
| **Ecole** | `tenant` | `CM-{REGION}-ECOLE-{ID}` |
| **User** | `tenantId` | `CM-{REGION}-ECOLE-{ID}` |
| **Classe** | `tenant` | `CM-{REGION}-ECOLE-{ID}` |
| **Annee Scolaire** | `tenant` | `CM-{REGION}-ECOLE-{ID}` |
| **Eleve** | `tenant` | `CM-{REGION}-ECOLE-{ID}` |
| **Parent** | `tenant` | `CM-{REGION}-ECOLE-{ID}` |
| **Enseignant** | `tenant` | `CM-{REGION}-ECOLE-{ID}` |
| **EleveParent** | `tenant` | `CM-{REGION}-ECOLE-{ID}` |

Chaque utilisateur appartient a une ecole via son `tenant` (extrait automatiquement du JWT).

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Ecole 1   │     │   Ecole 2   │     │   Ecole 3   │
│  La Victoire│     │ Progressive │     │ Champions   │
├─────────────┤     ├─────────────┤     ├─────────────┤
│ Directeur   │     │ Directeur   │     │ Directeur   │
│ Enseignants │     │ Enseignants │     │ Enseignants │
│ Classes     │     │ Classes     │     │ Classes     │
│ Eleves      │     │ Eleves      │     │ Eleves      │
└─────────────┘     └─────────────┘     └─────────────┘
        │                   │                   │
        └───────────────────┼───────────────────┘
                            │
                    ┌───────────────┐
                    │  Admin SaaS   │
                    │ (Acces total) │
                    └───────────────┘
```

- Le `tenant` est extrait du JWT (non manipulable)
- Chaque utilisateur voit uniquement les donnees de son ecole
- L'Admin SaaS voit toutes les ecoles

---

## Securite

| Fonctionnalite | Implementation |
|:---|:---|
| Authentification | JWT + Spring Security |
| Hashage mots de passe | BCrypt |
| Mode | Stateless (pas de session) |

### Routes publiques

```
POST /api/auth/login
POST /api/auth/refresh-token
POST /api/auth/forgot-password
POST /api/auth/reset-password
/swagger-ui/**
/v3/api-docs/**
/actuator/health
```

### Routes protegees

Toutes les autres routes necessitent :
```
Authorization: Bearer <token>
```

---

## Modele de donnees

### Hierarchie geographique

```
Region (10)
  └── Departement (58)
        └── Arrondissement
              └── Ville
                    └── Quartier
                          └── Ecole
                                └── Classe
```

### Entites principales

| Domaine | Entites |
|:---|:---|
| **Geographie** | Region, Departement, Arrondissement, Ville, Quartier, Adresse |
| **Organisation** | Ecole, Classe, Anneescolaire, Inscription, EmploiDuTemps |
| **Utilisateurs** | User, RoleType, Eleve, Enseignant, Parent, EleveParent |
| **Pedagogie** | Discipline, Periode, Evaluation, Note, Bulletin |
| **Auth** | User, RefreshToken, LoginAttempt |

---

## Tester l'API

```bash
# 1. Login et recuperer le token
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login": "admin@digischool.cm", "password": "Admin@2025"}' \
  | jq -r '.accessToken')

# 2. Appeler les endpoints
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/auth/me
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/roles
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/classes
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/regions
```

---

## Monitoring

| Endpoint | Description |
|:---|:---|
| `/actuator/health` | Etat de sante |
| `/actuator/prometheus` | Metriques Prometheus |
| `/actuator/info` | Informations application |

### Architecture complete (avec Monitoring)

```
┌─────────────┐     ┌──────────────┐     ┌─────────────────┐
│   Frontend  │     │   Traefik    │     │   Monitoring    │
│  (Next.js)  │     │   (proxy)    │     │ (Prometheus,    │
│  :3000      │     │   :8180      │     │  Grafana, Loki) │
└──────┬──────┘     └──────┬───────┘     └────────┬────────┘
       │                   │                      │
       └───────────────────┼──────────────────────┘
                           │
              ┌────────────┴────────────┐
              │     Backend Stack       │
              │  ┌────────┐ ┌────────┐  │
              │  │ MySQL  │ │ Spring │  │
              │  │ :3306  │ │ :8080  │  │
              │  └────────┘ └────────┘  │
              └─────────────────────────┘
```

---

## Commandes utiles

```bash
# Demarrer
docker compose up --build -d

# Logs en temps reel
docker logs -f digischool-backend

# Arreter
docker compose down

# Reset complet (supprime la BDD)
docker compose down -v

# Acceder a MySQL
docker exec -it mysql-db mysql -uroot -p1234 school_db

# Verifier la sante
curl http://localhost:8080/actuator/health
```

---

## Evolutions prevues

### Termine

- [x] Authentification JWT (login / refresh-token / logout / logout-all)
- [x] Login avec email OU telephone
- [x] Mot de passe oublie / reinitialisation
- [x] Gestion des statuts utilisateur (ACTIF, EN_ATTENTE, INACTIF)
- [x] CRUD Utilisateurs (users, teachers, students, parents)
- [x] Relations eleve-parent (contacts urgence, responsables legaux)
- [x] CRUD Classes avec isolation par ecole
- [x] Geographie complete du Cameroun (regions, departements, villes, quartiers, adresses)
- [x] Documentation Swagger / OpenAPI (81 endpoints)
- [x] Collection Postman complete
- [x] Multi-tenancy base sur JWT et TenantEntity
- [x] Seeders modulaires et organises (roles, geographie, ecoles, utilisateurs, classes)
- [x] Docker Compose complet (MySQL, Redis, phpMyAdmin, Redis Commander)

### A venir

- [ ] Gestion des inscriptions (workflow complet)
- [ ] Gestion des evaluations et notes
- [ ] Bulletins scolaires (generation PDF)
- [ ] Emplois du temps
- [ ] Paiements et frais de scolarite
- [ ] Notifications WebSocket
- [ ] Migrations avec Flyway
- [ ] CI/CD
- [ ] Tests unitaires et integration

---

## Documentation supplementaire

- **[ARCHITECTURE.md](./ARCHITECTURE.md)** - Guide detaille de l'architecture du projet
- **[docs/README.md](./docs/README.md)** - Guide d'utilisation de l'API

---

<p align="center">
  <b>DigiSchool</b> -- Gestion scolaire moderne pour le Cameroun
</p>
