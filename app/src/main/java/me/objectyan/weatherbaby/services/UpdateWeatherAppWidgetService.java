package me.objectyan.weatherbaby.services;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.common.BaseApplication;
import me.objectyan.weatherbaby.common.Util;
import me.objectyan.weatherbaby.common.WeatherBabyConstants;
import me.objectyan.weatherbaby.entities.database.CityBase;
import me.objectyan.weatherbaby.entities.database.CityBaseDao;
import me.objectyan.weatherbaby.widget.WeatherAppWidget;

public class UpdateWeatherAppWidgetService extends Service implements AMapLocationListener {
    private final String TAG = AutoLocationService.class.getSimpleName();
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private boolean mIsUnSubscribed = true;
    private Disposable mDisposable;
    private CityBaseDao cityBaseDao;
    private long intervalTime = 2000;

    public UpdateWeatherAppWidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        cityBaseDao = BaseApplication.getDaoSession().getCityBaseDao();
        mLocationClient = new AMapLocationClient(this);
        // 设置定位监听
        mLocationClient.setLocationListener(this);
        // 初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(intervalTime);
        // 设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        synchronized (this) {
            unSubscribed();
            if (mIsUnSubscribed) {
                Log.i(TAG, "onStartCommand");
                mIsUnSubscribed = false;
                CityBase cityBase = cityBaseDao.queryBuilder().where(CityBaseDao.Properties.IsLocation.eq(true)).unique();
                if (cityBase != null) {
                    mLocationClient.startLocation();
                } else {
                    cityBase = cityBaseDao.queryBuilder().where(CityBaseDao.Properties.IsDefault.eq(true)).unique();
                    if (cityBase != null) {
                        CityBase finalCityBase = cityBase;
                        mDisposable = Observable.interval(intervalTime, TimeUnit.MILLISECONDS, Schedulers.io())
                                .doOnNext(aLong -> {
                                    updateWeather(finalCityBase, new Date().getTime() - finalCityBase.getUpdateTime().getTime() < Util.getSettingsUpdateInterval() * 60 * 1000);
                                })
                                .subscribe();
                    }
                }
            }
        }
        return START_REDELIVER_INTENT;
    }

    private void unSubscribed() {
        mIsUnSubscribed = true;
        if (mLocationClient != null && mLocationClient.isStarted()) {
            Log.i(TAG, "unSubscribed");
            mLocationClient.stopLocation();
        }
        if (mDisposable != null && !mDisposable.isDisposed()) {
            Log.i(TAG, "unSubscribed");
            mDisposable.dispose();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.onDestroy();
    }

    @Override
    public boolean stopService(Intent name) {
        Log.i(TAG, "stopService");
        return super.stopService(name);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                String currentAddress = String.format("%s-%s-%s-%s",
                        aMapLocation.getCountry(), aMapLocation.getProvince(),
                        aMapLocation.getCity(), aMapLocation.getDistrict());
                CityBaseDao cityBaseDao = BaseApplication.getDaoSession().getCityBaseDao();
                CityBase cityBase = cityBaseDao.queryBuilder().where(CityBaseDao.Properties.IsLocation.eq(true)).unique();
                if (cityBase != null) {
                    cityBase.setLongitude(aMapLocation.getLongitude());
                    cityBase.setLatitude(aMapLocation.getLatitude());
                    String baseAddress = Util.getLocationAddress();
                    if (!currentAddress.equals(baseAddress)) {
                        Util.setLocationAddress(currentAddress);
                        cityBase.setLocation(aMapLocation.getDistrict());
                        cityBase.setCountry(aMapLocation.getCountry());
                        cityBase.setAdminArea(aMapLocation.getProvince());
                        cityBase.setParentCity(aMapLocation.getCity());
                        updateWeather(cityBase, true);
                    }
                    cityBaseDao.update(cityBase);
                    updateWeather(cityBase, false);
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e(TAG, "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    private void updateWeather(CityBase cityBase, boolean flag) {
        Log.i(TAG, "flag:" + flag);
        Observable.create(new ObservableOnSubscribe<CityBase>() {
            @Override
            public void subscribe(ObservableEmitter<CityBase> emitter) throws Exception {
                if (flag) {
                    Intent intent = new Intent(WeatherBabyConstants.RECEIVER_UPDATE_WEATHER);
                    intent.putExtra("Type", WeatherBabyConstants.RECEIVE_UPDATE_TYPE_LOCATION);
                    intent.putExtra("CityID", cityBase.getId());
                    sendBroadcast(intent);

                    CityManageService.refreshCityInfo(cityBase.getId()).subscribe(data -> {
                        CityBase cityBase = cityBaseDao.queryBuilder().where(CityBaseDao.Properties.IsDefault.eq(true)).unique();
                        emitter.onNext(cityBase);
                    }, error -> {
                        Log.e(TAG, error.getLocalizedMessage());
                    });
                } else {
                    emitter.onNext(cityBase);
                }
            }
        }).doOnNext(data -> {
            ComponentName componentName = new ComponentName(UpdateWeatherAppWidgetService.this, WeatherAppWidget.class);
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.weather_app_widget);
            remoteViews.setImageViewResource(R.id.weather_location, R.drawable.ic_location);
            remoteViews.setTextViewText(R.id.weather_name, data.getLocation());
            remoteViews.setTextViewText(R.id.weather_max_temp, String.valueOf(data.getTempHigh()));
            remoteViews.setTextViewText(R.id.weather_min_temp, String.valueOf(data.getTempLow()));
            remoteViews.setTextViewText(R.id.weather_temp, Util.getTempByUnit(data.getTemperature()));
            remoteViews.setViewVisibility(R.id.weather_location, data.getIsLocation() ? View.VISIBLE : View.GONE);
            remoteViews.setImageViewBitmap(R.id.weather_icon, Util.getHeWeatherBitmap(data.getCondCode()));
            AppWidgetManager widgetManager = AppWidgetManager.getInstance(getApplicationContext());
            widgetManager.updateAppWidget(componentName, remoteViews);
        }).subscribe();

    }
}
