<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white" />
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white" />
  <img src="https://img.shields.io/badge/JWT-Auth-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white" />
</p>

# DigiSchool Backend

> Plateforme de gestion scolaire complète pour le Cameroun -- conçue avec **Spring Boot 3**, **MySQL 8** et **Docker**.

---

## Table des matières

- [Stack technique](#-stack-technique)
- [Architecture du projet](#-architecture-du-projet)
- [Prérequis](#-prérequis)
- [Démarrage rapide](#-démarrage-rapide)
- [Accès aux services](#-accès-aux-services)
- [Configuration](#%EF%B8%8F-configuration)
- [Multi-tenancy](#-multi-tenancy)
- [Sécurité](#-sécurité)
- [Modèle de données](#-modèle-de-données)
- [Tester l'API](#-tester-lapi)
- [Commandes utiles](#-commandes-utiles)
- [Travail en équipe](#-travail-en-équipe)
- [Évolutions prévues](#-évolutions-prévues)

---

## 🛠 Stack technique

| Technologie | Rôle |
|:---|:---|
| **Java 17** | Langage principal |
| **Spring Boot 3.5** | Framework applicatif |
| **Spring Data JPA** | Persistance / ORM Hibernate |
| **Spring Security** | Authentification & autorisation |
| **JWT (jjwt 0.11)** | Tokens d'authentification stateless |
| **WebSocket** | Communication temps réel |
| **MySQL 8** | Base de données relationnelle |
| **Lombok** | Réduction du code répétitif |
| **Docker & Compose** | Conteneurisation & orchestration |
| **phpMyAdmin** | Interface d'administration BDD |

---

## 📁 Architecture du projet

```
src/main/java/com/digiSchool/digiSchool/
│
├── DigiSchoolApplication.java              Point d'entrée
│
├── academic/
│   ├── evaluation/
│   │   └── model/                          Evaluation, Note
│   │
│   ├── organisation/
│   │   ├── controller/                     RegionController
│   │   ├── dto/                            RegionDto
│   │   ├── model/                          Ecole, Classe, Inscription,
│   │   │                                   EmploiDuTemps, Anneescolaire
│   │   ├── repository/                     RegionRepository
│   │   ├── service/                        RegionService (interface)
│   │   └── serviceimp/                     RegionServiceImpl
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
    └── model/                              Utilisateur, Eleve, Role, StatutEleve
```

---

## 📋 Prérequis

| Outil | Version minimale |
|:---|:---|
| **Docker** | >= 20.x |
| **Docker Compose** | >= 2.x |
| **Java 17** | Uniquement pour le build Maven |

---

## 🚀 Démarrage rapide (backend seul)

```bash
cd digischool_backend
docker compose up --build -d
```

> Le Dockerfile utilise un **build multi-stage Maven** : plus besoin de `mvn package` en local.

Docker Compose démarre automatiquement **3 conteneurs** :

| Conteneur | Service | Port |
|:---|:---|:---:|
| `mysql-db` | Base de données MySQL 8 | `3306` |
| `phpmyadmin` | Interface admin BDD | `8081` |
| `digischool-backend` | API Spring Boot | `8080` |

> La base de données `school_db` est créée automatiquement au premier démarrage de MySQL.

---

## 🔗 Démarrage complet (avec Monitoring, Traefik & Frontend)

Le backend s'intègre dans l'écosystème complet via des réseaux Docker partagés. Les stacks doivent être lancées **dans cet ordre** car chaque stack crée un réseau dont la suivante dépend.

### Architecture réseau

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

### Étape 1 -- Monitoring

Crée le réseau `helpdigischool-monitoring` (Prometheus, Grafana, Loki, Promtail).

```bash
cd helpdigischool/infrastructure/monitoring
docker compose up -d
```

Vérification :

| Service | URL | Identifiants |
|:---|:---|:---|
| Prometheus | http://localhost:9090 | -- |
| Grafana | http://localhost:3001 | `admin` / `admin` |
| Loki | http://localhost:3100/ready | -- |

### Étape 2 -- Traefik

Crée le réseau `traefik-dev` (reverse proxy + dashboard).

```bash
cd helpdigischool/infrastructure/traefik
docker compose -f docker-compose.dev.yml up -d
```

Vérification :

| Service | URL |
|:---|:---|
| Dashboard Traefik | http://localhost:8083 |
| Via hostname | http://traefik.localhost:8180 |

### Étape 3 -- Backend

Crée le réseau `digischool-backend-network` et rejoint les réseaux des étapes précédentes.

```bash
cd digischool_backend
docker compose up --build -d
```

Vérification :
```bash
# Santé
curl http://localhost:8080/actuator/health

# Métriques Prometheus
curl http://localhost:8080/actuator/prometheus

# Accès via Traefik
curl http://api.helpdigischool.localhost:8180/actuator/health
```

### Étape 4 -- Frontend

Rejoint tous les réseaux.

```bash
cd helpdigischool/docker/compose
docker compose -f docker-compose.dev.yml up -d
```

Vérification :

| Service | URL |
|:---|:---|
| Frontend direct | http://localhost:3000 |
| Via Traefik | http://helpdigischool.localhost:8180 |

### Tout lancer d'un coup

```bash
# Depuis la racine du projet
docker compose -f helpdigischool/infrastructure/monitoring/docker-compose.yml up -d
docker compose -f helpdigischool/infrastructure/traefik/docker-compose.dev.yml up -d
docker compose -f digischool_backend/docker-compose.yml up -d --build
docker compose -f helpdigischool/docker/compose/docker-compose.dev.yml up -d
```

### Tout arrêter

```bash
docker compose -f helpdigischool/docker/compose/docker-compose.dev.yml down
docker compose -f digischool_backend/docker-compose.yml down
docker compose -f helpdigischool/infrastructure/traefik/docker-compose.dev.yml down
docker compose -f helpdigischool/infrastructure/monitoring/docker-compose.yml down
```

---

## 🌐 Accès aux services

| Service | URL directe | URL via Traefik | Identifiants |
|:---|:---|:---|:---|
| **API Backend** | http://localhost:8080 | http://api.helpdigischool.localhost:8180 | -- |
| **phpMyAdmin** | http://localhost:8081 | http://phpmyadmin.localhost:8180 | `root` / `1234` |
| **MySQL** | `localhost:3306` | -- | `root` / `1234` |
| **Frontend** | http://localhost:3000 | http://helpdigischool.localhost:8180 | -- |
| **Traefik Dashboard** | http://localhost:8083 | http://traefik.localhost:8180 | -- |
| **Prometheus** | http://localhost:9090 | -- | -- |
| **Grafana** | http://localhost:3001 | -- | `admin` / `admin` |

---

## ⚙️ Configuration

### Profils Spring

| Profil | Fichier | Hôte BDD |
|:---|:---|:---|
| **default** (local) | `application.properties` | `localhost:3306` |
| **docker** | `application-docker.properties` | `mysql-db:3306` |

Le profil `docker` est activé automatiquement via la variable d'environnement dans `docker-compose.yml` :

```yaml
environment:
  SPRING_PROFILES_ACTIVE: docker
```

### Propriétés principales

```properties
# Connexion BDD
spring.datasource.url=jdbc:mysql://<hôte>:3306/school_db
spring.datasource.username=root
spring.datasource.password=1234

# Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Serveur
server.port=8080
```

---

## 🏢 Multi-tenancy

Le projet implémente une architecture **multi-tenant** basée sur un header HTTP :

```
X-Tenant-ID: <identifiant_du_tenant>
```

| Composant | Rôle |
|:---|:---|
| `TenantFilter` | Intercepte chaque requête et extrait le header `X-Tenant-ID` |
| `TenantContext` | Stocke l'identifiant du tenant dans un `ThreadLocal` |
| `HibernateTenantInterceptor` | Filtre automatiquement les entités héritant de `TenantEntity` |

> **En développement** : si le header `X-Tenant-ID` est absent, un tenant par défaut est appliqué automatiquement.

---

## 🔐 Sécurité

| Fonctionnalité | Détail |
|:---|:---|
| **Mode** | Stateless (pas de session côté serveur) |
| **Authentification** | JWT via `JwtService` + `JwtAuthenticationFilter` |
| **Framework** | Spring Security |

### Routes ouvertes en développement

Les endpoints suivants sont accessibles **sans authentification** :

```
/api/**
/swagger-ui/**
/v3/api-docs/**
/actuator/**
```

---

## 📊 Modèle de données

### Organisation géographique & scolaire

```
Région → Département → Arrondissement → Ville → Quartier
                                                    └── École → Classe
```

| Entité | Description |
|:---|:---|
| `Ecole` | Établissement scolaire |
| `Classe` | Classe au sein d'une école |
| `Anneescolaire` | Année scolaire en cours |
| `Inscription` | Inscription d'un élève dans une classe |
| `EmploiDuTemps` | Emploi du temps d'une classe |

### Utilisateurs

| Entité | Description |
|:---|:---|
| `Utilisateur` | Compte utilisateur (tous rôles) |
| `Role` | ADMIN, DIRECTEUR, ENSEIGNANT, SECRETAIRE, PARENT |
| `Eleve` | Profil élève avec statut |
| `StatutEleve` | Actif, Transféré, Exclu, etc. |

### Pédagogie & évaluations

| Entité | Description |
|:---|:---|
| `Discipline` | Matière enseignée (Maths, Français, etc.) |
| `Periode` | Trimestre / Séquence |
| `Evaluation` | Contrôle ou examen |
| `Note` | Note obtenue par un élève |
| `Bulletin` | Bulletin de notes par période |
| `AppreciationBulletin` | Appréciations de l'enseignant / directeur |

---

## 🧪 Tester l'API

### Avec cURL

```bash
# Lister les régions
curl http://localhost:8080/api/regions

# Avec un tenant spécifique
curl -H "X-Tenant-ID: school-001" http://localhost:8080/api/regions
```

### Avec Postman

1. Importer l'URL `http://localhost:8080/api/regions`
2. Ajouter le header `X-Tenant-ID` si nécessaire
3. Aucun login requis en environnement de développement

---

## 📈 Monitoring du backend

### Endpoints Actuator

| Endpoint | Description |
|:---|:---|
| `/actuator/health` | État de santé (UP/DOWN) avec détails |
| `/actuator/prometheus` | Métriques au format Prometheus |
| `/actuator/info` | Informations sur l'application |

### Dashboard Grafana

Un dashboard **DigiSchool Backend** est automatiquement provisionné dans Grafana avec :

| Panel | Description |
|:---|:---|
| Mémoire JVM | Heap et non-heap |
| Requêtes HTTP | Taux de requêtes par seconde |
| Temps de réponse | Percentile 95 |
| Threads JVM | Threads actifs et daemon |
| Connexions DB | Pool HikariCP (active, idle, pending) |
| Garbage Collector | Durée des pauses GC |
| CPU | Utilisation processeur |
| Uptime | Temps depuis le dernier démarrage |

Accès : http://localhost:3001 → Dashboards → **DigiSchool Backend**

### Vérifier les targets Prometheus

Ouvrir http://localhost:9090/targets et vérifier que le target `digischool-backend` est en état **UP**.

### Logs dans Grafana

Les logs du backend sont collectés automatiquement par Promtail et consultables dans Grafana → Explore → Loki (filtrer par `service="digischool-backend"`).

---

## 💻 Commandes utiles

```bash
# Démarrer tous les services
docker compose up --build -d

# Voir les logs du backend en temps réel
docker logs -f digischool-backend

# Arrêter tous les services
docker compose down

# Arrêter et supprimer les volumes (reset complet de la BDD)
docker compose down -v

# Reconstruire après modification du code Java
docker compose up -d --build

# Accéder au shell MySQL
docker exec -it mysql-db mysql -uroot -p1234 school_db
```

---

## 👥 Travail en équipe

```bash
# 1. Cloner le dépôt
git clone <URL_DU_DEPOT>

# 2. Builder et lancer
./mvnw clean package -DskipTests
docker compose up --build -d
```

Chaque développeur dispose du **même environnement** grâce à Docker. Aucune installation locale de MySQL n'est nécessaire.

---

## 📌 Évolutions prévues

- [ ] Authentification JWT complète (login / register / refresh token)
- [ ] Gestion fine des rôles et permissions (RBAC)
- [ ] Profils Spring avancés (dev, staging, prod)
- [ ] Migrations de schéma avec Flyway ou Liquibase
- [ ] Documentation Swagger / OpenAPI
- [ ] CI/CD GitLab (build + tests + déploiement)
- [ ] Tests unitaires et d'intégration
- [ ] Gestion des fichiers (bulletins PDF, photos)

---

<p align="center">
  <b>DigiSchool</b> -- Gestion scolaire moderne pour le Cameroun
</p>
