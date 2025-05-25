# Full-Stack Project Base (Angular + Spring Boot + Keycloak + PostgreSQL)

This project provides a starting point for a full-stack application using Angular for the frontend, Spring Boot for the backend, Keycloak for authentication, and PostgreSQL as the database. Everything is orchestrated using Docker Compose for easy setup and development with hot reloading.

## Prerequisites

- Docker Desktop (or Docker Engine + Docker Compose) installed.
- Ensure ports 4200, 8080, 8180, and 5432 are available on your machine.

## Project Structure

```
.
├── backend/            # Spring Boot backend application
│   ├── src/
│   └── Dockerfile
├── config/
│   └── keycloak/
│       └── realm-export.json # Example Keycloak realm configuration
├── frontend/           # Angular frontend application
│   ├── src/
│   └── Dockerfile
├── docker-compose.yml  # Docker Compose configuration
├── keycloak_setup_notes.txt # Notes on Keycloak configuration
└── README.md           # This file
```

## Getting Started

1.  **Clone the repository** (if applicable, otherwise you have the files).

2.  **Review Keycloak Configuration:**
    *   The Keycloak service is configured in `docker-compose.yml`. It uses `admin`/`admin` as initial admin credentials (see `KEYCLOAK_ADMIN` and `KEYCLOAK_ADMIN_PASSWORD` in `docker-compose.yml`).
    *   An example realm configuration is provided in `config/keycloak/realm-export.json`.
        *   **Realm:** `my-app-realm`
        *   **Backend Client:** `my-backend-client` (initially configured as bearer-only or service account)
        *   **Frontend Client:** `my-frontend-client` (public client with redirect URI `http://localhost:4200/*`)
    *   The `docker-compose.yml` attempts to mount this file into a location Keycloak might use for auto-import (`/opt/keycloak/data/import/`). You may need to uncomment the `KEYCLOAK_IMPORT` environment variable in `docker-compose.yml` for the `keycloak` service, or manually import it after Keycloak starts.
        ```yaml
        # In docker-compose.yml, under keycloak service environments:
        # KEYCLOAK_IMPORT=/opt/keycloak/data/import/realm-export.json
        ```
    *   **Important:** The `realm-export.json` contains a placeholder `YOUR_BACKEND_CLIENT_SECRET`. If your backend client (`my-backend-client`) in Keycloak is 'confidential', generate a secret in Keycloak and update this value in `realm-export.json` (if using for import) AND in the `docker-compose.yml` environment variables for the `backend` service:
        ```yaml
        # In docker-compose.yml, under backend service environments:
        # KEYCLOAK_CREDENTIALS_SECRET: YOUR_ACTUAL_CLIENT_SECRET
        # SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_KEYCLOAK_CLIENT_SECRET: YOUR_ACTUAL_CLIENT_SECRET
        ```
        If the backend client is 'bearer-only' or 'public', no secret is needed. The current `realm-export.json` assumes a service account for the backend, and public for the frontend.

3.  **Update Frontend Keycloak Configuration (if necessary):**
    The Angular application in `frontend/src/app/core/auth/auth.config.ts` uses placeholder values:
    ```typescript
    export const keycloakConfig: KeycloakConfig = {
      url: '${KEYCLOAK_AUTH_SERVER_URL}', // To be replaced by env variable
      realm: '${KEYCLOAK_REALM}', // To be replaced by env variable
      clientId: '${KEYCLOAK_FRONTEND_CLIENT_ID}' // To be replaced by env variable
    };
    ```
    The `docker-compose.yml` provides these as `KEYCLOAK_AUTH_SERVER_URL_PLACEHOLDER`, etc., but these are not directly injected into the Angular `ng serve` environment.
    For now, you should manually update `frontend/src/app/core/auth/auth.config.ts` if the defaults in `docker-compose.yml` (which are `http://localhost:8180/auth`, `my-app-realm`, `my-frontend-client`) are different from your Keycloak setup.
    A more robust solution would involve serving this configuration dynamically.

4.  **Build and Run the Application:**
    Open a terminal in the project root and run:
    ```bash
    docker-compose up --build
    ```
    This will:
    - Build the Docker images for the backend and frontend.
    - Start all services (backend, frontend, PostgreSQL, Keycloak).
    - You should see logs from all services. Wait for Keycloak and the database to be healthy before the backend and frontend fully start.

5.  **Accessing the Services:**
    *   **Angular Frontend:** [http://localhost:4200](http://localhost:4200)
    *   **Spring Boot Backend:** [http://localhost:8080](http://localhost:8080) (e.g., API endpoints)
    *   **Keycloak Admin Console:** [http://localhost:8180/auth/](http://localhost:8180/auth/)
        *   Log in with `admin`/`admin` (or as configured in `docker-compose.yml`).
        *   Navigate to your realm (`my-app-realm` if imported/created).
        *   Here you can manage clients, users, roles, etc. Refer to `keycloak_setup_notes.txt` for more details.

6.  **Manual Keycloak Setup (if import fails or for customization):**
    - Access Keycloak admin console.
    - Create a realm named `my-app-realm`.
    - Create a client for the backend:
        - Client ID: `my-backend-client`
        - Client Protocol: `openid-connect`
        - Access Type: `bearer-only` (for service-to-service or if frontend handles all user auth) or `confidential` (if backend needs to exchange tokens). If confidential, note the secret.
    - Create a client for the frontend:
        - Client ID: `my-frontend-client`
        - Client Protocol: `openid-connect`
        - Access Type: `public`
        - Valid Redirect URIs: `http://localhost:4200/*`, `http://localhost:4200/assets/silent-check-sso.html`
        - Web Origins: `+` or `http://localhost:4200`
    - Create users within your realm.

## Hot Reloading

-   **Backend (Spring Boot):** Changes to Java files in `backend/src` should trigger a restart of the Spring Boot application within the Docker container (due to Spring Boot DevTools and volume mounting).
-   **Frontend (Angular):** Changes to files in `frontend/src` should trigger a recompilation and browser refresh (due to `ng serve`'s live reload and volume mounting with polling).

## Stopping the Application

-   Press `Ctrl+C` in the terminal where `docker-compose up` is running.
-   To remove the containers, networks, and volumes (including PostgreSQL data):
    ```bash
    docker-compose down -v
    ```
    If you want to keep PostgreSQL data, omit the `-v` flag:
    ```bash
    docker-compose down
    ```

## Further Development

-   **Database Migrations:** Add new Liquibase changelog files in `backend/src/main/resources/db/changelog/` and include them in `db.changelog-master.yaml`.
-   **API Development:** Add new controllers and services in the Spring Boot backend.
-   **UI Development:** Develop new components and services in the Angular frontend.
-   **Keycloak Configuration:**
    - Refine roles, users, and client configurations in Keycloak.
    - Ensure the `KEYCLOAK_CREDENTIALS_SECRET` is correctly set in `docker-compose.yml` if your backend client is confidential.
    - For a production setup, ensure Keycloak data is properly persisted and secured.
-   **Frontend Runtime Configuration:** Implement a dynamic way to provide Keycloak settings (and other runtime configs) to the Angular app rather than hardcoding or manual replacement. This could involve:
    - A script that generates a `config.js` or `config.json` file in the `assets` folder when the container starts, which Angular then fetches.
    - Using environment variables at build time (less flexible for different environments with the same image).
```
