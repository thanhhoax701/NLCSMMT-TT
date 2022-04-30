package com.example.appweather;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import time.DetailTime;
import time.DetailTimeAdapter;

public class HistoryWeather extends AppCompatActivity {

    String cityName = "", lat, lon, latO, lonO;
    TextView txtCityNameHW;
    ImageView imgBackHW;

    Context context;

    List<History> historyList;
    private RecyclerView rcvHistoryWeather;
    private HistoryAdapter mHistoryWeatherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_weather);

        context = this;

        Anhxa();

        // Nhận dữ liệu về
        Intent intent = getIntent();
        String city = intent.getStringExtra("name");
        Log.d("ketqua", "Du lieu truyen qua: " + city);
        if (city.equals("")) {
            cityName = "Vĩnh Long";
            currentWeather(cityName);
        }
        else {
            cityName = city;
            currentWeather(cityName);
        }
        // Trở về màn hình trước bằng action onBackPressed();
        imgBackHW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        historyList = new ArrayList<>();

        rcvHistoryWeather = findViewById(R.id.rcvHistoryWeather);
        mHistoryWeatherAdapter = new HistoryAdapter(getApplicationContext(), historyList);
        // Category hướng vertical
        LinearLayoutManager linearLayoutManagerA = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvHistoryWeather.setLayoutManager(linearLayoutManagerA);

        // Xét dữ liệu cho adapter
        mHistoryWeatherAdapter.setData(historyList);
        rcvHistoryWeather.setAdapter(mHistoryWeatherAdapter);
    }

    public void currentWeather (String data) {
        // Thực thi những request mà mình gửi đi
        // Cú pháp Request của thư viện Volley
        RequestQueue requestQueue = Volley.newRequestQueue(HistoryWeather.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log.d("ketqua", response);
                        try {
                            // Gán biến response vào để đọc dữ liệu từ Object
                            JSONObject jsonObject = new JSONObject(response);

                            // Lấy lon và lat
                            JSONObject jsonObjectLonLat = jsonObject.getJSONObject("coord");
                            lat = jsonObjectLonLat.getString("lat");
                            lon = jsonObjectLonLat.getString("lon");
//                             Log.d("lat", lat);
//                             Log.d("lon", lon);
                            oneCall(lat, lon);
                            historyWeather(lat,lon);

                            // Lấy ra tên thành phố
                            String name = jsonObject.getString("name");
                            // Xét text cho tên thành phố
                            txtCityNameHW.setText(name);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        // thực thi stringRequest
        requestQueue.add(stringRequest);
    }

    public void oneCall (String latA, String lonA) {
        RequestQueue requestQueue = Volley.newRequestQueue(HistoryWeather.this);
        String url = "https://api.openweathermap.org/data/2.5/onecall?lat="+latA+"&lon="+lonA+"&type=hours&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500";
        Log.d("urlOne", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("hehe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            latO = jsonObject.getString("lat");
                            lonO = jsonObject.getString("lon");
                            historyWeather(latO,lonO);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Loionecall", "onecall");
                    }
                });
        requestQueue.add(stringRequest);
    }

    public void historyWeather (String latB, String lonB) {
        RequestQueue requestQueue = Volley.newRequestQueue(HistoryWeather.this);
//        String url = "https://pro.openweathermap.org/data/2.5/forecast/climate?lat="+latB+"&lon="+lonB+"&type=hour&start=1646548432&end=1644129232&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500";
        String url = "http://history.openweathermap.org/data/2.5/history/city?lat="+latB+"&lon="+lonB+"&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500";
        Log.d("urlHistory", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("113", response);
                        try {
                            oneCall(latB,lonB);
                            JSONObject jsonObject = new JSONObject(response);
//
                            JSONArray jsonArrayList = jsonObject.getJSONArray("list");
//
////                            Log.d("List", String.valueOf(jsonArrayList));
//                            historyList.clear();
//                            // Do các thẻ có thuộc tính giống nhau
//                            // => dùng for để đọc giá trị của từng thẻ trong tag "list"
                            for (int k = 0; k < jsonArrayList.length(); k++) {
                                JSONObject jsonObjectList = jsonArrayList.getJSONObject(k);
//                                String dayHW = jsonObjectList.getString("dt");
////
////                                // Chuyển biến day về dạng long
//                                long lHW = Long.valueOf(dayHW);
////                                // Chuyển thành mili giây
//                                Date dateHW = new Date(lHW * 1000L);
////                                // Định dạng thứ ngày tháng năm
//                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
//                                String DayHW = simpleDateFormat.format(dateHW);
//
                                String imgTrangThaiHW = "04n";
                                String DayHW = "Thứ 6 16/10/2001";
                                String timeHW = "20:00";
                                String nhietDoHW = "30";
                                String trangThaiHW = "bão";
                                String gioHW = "3";
                                String apSuatHW = "1000";
                                String doAmHW = "78";
                                String chiSoUVHW = "3";
//
                                Log.d("history", "onResponse: "+imgTrangThaiHW +"," +DayHW +","+timeHW +","+nhietDoHW+","+trangThaiHW+","+gioHW+","+apSuatHW+","+doAmHW+","+chiSoUVHW);
                                historyList.add(new History(imgTrangThaiHW, DayHW, timeHW, nhietDoHW, trangThaiHW, gioHW, apSuatHW, doAmHW, chiSoUVHW));
                            }
//
//                            // Cập nhật khi có dữ liệu mới
                            mHistoryWeatherAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Loihistory", "history");
                    }
                });
        requestQueue.add(stringRequest);
    }

    private void Anhxa() {
        imgBackHW = (ImageView) findViewById(R.id.imageViewBackHW);
        txtCityNameHW = (TextView) findViewById(R.id.textViewCityNameHW);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHistoryWeatherAdapter.giaiPhong();
    }
}