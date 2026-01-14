# Stage 1: Build the Angular application
FROM node:22-alpine AS build

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build --configuration=production

# Stage 2: Serve the application with Nginx
FROM nginx:1.27-alpine

# Install gettext for envsubst
RUN apk add --no-cache gettext

COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html

# Copy the Nginx configuration template
# We place it outside /etc/nginx/templates/ to avoid the default entrypoint's aggressive envsubst
COPY nginx/default.conf /etc/nginx/conf.d/default.conf.template

EXPOSE 80

# Use envsubst to replace ONLY $API_URL in the Nginx configuration
CMD ["/bin/sh", "-c", "envsubst '$API_URL' < /etc/nginx/conf.d/default.conf.template > /etc/nginx/conf.d/default.conf && nginx -g 'daemon off;'"]
