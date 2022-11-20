package org.apache.druid.data.input.pravega;


public class PravegaRecordEntity{
    private final byte[] data;


    public PravegaRecordEntity(byte [] data){
        this.data = data;
    }

    public byte[] getRecord(){
        return this.data;
    }

    public String getRecord2String(){
        return new String(this.data);
    }
}
