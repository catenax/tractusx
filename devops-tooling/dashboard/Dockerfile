# Step 1
FROM node:14.18.2 as build-step
RUN mkdir /app
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . /app
RUN npm run build

# Step 2
FROM nginx:1.19.7-alpine
#RUN apt-get update && apt-get install -y curl && apt-get clean
COPY --from=build-step /app/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
