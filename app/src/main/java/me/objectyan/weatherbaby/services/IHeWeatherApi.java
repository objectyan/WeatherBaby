package me.objectyan.weatherbaby.services;

import io.reactivex.Observable;
import me.objectyan.weatherbaby.BuildConfig;
import me.objectyan.weatherbaby.entities.caiyun.DailyApi;
import me.objectyan.weatherbaby.entities.caiyun.HourlyApi;
import me.objectyan.weatherbaby.entities.heweather.AirNowApi;
import me.objectyan.weatherbaby.entities.heweather.TopCityApi;
import me.objectyan.weatherbaby.entities.heweather.WeatherApi;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
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

    /**
     * 热门城市列表
     *
     * @param group  特殊值：world，返回全球热门城市
     *               特殊值：cn，返回中国热门城市
     *               特殊值：overseas，查询海外热门城市（不含中国）
     * @param number 可选1-50个
     * @return
     */
    @GET("https://search.heweather.net/top?key=" + BuildConfig.HeWeatherKey)
    Observable<TopCityApi> mTopCityInfo(@Query("group") String group, @Query("number") int number);

    /**
     * 彩云天气接口 获取小时天气
     *
     * @param longitude
     * @param latitude
     * @return
     */
    @GET("https://api.caiyunapp.com/v2/" + BuildConfig.CaiYunWeatherKey + "/{longitude},{latitude}/hourly.json")
    Observable<HourlyApi> mHourly(@Path("longitude") Double longitude, @Path("latitude") Double latitude);

    /**
     * 彩云天气接口 获取天级预报
     *
     * @param longitude
     * @param latitude
     * @return
     */
    @GET("https://api.caiyunapp.com/v2/" + BuildConfig.CaiYunWeatherKey + "/{longitude},{latitude}/daily.json")
    Observable<DailyApi> mDaily(@Path("longitude") Double longitude, @Path("latitude") Double latitude);

}
