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

- [Stack technique](#-stack-technique)
- [Architecture du projet](#-architecture-du-projet)
- [Prerequis](#-prerequis)
- [Demarrage rapide](#-demarrage-rapide)
- [Acces aux services](#-acces-aux-services)
- [Documentation API](#-documentation-api)
- [Authentification](#-authentification)
- [Comptes de test](#-comptes-de-test)
- [Configuration](#%EF%B8%8F-configuration)
- [Multi-tenancy](#-multi-tenancy)
- [Securite](#-securite)
- [Modele de donnees](#-modele-de-donnees)
- [Tester l'API](#-tester-lapi)
- [Monitoring](#-monitoring)
- [Commandes utiles](#-commandes-utiles)
- [Travail en equipe](#-travail-en-equipe)
- [Evolutions prevues](#-evolutions-prevues)

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
| **WebSocket** | Communication temps reel |
| **MySQL 8** | Base de donnees relationnelle |
| **Lombok** | Reduction du code repetitif |
| **Docker & Compose** | Conteneurisation & orchestration |
| **phpMyAdmin** | Interface d'administration BDD |

---

## Architecture du projet

```
src/main/java/com/digiSchool/digiSchool/
│
├── DigiSchoolApplication.java              Point d'entree
│
├── config/
│   └── OpenApiConfig.java                  Configuration Swagger/OpenAPI
│
├── academic/
│   ├── evaluation/
│   │   └── model/                          Evaluation, Note
│   │
│   ├── organisation/
│   │   ├── controller/                     RegionController, ClasseController
│   │   ├── dto/                            RegionDto, ClasseDto
│   │   ├── model/                          Ecole, Classe, Inscription,
│   │   │                                   EmploiDuTemps, Anneescolaire
│   │   ├── repository/                     RegionRepository, ClasseRepository
│   │   ├── service/                        RegionService, ClasseService
│   │   └── serviceimp/                     RegionServiceImpl, ClasseServiceImpl
│   │
│   └── pedagogique/
│       └── model/                          Bulletin, Discipline, Periode,
│                                           AppreciationBulletin
│
├── Exceptionconfig/
│   ├── model/                              Region, Departement, Arrondissement,
│   │                                       Ville, Quartier, TenantEntity
│   └── service/                            SecurityConfig, JwtService,
│                                           JwtAuthenticationFilter, TenantFilter,
│                                           TenantContext, HibernateTenantInterceptor
│
└── user/
    ├── controller/                         AuthController, RoleController
    ├── dto/                                LoginRequest, AuthResponse
    ├── model/                              Utilisateur, Eleve, Role, StatutEleve
    ├── repository/                         UtilisateurRepository, RoleRepository
    └── service/                            AuthService
```

---

## Prerequis

| Outil | Version minimale |
|:---|:---|
| **Docker** | >= 20.x |
| **Docker Compose** | >= 2.x |
| **Java 17** | Uniquement pour le build Maven |

---

## Demarrage rapide (backend seul)

```bash
cd digischool_backend
docker compose up --build -d
```

> Le Dockerfile utilise un **build multi-stage Maven** : plus besoin de `mvn package` en local.

Docker Compose demarre automatiquement **3 conteneurs** :

| Conteneur | Service | Port |
|:---|:---|:---:|
| `mysql-db` | Base de donnees MySQL 8 | `3306` |
| `phpmyadmin` | Interface admin BDD | `8081` |
| `digischool-backend` | API Spring Boot | `8080` |

> La base de donnees `school_db` est creee automatiquement au premier demarrage de MySQL.

---

## Acces aux services

| Service | URL | Description |
|:---|:---|:---|
| **API Backend** | http://localhost:8080 | API REST |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | Documentation interactive |
| **OpenAPI JSON** | http://localhost:8080/v3/api-docs | Specification OpenAPI |
| **phpMyAdmin** | http://localhost:8081 | Interface BDD (`root` / `1234`) |
| **MySQL** | `localhost:3306` | Connexion directe (`root` / `1234`) |

---

## Documentation API

### Swagger UI (Recommande)

Accedez a l'interface interactive Swagger UI :

```
http://localhost:8080/swagger-ui.html
```

**Comment utiliser Swagger UI :**

1. **Se connecter :**
   - Cliquez sur `POST /api/auth/login`
   - Cliquez sur "Try it out"
   - Entrez vos identifiants (voir [Comptes de test](#-comptes-de-test))
   - Cliquez "Execute"
   - Copiez le `token` de la reponse

2. **Autoriser :**
   - Cliquez sur le bouton "Authorize" (cadenas en haut a droite)
   - Entrez : `Bearer <votre_token>`
   - Cliquez "Authorize"

3. **Tester les endpoints :**
   - Tous les endpoints sont maintenant accessibles
   - Cliquez sur un endpoint, puis "Try it out"

### Postman

Une collection Postman complete est disponible dans `docs/` :

```bash
docs/
├── DigiSchool_API.postman_collection.json    # Collection complete
├── DigiSchool_Local.postman_environment.json # Variables d'environnement
├── openapi.yaml                               # Specification OpenAPI
└── README.md                                  # Guide d'utilisation
```

**Importer dans Postman :**

1. Ouvrez Postman
2. Cliquez sur "Import"
3. Selectionnez les fichiers `.json`
4. Les tokens sont geres automatiquement apres login

---

## Authentification

### Login avec Email OU Telephone

L'API supporte l'authentification avec **email** ou **numero de telephone** :

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

### Reponse d'authentification

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "nom": "Admin",
    "prenom": "System",
    "email": "admin@digischool.cm",
    "telephone": "+237600000000",
    "role": "ADMIN",
    "statut": "ACTIF",
    "ecoleId": null
  }
}
```

### Statuts utilisateur

| Statut | Description | Connexion |
|:---|:---|:---:|
| `ACTIF` | Compte actif et valide | Autorise |
| `EN_ATTENTE` | En attente de validation | Refuse |
| `INACTIF` | Compte desactive | Refuse |

### Endpoints d'authentification

| Methode | Endpoint | Description | Auth |
|:---|:---|:---|:---:|
| POST | `/api/auth/login` | Connexion (email ou telephone) | Non |
| GET | `/api/auth/me` | Profil utilisateur connecte | Oui |
| POST | `/api/auth/refresh` | Rafraichir le token | Non |
| POST | `/api/auth/logout` | Deconnexion | Oui |

---

## Comptes de test

Les comptes suivants sont crees automatiquement au demarrage (via DataSeeder) :

| Role | Email | Telephone | Password | ecoleId |
|:---|:---|:---|:---|:---:|
| **Admin SaaS** | admin@digischool.cm | +237600000000 | `Admin@2025` | null |
| **Directeur** | smbarga@lavictoire.cm | +237677123456 | `Directeur@2025` | 1 |
| **Enseignant** | jpkamga@lavictoire.cm | +237670111222 | `Enseignant@2025` | 1 |
| **Secretaire** | catangana@lavictoire.cm | +237660555666 | `Secretaire@2025` | 1 |
| **Parent** | fnkoulou@gmail.com | +237691666777 | `Parent@2025` | 1 |
| **En Attente** | enattente@test.cm | +237699000111 | `Test@2025` | 1 |
| **Inactif** | inactif@test.cm | +237699000222 | `Test@2025` | 1 |

> **Note :** L'Admin SaaS (`ecoleId = null`) peut acceder a toutes les ressources de toutes les ecoles.

---

## Configuration

### Profils Spring

| Profil | Fichier | Hote BDD |
|:---|:---|:---|
| **default** (local) | `application.properties` | `localhost:3306` |
| **docker** | `application-docker.properties` | `mysql-db:3306` |

Le profil `docker` est active automatiquement via la variable d'environnement dans `docker-compose.yml` :

```yaml
environment:
  SPRING_PROFILES_ACTIVE: docker
```

### Proprietes principales

```properties
# Connexion BDD
spring.datasource.url=jdbc:mysql://<hote>:3306/school_db
spring.datasource.username=root
spring.datasource.password=1234

# Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
jwt.secret=<votre_secret_256_bits>
jwt.expiration=86400000
jwt.refresh-expiration=604800000

# Swagger
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html

# Serveur
server.port=8080
```

---

## Multi-tenancy

Le projet implemente une architecture **multi-tenant** basee sur le **JWT token** :

| Composant | Role |
|:---|:---|
| `UserContextService` | Extrait l'ecoleId depuis les claims JWT |
| `JwtService` | Decode et valide le token JWT |

### Securite du multi-tenant

- L'ecoleId est extrait **uniquement** du JWT token (signe cryptographiquement)
- Le client ne peut **pas** manipuler le tenant
- Chaque utilisateur accede uniquement aux donnees de son ecole
- L'Admin SaaS (`ecoleId = null`) peut voir toutes les ressources

---

## Securite

| Fonctionnalite | Detail |
|:---|:---|
| **Mode** | Stateless (pas de session cote serveur) |
| **Authentification** | JWT via `JwtService` + `JwtAuthenticationFilter` |
| **Framework** | Spring Security |
| **Hashage mots de passe** | BCrypt |

### Routes publiques

Les endpoints suivants sont accessibles **sans authentification** :

```
POST /api/auth/login
POST /api/auth/refresh
/swagger-ui/**
/v3/api-docs/**
/actuator/health
/actuator/prometheus
```

### Routes protegees

Toutes les autres routes necessitent un token JWT valide dans le header :

```
Authorization: Bearer <votre_token>
```

---

## Modele de donnees

### Organisation geographique & scolaire

```
Region → Departement → Arrondissement → Ville → Quartier
                                                    └── Ecole → Classe
```

| Entite | Description |
|:---|:---|
| `Ecole` | Etablissement scolaire |
| `Classe` | Classe au sein d'une ecole |
| `Anneescolaire` | Annee scolaire en cours |
| `Inscription` | Inscription d'un eleve dans une classe |
| `EmploiDuTemps` | Emploi du temps d'une classe |

### Utilisateurs

| Entite | Description |
|:---|:---|
| `Utilisateur` | Compte utilisateur (tous roles) |
| `Role` | ADMIN, DIRECTEUR, ENSEIGNANT, SECRETAIRE, PARENT |
| `Eleve` | Profil eleve avec statut |
| `StatutEleve` | Actif, Transfere, Exclu, etc. |

### Statuts utilisateur

| Statut | Description |
|:---|:---|
| `ACTIF` | Compte actif, connexion autorisee |
| `EN_ATTENTE` | En attente de validation admin |
| `INACTIF` | Compte desactive |

### Pedagogie & evaluations

| Entite | Description |
|:---|:---|
| `Discipline` | Matiere enseignee (Maths, Francais, etc.) |
| `Periode` | Trimestre / Sequence |
| `Evaluation` | Controle ou examen |
| `Note` | Note obtenue par un eleve |
| `Bulletin` | Bulletin de notes par periode |
| `AppreciationBulletin` | Appreciations de l'enseignant / directeur |

---

## Tester l'API

### Avec cURL

```bash
# 1. Login
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"identifier": "admin@digischool.cm", "password": "Admin@2025"}' \
  | jq -r '.token')

# 2. Obtenir le profil utilisateur
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/auth/me

# 3. Lister les roles
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/roles

# 4. Lister les classes
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/classes

# 5. Lister les regions
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/regions
```

### Avec Swagger UI

1. Ouvrir http://localhost:8080/swagger-ui.html
2. Executer `POST /api/auth/login` avec les identifiants de test
3. Copier le token et cliquer "Authorize"
4. Tester tous les endpoints

### Avec Postman

1. Importer la collection depuis `docs/DigiSchool_API.postman_collection.json`
2. Importer l'environnement depuis `docs/DigiSchool_Local.postman_environment.json`
3. Executer "Login (Email)" - le token est sauvegarde automatiquement
4. Toutes les requetes utilisent ce token

---

## Monitoring

### Endpoints Actuator

| Endpoint | Description |
|:---|:---|
| `/actuator/health` | Etat de sante (UP/DOWN) avec details |
| `/actuator/prometheus` | Metriques au format Prometheus |
| `/actuator/info` | Informations sur l'application |

### Dashboard Grafana

Un dashboard **DigiSchool Backend** est automatiquement provisionne dans Grafana avec :

| Panel | Description |
|:---|:---|
| Memoire JVM | Heap et non-heap |
| Requetes HTTP | Taux de requetes par seconde |
| Temps de reponse | Percentile 95 |
| Threads JVM | Threads actifs et daemon |
| Connexions DB | Pool HikariCP (active, idle, pending) |
| Garbage Collector | Duree des pauses GC |
| CPU | Utilisation processeur |
| Uptime | Temps depuis le dernier demarrage |

Acces : http://localhost:3001 → Dashboards → **DigiSchool Backend**

### Verifier les targets Prometheus

Ouvrir http://localhost:9090/targets et verifier que le target `digischool-backend` est en etat **UP**.

### Logs dans Grafana

Les logs du backend sont collectes automatiquement par Promtail et consultables dans Grafana → Explore → Loki (filtrer par `service="digischool-backend"`).

---

## Demarrage complet (avec Monitoring, Traefik & Frontend)

Le backend s'integre dans l'ecosysteme complet via des reseaux Docker partages.

### Architecture reseau

```
┌─────────────┐     ┌──────────────┐     ┌─────────────────┐
│   Frontend   │     │   Traefik    │     │   Monitoring    │
│  (Next.js)   │     │ (reverse     │     │ (Prometheus,    │
│  :3000       │     │  proxy)      │     │  Grafana, Loki) │
└──────┬───────┘     └──────┬───────┘     └────────┬────────┘
       │                    │                      │
       │  digischool-       │  traefik-dev         │  helpdigischool-
       │  backend-network   │                      │  monitoring
       │                    │                      │
┌──────┴────────────────────┴──────────────────────┴────────┐
│                    Backend Stack                           │
│  ┌────────────┐  ┌──────────────┐  ┌───────────────┐     │
│  │  MySQL 8.0 │  │  Spring Boot │  │  PhpMyAdmin   │     │
│  │  :3306     │  │  :8080       │  │  :8081        │     │
│  └────────────┘  └──────────────┘  └───────────────┘     │
└───────────────────────────────────────────────────────────┘
```

### Lancement par etapes

```bash
# 1. Monitoring (cree le reseau helpdigischool-monitoring)
cd helpdigischool/infrastructure/monitoring
docker compose up -d

# 2. Traefik (cree le reseau traefik-dev)
cd helpdigischool/infrastructure/traefik
docker compose -f docker-compose.dev.yml up -d

# 3. Backend (cree digischool-backend-network, rejoint les autres)
cd digischool_backend
docker compose up --build -d

# 4. Frontend (rejoint tous les reseaux)
cd helpdigischool/docker/compose
docker compose -f docker-compose.dev.yml up -d
```

### URLs via Traefik

| Service | URL via Traefik |
|:---|:---|
| API Backend | http://api.helpdigischool.localhost:8180 |
| Frontend | http://helpdigischool.localhost:8180 |
| phpMyAdmin | http://phpmyadmin.localhost:8180 |
| Traefik Dashboard | http://traefik.localhost:8180 |

---

## Commandes utiles

```bash
# Demarrer tous les services
docker compose up --build -d

# Voir les logs du backend en temps reel
docker logs -f digischool-backend

# Arreter tous les services
docker compose down

# Arreter et supprimer les volumes (reset complet de la BDD)
docker compose down -v

# Reconstruire apres modification du code Java
docker compose up -d --build

# Acceder au shell MySQL
docker exec -it mysql-db mysql -uroot -p1234 school_db

# Tester la sante de l'API
curl http://localhost:8080/actuator/health

# Verifier que Swagger est accessible
curl -I http://localhost:8080/swagger-ui.html
```

---

## Travail en equipe

```bash
# 1. Cloner le depot
git clone <URL_DU_DEPOT>

# 2. Builder et lancer
cd digischool_backend
docker compose up --build -d
```

Chaque developpeur dispose du **meme environnement** grace a Docker. Aucune installation locale de MySQL n'est necessaire.

---

## Evolutions prevues

### Termine

- [x] Authentification JWT complete (login / refresh token / logout)
- [x] Login avec email OU telephone
- [x] Gestion des statuts utilisateur (ACTIF, EN_ATTENTE, INACTIF)
- [x] Documentation Swagger / OpenAPI
- [x] Collection Postman complete
- [x] Multi-tenancy base sur JWT (ecoleId)
- [x] Monitoring avec Prometheus/Grafana

### En cours

- [ ] Gestion fine des roles et permissions (RBAC)
- [ ] Endpoints CRUD pour Utilisateurs
- [ ] Gestion des eleves et inscriptions

### A venir

- [ ] Profils Spring avances (dev, staging, prod)
- [ ] Migrations de schema avec Flyway ou Liquibase
- [ ] CI/CD GitLab (build + tests + deploiement)
- [ ] Tests unitaires et d'integration
- [ ] Gestion des fichiers (bulletins PDF, photos)
- [ ] Notifications en temps reel (WebSocket)

---

## Codes de statut HTTP

| Code | Description |
|:---|:---|
| 200 | Succes |
| 201 | Cree avec succes |
| 204 | Supprime avec succes (pas de contenu) |
| 400 | Requete invalide |
| 401 | Non authentifie (token manquant/invalide) |
| 403 | Acces refuse (permissions insuffisantes) |
| 404 | Ressource non trouvee |
| 500 | Erreur serveur |

---

<p align="center">
  <b>DigiSchool</b> -- Gestion scolaire moderne pour le Cameroun
</p>
