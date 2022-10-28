# Bist App Tracker Back End Project

The project is based on this repo , [Click!](https://github.com/alicanli1995/finance-event-driven-elasticsearch-cloud-project)

## Front End

The front end is based on this repo , [Click!](https://github.com/alicanli1995/finance-react-project)

## Project Description

This project is a simple back end project for a finance application. It is a simple application that tracks the user's BIST stock market share. 
The project is based on the monolith architecture, and layered architecture.

## Technologies

Project is created with:

* Java 17
* Spring Boot 2.7.3
* Spring Data JPA
* Spring Web
* Spring Security
* Spring Validation
* Spring Cache
* Spring Actuator
* Spring Scheduler
* Spring AOP
* Feign Client
* Lombok
* Keycloak
* Swagger
* MongoDB

## Project Properties

* BIST API ->
  - Adding a new share to the system
  - Getting a share value history from the system by share name and date
  - Getting all shares from the system
  - Getting a share comments from the system by share name
  - Adding a new comment to the system by share name
* User API ->
  - Getting user profile picture from the system by username
  - Save user profile picture to the system by username
  - Adding share to user's portfolio
  - Getting user's portfolio
  - Editing user's portfolio
  - Deleting user's portfolio
  - Getting a user's balance
* User Favorite API ->
  - Adding a new favorite share to the system by username
  - Getting all favorite shares from the system by username
  - Deleting a favorite share from the system by username
* User Finance API ->
  - Getting user's finance history

## Project Structure

* **config** : Contains the configuration classes of the project.
* **controller** : Contains the controller classes of the project.
* **exception** : Contains the exception classes of the project.
* **model** : Contains the model classes of the project.
* **repository** : Contains the repository classes of the project.
* **service** : Contains the service classes of the project.
* **application.yml** : Contains the properties of the project.
* **client** : Contains the client of BIST.
* **runner** : Contains the runner classes of keycloak of the project.
* **security** : Contains the security classes of the project.
* **aop** : Contains the aspect classes of the project.

## Setup

To run this project, install it locally using maven:

```
$ mvn clean install
```

## Run

```
$ mvn spring-boot:run
```

## Swagger

```
http://localhost:9081/swagger-ui.html
```

## Docker

```
$ docker-compose up -d
```

## Docker Compose

```
version: '3.8'

services:
  mongodb:
    image: mongo:latest
    container_name: mongodb
    ports:
      - 27017:27017
    volumes:
      - mongodb_data:/data/db
    environment:
      - MONGO_INITDB_ROOT_USERNAME=root
      - MONGO_INITDB_ROOT_PASSWORD=root
    networks:
      - mongodb-network

  keycloak:
    image: quay.io/keycloak/keycloak:15.0.2
    container_name: keycloak
    ports:
      - 8080:8080
    volumes:
      - keycloak_data:/opt/jboss/keycloak/standalone/data
      - keycloak_logs:/opt/jboss/keycloak/standalone/log
      - keycloak_config:/opt/jboss/keycloak/standalone/configuration
    environment:
      - KEYCLOAK_USER=admin
      - KEYCLOAK_PASSWORD=admin
      - KEYCLOAK_IMPORT=/opt/jboss/keycloak/realm-export.json
    networks:
      - mongodb-network

  bist-app-tracker:
    image: bist-api:latest
    container_name: bist-api
    ports:
      - 9081:9081
    environment:
      - SPRING_PROFILES_ACTIVE=default
      - SPRING_DATA_MONGODB_URI=mongodb://root:root@mongodb:27017/bist-app-tracker
      - KEYCLOAK_AUTH_SERVER_URL=http://keycloak:8080/auth
      - KEYCLOAK_REALM=company-services
      - KEYCLOAK_RESOURCE=finance-app
      - KEYCLOAK_CREDENTIALS_SECRET=secret
    networks:
      - mongodb-network

volumes:
    mongodb_data:
    keycloak_data:
    keycloak_logs:
    keycloak_config:
    
networks:
    mongodb-network:
```

## Keycloak

```
http://localhost:8080/auth/admin
```










