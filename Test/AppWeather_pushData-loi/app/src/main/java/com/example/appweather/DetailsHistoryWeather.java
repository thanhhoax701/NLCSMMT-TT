package com.example.appweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appweather.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetailsHistoryWeather extends AppCompatActivity {
    String cityName = "", lat, lon;
    Context context;
    TextView textViewDayDetailsHW, tvTimeDetailHistoryWeather;
    ImageView imgBackHW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_history_weather);

        context = this;
        Anhxa();
        // Trở về màn hình trước bằng action onBackPressed();
        imgBackHW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        // Get đúng key khi truyền
        int history_index = (int) bundle.get("history30");
//        textViewDayDetailsHW.setText(history.getDetailDayHW());
        tvTimeDetailHistoryWeather.setText("12:00");
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(date);
        try {
            date = formatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


//        Log.d("TAG11", "onCreate: "+date.getTime());

        long start = date.getTime()/1000;
        Log.d("TAG11", "onCreate: "+start);
        Date end1 = new Date();
        Log.d("TAG11", "onCreate: "+end1.getTime());
        long end = end1.getTime()/1000;
        if (history_index == 0){
            Log.d("TAG88", "onCreate: "+history_index);
//            callAPI(start,end);end
        }
        else if (history_index == 1){
            Log.d("TAG88", "onCreate: "+history_index);
            long startMoi =  start-86400;
            Log.d("TAG88", "start: "+startMoi+"end: "+start);
        }
        else{
//            lấy dữ liệu tên thành phố đó ra
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference myRef = database.getReference("weatherHistory");
//            myRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    String value = dataSnapshot.getValue(String.class);
//                    Toast.makeText(DetailsHistoryWeather.this, value, Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onCancelled(DatabaseError error) {
//                    // Failed to read value
//                    Toast.makeText(DetailsHistoryWeather.this, "Sai rồi", Toast.LENGTH_SHORT).show();
//                }
//            });
            long startMoi = start-(86400L *history_index);
            long endMoi = startMoi-86400;
            Log.d("TAG88", "onCreate: "+history_index);
            Log.d("TAG88", "start: "+startMoi +"end: "+endMoi);
        }

    }

//    private void callAPI(long start, long end, String name) {
//        Log.d("TAG88", "callAPI: "+start+","+end);
//        // Thực thi những request mà mình gửi đi
//        // Cú pháp Request của thư viện Volley
//        RequestQueue requestQueue = Volley.newRequestQueue(DetailsHistoryWeather.this);
//        String url = "https://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500";
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Log.d("ketqua", response);
//                        try {
//                            // Gán biến response vào để đọc dữ liệu từ Object
//                            JSONObject jsonObject = new JSONObject(response);
//
//                            // Lấy lon và lat
//                            JSONObject jsonObjectLonLat = jsonObject.getJSONObject("coord");
//                            lat = jsonObjectLonLat.getString("lat");
//                            lon = jsonObjectLonLat.getString("lon");
////                             Log.d("lat", lat);
////                             Log.d("lon", lon);
////                            oneCall(lat, lon);
//                            historyList.clear();
//                            historyWeather7(lat, lon, day7, today);
////                            historyWeather7(lat, lon, day14, day7);
////                            historyWeather7(lat, lon, day21, day14);
////                            historyWeather7(lat, lon, day28, day21);
////                            historyWeather7(lat, lon, day21, day14);
////                            historyWeather14(lat, lon, day14, day7);
////                            historyWeather14(lat, lon, day21, day14);
////                            historyWeather14(lat, lon, day28, day21);
////                            historyWeather14(lat, lon, day30, day28);
//                            Log.d("TAG69", "today: "+today);
//                            Log.d("TAG69", "day7: "+day7);
//                            Log.d("TAG69", "day14: "+day14);
//                            Log.d("TAG69", "day21: "+day21);
//                            Log.d("TAG69", "day28: "+day28);
//                            Log.d("TAG69", "day30: "+day30);
//
////                            historyWeather21(lat, lon, day21, day14);
////                            historyWeather28(lat, lon, day28, day21);
////                            historyWeather30(lat, lon, day30, day28);
//
//                            // Lấy ra tên thành phố
//                            String name = jsonObject.getString("name");
//                            // Xét text cho tên thành phố
//                            txtCityNameHW.setText(name);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//        // thực thi stringRequest
//        requestQueue.add(stringRequest);
//    }


    private void Anhxa() {
        imgBackHW = (ImageView) findViewById(R.id.imageViewBackHW);
        textViewDayDetailsHW = (TextView) findViewById(R.id.textViewDayDetailsHW);
        tvTimeDetailHistoryWeather = (TextView) findViewById(R.id.tvTimeDetailHistoryWeather);
    }
}