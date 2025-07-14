# Imagen base ligera de OpenJDK 17
FROM eclipse-temurin:17-jdk-alpine

# Crear directorio de trabajo
WORKDIR /app

# Copiar archivos esenciales para construir el proyecto
COPY build.gradle /app/build.gradle
COPY application.yml /app/src/main/resources/application.yml

# Instalar Gradle para construir el proyecto
RUN apk add --no-cache curl \
    && curl -s "https://get.sdkman.io" | bash \
    && source "$HOME/.sdkman/bin/sdkman-init.sh" \
    && sdk install gradle 8.7 \
    && gradle build -x test --no-daemon

# Copiar todos los archivos del proyecto al contenedor
COPY . /app

# Construir el proyecto y generar el JAR
RUN ./gradlew clean build -x test --no-daemon

# Exponer el puerto definido en el archivo application.yml (8082)
EXPOSE 8082

# Ejecutar el JAR generado
ENTRYPOINT ["java","-jar","/app/build/libs/servicioGestionDePedidos-0.0.1-SNAPSHOT.jar"]
