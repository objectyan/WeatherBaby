package me.objectyan.weatherbaby.common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import io.reactivex.plugins.RxJavaPlugins;
import me.objectyan.weatherbaby.BuildConfig;
import me.objectyan.weatherbaby.activities.MainActivity;
import me.objectyan.weatherbaby.entities.database.DaoMaster;
import me.objectyan.weatherbaby.entities.database.DaoSession;

public class BaseApplication extends Application {

    private static String LOG_TAG = "BaseApplication";

    private static String sCacheDir;
    private static Context sAppContext;
    private static DaoSession daoSession;

    static {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = getApplicationContext();
        if (getApplicationContext().getExternalCacheDir() != null && ExistSDCard()) {
            sCacheDir = getApplicationContext().getExternalCacheDir().toString();
        } else {
            sCacheDir = getApplicationContext().getCacheDir().toString();
        }
        RxJavaPlugins.setErrorHandler(throwable -> {
            if (throwable != null) {
                Log.e(LOG_TAG, throwable.toString());
            } else {
                Log.e(LOG_TAG, "call onError but exception is null");
            }
        });
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, BuildConfig.DataBaseName);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    private boolean ExistSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public static Context getAppContext() {
        return sAppContext;
    }

    public static String getAppCacheDir() {
        return sCacheDir;
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }
}
