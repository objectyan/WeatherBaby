package me.objectyan.weatherbaby.fragment;

import android.icu.util.Calendar;
import android.icu.util.ULocale;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.greendao.query.Query;

import java.util.Locale;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.common.BaseApplication;
import me.objectyan.weatherbaby.common.Util;
import me.objectyan.weatherbaby.entities.database.CityBase;
import me.objectyan.weatherbaby.entities.database.CityBaseDao;
import me.objectyan.weatherbaby.entities.database.CityDailyForecastDao;
import me.objectyan.weatherbaby.entities.database.CityHourlyForecastDao;
import me.objectyan.weatherbaby.entities.database.CityLifestyleForecastDao;
import me.objectyan.weatherbaby.services.HeWeatherApiService;
import me.objectyan.weatherbaby.widget.ArcView;

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
    @BindView(R.id.today_temp_curr_max_unit)
    TextView todayTempCurrMaxUnit;
    @BindView(R.id.today_temp_curr_to)
    TextView todayTempCurrTo;
    @BindView(R.id.today_temp_curr_min)
    TextView todayTempCurrMin;
    @BindView(R.id.today_temp_curr_min_unit)
    TextView todayTempCurrMinUnit;
    @BindView(R.id.today_temp_atmosphere)
    TextView todayTempAtmosphere;
    @BindView(R.id.today_temp_early_warning)
    TextView todayTempEarlyWarning;
    @BindView(R.id.today_temp_update_time)
    TextView todayTempUpdateTime;
    @BindView(R.id.timeline_items)
    LinearLayout timelineItems;
    @BindView(R.id.forecast_items)
    LinearLayout forecastItems;
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

    private Long mCityID;

    private CityBaseDao cityBaseDao;
    private CityDailyForecastDao cityDailyForecastDao;
    private CityHourlyForecastDao cityHourlyForecastDao;
    private CityLifestyleForecastDao cityLifestyleForecastDao;

    public WeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCityID = getArguments().getLong(ARG_CITYID);
        }
        cityBaseDao = BaseApplication.getDaoSession().getCityBaseDao();
        cityDailyForecastDao = BaseApplication.getDaoSession().getCityDailyForecastDao();
        cityHourlyForecastDao = BaseApplication.getDaoSession().getCityHourlyForecastDao();
        cityLifestyleForecastDao = BaseApplication.getDaoSession().getCityLifestyleForecastDao();
    }

    public static WeatherFragment newInstance(Long cityID) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CITYID, cityID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(view);
        initData();
        return view;
    }

    private void initData() {
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> emitter) throws Exception {
                CityBase cityBase = cityBaseDao.queryBuilder().where(CityBaseDao.Properties.Id.eq(mCityID)).unique();
                if (cityBase.getUpdateTime() == null) {
                    HeWeatherApiService.getInstance().fetchWeather(Util.getCityName(cityBase))
                            .doOnNext(weather -> {
                                cityBase.setUpdateTime(weather.updateEntity.getUtcTime());
                                cityBase.setLocation(weather.basic.Name);
                                cityBase.setAdminArea(weather.basic.adminArea);
                                cityBase.setCid(weather.basic.code);
                                cityBase.setLatitude(Double.valueOf(weather.basic.latitude));
                                cityBase.setLongitude(Double.valueOf(weather.basic.longitude));
                                cityBase.setParentCity(weather.basic.parentCity);
                                cityBase.setCountry(weather.basic.countryName);
                                cityBase.setTimeZone(Float.valueOf(weather.basic.timeZone));
                                cityBaseDao.update(cityBase);
                            }).doOnComplete(() -> {
                        emitter.onNext(cityBase.getId());
                    }).subscribe();
                } else {
                    emitter.onNext(cityBase.getId());
                }
            }
        }).doOnNext(cityID -> {
            CityBase cityBase = cityBaseDao.queryBuilder().where(CityBaseDao.Properties.Id.eq(cityID)).unique();
            todayTempUpdateTime.setText(Util.dateToStr(cityBase.getUpdateTime(), null));
        }).subscribe();
    }
}
