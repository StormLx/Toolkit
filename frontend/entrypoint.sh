#!/bin/sh
set -e

# Define the target configuration file
CONFIG_FILE="/app/src/app/core/auth/auth.config.ts"

# Check if the config file exists
if [ ! -f "$CONFIG_FILE" ]; then
  echo "Error: Configuration file $CONFIG_FILE not found!"
  exit 1
fi

# Perform replacements if environment variables are set
if [ -n "$KEYCLOAK_AUTH_SERVER_URL" ]; then
  sed -i "s~'\${KEYCLOAK_AUTH_SERVER_URL}'~'$KEYCLOAK_AUTH_SERVER_URL'~g" "$CONFIG_FILE"
  echo "Replaced KEYCLOAK_AUTH_SERVER_URL in $CONFIG_FILE"
else
  echo "Warning: KEYCLOAK_AUTH_SERVER_URL environment variable not set. Skipping replacement."
fi

if [ -n "$KEYCLOAK_REALM" ]; then
  sed -i "s~'\${KEYCLOAK_REALM}'~'$KEYCLOAK_REALM'~g" "$CONFIG_FILE"
  echo "Replaced KEYCLOAK_REALM in $CONFIG_FILE"
else
  echo "Warning: KEYCLOAK_REALM environment variable not set. Skipping replacement."
fi

if [ -n "$KEYCLOAK_FRONTEND_CLIENT_ID" ]; then
  sed -i "s~'\${KEYCLOAK_FRONTEND_CLIENT_ID}'~'$KEYCLOAK_FRONTEND_CLIENT_ID'~g" "$CONFIG_FILE"
  echo "Replaced KEYCLOAK_FRONTEND_CLIENT_ID in $CONFIG_FILE"
else
  echo "Warning: KEYCLOAK_FRONTEND_CLIENT_ID environment variable not set. Skipping replacement."
fi

# Execute the main command passed to the entrypoint (e.g., npm start)
exec "$@"
