package org.apache.druid;

import io.pravega.client.ClientConfig;
import io.pravega.client.EventStreamClientFactory;
import io.pravega.client.admin.ReaderGroupManager;
import io.pravega.client.admin.StreamManager;
import io.pravega.client.stream.*;
import io.pravega.client.stream.impl.UTF8StringSerializer;

import java.net.InetAddress;
import java.net.URI;
import java.util.UUID;



public class Connector {

    private static final int READER_TIMEOUT_MS = 2000;

    private final String scope;
    private final String streamName;
    private final URI controllerURI;

    public Connector(String scope, String streamName, String uriString){
        this.scope = scope;
        this.streamName = streamName;
        this.controllerURI = URI.create(uriString);
    }

    /**
     * Ideally, I think this should be multi thread runs every 2 seconds
     * if return reader is null, we alert user that disconnection happended.
     *
     * @return
     */
    public EventStreamReader<String> getEventStreamReader( ) {
        EventStreamReader<String> reader = null;
        try{
            StreamManager streamManager = StreamManager.create(controllerURI);
            final boolean scopeIsNew = streamManager.createScope(scope);
            StreamConfiguration streamConfig = StreamConfiguration.builder()
                    .scalingPolicy(ScalingPolicy.fixed(1))
                    .build();

            final String readerGroup = UUID.randomUUID().toString().replace("-", "");
            final ReaderGroupConfig readerGroupConfig = ReaderGroupConfig.builder()
                    .stream(Stream.of(scope, streamName))
                    .build();

            ReaderGroupManager readerGroupManager = ReaderGroupManager.withScope(scope, controllerURI);
            readerGroupManager.createReaderGroup(readerGroup, readerGroupConfig);

            EventStreamClientFactory clientFactory = EventStreamClientFactory.withScope(scope,
                    ClientConfig.builder().controllerURI(controllerURI).build());

            reader = clientFactory.createReader("reader",
                    readerGroup,
                    new UTF8StringSerializer(),
                    ReaderConfig.builder().build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reader;
    }

}
