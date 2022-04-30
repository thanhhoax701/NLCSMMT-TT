package com.example.appweather.model;

public class Main {
    private double temp = 0;
    private double feel_like = 0;
    private int pressure = 0;
    private int humidity= 0;
    private double temp_min = 0;
    private double temp_max = 0;

    public Main(double temp, double feel_like, int pressure, int humidity, double temp_min, double temp_max) {
        this.temp = temp;
        this.feel_like = feel_like;
        this.pressure = pressure;
        this.humidity = humidity;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
    }

    public Main() {
    }


    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getFeel_like() {
        return feel_like;
    }

    public void setFeel_like(double feel_like) {
        this.feel_like = feel_like;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }
}
