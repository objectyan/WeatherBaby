package me.objectyan.weatherbaby.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.greendao.query.Query;

import java.util.Date;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.adapter.WeatherForecastAdapter;
import me.objectyan.weatherbaby.adapter.WeatherLifestyleAdapter;
import me.objectyan.weatherbaby.adapter.WeatherTimelineAdapter;
import me.objectyan.weatherbaby.common.BaseApplication;
import me.objectyan.weatherbaby.common.Util;
import me.objectyan.weatherbaby.entities.CityInfo;
import me.objectyan.weatherbaby.entities.database.CityBase;
import me.objectyan.weatherbaby.entities.database.CityBaseDao;
import me.objectyan.weatherbaby.entities.database.CityDailyForecast;
import me.objectyan.weatherbaby.entities.database.CityDailyForecastDao;
import me.objectyan.weatherbaby.entities.database.CityHourlyForecast;
import me.objectyan.weatherbaby.entities.database.CityHourlyForecastDao;
import me.objectyan.weatherbaby.entities.database.CityLifestyleForecast;
import me.objectyan.weatherbaby.entities.database.CityLifestyleForecastDao;
import me.objectyan.weatherbaby.services.CityManageService;
import me.objectyan.weatherbaby.widget.ArcView;
import me.objectyan.weatherbaby.widget.GridView;
import me.objectyan.weatherbaby.widget.SunlightView;
import me.objectyan.weatherbaby.widget.WindView;

public class WeatherFragment extends Fragment {

    private static String ARG_CITYID = "ARG_CITYID";
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
    @BindView(R.id.today_temp_update_time)
    TextView todayTempUpdateTime;
    @BindView(R.id.today_temp_publish_time)
    TextView todayTempPublishTime;
    @BindView(R.id.timeline_items)
    RecyclerView timelineItems;
    @BindView(R.id.forecast_items)
    RecyclerView forecastItems;
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
    @BindView(R.id.wind_direction)
    TextView windDirection;
    @BindView(R.id.wind_power)
    TextView windPower;
    @BindView(R.id.fragment_weather_timeline)
    CardView fragmentWeatherTimeline;
    @BindView(R.id.wind_view)
    WindView windWiew;
    @BindView(R.id.sunlight_view)
    SunlightView sunlightView;
    @BindView(R.id.swipe_weather_layout)
    SwipeRefreshLayout swipeWeatherLayout;
    @BindView(R.id.gv_lifestyle_manage)
    me.objectyan.weatherbaby.widget.GridView gvLifestyleManage;

    private Long mCityID;

    private CityBaseDao cityBaseDao;
    private CityDailyForecastDao cityDailyForecastDao;
    private CityHourlyForecastDao cityHourlyForecastDao;
    private CityLifestyleForecastDao cityLifestyleForecastDao;

    private WeatherForecastAdapter weatherForecastAdapter;
    private WeatherTimelineAdapter weatherTimelineAdapter;
    private WeatherLifestyleAdapter weatherLifestyleAdapter;

    public WeatherFragment() {
        cityBaseDao = BaseApplication.getDaoSession().getCityBaseDao();
        cityDailyForecastDao = BaseApplication.getDaoSession().getCityDailyForecastDao();
        cityHourlyForecastDao = BaseApplication.getDaoSession().getCityHourlyForecastDao();
        cityLifestyleForecastDao = BaseApplication.getDaoSession().getCityLifestyleForecastDao();
    }

    public Long getCityID() {
        return mCityID;
    }

