# Micronaut Pokemon TCG<sup>©</sup> Demo Project
This is a small project created with [Micronaut 3.5.3](https://docs.micronaut.io/3.5.3/guide/index.html) to show how to 
create Micronaut services. The project features a back-end REST API to manage Pokemon cards on a Postgres database.

This is managed with Gradle and features schema management with Liquibase. It also includes a security login using JWT.

The main features used are:
- [Test Containers](https://www.testcontainers.org/): Used for integration testing.
- [Hibernate JPA](https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#hibernate): Used for database 
integration.
- [Micronaut Security JWT](https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html): Used for authentication.
- [Micronaut Liquibase Database Migration](https://micronaut-projects.github.io/micronaut-liquibase/latest/guide/index.html):
Used to migrate scripts to the database,

---

## How to build and run
The project uses Gradle Wrapper for builds and it requires Java 17.

It is prepared to be run as a jar or as a Docker java image or GraalVM native image.

It also assumes that a Postgres database by the name mn-pokemon-tcg exists, by default on localhost port 5432.
If Docker is installed the postgres folder on the project has the instructions to deploy this database.

### Running as a java jar:
To run the java jar first the application needs to be built.

Execute `./gradlew clean assemble` to compile.

Execute `./gradlew clean build` to compile and run tests (requires Docker for integration tests).

Execute `./gradlew unitTest` to only execute unit tests.

Execute `./gradlew run` to build and run the application.

### Running as java Docker image:
If Docker is present the application can be run as a java image container.

Execute `./gradlew dockerBuild` to build an image from the [DockerFile](Dockerfile).

The image generated name and tag is `mn-pokemon-tcg:latest`

After the image is created use docker compose to start the containers with the application and the database:

Execute `docker-compose up` this will use [docker-compose.yml](docker-compose.yml) file, 
this file uses as image name for the application *mn-pokemon-tcg* by default.

### Running as GraalVM Docker native image:
If Docker is present the application can be run as a GraalVm native image container.

Execute `./gradlew dockerBuildNative` to build an image.

The image generated name and tag is `mn-pokemon-tcg-native:latest`

After the image is created use docker compose to start the containers with the application and the database:

Execute `docker-compose up` this will use [docker-compose.yml](docker-compose.yml) file,
this file uses as image name for the application *mn-pokemon-tcg* by default so before running
it needs to be changed to *mn-pokemon-tcg-native*.  Also by default liquibase integration is 
enabled, for native image it needs to be disabled with the environment variable *ENABLE_LIQUIBASE*
as the integration with liquibase is not working.