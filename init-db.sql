-- Create the keycloak schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS keycloak;

-- Grant all privileges on the keycloak schema to the appuser
GRANT
ALL
PRIVILEGES
ON
SCHEMA
keycloak TO appuser;

-- Set default privileges for future tables created in the keycloak schema
ALTER
DEFAULT PRIVILEGES IN SCHEMA keycloak GRANT ALL ON TABLES TO appuser;
ALTER
DEFAULT PRIVILEGES IN SCHEMA keycloak GRANT ALL ON SEQUENCES TO appuser;
