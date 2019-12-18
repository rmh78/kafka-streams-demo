package de.harald.kafkastreams;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public class InteractiveQuery {

    private static final Logger LOGGER = Logger.getLogger(InteractiveQuery.class);

    @ConfigProperty(name="quarkus.http.host")
    String host;

    @ConfigProperty(name="quarkus.http.port")
    String port;

    @Inject
    KafkaStreams kafkaStreams;

    public List<String> getAll() {
        LOGGER.info("***HOST:PORT*** " + host + ":" + port);
        List<String> all = new ArrayList<>();
        
        KeyValueIterator<String, String> range = getStore().all();
        while (range.hasNext()) {
          KeyValue<String, String> next = range.next();
          all.add(next.value);
        }

        return all;
    }
    

    private ReadOnlyKeyValueStore<String, String> getStore() {
        while (true) {
            try {
                return kafkaStreams.store(TopologyProducer.STORE, QueryableStoreTypes.keyValueStore());
            } catch (InvalidStateStoreException e) {
                // ignore, store not ready yet
            }
        }
    }
}