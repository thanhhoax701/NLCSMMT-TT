package com.example.appweather;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.czp.library.ArcProgress;
//import com.czp.library.OnTextCenter;
//import com.czp.library.onImageCenter;
import com.example.appweather.api.Api;
import com.example.appweather.history.HistoryWeatherActivity;
import com.example.appweather.model.Weather;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import time.Time;
import time.TimeAdapter;

public class MainActivity extends AppCompatActivity {
    final String APP_ID = "7b16a3bb0d4c6253ab56ca6a2a14f500";
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    //    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/onecall?";
    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;
    String Location_Provider = LocationManager.GPS_PROVIDER;
    LocationManager mLocationManager;
    LocationListener mLocationListener;
    DatabaseReference mFirebaseDatabase;
    FirebaseDatabase mFirebaseInstance;
    EditText edtSearch;
    Button iconLocation, btnChangeActivity, btnAddTimeWeather, btnAddCurrentWeather, btnHistoryWeather;
    TextView txtViewName, txtViewCountry, txtViewTemp, txtViewStatus, txtViewHumidity, txtViewCloud, txtViewMill, txtViewDay, txtUpdateTime, txtUpdateCurrent, txtBinhMinh, txtHoangHon, txtTocDoGio, txtLuongMua, txtDoAmTuongDoi, txtCamGiacNhu, txtUV, txtTamNhin;
    ImageView iconSearch, imgIcon;

    String City = "", lat, lon;
    Context context;

    int pStatus = 0;
    private Handler handler = new Handler();
    TextView tv;
    ProgressBar mProgressAir;

    List<Time> list;
    private RecyclerView rcvTime;
    private TimeAdapter mTimeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseInstance = FirebaseDatabase.getInstance("https://weather-efe43-default-rtdb.asia-southeast1.firebasedatabase.app");

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("weatherHistory");

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.custom_progressbar2);
        mProgressAir = (ProgressBar) findViewById(R.id.circularProgressbar);
        // Main Progress
        mProgressAir.setProgress(0);
        // Secondary Progress
        mProgressAir.setSecondaryProgress(100);
        // Maximum Progress
        mProgressAir.setMax(100);
        mProgressAir.setProgressDrawable(drawable);
        tv = (TextView) findViewById(R.id.tv);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (pStatus < 1) {
                    pStatus += 1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressAir.setProgress(pStatus);
                            tv.setText(pStatus + "%");
                        }
                    });
                    try {
                        // Thời gian xoay trên 1%
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                callAll(lat, lon);
            }
        }).start();


        context = this;

        Anhxa();
        // gán 1 thành phố mặc định khi mở app
        getCurrentWeatherData("Vĩnh Long");
        callAll(lat, lon);
        getWeatherForCurrentLocation();


        // Tìm kiếm thành phố
        iconSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = edtSearch.getText().toString();
                // Cách 1
                if (city.equals("")) {
                    city = "Vĩnh Long";
                    getCurrentWeatherData(City);
                } else {
                    City = city;
                    getCurrentWeatherData(City);
                }
                // Cách 2
//                if (TextUtils.isEmpty(city)) {
//                    Toast.makeText(context, "Bạn chưa nhập dữ liệu", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                getCurrentWeatherData(city);
            }
        });

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String city = edtSearch.getText().toString();
                // Cách 1
                if (city.equals("")) {
                    city = "Vĩnh Long";
                    getCurrentWeatherData(City);
                } else {
                    City = city;
                    getCurrentWeatherData(City);
                }
                getCurrentWeatherData(city);
                return false;
            }
        });

        iconLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        btnChangeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = edtSearch.getText().toString();
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("name", city);
                startActivity(intent);
            }
        });

        // Dự báo theo giờ
        btnAddTimeWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = edtSearch.getText().toString();
                Intent intent = new Intent(MainActivity.this, TimeWeather.class);
                intent.putExtra("name", city);
                startActivity(intent);
            }
        });

        // Xem thời tiết hiện tại chi tiết hơn
        btnAddCurrentWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = edtSearch.getText().toString();
                Intent intent = new Intent(MainActivity.this, CurrentWeather.class);
                intent.putExtra("name", city);
                startActivity(intent);
            }
        });


        list = new ArrayList<>();

        rcvTime = findViewById(R.id.rcv_time);
        mTimeAdapter = new TimeAdapter(getApplicationContext(), list);
        // Category hướng horizontal
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rcvTime.setLayoutManager(linearLayoutManager);

        // Xét dữ liệu cho adapter
        mTimeAdapter.setData(list);
        rcvTime.setAdapter(mTimeAdapter);


        // Xem lịch sử thời tiết trong vòng 30 ngày
        btnHistoryWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = edtSearch.getText().toString();
                Intent intent = new Intent(MainActivity.this, HistoryWeatherActivity.class);
                intent.putExtra("name", city);
                startActivity(intent);
            }
        });
    }


