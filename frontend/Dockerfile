# Stage 1: Build the application
FROM node:18 AS build

WORKDIR /app

# Install Angular CLI globally (if needed for ng serve, though package.json scripts are preferred)
# RUN npm install -g @angular/cli

COPY package*.json ./
RUN npm install

COPY . .

# Expose port for Angular live server
EXPOSE 4200

# Start the Angular development server
CMD ["npm", "start"]
