package org.apache.druid.data.input.pravegainput;

import io.pravega.client.ClientConfig;
import io.pravega.client.EventStreamClientFactory;
import io.pravega.client.admin.ReaderGroupManager;
import io.pravega.client.admin.StreamManager;
import io.pravega.client.stream.*;
import io.pravega.client.stream.impl.UTF8StringSerializer;
import org.apache.druid.data.input.pravega.PravegaRecordEntity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PravegaReader {
    private static final int READER_TIMEOUT_MS = 2000;

    private final String scope;
    private final String streamName;
    private final URI controllerURI;

    public PravegaReader(String scope, String streamName, String uriString){
        this.scope = scope;
        this.streamName = streamName;
        this.controllerURI = URI.create(uriString);
    }

    public List<PravegaRecordEntity> readAllEvents(){
        List<PravegaRecordEntity> prEntityList = new ArrayList<PravegaRecordEntity>();

        StreamManager streamManager = StreamManager.create(controllerURI);
        final boolean scopeIsNew = streamManager.createScope(scope);
        StreamConfiguration streamConfig = StreamConfiguration.builder()
                .scalingPolicy(ScalingPolicy.fixed(1))
                .build();

        final String readerGroup = UUID.randomUUID().toString().replace("-", "");
        final ReaderGroupConfig readerGroupConfig = ReaderGroupConfig.builder()
                .stream(Stream.of(scope, streamName))
                .build();
        try (ReaderGroupManager readerGroupManager = ReaderGroupManager.withScope(scope, controllerURI)) {
            readerGroupManager.createReaderGroup(readerGroup, readerGroupConfig);
        }
        try (EventStreamClientFactory clientFactory = EventStreamClientFactory.withScope(scope,
                ClientConfig.builder().controllerURI(controllerURI).build());

             EventStreamReader<String> reader = clientFactory.createReader("reader",
                     readerGroup,
                     new UTF8StringSerializer(),
                     ReaderConfig.builder().build())) {

            System.out.format("Reading all the events from %s/%s%n", scope, streamName);
            EventRead<String> event = null;
            do {
                try {
                    event = reader.readNextEvent(READER_TIMEOUT_MS);
                    if (event.getEvent() != null) {
                        System.out.format("Read event '%s'%n", event.getEvent());
                        prEntityList.add(
                                new PravegaRecordEntity(event.getEvent().getBytes())
                        );
                    }
                } catch (ReinitializationRequiredException e) {
                    //There are certain circumstances where the reader needs to be reinitialized
                    e.printStackTrace();
                }
            } while (event.getEvent() != null);
            System.out.format("No more events from %s/%s%n", scope, streamName);
        }
        return prEntityList;
    }
}