//    Handler handlerA = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            ArcProgress progressBar = (ArcProgress) msg.obj;
//            progressBar.setProgress(msg.what);
//        }
//    };

//    public void addProrgress(ArcProgress progressBar) {
//        Thread thread = new Thread(new ProgressThread(progressBar));
//        thread.start();
//    }

//    class ProgressThread implements Runnable{
//        int i= 0;
//        private ArcProgress progressBar;
//        public ProgressThread(ArcProgress progressBar) {
//            this.progressBar = progressBar;
//        }
//        @Override
//        public void run() {
//            for(;i<=100;i++){
//                if(isFinishing()){
//                    break;
//                }
//                Message msg = new Message();
//                msg.what = i;
//                Log.e("DEMO","i == "+i);
//                msg.obj = progressBar;
//                SystemClock.sleep(100);
//                handler.sendMessage(msg);
//            }
//        }
//    }


    @Override
    protected void onResume() {
        super.onResume();
        getWeatherForCurrentLocation();
    }

    private void getWeatherForCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());
                Log.d("hello ", Longitude + "  " + Latitude);
                RequestParams params = new RequestParams();
                params.put("lat", Latitude);
                params.put("lon", Longitude);
                params.put("appid", APP_ID);
                letsDoSomeNetworking(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                //not able to get location
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, mLocationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Get The Location Successfully", Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            } else {
                //user denied the permission
                Toast.makeText(MainActivity.this, "Get Location Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void letsDoSomeNetworking(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(MainActivity.this, "Data Get Success", Toast.LENGTH_SHORT).show();
                weatherData weatherD = weatherData.fromJson(response);
                updateUI(weatherD);
//                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void updateUI(weatherData weather) {
        txtViewTemp.setText(weather.getmTemperature());
        txtViewName.setText(weather.getMcity());
        txtViewStatus.setText(weather.getmWeatherType());
//        int resourceID=getResources().getIdentifier(weather.getMicon(),"drawable",getPackageName());
//        imgIcon.setImageResource(resourceID);
        Picasso.with(MainActivity.this).load("https://openweathermap.org/img/wn/01d.png").into(imgIcon);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }

    public void getCurrentWeatherData(String data) {
        // Thực thi những request mà mình gửi đi
        // Cú pháp Request của thư viện Volley
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500";
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
                            Log.d("lat", lat);
                            Log.d("lon", lon);

                            callAll(lat, lon);
                            history30days();
                            // Lấy ra tên thành phố
                            String name = jsonObject.getString("name");
                            // Xét text cho tên thành phố
                            txtViewName.setText(name);

                            // Lấy dữ liệu từ tag "sys" ---------
                            JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                            String country = jsonObjectSys.getString("country");
                            // Xét text cho country
                            txtViewCountry.setText(", " + country);
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

    public void callAll(String lat1, String lon1) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/onecall?lat=" + lat1 + "&lon=" + lon1 + "&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500";
        Date date = new Date();
        Log.d("url", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Main", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            // Dự báo theo giờ
                            JSONArray jsonArrayList = jsonObject.getJSONArray("hourly");
                            // Do các thẻ có thuộc tính giống nhau
                            // => dùng for để đọc giá trị của từng thẻ trong tag "hourly"
                            list.clear();
                            for (int j = 0; j < jsonArrayList.length(); j++) {
                                JSONObject jsonObjectList = jsonArrayList.getJSONObject(j);

                                String dayHourly = jsonObjectList.getString("dt");
                                // Chuyển biến day về dạng long
                                long l = Long.valueOf(dayHourly);
                                // Chuyển thành mili giây
                                Date dateHourly = new Date(l * 1000L);
                                // Định dạng thứ ngày tháng năm
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H:mm");
                                String time = simpleDateFormat.format(dateHourly);


                                String tempHourly = jsonObjectList.getString("temp");
                                // -> cần chuyển đổi nhiệt độ sang kiểu int
                                Double x = Double.valueOf(tempHourly);
                                // Đổi về kiểu int bằng intValue rồi đổi sang chuỗi
                                String temp = String.valueOf(x.intValue());

                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                // Do nó có 1 thẻ object thôi
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String imgIcon = jsonObjectWeather.getString("icon");

                                String speed = jsonObjectList.getString("wind_speed");

                                Log.d("TAG", "onResponse: " + imgIcon + "," + time + "," + temp + "," + speed);
                                if (j == 0) {
                                    time = "Bây giờ";
                                }
                                list.add(new Time(imgIcon, time, temp, speed));
                            }
//                            // Cập nhật khi có dữ liệu mới
                            mTimeAdapter.notifyDataSetChanged();


                            JSONObject jsonObjectCurrent = jsonObject.getJSONObject("current");

                            // Lấy ra thằng weather
                            JSONArray jsonArrayWeather = jsonObjectCurrent.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String icon = jsonObjectWeather.getString("icon");

                            // Vào đường dẫn https://openweathermap.org/weather-conditions -> vào mục How to get icon URL để lấy link icon
                            // Gọi lại thư viện PICASSO
                            // Sử dụng with để cho biết đang ở trang hiện tại
                            // sử dụng load để lấy đường dẫn của icon về
                            // sử dụng into để hiển thị ra màn hình, bên trong into là tên biến của cái icon đã được đặt để hiển thị
                            Picasso.with(MainActivity.this).load("https://openweathermap.org/img/wn/" + icon + ".png").into(imgIcon);

                            // Lấy nhiệt độ và convert sang chuỗi
                            String temp = jsonObjectCurrent.getString("temp");
                            // Nhiệt độ có kiểu dữ liệu là double
                            // -> cần chuyển đổi nhiệt độ sang kiểu int
                            Double a = Double.valueOf(temp);
                            // Đổi về kiểu int bằng intValue rồi đổi sang chuỗi
                            String nhietDo = String.valueOf(a.intValue());
                            // Xét text cho nhiệt độ
                            txtViewTemp.setText(nhietDo + "°C");

                            String Status = jsonObjectWeather.getString("description");
                            // Xét text cho status: kiểu như in ra màn hình vậy
                            txtViewStatus.setText(Status);

                            // Lấy phần trăm độ ẩm
                            String doAm = jsonObjectCurrent.getString("humidity");
                            txtViewHumidity.setText(doAm + "%");
                            tv.setText(doAm + "%");
                            int x = Integer.parseInt(doAm);
                            mProgressAir.setProgress(x);

                            // Lấy mây
                            String cloud = jsonObjectCurrent.getString("clouds");
                            txtViewCloud.setText(cloud + "%");

                            // Lấy tốc độ gió
                            String windSpeed = jsonObjectCurrent.getString("wind_speed");
                            txtViewMill.setText(windSpeed + "m/s");


                            // Lấy ra thời gian hiện tại
                            String day = jsonObjectCurrent.getString("dt");
                            // Chuyển biến day về dạng long
                            long l = Long.valueOf(day);
                            // Giá trị mili giây
                            Date date = new Date(l * 1000L);
                            // Định dạng thứ ngày tháng năm giờ phút giây
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, d MMM yyyy HH:mm:ss");
                            String Day = simpleDateFormat.format(date);
                            // Xét text cho ngày
                            txtViewDay.setText(Day);

                            // Này là cập nhật thời gian hiện tại của Dự báo theo giờ
                            SimpleDateFormat simpleDateFormatDay = new SimpleDateFormat("HH:mm:ss");
                            String DayTime = simpleDateFormatDay.format(date);
                            txtUpdateTime.setText("Cập nhật: " + DayTime);

                            // Này là cập nhật thời gian hiện tại của Thời tiết hiện tại
                            txtUpdateCurrent.setText("Cập nhật: " + DayTime);


                            // Bình minh và hoàng hôn
                            String binhMinh = jsonObjectCurrent.getString("sunrise");
                            // Chuyển biến day về dạng long
                            long bm = Long.valueOf(binhMinh);
                            // Giá trị mili giây
                            Date BM = new Date(bm * 1000L);
                            // Định dạng giờ phút giây
                            SimpleDateFormat simpleDateFormatBM = new SimpleDateFormat("h:mm a");
                            String BinhMinh = simpleDateFormatBM.format(BM);
                            txtBinhMinh.setText(BinhMinh);

                            String hoangHon = jsonObjectCurrent.getString("sunset");
                            // Chuyển biến day về dạng long
                            long hh = Long.valueOf(hoangHon);
                            // Giá trị mili giây
                            Date HH = new Date(hh * 1000L);
                            // Định dạng giờ phút giây
                            SimpleDateFormat simpleDateFormatHH = new SimpleDateFormat("h:mm a");
                            String HoangHon = simpleDateFormatHH.format(HH);
                            txtHoangHon.setText(HoangHon);


                            // Thời tiết hiện tại
                            // Tốc độ gió
                            txtTocDoGio.setText(windSpeed + "m/s");

//                            // Lượng mưa
                            JSONObject jsonObjectRain = jsonObjectCurrent.getJSONObject("rain");
                            String luongMua = jsonObjectRain.getString("1h");
                            txtLuongMua.setText(luongMua + "mm");

                            // Độ ẩm tương đối
                            String doAmTuongDoi = jsonObjectCurrent.getString("humidity");
                            txtDoAmTuongDoi.setText(doAmTuongDoi + "%");

                            // Cảm giác như
                            String camGiacNhu = jsonObjectCurrent.getString("feels_like");
                            Double b = Double.valueOf(camGiacNhu);
                            // Đổi về kiểu int bằng intValue rồi đổi sang chuỗi
                            String CamGiacNhu = String.valueOf(b.intValue());
                            txtCamGiacNhu.setText(CamGiacNhu + "°C");
//
                            // Chỉ số UV
                            String UV = jsonObjectCurrent.getString("uvi");
                            txtUV.setText(UV);

//                            // Tầm nhìn
                            String tamNhin = jsonObjectCurrent.getString("visibility");
                            txtTamNhin.setText(tamNhin + "m");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", "Có Lỗi");
                    }
                });
        requestQueue.add(stringRequest);
    }

    // Ánh xạ để lấy từng thành phần đã được đặt id
    private void Anhxa() {
        iconLocation = (Button) findViewById(R.id.viTriHienTai);

        edtSearch = (EditText) findViewById(R.id.editTextSearch);
        iconSearch = (ImageView) findViewById(R.id.iconSearch);
        btnChangeActivity = (Button) findViewById(R.id.buttonChangeActivity);
        txtViewName = (TextView) findViewById(R.id.textViewName);
        txtViewCountry = (TextView) findViewById(R.id.textViewCountry);
        txtViewTemp = (TextView) findViewById(R.id.textViewTemp);
        txtViewStatus = (TextView) findViewById(R.id.textViewStatus);
        txtViewHumidity = (TextView) findViewById(R.id.textViewHumidity);
        txtViewCloud = (TextView) findViewById(R.id.textViewCloud);
        txtViewMill = (TextView) findViewById(R.id.textViewMill);
        txtViewDay = (TextView) findViewById(R.id.textViewDay);
        imgIcon = (ImageView) findViewById(R.id.imageIcon);
        // Dự báo theo giờ
        txtUpdateTime = (TextView) findViewById(R.id.textViewUpdateTime);
        rcvTime = (RecyclerView) findViewById(R.id.rcv_time);
        list = new ArrayList<Time>();
        mTimeAdapter = new TimeAdapter(MainActivity.this, list);
        rcvTime.setAdapter(mTimeAdapter);
        btnAddTimeWeather = (Button) findViewById(R.id.btnAddTimeWeather);


        // Bình minh và hoàng hôn
        txtBinhMinh = (TextView) findViewById(R.id.textViewBinhMinh);
        txtHoangHon = (TextView) findViewById(R.id.textViewHoangHon);


        // Thời tiết hiện tại tại màn hình chính
        txtUpdateCurrent = (TextView) findViewById(R.id.textViewUpdateCurrent);
        txtTocDoGio = (TextView) findViewById(R.id.textViewTocDoGio);
        txtLuongMua = (TextView) findViewById(R.id.textViewLuongMua);
        txtDoAmTuongDoi = (TextView) findViewById(R.id.textViewDoAmTuongDoi);
        txtCamGiacNhu = (TextView) findViewById(R.id.textViewCamGiacNhu);
        txtUV = (TextView) findViewById(R.id.textViewUV);
        txtTamNhin = (TextView) findViewById(R.id.textViewTamNhin);
        btnAddCurrentWeather = (Button) findViewById(R.id.btnAddCurrentWeather);

        // Xem lịch sử thời tiết trong vòng 30 ngày, dữ liệu được tính trên mỗi giờ
        btnHistoryWeather = (Button) findViewById(R.id.btnHistoryWeather);
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
        severApi.callApi(lat, lon, "hour", "metric", "en", "7b16a3bb0d4c6253ab56ca6a2a14f500", timeUnix(date, 30), unixTime).enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, retrofit2.Response<Weather> response) {
                mFirebaseDatabase.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });

    }

    public long timeUnix(Date d, int beforeDays) {
        d.setTime(d.getTime() - (beforeDays * 1000L * 60 * 60 * 24));
        return d.getTime() / 1000;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimeAdapter.giaiPhong();
    }
}