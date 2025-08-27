package com.example.pentaho.component;

import java.util.List;

public class OpenAPISingleQueryTrackDTO {

    private String text ;

    private List<OpenAPIIbdTbIhChangeDoorplateHis> data;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<OpenAPIIbdTbIhChangeDoorplateHis> getData() {
        return data;
    }

    public void setData(List<OpenAPIIbdTbIhChangeDoorplateHis> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "OpenAPISingleQueryTrackDTO{" +
                "text='" + text + '\'' +
                ", data=" + data +
                '}';
    }
}
