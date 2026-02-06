<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk&logoColor=white" />
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
| **Java 17** | Langage principal |
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
│       ├── AnneeScolaireSeeder.java  # Annees scolaires
│       ├── UtilisateurSeeder.java    # Utilisateurs de test
│       └── ClasseSeeder.java         # Classes
│
├── auth/                             # AUTHENTIFICATION
│   ├── controller/
│   │   └── AuthController.java       # POST /api/auth/login, GET /me
│   ├── dto/
│   │   ├── LoginRequest.java         # { identifier, password }
│   │   ├── LoginResponse.java        # { token, refreshToken, user }
│   │   └── UserDto.java              # Representation utilisateur
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
├── AnneeScolaireSeeder.java  # Cree les annees scolaires
├── UtilisateurSeeder.java    # Cree 14 utilisateurs de test
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
4. AnneeScolaireSeeder  Cree: 2024-2025 (archivee), 2025-2026 (active)
        │
        ▼
5. UtilisateurSeeder    Cree: 14 utilisateurs avec differents roles
        │
        ▼
6. ClasseSeeder         Cree: 20 classes (maternelle au lycee)
```

### Donnees creees

| Seeder | Nombre | Details |
|:-------|:------:|:--------|
| **RoleSeeder** | 5 | ADMIN, DIRECTEUR, ENSEIGNANT, SECRETAIRE, PARENT |
| **RegionSeeder** | ~200 | 10 regions, 58 departements, villes, quartiers |
| **EcoleSeeder** | 3 | Ecoles a Yaounde, Douala, Bafoussam |
| **AnneeScolaireSeeder** | 2 | 2024-2025 (archivee), 2025-2026 (active) |
| **UtilisateurSeeder** | 14 | Admin, directeurs, enseignants, parents, etc. |
| **ClasseSeeder** | 20 | Maternelle, primaire, college, lycee |

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
  -> Annees scolaires : 2 annees creees
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
| **Java 17** | Uniquement pour le build Maven (optionnel avec Docker) |

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
2. Entrez : `{"identifier": "admin@digischool.cm", "password": "Admin@2025"}`
3. Copiez le `token` de la reponse
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

```bash
# Login avec email
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"identifier": "admin@digischool.cm", "password": "Admin@2025"}'

# Login avec telephone
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"identifier": "+237600000000", "password": "Admin@2025"}'
```

### Reponse

```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
  "user": {
    "id": 1,
    "nom": "Admin",
    "prenom": "Super",
    "email": "admin@digischool.cm",
    "telephone": "+237600000000",
    "role": "ADMIN",
    "statut": "ACTIF",
    "ecoleId": null
  }
}
```

### Endpoints

| Methode | Endpoint | Description | Auth |
|:---|:---|:---|:---:|
| POST | `/api/auth/login` | Connexion | Non |
| GET | `/api/auth/me` | Profil utilisateur | Oui |
| POST | `/api/auth/refresh` | Rafraichir le token | Non |
| POST | `/api/auth/logout` | Deconnexion | Oui |

---

## Comptes de test

Les comptes suivants sont crees automatiquement par les seeders :

| Role | Email | Telephone | Password |
|:---|:---|:---|:---|
| **Admin SaaS** | admin@digischool.cm | +237600000000 | `Admin@2025` |
| **Directeur** | smbarga@lavictoire.cm | +237677123456 | `Directeur@2025` |
| **Enseignant** | jpkamga@lavictoire.cm | +237670111222 | `Enseignant@2025` |
| **Secretaire** | catangana@lavictoire.cm | +237660555666 | `Secretaire@2025` |
| **Parent** | fnkoulou@gmail.com | +237691666777 | `Parent@2025` |
| **En Attente** | enattente@test.cm | +237699000111 | `Test@2025` |
| **Inactif** | inactif@test.cm | +237699000222 | `Test@2025` |

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

Chaque utilisateur appartient a une ecole (`ecoleId` dans le JWT).

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

- L'`ecoleId` est extrait du JWT (non manipulable)
- Chaque utilisateur voit uniquement les donnees de son ecole
- L'Admin SaaS (`ecoleId = null`) voit tout

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
POST /api/auth/refresh
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
| **Geographie** | Region, Departement, Arrondissement, Ville, Quartier |
| **Organisation** | Ecole, Classe, Anneescolaire, Inscription |
| **Utilisateurs** | Utilisateur, Role, Eleve |
| **Pedagogie** | Discipline, Periode, Evaluation, Note, Bulletin |

---

## Tester l'API

```bash
# 1. Login et recuperer le token
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"identifier": "admin@digischool.cm", "password": "Admin@2025"}' \
  | jq -r '.token')

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

- [x] Authentification JWT (login / refresh / logout)
- [x] Login avec email OU telephone
- [x] Gestion des statuts utilisateur (ACTIF, EN_ATTENTE, INACTIF)
- [x] Documentation Swagger / OpenAPI
- [x] Collection Postman complete
- [x] Multi-tenancy base sur JWT
- [x] Monitoring Prometheus/Grafana
- [x] Seeders modulaires et organises

### En cours

- [ ] Gestion fine des roles et permissions (RBAC)
- [ ] CRUD Utilisateurs
- [ ] Gestion des eleves et inscriptions

### A venir

- [ ] Migrations avec Flyway
- [ ] CI/CD GitLab
- [ ] Tests unitaires et integration
- [ ] Gestion des fichiers (bulletins PDF)
- [ ] Notifications WebSocket

---

## Documentation supplementaire

- **[ARCHITECTURE.md](./ARCHITECTURE.md)** - Guide detaille de l'architecture du projet
- **[docs/README.md](./docs/README.md)** - Guide d'utilisation de l'API

---

<p align="center">
  <b>DigiSchool</b> -- Gestion scolaire moderne pour le Cameroun
</p>
