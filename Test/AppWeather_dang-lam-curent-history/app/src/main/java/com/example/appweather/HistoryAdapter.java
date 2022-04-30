package com.example.appweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import time.DetailTime;
import time.DetailTimeAdapter;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryWeatherViewHolder> {

    private Context mContextHistoryWeather;
    private List<History> mListHistoryWeather;

    public HistoryAdapter(Context mContextHistoryWeather, List<History> historyList) {
        this.mContextHistoryWeather = mContextHistoryWeather;
    }

    public void setData (List<History> historyList) {
        this.mListHistoryWeather = historyList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public HistoryWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_weather, parent, false);
        return new HistoryAdapter.HistoryWeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryWeatherViewHolder holder, int position) {
        History history = mListHistoryWeather.get(position);
        if (history == null) {
            return;
        }

        Picasso.with(mContextHistoryWeather).load("https://openweathermap.org/img/wn/"+history.getImgTrangThaiHW()+".png").into(holder.imgIconTrangThaiHW);
        holder.tvDayHW.setText(history.getDetailDayHW());
        holder.tvHW.setText(history.getDetailHW());
        holder.tvNhietDoHW.setText(history.getNhietDoHW() + "°C");
        holder.tvTrangThaiHW.setText(history.getTrangThaiHW());
        holder.tvTocDoGioHW.setText(history.getGioHW() + "m/s");
        holder.tvApSuatHW.setText(history.getApSuatHW() + "hPa");
        holder.tvDoAmHW.setText(history.getDoAmHW() + "%");
        holder.tvChiSoUVHW.setText(history.getChiSoUVHW());
    }

    @Override
    public int getItemCount() {
        if (mListHistoryWeather!= null) {
            return mListHistoryWeather.size();
        }
        return 0;
    }


    public class HistoryWeatherViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgIconTrangThaiHW;
        private TextView tvDayHW, tvHW, tvNhietDoHW, tvTrangThaiHW, tvTocDoGioHW, tvApSuatHW, tvDoAmHW, tvChiSoUVHW;

        public HistoryWeatherViewHolder(@NonNull View itemView) {
            super(itemView);

            imgIconTrangThaiHW = (ImageView) itemView.findViewById(R.id.imgTrangThaiHistoryWeather);
            tvDayHW = (TextView) itemView.findViewById(R.id.tvDayHistoryWeather);
            tvHW = (TextView) itemView.findViewById(R.id.tvHistoryWeather);
            tvNhietDoHW= (TextView) itemView.findViewById(R.id.tvNhietDoHistoryWeather);
            tvTrangThaiHW = (TextView) itemView.findViewById(R.id.tvTrangThaiHistoryWeather);
            tvTocDoGioHW = (TextView) itemView.findViewById(R.id.tvTocDoGioHistoryWeather);
            tvApSuatHW = (TextView) itemView.findViewById(R.id.tvApSuatHistoryWeather);
            tvDoAmHW = (TextView) itemView.findViewById(R.id.tvDoAmHistoryWeather);
            tvChiSoUVHW = (TextView) itemView.findViewById(R.id.tvChiSoUVHistoryWeather);
        }
    }

    public void giaiPhong (){
        mContextHistoryWeather= null;
    }
}
