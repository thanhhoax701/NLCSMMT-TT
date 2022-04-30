package com.example.appweather;

import java.io.Serializable;

public class DetailHistory implements Serializable {
    private String detailDayHW;

    public String getDetailDayHW() {
        return detailDayHW;
    }

    public void setDetailDayHW(String detailDayHW) {
        this.detailDayHW = detailDayHW;
    }
}
