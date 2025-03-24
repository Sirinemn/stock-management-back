# Étape 1 : Build avec Maven dans une image temporaire
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Étape 2 : Création d'une image légère pour exécution
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/stock-management-back-0.0.1-SNAPSHOT.jar app.jar

# Exposition du port 8081
EXPOSE 8081

# Démarrage de l'application avec le profil production
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
