package me.objectyan.weatherbaby.services;

import io.reactivex.Observable;
import me.objectyan.weatherbaby.BuildConfig;
import me.objectyan.weatherbaby.entities.heweather.AirNowApi;
import me.objectyan.weatherbaby.entities.heweather.WeatherApi;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static java.lang.String.format;

/**
 * 和风API接口
 */
public interface IHeWeatherApi {

    String HOST = "https://free-api.heweather.net/s6/";

    /**
     * 空气质量实况
     * https://free-api.heweather.net/s6/air/now?parameters
     *
     * @param location
     * @return
     */
    @GET("air/now?key=" + BuildConfig.HeWeatherKey)
    Observable<AirNowApi> mAirNow(@Query("location") String location);

    /**
     * 常规天气数据集合
     * https://free-api.heweather.net/s6/weather?parameters
     *
     * @param location
     * @return
     */
    @GET("weather?key=" + BuildConfig.HeWeatherKey)
    Observable<WeatherApi> mWeather(@Query("location") String location);
}
