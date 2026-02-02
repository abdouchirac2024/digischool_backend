# Image Java 17 (compatible Spring Boot 3.x)
FROM eclipse-temurin:17-jre

# Dossier de travail
WORKDIR /app

# Copier le jar
COPY target/digiSchool-0.0.1-SNAPSHOT.jar app.jar

# Port exposé
EXPOSE 8080

# Lancement de l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
