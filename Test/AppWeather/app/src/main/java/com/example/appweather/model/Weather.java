package com.example.appweather.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

public class Weather {
    private int cod =0;

    private int city_id=  0;
    private double calctime = 0.0;

    private ArrayList<List> list = null;
    public Weather(int cod, int city_id, double calctime, ArrayList<List> list) {

        this.cod = cod;
        this.city_id = city_id;
        this.calctime = calctime;
        this.list = list;
    }


    public Weather() {
    }

    public ArrayList<List> getList() {
        return list;
    }

    public void setList(ArrayList<List> list) {
        this.list = list;
    }


    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public double getCalctime() {
        return calctime;
    }

    public void setCalctime(double calctime) {
        this.calctime = calctime;
    }
}
