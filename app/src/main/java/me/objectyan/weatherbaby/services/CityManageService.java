package me.objectyan.weatherbaby.services;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;

import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import me.objectyan.weatherbaby.common.BaseApplication;
import me.objectyan.weatherbaby.common.Util;
import me.objectyan.weatherbaby.entities.database.CityAirNowStation;
import me.objectyan.weatherbaby.entities.database.CityAirNowStationDao;
import me.objectyan.weatherbaby.entities.database.CityBase;
import me.objectyan.weatherbaby.entities.database.CityBaseDao;
import me.objectyan.weatherbaby.entities.database.CityDailyForecast;
import me.objectyan.weatherbaby.entities.database.CityDailyForecastDao;
import me.objectyan.weatherbaby.entities.database.CityHourlyForecast;
import me.objectyan.weatherbaby.entities.database.CityHourlyForecastDao;
import me.objectyan.weatherbaby.entities.database.CityLifestyleForecast;
import me.objectyan.weatherbaby.entities.database.CityLifestyleForecastDao;

public class CityManageService {
    /**
     * 更新城市天气信息
     *
     * @param mCityID
     * @return
     */
    @TargetApi(Build.VERSION_CODES.N)
    public static Observable<Long> refreshCityInfo(Long mCityID) {
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> emitter) throws Exception {
                CityBaseDao cityBaseDao = BaseApplication.getDaoSession().getCityBaseDao();
                CityDailyForecastDao cityDailyForecastDao = BaseApplication.getDaoSession().getCityDailyForecastDao();
                CityHourlyForecastDao cityHourlyForecastDao = BaseApplication.getDaoSession().getCityHourlyForecastDao();
                CityLifestyleForecastDao cityLifestyleForecastDao = BaseApplication.getDaoSession().getCityLifestyleForecastDao();
                CityAirNowStationDao cityAirNowStationDao = BaseApplication.getDaoSession().getCityAirNowStationDao();
                CityBase cityBase = cityBaseDao.queryBuilder().where(CityBaseDao.Properties.Id.eq(mCityID)).unique();
                HeWeatherApiService.getInstance().fetchWeather(Util.getCityName(cityBase)).
                        doOnNext(weather -> {
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
                        }).
                        doOnComplete(() -> {
                            HeWeatherApiService.getInstance().fetchAirNow(Util.getCityName(cityBase)).doOnNext(air -> {
                                if (air.airNow != null) {
                                    cityBase.setAqi(air.airNow.aqi);
                                    cityBase.setCo(air.airNow.co);
                                    cityBase.setNo2(air.airNow.no2);
                                    cityBase.setO3(air.airNow.o3);
                                    cityBase.setPm10(air.airNow.pm10);
                                    cityBase.setPm25(air.airNow.pm25);
                                    cityBase.setSo2(air.airNow.so2);
                                    cityBase.setAirQuality(air.airNow.airQuality);
                                    cityBase.setMajorPollutants(air.airNow.main);
                                    cityBaseDao.update(cityBase);
                                }
                                if (air.airNowStationEntities != null) {
                                    cityAirNowStationDao.queryBuilder().where(CityAirNowStationDao.Properties.CityID.eq(mCityID))
                                            .buildDelete().executeDeleteWithoutDetachingEntities();
                                    air.airNowStationEntities.forEach(item -> {
                                        CityAirNowStation cityAirNowStation = new CityAirNowStation();
                                        cityAirNowStation.setCityID(mCityID);
                                        cityAirNowStation.setAqi(item.aqi);
                                        cityAirNowStation.setCo(item.co);
                                        cityAirNowStation.setNo2(item.no2);
                                        cityAirNowStation.setO3(item.o3);
                                        cityAirNowStation.setPm10(item.pm10);
                                        cityAirNowStation.setPm25(item.pm25);
                                        cityAirNowStation.setSo2(item.so2);
                                        cityAirNowStation.setAirQuality(item.airQuality);
                                        cityAirNowStation.setMajorPollutants(item.main);
                                        cityAirNowStation.setAirStation(item.airStation);
                                        cityAirNowStation.setAirStationID(item.airStationID);
                                        cityAirNowStation.setPubTime(item.getPubTime());
                                        cityAirNowStation.setLatitude(item.latitude);
                                        cityAirNowStation.setLongitude(item.longitude);
                                        cityAirNowStationDao.insert(cityAirNowStation);
                                    });
                                }
                            })
                                    .doOnComplete(() -> {
                                        emitter.onNext(cityBase.getId());
                                    }).
                                    subscribe();
                        }).

                        subscribe();
            }
        });
    }

    /**
     * 根据城市编号判断可继续添加 除 默认定位
     *
     * @param cityName
     * @return
     */
    public static Boolean isDisabled(String cityName) {
        CityBaseDao cityBaseDao = BaseApplication.getDaoSession().getCityBaseDao();
        if (cityBaseDao.loadAll().isEmpty() || TextUtils.isEmpty(cityName)) return false;
        return cityBaseDao.queryBuilder().where(CityBaseDao.Properties.Location.eq(cityName),
                CityBaseDao.Properties.IsLocation.eq(false)).buildCount().count() > 0;
    }

    /**
     * 是否含有定位信息
     *
     * @return
     */
    public static Boolean hasLocation() {
        CityBaseDao cityBaseDao = BaseApplication.getDaoSession().getCityBaseDao();
        if (cityBaseDao.loadAll().isEmpty()) return false;
        return cityBaseDao.queryBuilder().where(CityBaseDao.Properties.IsLocation.eq(true)).buildCount().count() > 0;
    }
}
