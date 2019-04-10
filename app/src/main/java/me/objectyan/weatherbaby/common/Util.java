package me.objectyan.weatherbaby.common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.core.app.ActivityCompat;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.objectyan.weatherbaby.R;
import me.objectyan.weatherbaby.entities.CityInfo;
import me.objectyan.weatherbaby.entities.database.CityBase;

public class Util {
    private static final String LOG_TAG = "Util";

    /**
     * 检查网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static void showShort(String msg) {
        Toast.makeText(BaseApplication.getAppContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(String msg) {
        Toast.makeText(BaseApplication.getAppContext(), msg, Toast.LENGTH_LONG).show();
    }

    /**
     * City Base To City Info
     *
     * @param cityBase
     * @return
     */
    public static CityInfo cityBaseToInfo(CityBase cityBase) {
        CityInfo cityInfo = new CityInfo();
        if (cityBase.getLongitude() != null)
            cityInfo.setLongitude(Double.toString(cityBase.getLongitude()));
        if (cityBase.getLatitude() != null)
            cityInfo.setLatitude(Double.toString(cityBase.getLatitude()));
        cityInfo.setCityName(cityBase.getLocation());
        cityInfo.setId(cityBase.getId());
        cityInfo.setWeatherCode(cityBase.getCid());
        cityInfo.setType(CityInfo.CityType.None.getKey());
        if (cityBase.getIsDefault()) cityInfo.setType(CityInfo.CityType.Default.getKey());
        if (cityBase.getIsLocation()) cityInfo.setType(CityInfo.CityType.Location.getKey());
        return cityInfo;
    }

    /**
     * City Info To City Base
     *
     * @param cityInfo
     * @return
     */
    public static CityBase cityInfoToBase(CityInfo cityInfo) {
        CityBase cityBase = new CityBase();
        if (!TextUtils.isEmpty(cityInfo.getLongitude()))
            cityBase.setLongitude(Double.valueOf(cityInfo.getLongitude()));
        if (!TextUtils.isEmpty(cityInfo.getLatitude()))
            cityBase.setLatitude(Double.valueOf(cityInfo.getLatitude()));
        cityBase.setLocation(cityInfo.getCityName());
        cityBase.setId(cityInfo.getId());
        cityBase.setCid(cityInfo.getWeatherCode());
        cityBase.setIsDefault(cityInfo.isDefault());
        cityBase.setIsLocation(cityInfo.isLocation());
        return cityBase;
    }

    @SuppressLint("MissingPermission")
    public static Location getCurrentLocation() {
        LocationManager locationManager = (LocationManager) BaseApplication.getAppContext().getSystemService(Context.LOCATION_SERVICE);
        // 返回所有已知的位置提供者的名称列表，包括未获准访问或调用活动目前已停用的。
        String provider = null;
        List<String> lp = locationManager.getAllProviders();
        for (String item : lp) {
            Log.i(LOG_TAG, "可用位置服务：" + item);
        }
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(false);
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        if (provider != null) {
            return locationManager.getLastKnownLocation(provider);
        } else {
            Toast.makeText(BaseApplication.getAppContext(), R.string.toast_not_location, Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /**
     * 获取当前时间的UTC
     *
     * @return
     */
    public static Date getCurrentTimeByUTC() {
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return cal.getTime();
    }

    /**
     * 获取城市名称用户查询接口
     *
     * @param cityBase
     * @return
     */
    public static String getCityName(CityBase cityBase) {
        if (!TextUtils.isEmpty(cityBase.getLocation())) return cityBase.getLocation();
        if (!TextUtils.isEmpty(cityBase.getCid())) return cityBase.getCid();
        if (cityBase.getLatitude() != null && cityBase.getLongitude() != null)
            return String.format("%d,%d", cityBase.getLongitude(), cityBase.getLatitude());
        return "auto_ip";
    }

    public static ObservableTransformer schedulersTransformer() {
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread(), true);
            }
        };
    }

    public static String dateToStr(Date date, String format) {
        SimpleDateFormat formatDate = new SimpleDateFormat(TextUtils.isEmpty(format) ? "yyyy-MM-dd HH:mm:ss" : format);
        return formatDate.format(date);
    }

    /**
     * UTC 转 Local
     *
     * @param date
     * @return
     */
    public static String utcToLocal(Date date) {
        SimpleDateFormat localFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//当地时间格式
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(date.getTime());
        return localTime;
    }
}
