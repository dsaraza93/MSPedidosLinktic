FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copiar todo el proyecto (incluye gradlew)
COPY . /app

# Instalar utilidades necesarias
RUN apk add --no-cache bash curl dos2unix

# Corregir fin de línea y dar permisos
RUN dos2unix ./gradlew && chmod +x ./gradlew

# Ejecutar build (sin tests)
RUN ./gradlew clean build -x test --no-daemon

# Exponer el puerto definido en application.yml
EXPOSE 8082

# Ejecutar el JAR generado
ENTRYPOINT ["java", "-jar", "/app/build/libs/servicioGestionDePedidos-0.0.1-SNAPSHOT.jar"]
