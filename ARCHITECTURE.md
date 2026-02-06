# Architecture du Projet DigiSchool Backend

> Guide simple pour comprendre la structure du projet

---

## Vue d'ensemble

```
digischool_backend/
│
├── src/                    Code source Java
├── docs/                   Documentation API (Postman, OpenAPI)
├── target/                 Fichiers compiles (genere automatiquement)
├── docker-compose.yml      Configuration Docker
├── Dockerfile              Image Docker du backend
├── pom.xml                 Dependances Maven
└── README.md               Documentation principale
```

---

## Fichiers a la racine

| Fichier | A quoi ca sert |
|:--------|:---------------|
| `pom.xml` | Liste des bibliotheques utilisees (Spring, MySQL, JWT, etc.) |
| `Dockerfile` | Instructions pour creer l'image Docker du backend |
| `docker-compose.yml` | Lance MySQL + phpMyAdmin + Backend ensemble |
| `.env` | Variables secretes (mots de passe, cles JWT) - **NE PAS COMMITER** |
| `.env.example` | Exemple de fichier .env (sans secrets) |
| `mvnw` / `mvnw.cmd` | Scripts pour lancer Maven sans l'installer |
| `.gitignore` | Fichiers a ignorer par Git |

---

## Dossier `docs/`

Documentation pour tester l'API :

| Fichier | A quoi ca sert |
|:--------|:---------------|
| `DigiSchool_API.postman_collection.json` | Collection Postman avec toutes les requetes |
| `DigiSchool_Local.postman_environment.json` | Variables Postman (URL, tokens) |
| `openapi.yaml` | Specification OpenAPI / Swagger |
| `README.md` | Guide d'utilisation de l'API |

---

## Dossier `src/main/resources/`

Fichiers de configuration :

| Fichier | A quoi ca sert |
|:--------|:---------------|
| `application.properties` | Configuration pour developpement local |
| `application-docker.properties` | Configuration pour Docker |

---

## Structure du code Java

```
src/main/java/com/digiSchool/digiSchool/
│
├── DigiSchoolApplication.java      Point de demarrage de l'application
│
├── config/                         Configuration globale
├── auth/                           Authentification (login, JWT)
├── user/                           Gestion des utilisateurs
├── academic/                       Gestion scolaire
│   ├── organisation/               Ecoles, classes, inscriptions
│   ├── evaluation/                 Notes, evaluations
│   └── pedagogique/                Bulletins, disciplines
│
└── Exceptionconfig/                Geographie (regions, villes, etc.)
```

---

## Dossier `config/` - Configuration

| Fichier | A quoi ca sert |
|:--------|:---------------|
| `SecurityConfig.java` | Configure la securite (quelles routes sont protegees) |
| `OpenApiConfig.java` | Configure Swagger UI (documentation interactive) |

### Sous-dossier `config/seeder/` - Donnees de test

Les seeders creent les donnees de demonstration au demarrage.
Chaque seeder est responsable d'un type de donnees.

```
config/seeder/
├── DataSeeder.java           # Orchestrateur - appelle les autres dans l'ordre
├── RoleSeeder.java           # Cree les 5 roles (ADMIN, DIRECTEUR, etc.)
├── RegionSeeder.java         # Cree les 10 regions du Cameroun + villes + quartiers
├── EcoleSeeder.java          # Cree 3 ecoles de demonstration
├── AnneeScolaireSeeder.java  # Cree les annees scolaires (2024-2025, 2025-2026)
├── UtilisateurSeeder.java    # Cree 14 utilisateurs de test
└── ClasseSeeder.java         # Cree 20 classes
```

**Ordre d'execution :**

```
1. RoleSeeder        (pas de dependance)
        ↓
2. RegionSeeder      (pas de dependance)
        ↓
3. EcoleSeeder       (depend des quartiers)
        ↓
4. AnneeScolaireSeeder (pas de dependance)
        ↓
5. UtilisateurSeeder (depend des roles + ecoles)
        ↓
6. ClasseSeeder      (depend des ecoles + annees + utilisateurs)
```

**Avantages de cette structure :**
- Chaque seeder est independant et facile a modifier
- On peut ajouter/supprimer des donnees sans toucher aux autres
- Le code est plus lisible et maintenable
- Chaque seeder verifie si les donnees existent deja (pas de doublons)

---

## Dossier `auth/` - Authentification

Gere la connexion et les tokens JWT.

### Structure
```
auth/
├── controller/
│   └── AuthController.java       Endpoints: /api/auth/login, /me, /refresh
├── dto/
│   ├── LoginRequest.java         Donnees recues: { identifier, password }
│   ├── LoginResponse.java        Donnees envoyees: { token, user }
│   └── UserDto.java              Representation de l'utilisateur
├── service/
│   ├── AuthService.java          Logique de connexion
│   ├── JwtService.java           Creation et validation des tokens JWT
│   └── UserContextService.java   Recupere l'utilisateur connecte
├── filter/
│   └── JwtAuthenticationFilter.java   Verifie le token a chaque requete
└── exception/
    └── AuthenticationException.java   Erreurs d'authentification
```

