package me.objectyan.weatherbaby.common;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import org.greenrobot.greendao.database.Database;

import androidx.appcompat.app.AppCompatDelegate;
import me.objectyan.weatherbaby.BuildConfig;
import me.objectyan.weatherbaby.entities.database.DaoMaster;
import me.objectyan.weatherbaby.entities.database.DaoSession;

public class BaseApplication extends Application {

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
