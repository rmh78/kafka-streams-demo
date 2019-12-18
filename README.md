# kafka-streams-demo

Simple demo project for testing some APIs in combination with Quarkus.

## APIs

- Kafka Streams
- MicroProfile Reactive Messaging
- MicroProfile Health

## Start Dev-Environment

- Start kafka and zookeeper with docker-compose:

    ```bash
    docker-compose -f docker-compose-local.yaml up
    ```

- Start demo application on quarkus dev-server:

    ```bash
    mvn compile quarkus:dev
    ```

- See requests.http file for creating events and query for events

## Build, Run and Scale

- Build native image inside docker:

    ```bash
    mvn package -Pnative -Dquarkus.native.container-build=true
    ```

- Start kafka, zookeeper and demo application inside docker:

    ```bash
    docker-compose up --build
    ```

- Scale up demo application: 

    ```bash
    docker-compose stop demo
    docker-compose up --build -d --scale demo=3
    ```

- See requests.http file for creating events and query for events (change port to that port used by the docker container)