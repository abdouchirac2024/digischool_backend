# DigiSchool Backend

Backend de gestion scolaire développé avec **Spring Boot**.
Le projet inclut une architecture DTO, une sécurité Spring, et une gestion **multi-tenant** avec un tenant par défaut pour le développement.

---

## Stack technique

- Java 17
- Spring Boot
  - Spring Data JPA
  - Spring Security
- MySQL 8
- Docker & Docker Compose
- phpMyAdmin
- Architecture DTO / Entity / Repository / Service
- Multi-tenant (tenant par défaut en développement)

---

## Structure du projet

```text
src/main/java/com/digiSchool/digiSchool
├── config
│   ├── security
│   ├── tenant
├── controller
├── dto
├── entity
├── repository
├── service
└── DigiSchoolApplication.java

---

## application.properties

```properties
spring.application.name=digiSchool

spring.datasource.url=jdbc:mysql://mysql-db:3306/school_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=1234

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8080

---

## Lancement avec Docker

```Prérequis
Docker
Docker Compose

```Démarrage du projet
   ```À la racine du projet :
. docker compose up --build

Docker Compose lance automatiquement :

MySQL

phpMyAdmin

le backend Spring Boot

La base de données school_db est créée automatiquement au démarrage de MySQL.

Accès aux services
Service	URL
Backend	http://localhost:8080

Swagger	http://localhost:8080/swagger-ui.html

phpMyAdmin	http://localhost:8081

MySQL	localhost:3306


🏷 Multi-tenancy

Le projet utilise un filtre Hibernate multi-tenant

Un tenant par défaut est configuré pour faciliter le développement

L’en-tête HTTP attendu est :
X-Tenant-ID
En développement, un tenant par défaut est automatiquement appliqué si l’en-tête est absent.


🔐 Sécurité

Spring Security activé

Application stateless

Les routes suivantes sont accessibles sans authentification en développement :

/api/**
/swagger-ui/**
/v3/api-docs/**


L’authentification JWT est prévue pour les prochaines évolutions.

🧪 Test avec Postman

URL exemple :

http://localhost:8080/api/regions


Aucun login requis en environnement de développement

L’en-tête X-Tenant-ID peut être ajouté si nécessaire

👥 Travail en équipe
Cloner le projet
git clone https://gitlab.com/<GROUP_OR_USER>/digiSchool-backend.git

Lancer le projet
docker compose up --build


Chaque développeur dispose du même environnement grâce à Docker.

🚀 Évolutions prévues

Authentification JWT

Gestion des rôles et permissions

Profils Spring (dev, docker, prod)

Migration Flyway / Liquibase

CI/CD GitLab

## ✅ Étape finale : commit du README

```bash
git add README.md
git commit -m "Add complete project documentation (Docker, config, security, tenant)"
git push



