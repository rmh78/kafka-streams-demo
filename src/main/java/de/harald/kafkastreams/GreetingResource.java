package de.harald.kafkastreams;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import io.smallrye.reactive.messaging.kafka.KafkaMessage;

@Path("/hello")
@ApplicationScoped
public class GreetingResource {

    @Inject
    InteractiveQuery iq;

    @Inject
    @Channel("greetings") 
    Emitter<KafkaMessage<String, String>> emitter;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getAll() {
        return iq.getAll();
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public void createEvent(String message) {
        emitter.send(KafkaMessage.of(message, message));
    }
}