package me.objectyan.weatherbaby.services;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.functions.Function;
import me.objectyan.weatherbaby.common.BaseApplication;
import me.objectyan.weatherbaby.common.Util;
import me.objectyan.weatherbaby.common.WeatherBabyConstants;
import me.objectyan.weatherbaby.entities.caiyun.DailyAvg;
import me.objectyan.weatherbaby.entities.caiyun.HourlyKeyValue;
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
import me.objectyan.weatherbaby.entities.heweather.Weather;
import me.objectyan.weatherbaby.entities.heweather.WeatherApi;

public class CityManageService {

    private static String TAG = CityManageService.class.getSimpleName();

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
                            cityBase.setUpdateTime(Util.getCurrentTimeByUTC());
                            cityBase.setPublishTime(weather.updateEntity.getUtcTime());
                            cityBase.setLocation(weather.basic.Name);
                            cityBase.setAdminArea(weather.basic.adminArea);
                            cityBase.setParentCity(weather.basic.parentCity);
                            cityBase.setCountry(weather.basic.countryName);
                            cityBase.setCid(weather.basic.code);
                            cityBase.setLatitude(Double.valueOf(weather.basic.latitude));
                            cityBase.setLongitude(Double.valueOf(weather.basic.longitude));
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
                                    cityDailyForecast.setDateTime(item.getDateTime());
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
                                        HeWeatherApiService.getInstance().fetchHourly(cityBase.getLongitude(), cityBase.getLatitude()).doOnNext(hourly -> {
                                            if (hourly.temperature != null) {
                                                cityHourlyForecastDao.queryBuilder().where(CityHourlyForecastDao.Properties.CityID.eq(mCityID))
                                                        .buildDelete().executeDeleteWithoutDetachingEntities();
                                                for (int i = 0; i < hourly.temperature.size(); i++) {
                                                    HourlyKeyValue<Double> item = hourly.temperature.get(i);
                                                    CityHourlyForecast cityHourlyForecast = new CityHourlyForecast();
                                                    cityHourlyForecast.setCityID(mCityID);
                                                    cityHourlyForecast.setTemperature(item.value);
                                                    cityHourlyForecast.setWindSpeed(hourly.wind.get(i).speed);
                                                    cityHourlyForecast.setWindDirectionAngle(hourly.wind.get(i).direction);
                                                    cityHourlyForecast.setTime(item.getTime());
                                                    cityHourlyForecast.setDate(item.getDate());
                                                    cityHourlyForecast.setDateTime(item.getLocalTime());
                                                    cityHourlyForecast.setRelativeHumidity(hourly.humidity.get(i).value);
                                                    cityHourlyForecast.setProbability(hourly.precipitation.get(i).value);
                                                    cityHourlyForecast.setPressure(hourly.pres.get(i).value);
                                                    cityHourlyForecast.setCloud(new Double(hourly.cloudrate.get(i).value * 100).intValue());
                                                    cityHourlyForecast.setCondTxt(hourly.skycon.get(i).getCondTxt());
                                                    cityHourlyForecast.setCondCode(hourly.skycon.get(i).getCondCode());
                                                    cityHourlyForecastDao.insert(cityHourlyForecast);
                                                }
                                            }
                                        })
                                                .doOnComplete(() -> {
                                                    HeWeatherApiService.getInstance().fetchDaily(cityBase.getLongitude(), cityBase.getLatitude()).doOnNext(daily -> {
                                                        if (daily.temperature != null) {
                                                            List<CityDailyForecast> data = cityDailyForecastDao.queryBuilder().where(CityDailyForecastDao.Properties.CityID.eq(mCityID)).build().list();
                                                            for (int i = 0; i < daily.temperature.size(); i++) {
                                                                DailyAvg dailyAvg = daily.temperature.get(i);
                                                                if (data.stream().anyMatch(f -> f.getDate().equals(dailyAvg.date)))
                                                                    continue;
                                                                DailyAvg dailyAvgPre = daily.precipitation.get(i);
                                                                DailyAvg dailyAvgHumidity = daily.humidity.get(i);
                                                                DailyAvg dailyAvgVisibility = daily.visibility.get(i);
                                                                DailyAvg dailyAvgUltraviolet = daily.ultraviolet.get(i);
                                                                DailyAvg dailyAvgPres = daily.pres.get(i);
                                                                HourlyKeyValue<String> skycon = daily.skycon.get(i);
                                                                CityDailyForecast cityDailyForecast = new CityDailyForecast();
                                                                cityDailyForecast.setCityID(mCityID);
                                                                cityDailyForecast.setCondCodeDay(skycon.getCondCode());
                                                                cityDailyForecast.setCondCodeEvening(skycon.getCondCode());
                                                                cityDailyForecast.setCondTxtDay(skycon.getCondTxt());
                                                                cityDailyForecast.setCondTxtEvening(skycon.getCondTxt());
                                                                cityDailyForecast.setDate(dailyAvg.date);
                                                                cityDailyForecast.setDateTime(dailyAvg.getLocalTime());
                                                                cityDailyForecast.setMaximumTemperature(dailyAvg.max);
                                                                cityDailyForecast.setMinimumTemperature(dailyAvg.min);
                                                                cityDailyForecast.setPrecipitation(dailyAvgPre.avg);
                                                                cityDailyForecast.setPressure(dailyAvgPres.avg);
//                                                                cityDailyForecast.setProbability(item.precipitationProbability);
//                                                                cityDailyForecast.setMonthlyRise(item.monthlyRiseTime);
//                                                                cityDailyForecast.setMonthlySet(item.monthlySetTime);
//                                                                cityDailyForecast.setMonthlySet(item.monthlySetTime);
//                                                                cityDailyForecast.setSunRise(item.sunRiseTime);
//                                                                cityDailyForecast.setSunSet(item.sunSetTime);
                                                                cityDailyForecast.setRelativeHumidity(dailyAvgHumidity.avg);
                                                                cityDailyForecast.setUltravioletIntensity(dailyAvgUltraviolet.index);
                                                                cityDailyForecast.setVisibility(dailyAvgVisibility.avg);
//                                                                cityDailyForecast.setWindDirection(item.windDirection);
//                                                                cityDailyForecast.setWindDirectionAngle(item.windDirectionAngle);
//                                                                cityDailyForecast.setWindPower(item.windPower);
//                                                                cityDailyForecast.setWindSpeed(item.windSpeed);
                                                                cityDailyForecastDao.insert(cityDailyForecast);
                                                            }
                                                        }
                                                    }).doOnComplete(() -> {
                                                        emitter.onNext(cityBase.getId());
                                                    }).subscribe();
                                                }).subscribe();
                                    }).subscribe();

                        }).
                        subscribe(data -> {

                                },
                                error -> {
                                    Util.showLong(error.getLocalizedMessage());
                                    Log.e(TAG, error.toString());
                                    emitter.onNext(cityBase.getId());
                                });
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
