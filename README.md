# kafka-streams-demo

Simple demo project for testing some APIs in combination with Quarkus.
1

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

## Deploy to OpenShift

- Start Minishift

    ```bash
    minishift start
    eval $(minishift oc-env)
    ```

- Build native image inside docker:

    ```bash
    mvn clean package -Pnative -Dquarkus.native.container-build=true
    ```

- Create a new build-configuration and start the build 

    ```json
    oc new-build --binary --name=kafka-streams-demo -l app=kafka-streams-demo
    oc patch bc/kafka-streams-demo -p '{"spec":{"strategy":{"dockerStrategy":{"dockerfilePath":"src/main/docker/Dockerfile.native"}}}}'
    oc start-build kafka-streams-demo --from-dir=. --follow
    ```

- Create a new application and set the POD_IP environment variable

    ```json
    oc new-app --image-stream=kafka-streams-demo:latest -e POD_IP=0.0.0.0
    oc patch dc kafka-streams-demo --type=json --patch '[{"op":"replace","path":"/spec/template/spec/containers/0/env/0","value":{"name":"POD_IP","valueFrom":{"fieldRef":{"apiVersion":"v1","fieldPath":"status.podIP"}}}}]'
    ```

- Create the route

    ```bash
    oc expose service kafka-streams-demo
    ````

- Scale up to 3 pods

    ```bash
    oc scale dc kafka-streams-demo --replicas=3
    ```

- Remove the whole application

    ```bash
    oc delete all --selector app=kafka-streams-demo
    ```
