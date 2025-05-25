# Development Dockerfile for Angular
FROM node:18-alpine

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci

# Copy, set permissions for entrypoint script, and verify
COPY entrypoint.sh /app/entrypoint.sh
RUN ls -la /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

# Copy the rest of the application
COPY . .

# Expose the Angular dev server port
EXPOSE 4200

# Set the entrypoint
ENTRYPOINT ["/bin/sh", "/app/entrypoint.sh"]
# Default command for the entrypoint
CMD ["npm", "start", "--", "--host", "0.0.0.0", "--poll=2000"]
