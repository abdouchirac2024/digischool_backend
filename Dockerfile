# ============================
# Stage 1: Build avec Maven
# ============================
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /build

# Copy pom.xml first for dependency caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build (skip tests)
COPY src ./src
RUN mvn package -DskipTests -B

# ============================
# Stage 2: Runtime (image légère)
# ============================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Curl pour le healthcheck
RUN apk add --no-cache curl

# Copie du JAR compilé
COPY --from=build /build/target/digiSchool-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --retries=5 --start-period=90s \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Flags JVM optimisés pour conteneur :
#   -XX:+UseContainerSupport     → respecte les limites CPU/RAM du conteneur (pas la machine hôte)
#   -XX:MaxRAMPercentage=75.0    → utilise 75% de la RAM allouée au conteneur
#   -XX:+UseG1GC                 → collecteur GC adapté aux serveurs web (faible latence)
#   -XX:+OptimizeStringConcat    → optimisation des concat strings (Java 21)
#   -Djava.security.egd=...      → accélère la génération de nombres aléatoires (JWT, UUID)
#   -Dfile.encoding=UTF-8        → s'assurer que l'encodage est correct
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+UseG1GC", \
  "-XX:G1HeapRegionSize=16m", \
  "-XX:+ExplicitGCInvokesConcurrent", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-Dfile.encoding=UTF-8", \
  "-jar", "app.jar"]
