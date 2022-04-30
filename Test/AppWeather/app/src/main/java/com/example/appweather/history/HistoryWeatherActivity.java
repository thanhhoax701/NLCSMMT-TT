package com.example.appweather.history;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appweather.R;
import com.example.appweather.api.Api;
import com.example.appweather.model.ListFirebase;
import com.example.appweather.model.Weather;
import com.example.appweather.model.WeatherDay;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoryWeatherActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    DatabaseReference mFirebaseDatabase;
    FirebaseDatabase mFirebaseInstance;
    TextView txtCityNameHW;
    ImageView imgBackHW;
    String cityName = "";
    int j = 0;
    double lat =10.2544;
    double lon= 105.967;
    ArrayList<ListFirebase> listFirebases = null;
    ArrayList<WeatherDay> weatherDays = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_wreather);
        mFirebaseInstance = FirebaseDatabase.getInstance("https://weather-efe43-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mFirebaseDatabase = mFirebaseInstance.getReference("weatherHistory");
        Anhxa();
        init();

        //  Trở về
        imgBackHW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public void init(){
        Intent intent = getIntent();
        String city = intent.getStringExtra("name");
        lat  = intent.getDoubleExtra("lat",10.2544);
        lon = intent.getDoubleExtra("lon",105.967);
        Log.d("ketqua", "Du lieu truyen qua: " + city);
        if (city.equals("")) {
            cityName = "Vĩnh Long";
        }
        else {
            cityName = city;
        }
        recyclerView = findViewById(R.id.rcvHistoryWeather);
        recyclerView.setHasFixedSize(true);
        historyAdapter = new HistoryAdapter(getApplicationContext());
        recyclerView.setAdapter(historyAdapter);
        history30days();
    }

    public void historry30dayFirebase(){
        mFirebaseDatabase.child("list").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listFirebases = (ArrayList<ListFirebase>) snapshot.getValue();
                for (int i =0; i <listFirebases.size(); i++){
                    mFirebaseDatabase.child("list").child(""+i).child("weather").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            j++;
                            ArrayList<WeatherDay> a =(ArrayList<WeatherDay>) snapshot.getValue();

                            if (j== listFirebases.size()-1){
                                historyAdapter.setDataList(listFirebases);
                                historyAdapter.setWeatherDays(weatherDays);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void history30days() {
        Date date = new Date();
        long unixTime = new Date().getTime() / 1000L;
        final Gson gson = new GsonBuilder().setLenient().create();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://history.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        com.example.appweather.api.Api severApi = retrofit.create(Api.class);
        Log.d(TAG, "history30days: " + timeUnix(date, 30));
        Log.d(TAG, "history30days: " + unixTime);
        severApi.callApi(""+lat, ""+lon, "hour", "metric", "vi", "7b16a3bb0d4c6253ab56ca6a2a14f500", timeUnix(date, 30), unixTime).enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, retrofit2.Response<Weather> response) {
               historyAdapter.setData(response.body().getList());
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });
    }
    public long  timeUnix(Date d,int beforeDays){
        d.setTime(d.getTime()-(beforeDays * 1000L * 60 * 60 * 24));
        return d.getTime()/1000;
    }

        private void Anhxa() {
        imgBackHW = (ImageView) findViewById(R.id.imageViewBackHW);
        txtCityNameHW = (TextView) findViewById(R.id.textViewCityNameHW);
    }
}