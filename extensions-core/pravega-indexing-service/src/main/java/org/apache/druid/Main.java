package org.apache.druid;

import org.apache.druid.data.input.pravega.PravegaRecordEntity;
import org.apache.druid.data.input.pravegainput.PravegaReader;

import java.util.List;

public class Main {
    public static void main(String[] args) {
//        writeEvents();
//        readEvents();
        System.out.println("hello");
    }
    public static void writeEvents(){
        // one writer per stream
        // one stream to many events
        PravegaWriter pw = new PravegaWriter(
                Constant.DEFAULT_SCOPE,
                Constant.DEFAULT_STREAM_NAME,
                Constant.DEFAULT_CONTROLLER_URI);
        pw.writeEvent("helloworldKey", "helloworld!!!");
    }

    public static void readEvents(){
        PravegaReader pr = new PravegaReader(
                Constant.DEFAULT_SCOPE,
                Constant.DEFAULT_STREAM_NAME,
                Constant.DEFAULT_CONTROLLER_URI);
        List<PravegaRecordEntity> prEntityList = pr.readAllEvents();
        System.out.println("--------------- Events Read -------------------------");
        for (PravegaRecordEntity pre : prEntityList){
            System.out.println(pre.getRecord2String());
        }
    }
}