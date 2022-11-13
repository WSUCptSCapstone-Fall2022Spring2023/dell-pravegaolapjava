package org.apache.druid;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import io.pravega.client.ClientConfig;
import io.pravega.client.EventStreamClientFactory;
import io.pravega.client.admin.StreamManager;
import io.pravega.client.stream.EventStreamWriter;
import io.pravega.client.stream.EventWriterConfig;
import io.pravega.client.stream.ScalingPolicy;
import io.pravega.client.stream.StreamConfiguration;
import io.pravega.client.stream.impl.UTF8StringSerializer;

public class PravegaWriter {
    private final String scope;
    private final String streamName;
    private final URI controllerURI;
    public PravegaWriter(String scope, String streamName, String uriString){
        this.scope = scope;
        this.streamName = streamName;
        this.controllerURI = URI.create(uriString);
    }

    /**
     * Write en event
     * @param routingKey
     * @param message
     */
    public void writeEvent(String routingKey, String message){
        StreamManager streamManager = StreamManager.create(controllerURI);
        final boolean scopeIsNew = streamManager.createScope(scope);
        StreamConfiguration streamConfig = StreamConfiguration.builder()
                .scalingPolicy(ScalingPolicy.fixed(1))
                .build();
        final boolean streamIsNew = streamManager.createStream(scope, streamName, streamConfig);

        try (EventStreamClientFactory clientFactory = EventStreamClientFactory.withScope(scope,
                ClientConfig.builder().controllerURI(controllerURI).build());
             EventStreamWriter<String> writer = clientFactory.createEventWriter(streamName,
                     new UTF8StringSerializer(),
                     EventWriterConfig.builder().build())) {

            System.out.format("Writing message: '%s' with routing-key: '%s' to stream '%s / %s'%n",
                    message, routingKey, scope, streamName);
            final CompletableFuture writeFuture = writer.writeEvent(routingKey, message);
        }
    }
}
