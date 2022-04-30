package com.example.appweather.history;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.appweather.MainActivity;
import com.example.appweather.R;
import com.example.appweather.model.List;
import com.example.appweather.model.ListFirebase;
import com.example.appweather.model.Weather;
import com.example.appweather.model.WeatherDay;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryWeatherViewHolder> {

    private Context mContextHistoryWeather;
    private ArrayList<ListFirebase> listFirebases= null;
    private ArrayList<WeatherDay>weatherDays = null;
    private ArrayList<List> list = null;

    public HistoryAdapter(Context mContextHistoryWeather) {
        this.mContextHistoryWeather = mContextHistoryWeather;
    }

    public void setDataList(ArrayList<ListFirebase> list) {
        this.listFirebases = list;
        notifyDataSetChanged();
    }
    public void setData(ArrayList<List>list){
        this.list= list;
        notifyDataSetChanged();
    }
    public void  setWeatherDays(ArrayList<WeatherDay> weatherDays){
        this.weatherDays = weatherDays;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public HistoryWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_weather, parent, false);
            return  new HistoryAdapter.HistoryWeatherViewHolder(view);
        }


    @Override
    public void onBindViewHolder(@NonNull HistoryWeatherViewHolder holder, int position) {
        if (list.get(position)== null) {
            return;
        }
        ArrayList<WeatherDay> weatherDay = list.get(position).getWeather();
        Log.d("hhh", "onBindViewHolder: "+ list.get(position));
        Picasso.with(mContextHistoryWeather).load("https://openweathermap.org/img/wn/" +weatherDay.get(0).getIcon() + ".png").into(holder.imgIconTrangThaiHW);
//        Picasso.with(mContextHistoryWeather).load("https://openweathermap.org/img/wn/"+weatherDay.get(0).getIcon()+".png").into(holder.imgIconTrangThaiHW);
        holder.tvDayHW.setText(date(list.get(position).getDt()));
        holder.tvNhietDoHW.setText(list.get(position).getMain().getTemp()+ "°C");
        holder.tvTrangThaiHW.setText(weatherDay.get(0).getDescription());
        holder.tvCamGiacNhuHW.setText(list.get(position).getMain().getFeel_like()+ "°C");
        holder.tvTocDoGioHW.setText(list.get(position).getWind().getSpeed()+ "m/s");
        holder.tvHuongGioHW.setText(doiHuong(""+list.get(position).getWind().getDeg()));
        holder.tvApSuatHW.setText(list.get(position).getMain().getPressure()+ "hPa");
        holder.tvDoAmHW.setText(list.get(position).getMain().getHumidity() + "%");
    }

    @Override
    public int getItemCount() {
        if (list!= null) {
            return list.size();
        }
        return 0;
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
    public String date(long time){
        Date date = new Date();
        date.setTime(time *1000);
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat();
        return simpleDateFormat.format(date);
    }
    public class HistoryWeatherViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgIconTrangThaiHW;
        private TextView tvDayHW, tvNhietDoHW, tvTrangThaiHW, tvCamGiacNhuHW, tvTocDoGioHW, tvHuongGioHW, tvApSuatHW, tvDoAmHW;

        public HistoryWeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIconTrangThaiHW = (ImageView) itemView.findViewById(R.id.imgTrangThaiHistoryWeather);
            tvDayHW = (TextView) itemView.findViewById(R.id.tvDayHistoryWeather);
            tvNhietDoHW= (TextView) itemView.findViewById(R.id.tvNhietDoHistoryWeather);
            tvTrangThaiHW = (TextView) itemView.findViewById(R.id.tvTrangThaiHistoryWeather);
            tvCamGiacNhuHW = (TextView) itemView.findViewById(R.id.tvCamGiacNhuHistoryWeather);
            tvTocDoGioHW = (TextView) itemView.findViewById(R.id.tvTocDoGioHistoryWeather);
            tvHuongGioHW = (TextView) itemView.findViewById(R.id.tvHuongGioHistoryWeather);
            tvApSuatHW = (TextView) itemView.findViewById(R.id.tvApSuatHistoryWeather);
            tvDoAmHW = (TextView) itemView.findViewById(R.id.tvDoAmHistoryWeather);
        }

    }


}