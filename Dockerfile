# Etapa 1: Build com Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copia o projeto para dentro da imagem
COPY . .

# Compila o projeto e gera o .jar
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final com apenas o .jar
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copia o .jar da etapa de build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