### Comment ca marche ?

1. L'utilisateur envoie email/telephone + mot de passe
2. `AuthController` recoit la requete
3. `AuthService` verifie les identifiants dans la base
4. `JwtService` cree un token JWT
5. Le token est renvoye a l'utilisateur
6. Pour les prochaines requetes, `JwtAuthenticationFilter` verifie le token

---

## Dossier `user/` - Utilisateurs

Gere les utilisateurs et leurs roles.

### Structure
```
user/
├── controller/
│   └── RoleController.java       Endpoint: GET /api/roles
├── model/
│   ├── Utilisateur.java          Entite utilisateur (nom, email, mot de passe)
│   ├── Role.java                 Enum: ADMIN, DIRECTEUR, ENSEIGNANT, etc.
│   ├── StatutUtilisateur.java    Enum: ACTIF, INACTIF, EN_ATTENTE
│   ├── Eleve.java                Entite eleve
│   └── StatutEleve.java          Enum: statuts des eleves
└── repository/
    ├── UtilisateurRepository.java    Requetes vers la table utilisateur
    └── RoleRepository.java           Requetes vers la table role
```

### Les roles

| Role | Description |
|:-----|:------------|
| `ADMIN` | Administrateur SaaS (acces a toutes les ecoles) |
| `DIRECTEUR` | Directeur d'ecole |
| `ENSEIGNANT` | Professeur |
| `SECRETAIRE` | Secretaire administratif |
| `PARENT` | Parent d'eleve |

### Les statuts utilisateur

| Statut | Description |
|:-------|:------------|
| `ACTIF` | Compte actif, peut se connecter |
| `EN_ATTENTE` | En attente de validation |
| `INACTIF` | Compte desactive |

---

## Dossier `academic/` - Gestion scolaire

Contient tout ce qui concerne les ecoles, classes, notes, etc.

### Sous-dossier `organisation/` - Ecoles et classes

```
organisation/
├── controller/
│   ├── RegionController.java     CRUD regions
│   └── ClasseController.java     CRUD classes
├── dto/
│   ├── RegionDto.java            Donnees region pour l'API
│   └── ClasseDto.java            Donnees classe pour l'API
├── model/
│   ├── Ecole.java                Entite ecole
│   ├── Classe.java               Entite classe
│   ├── Inscription.java          Liaison eleve <-> classe
│   ├── Anneescolaire.java        Annee scolaire (2024-2025)
│   ├── EmploiDuTemps.java        Emploi du temps
│   ├── Niveau.java               Enum: MATERNELLE, PRIMAIRE, COLLEGE, LYCEE
│   ├── SousSysteme.java          Enum: FRANCOPHONE, ANGLOPHONE
│   └── StatutClasse.java         Enum: ACTIVE, INACTIVE
├── service/
│   ├── RegionService.java        Interface du service
│   └── ClasseService.java        Interface du service
├── serviceimp/
│   ├── RegionServiceImpl.java    Implementation de la logique
│   └── ClasseServiceImpl.java    Implementation de la logique
└── repository/
    ├── RegionRepository.java     Requetes BDD regions
    ├── EcoleRepository.java      Requetes BDD ecoles
    ├── ClasseRepository.java     Requetes BDD classes
    └── AnneescolaireRepository.java
```

### Sous-dossier `evaluation/` - Notes

```
evaluation/
└── model/
    ├── Evaluation.java           Un controle ou examen
    └── Note.java                 Note d'un eleve a une evaluation
```

### Sous-dossier `pedagogique/` - Bulletins

```
pedagogique/
└── model/
    ├── Discipline.java           Matiere (Maths, Francais, etc.)
    ├── Periode.java              Trimestre ou sequence
    ├── Bulletin.java             Bulletin de notes
    └── AppreciationBulletin.java Appreciation du prof/directeur
```

---

## Dossier `Exceptionconfig/` - Geographie

Gere la hierarchie geographique du Cameroun.

### Structure
```
Exceptionconfig/
├── model/
│   ├── Region.java               Ex: Centre, Littoral, Ouest
│   ├── Departement.java          Ex: Mfoundi, Wouri
│   ├── Arrondissement.java       Ex: Yaounde 1, Douala 3
│   ├── Ville.java                Ex: Yaounde, Douala
│   ├── Quartier.java             Ex: Bastos, Akwa
│   └── TenantEntity.java         Classe de base pour le multi-tenancy
└── repository/
    ├── DepartementRepository.java
    ├── ArrondissementRepository.java
    ├── VilleRepository.java
    └── QuartierRepository.java
```

