package com.example.appweather;

public class Weather {
    public String Day;
    public Integer Temp;
    public String Speed;
    public String Cloud;
    public Integer Rain;

    public Weather(String day, Integer temp, String speed, String cloud, Integer rain) {
        Day = day;
        Temp = temp;
        Speed = speed;
        Cloud = cloud;
        Rain = rain;
    }
}
