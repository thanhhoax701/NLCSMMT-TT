package com.example.appweather.api;

import com.example.appweather.model.Weather;
import com.example.appweather.model.WeatherDay;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    //    https://history.openweathermap.org/data/2.5/history/city?lat=10.2544&lon=105.967&type=hour&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500&start=1647473851&end=1648392381
    @GET("/data/2.5/history/city")
    Call<Weather> callApi(@Query("lat") String lat,
                          @Query("lon") String lon,
                          @Query("type") String type,
                          @Query("units") String units,
                          @Query("lang") String lang,
                          @Query("appid") String appid,
                          @Query("start") Long start,
                          @Query("end") Long end);

}
