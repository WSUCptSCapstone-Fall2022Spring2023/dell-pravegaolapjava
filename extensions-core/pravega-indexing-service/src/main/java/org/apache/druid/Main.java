package org.apache.druid;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}

/*
* Pravega API Notes:
*   StreamConfiguration streamConfig = StreamConfiguration.builder()
        .scalingPolicy(ScalingPolicy.fixed(1))
        .build();
    * The above code defines a stream. the scaling policy referes to how many segments it'll contain (just one)
    * The created streamConfig allows us to treat the stream like an append only file

* URI controllerURI = URI.create("tcp://localhost:9090");
    * We need this so pravega can create our stream in a certain scope (run pravega standalone in localhost)
    * Pravega uses a stream manager to create our stream (after we've connected using the URI above)
* try (StreamManager streamManager = StreamManager.create(controllerURI)) {
    streamManager.createScope("tutorial");
    streamManager.createStream("tutorial", "numbers", streamConfig);
}
*
* ClientConfig clientConfig = ClientConfig.builder()
        .controllerURI(controllerURI).build();
* After manager creation, we need to define a clientconfig to tell the clien about the controller addresses that
*   we conencted to pravega with the above URI
*
*
* */