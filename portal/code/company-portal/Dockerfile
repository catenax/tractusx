# Step 1
FROM node:10-alpine as build-step
RUN mkdir /app
WORKDIR /app
COPY package.json /app
RUN npm install
COPY . /app
RUN npm run build

# Stage 2
FROM nginx
RUN apt-get update && apt-get install -y curl && apt-get clean
COPY --from=build-step /app/build /usr/share/nginx/html