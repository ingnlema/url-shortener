# Etapa 1: Construcción usando Maven
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /build
# Copiar archivos pom.xml y src al contenedor
COPY pom.xml .
COPY src ./src
# Compilar el proyecto y empaquetar el ejecutable
RUN mvn clean package -DskipTests

# Etapa 2: Preparar la imagen de ejecución
FROM openjdk:17-slim
WORKDIR /app
# Copiar solo el JAR de la etapa de construcción a la etapa de ejecución
COPY --from=build /build/target/url-shortener-0.0.1-SNAPSHOT.jar /app/url-shortener.jar
EXPOSE 8080
CMD ["java", "-jar", "url-shortener.jar"]