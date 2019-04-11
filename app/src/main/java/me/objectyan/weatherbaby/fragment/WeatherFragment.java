package me.objectyan.weatherbaby.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.greendao.query.Query;

import java.util.Date;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.adapter.WeatherForecastAdapter;
import me.objectyan.weatherbaby.common.BaseApplication;
import me.objectyan.weatherbaby.common.Util;
import me.objectyan.weatherbaby.entities.database.CityBase;
import me.objectyan.weatherbaby.entities.database.CityBaseDao;
import me.objectyan.weatherbaby.entities.database.CityDailyForecast;
import me.objectyan.weatherbaby.entities.database.CityDailyForecastDao;
import me.objectyan.weatherbaby.entities.database.CityHourlyForecast;
import me.objectyan.weatherbaby.entities.database.CityHourlyForecastDao;
import me.objectyan.weatherbaby.entities.database.CityLifestyleForecast;
import me.objectyan.weatherbaby.entities.database.CityLifestyleForecastDao;
import me.objectyan.weatherbaby.services.HeWeatherApiService;
import me.objectyan.weatherbaby.widget.ArcView;
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

    private Long mCityID;
    private String mLocation;

    private CityBaseDao cityBaseDao;
    private CityDailyForecastDao cityDailyForecastDao;
    private CityHourlyForecastDao cityHourlyForecastDao;
    private CityLifestyleForecastDao cityLifestyleForecastDao;

    private WeatherForecastAdapter weatherForecastAdapter;

    public WeatherFragment() {
        // Required empty public constructor
    }

    public Long getCityID() {
        return mCityID;
    }

    public String getLocation() {
        return mLocation;
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
        ButterKnife.bind(this, view);
        weatherForecastAdapter = new WeatherForecastAdapter(mCityID);
        forecastItems.setLayoutManager(new LinearLayoutManager(view.getContext()));
        forecastItems.setAdapter(weatherForecastAdapter);
        initData();
        return view;
    }

    private void initData() {
        Observable.create(new ObservableOnSubscribe<Long>() {
            @SuppressLint("NewApi")
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
                                cityBase.setCondCode(weather.nowEntity.condCode);
                                cityBase.setCondTxt(weather.nowEntity.condTxt);
                                cityBase.setSendibleTemperature(weather.nowEntity.somatosensory);
                                cityBase.setTemperature(weather.nowEntity.temperature);
                                cityBase.setWindDirection(weather.nowEntity.windDirection);
                                cityBase.setWindPower(weather.nowEntity.windPower);
                                cityBase.setWindSpeed(weather.nowEntity.windSpeed);
                                cityBase.setTempHigh(0d);
                                cityBase.setTempLow(0d);
                                cityBase.setHumidity(weather.nowEntity.humidity);
                                cityBase.setPrecipitation(weather.nowEntity.precipitation);
                                cityBase.setCloud(weather.nowEntity.cloud);
                                cityBase.setPrecipitationProbability(0d);
                                cityBase.setPressure(weather.nowEntity.pressure);
                                cityBase.setUltravioletIndex(0d);
                                cityBase.setVisibility(weather.nowEntity.visibility);
                                cityBaseDao.update(cityBase);
                                if (weather.dailyForecastEntities != null) {
                                    cityDailyForecastDao.queryBuilder().where(CityDailyForecastDao.Properties.CityID.eq(mCityID))
                                            .buildDelete().executeDeleteWithoutDetachingEntities();
                                    weather.dailyForecastEntities.forEach(item -> {
                                        CityDailyForecast cityDailyForecast = new CityDailyForecast();
                                        cityDailyForecast.setCityID(mCityID);
                                        cityDailyForecast.setCondCodeDay(item.condCodeDay);
                                        cityDailyForecast.setCondCodeEvening(item.condCodeNight);
                                        cityDailyForecast.setCondTxtDay(item.condTxtDay);
                                        cityDailyForecast.setCondTxtEvening(item.condTxtNight);
                                        cityDailyForecast.setDate(item.date);
                                        cityDailyForecast.setMaximumTemperature(item.temperatureMax);
                                        cityDailyForecast.setMinimumTemperature(item.temperatureMin);
                                        cityDailyForecast.setMonthlyRise(item.monthlyRiseTime);
                                        cityDailyForecast.setMonthlySet(item.monthlySetTime);
                                        cityDailyForecast.setPrecipitation(item.precipitation);
                                        cityDailyForecast.setPressure(item.pressure);
                                        cityDailyForecast.setProbability(item.precipitationProbability);
                                        cityDailyForecast.setMonthlySet(item.monthlySetTime);
                                        cityDailyForecast.setRelativeHumidity(item.humidity);
                                        cityDailyForecast.setSunRise(item.sunRiseTime);
                                        cityDailyForecast.setSunSet(item.sunSetTime);
                                        cityDailyForecast.setUltravioletIntensity(item.ultravioletIndex);
                                        cityDailyForecast.setVisibility(item.visibility);
                                        cityDailyForecast.setWindDirection(item.windDirection);
                                        cityDailyForecast.setWindDirectionAngle(item.windDirectionAngle);
                                        cityDailyForecast.setWindPower(item.windPower);
                                        cityDailyForecast.setWindSpeed(item.windSpeed);
                                        if (Util.dateToStr(new Date(), "yyyy-MM-dd").equals(item.date)) {
                                            cityBase.setTempHigh(item.temperatureMax);
                                            cityBase.setTempLow(item.temperatureMin);
                                            cityBase.setPrecipitationProbability(item.precipitationProbability);
                                            cityBase.setUltravioletIndex(item.ultravioletIndex);
                                            cityBase.setSunRise(item.sunRiseTime);
                                            cityBase.setSunSet(item.sunSetTime);
                                            cityBase.setMonthlyRise(item.monthlyRiseTime);
                                            cityBase.setMonthlySet(item.monthlySetTime);
                                            cityBaseDao.update(cityBase);
                                        }
                                        cityDailyForecastDao.insert(cityDailyForecast);
                                    });
                                }
                                if (weather.hourlyEntities != null) {
                                    cityHourlyForecastDao.queryBuilder().where(CityHourlyForecastDao.Properties.CityID.eq(mCityID))
                                            .buildDelete().executeDeleteWithoutDetachingEntities();
                                    weather.hourlyEntities.forEach(item -> {
                                        CityHourlyForecast cityHourlyForecast = new CityHourlyForecast();
                                        cityHourlyForecast.setCityID(mCityID);
                                        cityHourlyForecast.setCloud(item.cloud);
                                        cityHourlyForecast.setCondCode(item.condCode);
                                        cityHourlyForecast.setCondTxt(item.condTxt);
                                        cityHourlyForecast.setDate(item.getDate());
                                        cityHourlyForecast.setDewPoint(item.dew);
                                        cityHourlyForecast.setPressure(item.pressure);
                                        cityHourlyForecast.setProbability(item.precipitationProbability);
                                        cityHourlyForecast.setRelativeHumidity(item.humidity);
                                        cityHourlyForecast.setTemperature(item.temperature);
                                        cityHourlyForecast.setTime(item.getTime());
                                        cityHourlyForecast.setWindDirection(item.windDirection);
                                        cityHourlyForecast.setWindDirectionAngle(item.windDirectionAngle);
                                        cityHourlyForecast.setWindPower(item.windPower);
                                        cityHourlyForecast.setWindSpeed(item.windSpeed);
                                        cityHourlyForecastDao.insert(cityHourlyForecast);
                                    });
                                }
                                if (weather.lifestyleEntities != null) {
                                    cityLifestyleForecastDao.queryBuilder().where(CityLifestyleForecastDao.Properties.CityID.eq(mCityID))
                                            .buildDelete().executeDeleteWithoutDetachingEntities();
                                    weather.lifestyleEntities.forEach(item -> {
                                        CityLifestyleForecast cityLifestyleForecast = new CityLifestyleForecast();
                                        cityLifestyleForecast.setBriefIntroduction(item.briefLife);
                                        cityLifestyleForecast.setCityID(mCityID);
                                        cityLifestyleForecast.setDescription(item.txt);
                                        cityLifestyleForecast.setType(item.type);
                                        cityLifestyleForecastDao.insert(cityLifestyleForecast);
                                    });
                                }
                            }).doOnComplete(() -> {
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
            todayTempUpdateTime.setText(Util.utcToLocal(cityBase.getUpdateTime()));
            todayTempCurr.setText(String.valueOf(cityBase.getTemperature()));
            todayTempWeather.setText(cityBase.getCondTxt());
            fragmentWeatherTimeline.setVisibility(cityHourlyForecastQuery.list().size() == 0 ? View.GONE : View.VISIBLE);
            todayTempCurrMax.setText(String.valueOf(cityBase.getTempHigh()));
            todayTempCurrMin.setText(String.valueOf(cityBase.getTempLow()));

            windDirection.setText(cityBase.getWindDirection());
            windPower.setText(cityBase.getWindPower());
            windWiew.setSpeed(cityBase.getWindSpeed());

            comfortDegreeSendibleTemp.setText(String.valueOf(cityBase.getSendibleTemperature()));
            comfortDegreeChart.setDensity(cityBase.getHumidity());
            comfortDegreeUltravioletIntensity.setText(String.valueOf(cityBase.getUltravioletIndex()));
            comfortDegreePressure.setText(String.valueOf(cityBase.getPressure()));
            comfortDegreeVisibility.setText(String.valueOf(cityBase.getVisibility()));

            weatherForecastAdapter.updateData();

            sunlightView.setMonthlyRise(Util.getDateByTime(cityBase.getMonthlyRise()));
            sunlightView.setMonthlySet(Util.getDateByTime(cityBase.getMonthlySet()));
            sunlightView.setSunRise(Util.getDateByTime(cityBase.getSunRise()));
            sunlightView.setSunSet(Util.getDateByTime(cityBase.getSunSet()));
            sunlightView.updateData();

            this.mLocation = cityBase.getLocation();
        }).subscribe();
    }
}
