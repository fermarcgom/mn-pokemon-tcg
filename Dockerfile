FROM openjdk:17-jdk-alpine
COPY ./build/libs/mn-pokemon-tcg-*-all.jar mn-pokemon-tcg.jar
CMD ["java","-jar","mn-pokemon-tcg.jar"]