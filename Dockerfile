FROM eclipse-temurin:17-jdk

ARG JAR_FILE=target/*.jar

# Crear usuario no root (sin UID fijo)
RUN useradd -m appuser \
    && mkdir -p /app

WORKDIR /app

# Copiar jar y asignar permisos
COPY ${JAR_FILE} app.jar
RUN chown -R appuser:appuser /app

USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]