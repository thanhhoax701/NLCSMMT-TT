package com.example.appweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentWeather extends AppCompatActivity {
    String cityName = "", lat, lon;
    TextView txtCityNameCW, txtNhietDoCW
            , txtCamGiacNhuCW, txtApSuatCW, txtDoAmCW, txtNhietDoKhiQuyenCW, txtMayCW
            , txtUVCW, txtTamNhinCW, txtTocDoGioCW, txtHuongGioCW, txtGioGiatCW, txtLuongMuaCW, txtTuyetCW;
    ImageView imgClose;

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_weather);

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
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void currentWeather (String data) {
        // Thực thi những request mà mình gửi đi
        // Cú pháp Request của thư viện Volley
        RequestQueue requestQueue = Volley.newRequestQueue(CurrentWeather.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Dòng dưới là hiển thị ra Logcat để coi thử thôi
                        // Log.d("ketqua", response);
                        try {
                            // Gán biến response vào để đọc dữ liệu từ Object
                            JSONObject jsonObject = new JSONObject(response);

                            // Lấy lon và lat
                            JSONObject jsonObjectLonLat = jsonObject.getJSONObject("coord");
                            lat = jsonObjectLonLat.getString("lat");
                            lon = jsonObjectLonLat.getString("lon");
                            // Log.d("lat", lat);
                            // Log.d("lon", lon);

                            oneCallCurrentWeather(lat, lon);

                            // Lấy ra tên thành phố
                            String name = jsonObject.getString("name");
                            // Xét text cho tên thành phố
                            txtCityNameCW.setText(name);

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

    public void oneCallCurrentWeather (String lat1, String lon1) {
        RequestQueue requestQueue = Volley.newRequestQueue(CurrentWeather.this);
        String url = "https://api.openweathermap.org/data/2.5/onecall?lat="+lat1+"&lon="+lon1+"&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500";
//        Log.d("url", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("HI", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject jsonObjectCurrent = jsonObject.getJSONObject("current");

                            // Lấy nhiệt độ và convert sang chuỗi
                            String temp = jsonObjectCurrent.getString("temp");
                            // Nhiệt độ có kiểu dữ liệu là double
                            // -> cần chuyển đổi nhiệt độ sang kiểu int
                            Double a = Double.valueOf(temp);
                            // Đổi về kiểu int bằng intValue rồi đổi sang chuỗi
                            String nhietDo = String.valueOf(a.intValue());
                            // Xét text cho nhiệt độ
                            txtNhietDoCW.setText(nhietDo + "°C");

                            // Lấy thông tin chi tiết về thời tiết hiện tại
                            String camGiacNhuCW = jsonObjectCurrent.getString("feels_like");
                            Double cw = Double.valueOf(camGiacNhuCW);
                            // Đổi về kiểu int bằng intValue rồi đổi sang chuỗi
                            String CamGiacNhuCW = String.valueOf(cw.intValue());
                            txtCamGiacNhuCW.setText(CamGiacNhuCW + "°C");

                            String apSuatCW = jsonObjectCurrent.getString("pressure");
                            txtApSuatCW.setText(apSuatCW + "hPa");

                            String doAmCW = jsonObjectCurrent.getString("humidity");
                            txtDoAmCW.setText(doAmCW + "%");

                            String nhietDoCW = jsonObjectCurrent.getString("dew_point");
                            Double tempCW = Double.valueOf(nhietDoCW);
                            // Đổi về kiểu int bằng intValue rồi đổi sang chuỗi
                            String NhietDoCW = String.valueOf(tempCW.intValue());
                            txtNhietDoKhiQuyenCW.setText(NhietDoCW + "°C");

                            String mayCW= jsonObjectCurrent.getString("clouds");
                            txtMayCW.setText(mayCW + "%");

                            String chiSoUVCW = jsonObjectCurrent.getString("uvi");
                            txtUVCW.setText(chiSoUVCW);

                            String tamNhinCW = jsonObjectCurrent.getString("visibility");
                            txtTamNhinCW.setText(tamNhinCW + "m");

                            String tocDoGioCW = jsonObjectCurrent.getString("wind_speed");
                            txtTocDoGioCW.setText(tocDoGioCW + "m/s");

                            String huongGioCW = jsonObjectCurrent.getString("wind_deg");
                            huongGioCW = doiHuong(huongGioCW);
                            txtHuongGioCW.setText(huongGioCW);

                            String gioGiatCW = jsonObjectCurrent.getString("wind_gust");
                            txtGioGiatCW.setText(gioGiatCW + "km/h");

                            JSONObject jsonObjectRain = jsonObjectCurrent.getJSONObject("rain");
                            String luongMuaCW = jsonObjectRain.getString("1h");
                            txtLuongMuaCW.setText(luongMuaCW + "mm");

                            JSONObject jsonObjectSnow = jsonObjectCurrent.getJSONObject("snow");
                            String tuyetCW = jsonObjectSnow.getString("1h");
                            txtTuyetCW.setText(tuyetCW + "%");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", "hoa");
                    }
                });
        requestQueue.add(stringRequest);
    }

    public void Anhxa () {
        txtCityNameCW = (TextView) findViewById(R.id.textViewCityNameCurrentWeather);
        imgClose = (ImageView) findViewById(R.id.closeCurrentWeather);
        txtNhietDoCW = (TextView) findViewById(R.id.textViewNhietDoCW);
        txtCamGiacNhuCW = (TextView) findViewById(R.id.textViewCamGiacNhuCW);
        txtApSuatCW = (TextView) findViewById(R.id.textViewApSuatCW);
        txtDoAmCW = (TextView) findViewById(R.id.textViewDoAmCW);
        txtNhietDoKhiQuyenCW = (TextView) findViewById(R.id.textViewNhietDoKhiQuyenCW);
        txtMayCW = (TextView) findViewById(R.id.textViewMayCW);
        txtUVCW = (TextView) findViewById(R.id.textViewUVCW);
        txtTamNhinCW = (TextView) findViewById(R.id.textViewTamNhinCW);
        txtTocDoGioCW = (TextView) findViewById(R.id.textViewTocDoGioCW);
        txtHuongGioCW = (TextView) findViewById(R.id.textViewHuongGioCW);
        txtGioGiatCW = (TextView) findViewById(R.id.textViewGioGiatCW);
        txtLuongMuaCW = (TextView) findViewById(R.id.textViewLuongMuaCW);
        txtTuyetCW = (TextView) findViewById(R.id.textViewTuyetCW);
    }

    public  String doiHuong(String abc){
        int a = Integer.parseInt(abc);
        if (a >0 && a < 90){
            abc = "Đông Bắc";
        }
        if (a > 90 && a < 180){
            abc = "Đông Nam";
        }
        if (a > 180 && a < 270){
            abc = "Tây Nam";
        }
        if ( a > 270 && a< 360){
            abc = "Tây Bắc";
        }
        switch (a){
            case 0:
                abc= "Bắc";
                break;
            case 90:
                abc = "Đông";
                break;
            case 180:
                abc = "Nam";
                break;
            case 270:
                abc = "Tây";
                break;
        }
        return abc;
    }
}