    public CityInfo getCityInfo() {
        if (mCityID != null)
            return Util.cityBaseToInfo(cityBaseDao.load(mCityID));
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCityID = getArguments().getLong(ARG_CITYID);
        }
    }

    public static WeatherFragment newInstance(Long cityID) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CITYID, cityID);
        fragment.setArguments(args);
        fragment.mCityID = cityID;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this, view);
        weatherForecastAdapter = new WeatherForecastAdapter(mCityID);
        weatherTimelineAdapter = new WeatherTimelineAdapter(mCityID);
        weatherLifestyleAdapter = new WeatherLifestyleAdapter(this.getContext(), R.layout.fragment_weather_lifestyle_item, mCityID);
        gvLifestyleManage.setAdapter(weatherLifestyleAdapter);
        forecastItems.setLayoutManager(new LinearLayoutManager(view.getContext()));
        forecastItems.setAdapter(weatherForecastAdapter);
        timelineItems.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        timelineItems.setAdapter(weatherTimelineAdapter);
        swipeWeatherLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(true);
            }
        });
        initData(false);
        return view;
    }

    private void initData(Boolean isRefresh) {
        Observable.create(new ObservableOnSubscribe<Long>() {
            @SuppressLint("NewApi")
            @Override
            public void subscribe(ObservableEmitter<Long> emitter) throws Exception {
                CityBase cityBase = cityBaseDao.queryBuilder().where(CityBaseDao.Properties.Id.eq(mCityID)).unique();
                if ((cityBase.getUpdateTime() == null ||
                        cityBase.getUpdateTime().getTime() < new Date().getTime() ||
                        (new Date().getTime() - cityBase.getUpdateTime().getTime() > Util.getSettingsUpdateInterval() * 60 * 1000) ||
                        (new Date().getTime() - cityBase.getPublishTime().getTime() > Util.getSettingsUpdateInterval() * 60 * 1000) ||
                        isRefresh) && Util.isNetworkConnected()) {
                    swipeWeatherLayout.setRefreshing(true);
                    CityManageService.refreshCityInfo(mCityID).doOnNext(cityID -> {
                        emitter.onNext(cityBase.getId());
                    }).subscribe();
                } else {
                    emitter.onNext(cityBase.getId());
                }
            }
        }).doOnNext(cityID -> {
            CityBase cityBase = cityBaseDao.queryBuilder().where(CityBaseDao.Properties.Id.eq(cityID)).unique();
            Query<CityDailyForecast> cityDailyForecastQuery = cityDailyForecastDao.queryBuilder().where(CityDailyForecastDao.Properties.CityID.eq(mCityID)).build();
            Query<CityHourlyForecast> cityHourlyForecastQuery = cityHourlyForecastDao.queryBuilder().where(CityHourlyForecastDao.Properties.CityID.eq(mCityID)).build();
            Query<CityLifestyleForecast> cityLifestyleForecastQuery = cityLifestyleForecastDao.queryBuilder().where(CityLifestyleForecastDao.Properties.CityID.eq(mCityID)).build();
            todayTempUpdateTime.setText(String.format(getString(R.string.weather_update_time), Util.utcToLocal(cityBase.getUpdateTime())));
            todayTempPublishTime.setText(String.format(getString(R.string.weather_publish_time), Util.utcToLocal(cityBase.getPublishTime())));
            todayTempCurr.setText(Util.getTemp(cityBase.getTemperature()));
            todayTempCurrUnit.setText(Util.getSettingsTempUnit());
            todayTempWeather.setText(cityBase.getCondTxt());
            fragmentWeatherTimeline.setVisibility(cityHourlyForecastQuery.list().size() == 0 ? View.GONE : View.VISIBLE);
            todayTempCurrMax.setText(Util.getTempByUnit(cityBase.getTempHigh()));
            todayTempCurrMin.setText(Util.getTempByUnit(cityBase.getTempLow()));
            windDirection.setText(cityBase.getWindDirection());
            windPower.setText(cityBase.getWindPower());
            windWiew.setSpeed(cityBase.getWindSpeed());
            comfortDegreeSendibleTemp.setText(String.valueOf(cityBase.getSendibleTemperature()));
            comfortDegreeChart.setDensity(cityBase.getHumidity());
            comfortDegreeUltravioletIntensity.setText(String.valueOf(cityBase.getUltravioletIndex()));
            comfortDegreePressure.setText(String.valueOf(cityBase.getPressure()));
            comfortDegreeVisibility.setText(String.valueOf(cityBase.getVisibility()));
            weatherForecastAdapter.updateData();
            weatherTimelineAdapter.updateData();
            weatherLifestyleAdapter.updateData();
            sunlightView.setMonthlyRise(Util.getDateByTime(cityBase.getMonthlyRise()));
            sunlightView.setMonthlySet(Util.getDateByTime(cityBase.getMonthlySet()));
            sunlightView.setSunRise(Util.getDateByTime(cityBase.getSunRise()));
            sunlightView.setSunSet(Util.getDateByTime(cityBase.getSunSet()));
            sunlightView.updateData();
            todayTempAtmosphere.setText(cityBase.getAirQuality());
            atmosphereChart.setDensity(cityBase.getAqi());
            atmosphereChart.setValue(String.valueOf(cityBase.getAqi()));
            atmosphereChart.setSubValue(cityBase.getAirQuality());
            atmospherePM10.setText(String.valueOf(cityBase.getPm10()));
            atmospherePM25.setText(String.valueOf(cityBase.getPm25()));
            atmosphereCO.setText(String.valueOf(cityBase.getCo()));
            atmosphereNO2.setText(String.valueOf(cityBase.getNo2()));
            atmosphereO3.setText(String.valueOf(cityBase.getO3()));
            atmosphereSO2.setText(String.valueOf(cityBase.getSo2()));
            swipeWeatherLayout.setRefreshing(false);
        }).subscribe();
    }

    public void refreshData() {
        initData(true);
    }
}
