package me.objectyan.weatherbaby.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.common.BaseApplication;
import me.objectyan.weatherbaby.common.Util;
import me.objectyan.weatherbaby.entities.database.CityBase;

public class AutoUpdateService extends Service {

    private final String TAG = AutoUpdateService.class.getSimpleName();
    private Disposable mDisposable;
    private boolean mIsUnSubscribed = true;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        synchronized (this) {
            unSubscribed();
            if (mIsUnSubscribed) {
                unSubscribed();
                Log.i(TAG, "onStartCommand");
                mDisposable = Observable.interval(Util.getSettingsUpdateInterval(), TimeUnit.HOURS, Schedulers.io())
                        .doOnNext(aLong -> {
                            mIsUnSubscribed = false;
                            fetchDataByNetWork();
                        })
                        .subscribe();
            }
        }
        return START_REDELIVER_INTENT;
    }

    private void unSubscribed() {
        mIsUnSubscribed = true;
        if (mDisposable != null && !mDisposable.isDisposed()) {
            Log.i(TAG, "unSubscribed");
            mDisposable.dispose();
        }
    }

    @Override
    public boolean stopService(Intent name) {
        Log.i(TAG, "stopService");
        return super.stopService(name);
    }

    private void fetchDataByNetWork() {
        Log.i(TAG, "fetchDataByNetWork");
        List<CityBase> refreshCity = BaseApplication.getDaoSession().getCityBaseDao().loadAll();
        for (CityBase cityBase : refreshCity) {
            CityManageService.refreshCityInfo(cityBase.getId()).subscribe();
        }
    }
}
