package org.apache.druid.data.input.pravegainput;

import org.apache.druid.Constant;
import org.apache.druid.PravegaWriter;
import org.apache.druid.data.input.pravega.PravegaRecordEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PravegaReaderTest {

    /**
     * before run this testcase make sure start the pravega in a fresh state
     */
    @Test
    public void readAllStringEventsFromStream(){
        // write some events to pravega
        String [] stringEvents = new String[]{"hello", "world", "ok", "good"};
        PravegaWriter pw = new PravegaWriter(
                Constant.DEFAULT_SCOPE,
                Constant.DEFAULT_STREAM_NAME,
                Constant.DEFAULT_CONTROLLER_URI);
        for (String event:stringEvents){
            pw.writeEvent(event);
        }

        // read events from pravega
        PravegaReader pr = new PravegaReader(
                Constant.DEFAULT_SCOPE,
                Constant.DEFAULT_STREAM_NAME,
                Constant.DEFAULT_CONTROLLER_URI);
        List<String> readEvents = new ArrayList<>();
        List<PravegaRecordEntity> prEntityList = pr.readAllEvents();
        for (PravegaRecordEntity pre : prEntityList){
            readEvents.add(pre.getRecord2String());
        }
        String [] output = new String[ readEvents.size() ];
        output = readEvents.toArray(output);

        Assertions.assertArrayEquals(stringEvents, output);
    }


}