### Hierarchie geographique

```
Region
  └── Departement
        └── Arrondissement
              └── Ville
                    └── Quartier
                          └── Ecole
```

---

## Pattern MVC utilise

Le projet suit le pattern **Model-View-Controller** :

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   Client    │───>│  Controller │───>│   Service   │───>│ Repository  │
│ (Frontend)  │<───│   (API)     │<───│  (Logique)  │<───│   (BDD)     │
└─────────────┘    └─────────────┘    └─────────────┘    └─────────────┘
```

| Couche | Role | Exemple |
|:-------|:-----|:--------|
| **Controller** | Recoit les requetes HTTP | `AuthController.java` |
| **Service** | Contient la logique metier | `AuthService.java` |
| **Repository** | Communique avec la base de donnees | `UtilisateurRepository.java` |
| **Model** | Represente les tables de la BDD | `Utilisateur.java` |
| **DTO** | Donnees envoyees/recues par l'API | `LoginRequest.java` |

---

## Flux d'une requete

Exemple : Login d'un utilisateur

```
1. POST /api/auth/login
   { "identifier": "admin@digischool.cm", "password": "Admin@2025" }
                    │
                    ▼
2. AuthController.login()
   - Recoit la requete
   - Appelle AuthService
                    │
                    ▼
3. AuthService.login()
   - Cherche l'utilisateur par email ou telephone
   - Verifie le mot de passe avec BCrypt
   - Verifie le statut (ACTIF?)
   - Cree le token JWT
                    │
                    ▼
4. UtilisateurRepository.findByEmail()
   - Execute: SELECT * FROM utilisateur WHERE email = ?
                    │
                    ▼
5. Reponse
   { "token": "eyJ...", "user": { "nom": "Admin", ... } }
```

---

## Multi-tenancy (Multi-ecoles)

Chaque utilisateur appartient a une ecole (`ecoleId`).

### Comment ca marche ?

1. L'`ecoleId` est stocke dans le token JWT
2. A chaque requete, `UserContextService` extrait l'`ecoleId`
3. Les requetes BDD filtrent automatiquement par `ecoleId`
4. Un utilisateur ne voit que les donnees de son ecole

### Exception : Admin SaaS

L'Admin SaaS a `ecoleId = null` et peut voir **toutes** les ecoles.

---

## Resume visuel

```
digischool_backend/
│
├── pom.xml                              # Dependances
├── docker-compose.yml                   # Docker
│
└── src/main/java/.../digiSchool/
    │
    ├── DigiSchoolApplication.java       # Point d'entree
    │
    ├── config/
    │   ├── SecurityConfig.java          # Securite Spring
    │   ├── OpenApiConfig.java           # Swagger
    │   │
    │   └── seeder/                      # DONNEES DE TEST
    │       ├── DataSeeder.java          # Orchestrateur principal
    │       ├── RoleSeeder.java          # Roles
    │       ├── RegionSeeder.java        # Geographie Cameroun
    │       ├── EcoleSeeder.java         # Ecoles
    │       ├── AnneeScolaireSeeder.java # Annees scolaires
    │       ├── UtilisateurSeeder.java   # Utilisateurs
    │       └── ClasseSeeder.java        # Classes
    │
    ├── auth/                            # AUTHENTIFICATION
    │   ├── controller/AuthController    # POST /api/auth/login
    │   ├── service/AuthService          # Logique login
    │   ├── service/JwtService           # Tokens JWT
    │   └── filter/JwtAuthFilter         # Verification token
    │
    ├── user/                            # UTILISATEURS
    │   ├── model/Utilisateur            # Table utilisateur
    │   ├── model/Role                   # ADMIN, DIRECTEUR, etc.
    │   └── repository/                  # Requetes BDD
    │
    ├── academic/
    │   ├── organisation/                # ECOLES & CLASSES
    │   │   ├── model/Ecole, Classe
    │   │   ├── controller/              # API REST
    │   │   └── service/                 # Logique metier
    │   │
    │   ├── evaluation/                  # NOTES
    │   │   └── model/Note, Evaluation
    │   │
    │   └── pedagogique/                 # BULLETINS
    │       └── model/Bulletin, Discipline
    │
    └── Exceptionconfig/                 # GEOGRAPHIE
        └── model/Region, Departement, Ville, Quartier
```

---

## Commandes utiles

```bash
# Lancer le projet
docker compose up --build -d

# Voir les logs
docker logs -f digischool-backend

# Acceder a Swagger
open http://localhost:8080/swagger-ui.html

# Acceder a phpMyAdmin
open http://localhost:8081
```

---

<p align="center">
  <b>DigiSchool Backend</b> - Architecture simple et claire
</p>
