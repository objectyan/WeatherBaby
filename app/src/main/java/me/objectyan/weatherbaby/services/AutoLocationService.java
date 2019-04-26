package me.objectyan.weatherbaby.services;

import android.app.Service;
import android.content.Intent;
import android.nfc.Tag;
import android.os.IBinder;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.objectyan.weatherbaby.common.BaseApplication;
import me.objectyan.weatherbaby.common.Util;
import me.objectyan.weatherbaby.common.WeatherBabyConstants;
import me.objectyan.weatherbaby.entities.database.CityBase;
import me.objectyan.weatherbaby.entities.database.CityBaseDao;

public class AutoLocationService extends Service implements AMapLocationListener {
    private final String TAG = AutoLocationService.class.getSimpleName();
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private boolean mIsUnSubscribed = true;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
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
        mLocationOption.setInterval(2000);
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
                mLocationClient.startLocation();
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
                        Intent intent = new Intent(WeatherBabyConstants.RECEIVER_UPDATE_WEATHER);
                        intent.putExtra("Type", WeatherBabyConstants.RECEIVE_UPDATE_TYPE_LOCATION);
                        intent.putExtra("CityID", cityBase.getId());
                        sendBroadcast(intent);
                    }
                    cityBaseDao.update(cityBase);
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e(TAG, "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }
}
