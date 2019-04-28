package me.objectyan.weatherbaby.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.common.BaseApplication;
import me.objectyan.weatherbaby.common.Util;
import me.objectyan.weatherbaby.entities.database.CityBase;
import me.objectyan.weatherbaby.entities.database.CityBaseDao;
import me.objectyan.weatherbaby.entities.database.CityHourlyForecastDao;
import me.objectyan.weatherbaby.widget.ArcView;
import me.objectyan.weatherbaby.widget.GridView;
import me.objectyan.weatherbaby.widget.SunlightView;
import me.objectyan.weatherbaby.widget.WindView;

public class CityWeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int WEATHER_TODAY = 0;
    private static final int WEATHER_TIMELINE = 1;
    private static final int WEATHER_FORECAST = 2;
    private static final int WEATHER_LIFESTYLE = 3;
    private static final int WEATHER_ATMOSPHERE = 4;
    private static final int WEATHER_COMFORT_DEGREE = 5;
    private static final int WEATHER_WIND = 6;
    private static final int WEATHER_SUNLIGHT = 7;

    private Long mCityID;

    private Context mContext;

    private CityBaseDao cityBaseDao;
    private CityHourlyForecastDao cityHourlyForecastDao;
    private CityBase cityBase;
    private long cityHourlySize;

    private RecyclerView.RecycledViewPool mSharedPool = new RecyclerView.RecycledViewPool();

    public CityWeatherAdapter(Long mCityID) {
        this.mCityID = mCityID;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case WEATHER_TODAY:
                return WEATHER_TODAY;
            case WEATHER_TIMELINE:
                return WEATHER_TIMELINE;
            case WEATHER_FORECAST:
                return WEATHER_FORECAST;
            case WEATHER_LIFESTYLE:
                return WEATHER_LIFESTYLE;
            case WEATHER_ATMOSPHERE:
                return WEATHER_ATMOSPHERE;
            case WEATHER_COMFORT_DEGREE:
                return WEATHER_COMFORT_DEGREE;
            case WEATHER_WIND:
                return WEATHER_WIND;
            case WEATHER_SUNLIGHT:
                return WEATHER_SUNLIGHT;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        cityBaseDao = BaseApplication.getDaoSession().getCityBaseDao();
        cityHourlyForecastDao = BaseApplication.getDaoSession().getCityHourlyForecastDao();
        initData();
        switch (viewType) {
            case WEATHER_TODAY:
                return new TodayViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.fragment_weather_today, parent, false));
            case WEATHER_TIMELINE:
                return new TimelineViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.fragment_weather_timeline, parent, false));
            case WEATHER_FORECAST:
                return new ForecastViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.fragment_weather_forecast, parent, false));
            case WEATHER_LIFESTYLE:
                return new LifestyleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.fragment_weather_lifestyle, parent, false));
            case WEATHER_ATMOSPHERE:
                return new AtmosphereViewHolder(LayoutInflater.from(mContext).inflate(R.layout.fragment_weather_atmosphere, parent, false));
            case WEATHER_COMFORT_DEGREE:
                return new ComfortDegreeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.fragment_weather_comfort_degree, parent, false));
            case WEATHER_WIND:
                return new WindViewHolder(LayoutInflater.from(mContext).inflate(R.layout.fragment_weather_wind, parent, false));
            case WEATHER_SUNLIGHT:
                return new SunlightViewHolder(LayoutInflater.from(mContext).inflate(R.layout.fragment_weather_sunlight, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case WEATHER_TODAY:
                ((TodayViewHolder) holder).bind();
                break;
            case WEATHER_TIMELINE:
                ((TimelineViewHolder) holder).bind();
                break;
            case WEATHER_FORECAST:
                ((ForecastViewHolder) holder).bind();
                break;
            case WEATHER_LIFESTYLE:
                ((LifestyleViewHolder) holder).bind();
                break;
            case WEATHER_ATMOSPHERE:
                ((AtmosphereViewHolder) holder).bind();
                break;
            case WEATHER_COMFORT_DEGREE:
                ((ComfortDegreeViewHolder) holder).bind();
                break;
            case WEATHER_WIND:
                ((WindViewHolder) holder).bind();
                break;
            case WEATHER_SUNLIGHT:
                ((SunlightViewHolder) holder).bind();
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    private void initData() {
        cityBase = cityBaseDao.queryBuilder().where(CityBaseDao.Properties.Id.eq(mCityID)).unique();
        cityHourlySize = cityHourlyForecastDao.queryBuilder().where(CityHourlyForecastDao.Properties.CityID.eq(mCityID)).count();
    }

    public void refreshing() {
        initData();
        notifyDataSetChanged();
    }

    class TodayViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.today_temp_curr)
        TextView todayTempCurr;
        @BindView(R.id.today_temp_curr_unit)
        TextView todayTempCurrUnit;
        @BindView(R.id.today_temp_weather)
        TextView todayTempWeather;
        @BindView(R.id.today_temp_curr_max)
        TextView todayTempCurrMax;
        @BindView(R.id.today_temp_curr_to)
        TextView todayTempCurrTo;
        @BindView(R.id.today_temp_curr_min)
        TextView todayTempCurrMin;
        @BindView(R.id.today_temp_atmosphere)
        TextView todayTempAtmosphere;
        @BindView(R.id.today_temp_early_warning)
        TextView todayTempEarlyWarning;
        @BindView(R.id.today_temp_publish_time)
        TextView todayTempPublishTime;
        @BindView(R.id.today_temp_update_time)
        TextView todayTempUpdateTime;

        TodayViewHolder(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
        }

        void bind() {
            if (cityBase.getUpdateTime() == null) return;
            todayTempUpdateTime.setText(String.format(mContext.getString(R.string.weather_update_time), Util.utcToLocal(cityBase.getUpdateTime())));
            todayTempPublishTime.setText(String.format(mContext.getString(R.string.weather_publish_time), Util.utcToLocal(cityBase.getPublishTime())));
            todayTempCurr.setText(Util.getTemp(cityBase.getTemperature()));
            todayTempCurrUnit.setText(Util.getSettingsTempUnit());
            todayTempWeather.setText(cityBase.getCondTxt());
            todayTempCurrMax.setText(Util.getTempByUnit(cityBase.getTempHigh()));
            todayTempCurrMin.setText(Util.getTempByUnit(cityBase.getTempLow()));
            todayTempAtmosphere.setText(cityBase.getAirQuality());
        }
    }

    class TimelineViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.timeline_items)
        RecyclerView timelineItems;
        @BindView(R.id.fragment_weather_timeline)
        CardView fragmentWeatherTimeline;
        private WeatherTimelineAdapter weatherTimelineAdapter;

        TimelineViewHolder(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
            weatherTimelineAdapter = new WeatherTimelineAdapter(mCityID);
            timelineItems.setLayoutManager(new LinearLayoutManager(inflate.getContext(), LinearLayoutManager.HORIZONTAL, false));
            timelineItems.setAdapter(weatherTimelineAdapter);
        }

        void bind() {
            fragmentWeatherTimeline.setVisibility(cityHourlySize == 0 ? View.GONE : View.VISIBLE);
            if (cityHourlySize > 0)
                weatherTimelineAdapter.updateData();
        }
    }

    class ForecastViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.forecast_items)
        RecyclerView forecastItems;
        private WeatherForecastAdapter weatherForecastAdapter;

        ForecastViewHolder(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
            weatherForecastAdapter = new WeatherForecastAdapter(mCityID);
            forecastItems.setLayoutManager(new LinearLayoutManager(inflate.getContext()));
            forecastItems.setAdapter(weatherForecastAdapter);
        }

        void bind() {
            weatherForecastAdapter.updateData();
        }
    }

    class LifestyleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.gv_lifestyle_manage)
        GridView gvLifestyleManage;
        private WeatherLifestyleAdapter weatherLifestyleAdapter;

        LifestyleViewHolder(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
            weatherLifestyleAdapter = new WeatherLifestyleAdapter(inflate.getContext(), R.layout.fragment_weather_lifestyle_item, mCityID);
            gvLifestyleManage.setAdapter(weatherLifestyleAdapter);
        }

        public void bind() {
            weatherLifestyleAdapter.updateData();
        }
    }

    class AtmosphereViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.atmosphere_chart)
        ArcView atmosphereChart;
        @BindView(R.id.atmosphere_PM10)
        TextView atmospherePM10;
        @BindView(R.id.atmosphere_PM25)
        TextView atmospherePM25;
        @BindView(R.id.atmosphere_NO2)
        TextView atmosphereNO2;
        @BindView(R.id.atmosphere_SO2)
        TextView atmosphereSO2;
        @BindView(R.id.atmosphere_CO)
        TextView atmosphereCO;
        @BindView(R.id.atmosphere_O3)
        TextView atmosphereO3;

        AtmosphereViewHolder(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
        }

        void bind() {

            atmosphereChart.setDensity(cityBase.getAqi());
            atmosphereChart.setValue(String.valueOf(cityBase.getAqi()));
            atmosphereChart.setSubValue(cityBase.getAirQuality());
            atmospherePM10.setText(String.valueOf(cityBase.getPm10()));
            atmospherePM25.setText(String.valueOf(cityBase.getPm25()));
            atmosphereCO.setText(String.valueOf(cityBase.getCo()));
            atmosphereNO2.setText(String.valueOf(cityBase.getNo2()));
            atmosphereO3.setText(String.valueOf(cityBase.getO3()));
            atmosphereSO2.setText(String.valueOf(cityBase.getSo2()));
        }
    }

    class ComfortDegreeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.comfort_degree_chart)
        ArcView comfortDegreeChart;
        @BindView(R.id.comfort_degree_sendible_temp)
        TextView comfortDegreeSendibleTemp;
        @BindView(R.id.comfort_degree_ultraviolet_intensity)
        TextView comfortDegreeUltravioletIntensity;
        @BindView(R.id.comfort_degree_visibility)
        TextView comfortDegreeVisibility;
        @BindView(R.id.comfort_degree_pressure)
        TextView comfortDegreePressure;

        ComfortDegreeViewHolder(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
        }

        void bind() {

            comfortDegreeSendibleTemp.setText(String.valueOf(cityBase.getSendibleTemperature()));
            comfortDegreeChart.setDensity(cityBase.getHumidity());
            comfortDegreeUltravioletIntensity.setText(String.valueOf(cityBase.getUltravioletIndex()));
            comfortDegreePressure.setText(String.valueOf(cityBase.getPressure()));
            comfortDegreeVisibility.setText(String.valueOf(cityBase.getVisibility()));
        }
    }

    class WindViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.wind_view)
        WindView windView;
        @BindView(R.id.wind_direction)
        TextView windDirection;
        @BindView(R.id.wind_power)
        TextView windPower;

        WindViewHolder(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
        }

        void bind() {

            windDirection.setText(cityBase.getWindDirection());
            windPower.setText(cityBase.getWindPower());
            windView.setSpeed(cityBase.getWindSpeed());
        }
    }

    class SunlightViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sunlight_view)
        SunlightView sunlightView;

        SunlightViewHolder(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
        }

        void bind() {
            sunlightView.setMonthlyRise(Util.getDateByTime(cityBase.getMonthlyRise()));
            sunlightView.setMonthlySet(Util.getDateByTime(cityBase.getMonthlySet()));
            sunlightView.setSunRise(Util.getDateByTime(cityBase.getSunRise()));
            sunlightView.setSunSet(Util.getDateByTime(cityBase.getSunSet()));
            sunlightView.updateData();
        }
    }

}
