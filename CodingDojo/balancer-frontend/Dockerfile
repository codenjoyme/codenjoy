# create a file named Dockerfile
FROM mhart/alpine-node:10.10.0 as builder
WORKDIR /app
COPY package.json /app
COPY package-lock.json /app
COPY static-pages /app
RUN npm install
COPY . /app
RUN npm run build

# production environment
FROM nginx:1.13.9-alpine
COPY --from=builder /app/build /usr/share/nginx/html
COPY --from=builder /app/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
