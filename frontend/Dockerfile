# Development Dockerfile for Angular
FROM node:18-alpine

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci

# Copy the rest of the application
COPY . .

# Expose the Angular dev server port
EXPOSE 4200

# Run the Angular development server
CMD ["npm", "start", "--", "--host", "0.0.0.0", "--poll=2000"]
