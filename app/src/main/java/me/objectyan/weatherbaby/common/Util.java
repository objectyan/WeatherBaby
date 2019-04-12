package me.objectyan.weatherbaby.common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
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

    public static void showShort(@StringRes int resId) {
        Toast.makeText(BaseApplication.getAppContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(String msg) {
        Toast.makeText(BaseApplication.getAppContext(), msg, Toast.LENGTH_LONG).show();
    }

    public static void showLong(@StringRes int resId) {
        Toast.makeText(BaseApplication.getAppContext(), resId, Toast.LENGTH_LONG).show();
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
        cityInfo.setWeatherTypeDay(cityBase.getCondCode());
        cityInfo.setWeatherTypeNight(cityBase.getCondCode());
        cityInfo.setWeatherTypeTxt(cityBase.getCondTxt());
        cityInfo.setTempHigh(cityBase.getTempHigh());
        cityInfo.setTempLow(cityBase.getTempLow());
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

    public static Location getCurrentLocation() {
        LocationManager locationManager = (LocationManager) BaseApplication.getAppContext().getSystemService(Context.LOCATION_SERVICE);
        // 返回所有已知的位置提供者的名称列表，包括未获准访问或调用活动目前已停用的。
        String provider = null;
        List<String> locationManagerAllProviders = locationManager.getAllProviders();
        if (locationManagerAllProviders.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER; // 网络定位
        } else if (locationManagerAllProviders.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER; // GPS定位
        }
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(false);
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        if (provider != null) {
            if (ActivityCompat.checkSelfPermission(BaseApplication.getAppContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(BaseApplication.getAppContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            return locationManager.getLastKnownLocation(provider);
        } else {
            Util.showShort(R.string.toast_not_location);
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
        if (!TextUtils.isEmpty(cityBase.getLocation())
                && !cityBase.getLocation().equals(
                BaseApplication.getAppContext().getString(R.string.current_location_addresss)))
            return cityBase.getLocation();
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

    /**
     * 获取和风天气图片
     *
     * @param weatherCode
     * @return
     */
    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Drawable getHeWeatherIcon(String weatherCode) {
        if (!TextUtils.isEmpty(weatherCode))
            try {
                InputStream is = BaseApplication.getAppContext().getResources().getAssets().open(String.format("icon-heweather/%s.png", weatherCode));
                return Drawable.createFromStream(is, null);
            } catch (IOException e) {
                if (e != null) {
                    e.printStackTrace();
                }
            } catch (OutOfMemoryError e) {
                if (e != null) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                if (e != null) {
                    e.printStackTrace();
                }
            }
        return BaseApplication.getAppContext().getDrawable(R.drawable.ic_disabled);
    }

    /**
     * 判断白天与晚上
     *
     * @return
     */
    public static boolean isDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String hour = sdf.format(new Date());
        int k = Integer.parseInt(hour);
        if ((k >= 0 && k < 6) || (k >= 18 && k < 24)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 字符串转时间
     *
     * @param strDate
     * @return
     * @throws ParseException
     */
    public static Date strToDate(String strDate) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.parse(strDate);
    }

    /**
     * 时间格式化为 12月14日 明天
     *
     * @param date
     * @return
     */
    public static String getDateByFormat(String date) {
        String format = BaseApplication.getAppContext().getString(R.string.forecast_item_date_format);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(dateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
        String day = String.valueOf(c.get(Calendar.DATE));
        return String.format(format, month, day, getWeek(date));
    }

    /**
     * 获取星期几
     *
     * @param time
     * @return
     */
    public static String getWeek(String time) {
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int wek = c.get(Calendar.DAY_OF_WEEK);
        if (wek == 1) {
            Week += "星期日";
        }
        if (wek == 2) {
            Week += "星期一";
        }
        if (wek == 3) {
            Week += "星期二";
        }
        if (wek == 4) {
            Week += "星期三";
        }
        if (wek == 5) {
            Week += "星期四";
        }
        if (wek == 6) {
            Week += "星期五";
        }
        if (wek == 7) {
            Week += "星期六";
        }
        return Week;
    }

    public static Date getDateByTime(String time) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, Integer.valueOf(time.split(":")[0]));
        c.set(Calendar.MINUTE, Integer.valueOf(time.split(":")[1]));
        return c.getTime();
    }

    /**
     * 获取默认城市编号
     *
     * @return
     */
    public static Long getDefaultCityID() {
        SharedPreferences share = BaseApplication.getAppContext().getSharedPreferences(
                WeatherBabyConstants.EXTRA_WEATHERBABY_SHARE, Activity.MODE_PRIVATE);
        return share.getLong(WeatherBabyConstants.SHARE_DEFAULT_CITY_ID, 0);
    }

    /**
     * 设置默认城市编号
     *
     * @param cityID
     */
    public static void setDefaultCityID(Long cityID) {
        SharedPreferences share = BaseApplication.getAppContext().getSharedPreferences(
                WeatherBabyConstants.EXTRA_WEATHERBABY_SHARE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putLong(WeatherBabyConstants.SHARE_DEFAULT_CITY_ID, cityID);
        editor.apply();
    }
}
