package me.objectyan.weatherbaby.adapter;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.ZoneId;
import java.time.zone.ZoneRules;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.common.BaseApplication;
import me.objectyan.weatherbaby.common.Util;
import me.objectyan.weatherbaby.entities.database.CityHourlyForecast;
import me.objectyan.weatherbaby.entities.database.CityHourlyForecastDao;

public class WeatherTimelineAdapter extends RecyclerView.Adapter<WeatherTimelineAdapter.WeatherTimelineItem> {

    @NonNull
    @Override
    public WeatherTimelineItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_weather_timeline_item, parent, false);
        return new WeatherTimelineItem(view);
    }

    private CityHourlyForecastDao cityHourlyForecastDao;
    private List<CityHourlyForecast> mDatas;
    private Long mCityID;

    public WeatherTimelineAdapter(Long cityID) {
        mDatas = new ArrayList<>();
        cityHourlyForecastDao = BaseApplication.getDaoSession().getCityHourlyForecastDao();
        mCityID = cityID;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull WeatherTimelineItem holder, int position) {
        CityHourlyForecast cityHourlyForecast = mDatas.get(position);
        holder.time.setText(Util.dateToTimeSlot(cityHourlyForecast.getDateTime()));
        if (cityHourlyForecast.getProbability() > 0)
            holder.probability.setText(String.format("%.4f mm/h", cityHourlyForecast.getProbability()));
        else
            holder.probability.setText("");
        holder.timeDesc.setText(Util.getTempByUnit(cityHourlyForecast.getTemperature()));
        holder.timeIcon.setImageDrawable(Util.getHeWeatherIcon(cityHourlyForecast.getCondCode()));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void updateData() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        mDatas = cityHourlyForecastDao.queryBuilder().where(CityHourlyForecastDao.Properties.CityID.eq(mCityID),
                CityHourlyForecastDao.Properties.DateTime.ge(calendar.getTimeInMillis())).orderAsc(CityHourlyForecastDao.Properties.DateTime).list();
        notifyDataSetChanged();
    }

    public class WeatherTimelineItem extends RecyclerView.ViewHolder {

        @BindView(R.id.time_icon)
        AppCompatImageView timeIcon;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.probability)
        TextView probability;
        @BindView(R.id.time_desc)
        TextView timeDesc;

        public WeatherTimelineItem(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
