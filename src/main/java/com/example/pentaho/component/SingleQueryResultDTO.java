package com.example.pentaho.component;

import java.util.List;

public class SingleQueryResultDTO {
    private String text;

    private List<IbdTbAddrCodeOfDataStandardDTO> data;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<IbdTbAddrCodeOfDataStandardDTO> getData() {
        return data;
    }

    public void setData(List<IbdTbAddrCodeOfDataStandardDTO> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SingleQueryResultDTO{" +
                "text='" + text + '\'' +
                ", data=" + data +
                '}';
    }
}
