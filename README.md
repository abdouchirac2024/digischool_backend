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

## 🚀 Démarrage rapide

### 1. Cloner le projet

```bash
git clone <URL_DU_DEPOT>
cd digischool_backend
```

### 2. Builder le JAR

```bash
./mvnw clean package -DskipTests
```

### 3. Lancer les services

```bash
docker compose up --build -d
```

Docker Compose démarre automatiquement **3 conteneurs** :

| Conteneur | Service | Port |
|:---|:---|:---:|
| `mysql-db` | Base de données MySQL 8 | `3306` |
| `phpmyadmin` | Interface admin BDD | `8081` |
| `digischool-backend` | API Spring Boot | `8080` |

> La base de données `school_db` est créée automatiquement au premier démarrage de MySQL.

---

## 🌐 Accès aux services

| Service | URL | Identifiants |
|:---|:---|:---|
| **API Backend** | [http://localhost:8080](http://localhost:8080) | -- |
| **phpMyAdmin** | [http://localhost:8081](http://localhost:8081) | `root` / `1234` |
| **MySQL** | `localhost:3306` | `root` / `1234` |

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

# Reconstruire uniquement le backend
./mvnw clean package -DskipTests && docker compose up --build -d backend

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
