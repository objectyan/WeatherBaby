package me.objectyan.weatherbaby.services;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;

import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
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
                            emitter.onNext(cityBase.getId());
                        }).
                        subscribe();
            }
        });
    }
}
