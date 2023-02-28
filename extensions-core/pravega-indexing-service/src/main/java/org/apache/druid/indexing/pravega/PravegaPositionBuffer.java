package org.apache.druid.indexing.pravega;

import java.nio.ByteBuffer;

// This class is planned to be used to contain our position in a stream. This wrapper will be unwrapped
// when we make a call to compareTo() in the seekablestreamsupervisor so that we can deserialize the position
// contents and make comparisons
public class PravegaPositionBuffer {
    private ByteBuffer byteBuffer;
    // static counter for comparisons
}
