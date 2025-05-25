#!/bin/sh

set -e

# Define the target configuration file
CONFIG_FILE="/app/src/app/core/auth/auth.config.ts"

# Wait for the volume to be mounted (give it a few seconds)
echo "Waiting for volume mount..."
sleep 2

# Check multiple times if file exists (in case of slow mount)
for i in 1 2 3 4 5; do
  if [ -f "$CONFIG_FILE" ]; then
    echo "Configuration file found!"
    break
  else
    echo "Waiting for configuration file... (attempt $i/5)"
    sleep 1
  fi
done

# Check if the config file exists
if [ ! -f "$CONFIG_FILE" ]; then
  echo "Warning: Configuration file $CONFIG_FILE not found after waiting!"
  echo "Contents of /app/src:"
  ls -la /app/src/ || echo "/app/src not found"
  echo "Contents of /app:"
  ls -la /app/
else
  # Perform replacements if environment variables are set
  if [ -n "$KEYCLOAK_AUTH_SERVER_URL" ]; then
    sed -i "s~\${KEYCLOAK_AUTH_SERVER_URL}~$KEYCLOAK_AUTH_SERVER_URL~g" "$CONFIG_FILE"
    echo "Replaced KEYCLOAK_AUTH_SERVER_URL with: $KEYCLOAK_AUTH_SERVER_URL"
  fi

  if [ -n "$KEYCLOAK_REALM" ]; then
    sed -i "s~\${KEYCLOAK_REALM}~$KEYCLOAK_REALM~g" "$CONFIG_FILE"
    echo "Replaced KEYCLOAK_REALM with: $KEYCLOAK_REALM"
  fi

  if [ -n "$KEYCLOAK_FRONTEND_CLIENT_ID" ]; then
    sed -i "s~\${KEYCLOAK_FRONTEND_CLIENT_ID}~$KEYCLOAK_FRONTEND_CLIENT_ID~g" "$CONFIG_FILE"
    echo "Replaced KEYCLOAK_FRONTEND_CLIENT_ID with: $KEYCLOAK_FRONTEND_CLIENT_ID"
  fi
  
  echo "Final configuration:"
  cat "$CONFIG_FILE"
fi

# Execute the main command passed to the entrypoint (e.g., npm start)
exec "$@"
