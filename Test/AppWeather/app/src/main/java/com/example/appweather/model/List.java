package com.example.appweather.model;

import java.util.ArrayList;

public class List {
    private long dt = 0;
    private  Main main = null;
    private Wind wind= null;
    private Cloud clouds= null;
    private ArrayList<WeatherDay> weather = null;

    public List() {
    }

    public List(long dt, Main main, Wind wind, Cloud clouds, ArrayList<WeatherDay> weather) {
        this.dt = dt;
        this.main = main;
        this.wind = wind;
        this.clouds = clouds;
        this.weather = weather;
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

    public ArrayList<WeatherDay> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<WeatherDay> weather) {
        this.weather = weather;
    }
}
