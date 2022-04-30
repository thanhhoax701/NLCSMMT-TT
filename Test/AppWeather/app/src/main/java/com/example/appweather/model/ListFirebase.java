package com.example.appweather.model;

import java.util.ArrayList;

public class ListFirebase {
    private long dt = 0;
    private Main main = null;
    private Wind wind = null;
    private Cloud clouds = null;

    public ListFirebase() {
    }

    public ListFirebase(long dt, Main main, Wind wind, Cloud clouds) {
        this.dt = dt;
        this.main = main;
        this.wind = wind;
        this.clouds = clouds;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Cloud getClouds() {
        return clouds;
    }

    public void setClouds(Cloud clouds) {
        this.clouds = clouds;
    }
    
}
