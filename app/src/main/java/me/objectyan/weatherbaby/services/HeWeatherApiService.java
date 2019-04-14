package me.objectyan.weatherbaby.services;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.objectyan.weatherbaby.common.BaseApplication;
import me.objectyan.weatherbaby.common.Util;
import me.objectyan.weatherbaby.entities.CityInfo;
import me.objectyan.weatherbaby.entities.heweather.AirNow;
import me.objectyan.weatherbaby.entities.heweather.AirNowApi;
import me.objectyan.weatherbaby.entities.heweather.BasicEntity;
import me.objectyan.weatherbaby.entities.heweather.Weather;
import me.objectyan.weatherbaby.entities.heweather.WeatherApi;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HeWeatherApiService {

    private static IHeWeatherApi sHeWeatherApi = null;
    private static Retrofit sRetrofit = null;
    private static OkHttpClient sOkHttpClient = null;
    private static String TAG = "HeWeatherApiService";

    private HeWeatherApiService() {
        initOkHttp();
        initRetrofit();
        sHeWeatherApi = sRetrofit.create(IHeWeatherApi.class);
    }

    private static void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 缓存 http://www.jianshu.com/p/93153b34310e
        File cacheFile = new File(BaseApplication.getAppCacheDir() + File.separator + "NetCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        Interceptor cacheInterceptor = chain -> {
            Request request = chain.request();
            if (!Util.isNetworkConnected()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            Response.Builder newBuilder = response.newBuilder();
            if (Util.isNetworkConnected()) {
                int maxAge = 0;
                // 有网络时 设置缓存超时时间0个小时
                newBuilder.header("Cache-Control", "public, max-age=" + maxAge);
            } else {
                // 无网络时，设置超时为4周
                int maxStale = 60 * 60 * 24 * 28;
                newBuilder.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
            }
            return newBuilder.build();
        };
        builder.cache(cache).addInterceptor(cacheInterceptor);
        //设置超时
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        builder.addInterceptor(logging);
        sOkHttpClient = builder.build();
    }

    private static void initRetrofit() {
        sRetrofit = new Retrofit.Builder()
                .baseUrl(IHeWeatherApi.HOST)
                .client(sOkHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static HeWeatherApiService getInstance() {
        return HeWeatherApiServiceHolder.INSTANCE;
    }

    private static class HeWeatherApiServiceHolder {
        private static final HeWeatherApiService INSTANCE = new HeWeatherApiService();
    }

    private static Consumer<Throwable> disposeFailureInfo(Throwable t) {
        return throwable -> {
            if (t.toString().contains("GaiException") || t.toString().contains("SocketTimeoutException") ||
                    t.toString().contains("UnknownHostException")) {
                Util.showShort("网络问题");
            } else if (t.toString().contains("API没有")) {
                Util.showShort("错误: " + t.getMessage());
            }
            Log.w(TAG, t.getMessage());
        };
    }

    /**
     * 常规天气数据集合
     *
     * @param location
     * @return
     */
    public Observable<Weather> fetchWeather(String location) {
        return sHeWeatherApi.mWeather(location)
                .flatMap(weather -> {
                    String status = weather.mWeather.get(0).status;
                    if ("no more requests".equals(status)) {
                        return Observable.error(new RuntimeException("/(ㄒoㄒ)/~~,API免费次数已用完"));
                    } else if ("unknown city".equals(status)) {
                        return Observable.error(new RuntimeException(String.format("API没有%s", location)));
                    }
                    return Observable.just(weather);
                })
                .map(weather -> weather.mWeather.get(0))
                .doOnError(HeWeatherApiService::disposeFailureInfo)
                .compose(Util.schedulersTransformer());
    }

    /**
     * 空气质量实况
     *
     * @param location
     * @return
     */
    public Observable<AirNow> fetchAirNow(String location) {
        return sHeWeatherApi.mAirNow(location)
                .flatMap(weather -> {
                    String status = weather.mAirNow.get(0).status;
                    if ("no more requests".equals(status)) {
                        return Observable.error(new RuntimeException("/(ㄒoㄒ)/~~,API免费次数已用完"));
                    } else if ("unknown city".equals(status)) {
                        return Observable.error(new RuntimeException(String.format("API没有%s", location)));
                    }
                    return Observable.just(weather);
                })
                .map(weather -> weather.mAirNow.get(0))
                .doOnError(HeWeatherApiService::disposeFailureInfo)
                .compose(Util.schedulersTransformer());
    }

    @TargetApi(Build.VERSION_CODES.N)
    public Observable<List<BasicEntity>> fetchTopCityInfo() {
        return sHeWeatherApi.mTopCityInfo(Util.getTopCityByGroup(), Util.getTopCityByNumber())
                .flatMap(topCity -> {
                    String status = topCity.mTopCity.get(0).status;
                    if ("no more requests".equals(status)) {
                        return Observable.error(new RuntimeException("/(ㄒoㄒ)/~~,API免费次数已用完"));
                    }
                    return Observable.just(topCity);
                })
                .map(weather -> weather.mTopCity.get(0).basic)
                .doOnError(HeWeatherApiService::disposeFailureInfo)
                .compose(Util.schedulersTransformer());
    }
}
