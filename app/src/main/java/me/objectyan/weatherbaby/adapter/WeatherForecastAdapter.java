package me.objectyan.weatherbaby.adapter;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.common.BaseApplication;
import me.objectyan.weatherbaby.common.Util;
import me.objectyan.weatherbaby.entities.CityInfo;
import me.objectyan.weatherbaby.entities.database.CityDailyForecast;
import me.objectyan.weatherbaby.entities.database.CityDailyForecastDao;
import me.objectyan.weatherbaby.entities.database.CityHourlyForecastDao;
import me.objectyan.weatherbaby.entities.database.CityLifestyleForecastDao;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.WeatherForecastItem> {

    @NonNull
    @Override
    public WeatherForecastItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_weather_forecast_item, parent, false);
        return new WeatherForecastItem(view);
    }

    private CityDailyForecastDao cityDailyForecastDao;
    private List<CityDailyForecast> mDatas;
    private Long mCityID;

    public WeatherForecastAdapter(Long cityID) {
        mDatas = new ArrayList<>();
        cityDailyForecastDao = BaseApplication.getDaoSession().getCityDailyForecastDao();
        mCityID = cityID;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull WeatherForecastItem holder, int position) {
        CityDailyForecast cityDailyForecast = mDatas.get(position);
        holder.todayTempCurrMax.setText(Util.getTempByUnit(cityDailyForecast.getMaximumTemperature()));
        holder.todayTempCurrMin.setText(Util.getTempByUnit(cityDailyForecast.getMinimumTemperature()));
        holder.forecastItemIcWeather.setImageDrawable(Util.getHeWeatherIcon(Util.isDay() ? cityDailyForecast.getCondCodeDay() : cityDailyForecast.getCondCodeEvening()));
        holder.forecastItemDate.setText(Util.getDateByFormat(cityDailyForecast.getDate()));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void updateData() {
        mDatas = cityDailyForecastDao.queryBuilder().where(CityDailyForecastDao.Properties.CityID.eq(mCityID)).list();
        notifyDataSetChanged();
    }

    public class WeatherForecastItem extends RecyclerView.ViewHolder {

        @BindView(R.id.forecast_item_ic_weather)
        AppCompatImageView forecastItemIcWeather;
        @BindView(R.id.today_temp_curr_max)
        TextView todayTempCurrMax;
        @BindView(R.id.today_temp_curr_to)
        TextView todayTempCurrTo;
        @BindView(R.id.today_temp_curr_min)
        TextView todayTempCurrMin;
        @BindView(R.id.forecast_item_date)
        TextView forecastItemDate;

        public WeatherForecastItem(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
