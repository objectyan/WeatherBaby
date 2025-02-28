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
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
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
import me.objectyan.weatherbaby.activities.MainActivity;
import me.objectyan.weatherbaby.entities.CityInfo;
import me.objectyan.weatherbaby.entities.database.CityBase;

public class Util {
    private static final String LOG_TAG = "Util";

    /**
     * 检查网络是否连接
     *
     * @return
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager =
                (ConnectivityManager) BaseApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable() && mNetworkInfo.isConnectedOrConnecting() && mNetworkInfo.getState() == NetworkInfo.State.CONNECTED;
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

    /**
     * 获取当前时间的UTC
     *
     * @return
     */
    public static Date getCurrentTimeByUTC() throws ParseException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fmt.setTimeZone(TimeZone.getTimeZone("Etc/GMT+0"));
        String utcTime = fmt.format(new Date());
        fmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        return fmt.parse(utcTime);
    }

    /**
     * 获取城市名称用户查询接口
     *
     * @param cityBase
     * @return
     */
    public static String getCityName(CityBase cityBase) {
        if (cityBase.getIsLocation()) {
            if (cityBase.getLatitude() != null && cityBase.getLongitude() != null)
                return String.format("%f,%f", cityBase.getLongitude(), cityBase.getLatitude());
            return "auto_ip";
        }
        if (!TextUtils.isEmpty(cityBase.getLocation())) return cityBase.getLocation();
        if (!TextUtils.isEmpty(cityBase.getCid())) return cityBase.getCid();
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
     * Local 转 UTC
     *
     * @param date
     * @return
     */
    public static Date localToUTC(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String gmtTime = sdf.format(new Date());
        SimpleDateFormat localFormater = new SimpleDateFormat();//当地时间格式
        localFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        return localFormater.parse(gmtTime);
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
        int currentYear = c.get(Calendar.YEAR);
        int currentDays = c.get(Calendar.DAY_OF_YEAR);
        try {
            c.setTime(dateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int year = c.get(Calendar.YEAR);
        int days = c.get(Calendar.DAY_OF_YEAR);
        int yearDays = year % 4 == 0 ? 366 : 365;
        String week = getWeek(date);
        if (currentYear > year) {
            currentDays += yearDays;
        }
        switch (currentDays - days) {
            case 1:
                week = BaseApplication.getAppContext().getString(R.string.forecast_item_date_format_yesterday);
                break;
            case 0:
                week = BaseApplication.getAppContext().getString(R.string.forecast_item_date_format_today);
                break;
            case -1:
                week = BaseApplication.getAppContext().getString(R.string.forecast_item_date_format_tomorrow);
                break;
        }
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
        String day = String.valueOf(c.get(Calendar.DATE));
        return String.format(format, month, day, week);
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
        if (TextUtils.isEmpty(time)) return null;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time.split(":")[0]));
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

    public static String getTopCityByGroup() {
        SharedPreferences share = BaseApplication.getAppContext().getSharedPreferences(
                WeatherBabyConstants.EXTRA_WEATHERBABY_SHARE, Activity.MODE_PRIVATE);
        return share.getString(WeatherBabyConstants.SHARE_TOP_CITY_GROUP, "cn");
    }

    public static int getTopCityByNumber() {
        SharedPreferences share = BaseApplication.getAppContext().getSharedPreferences(
                WeatherBabyConstants.EXTRA_WEATHERBABY_SHARE, Activity.MODE_PRIVATE);
        return share.getInt(WeatherBabyConstants.SHARE_TOP_CITY_NUMBER, 15);
    }

    /**
     * 获取更新时间
     *
     * @return
     */
    public static int getSettingsUpdateInterval() {
        SharedPreferences share = BaseApplication.getAppContext().getSharedPreferences(
                WeatherBabyConstants.EXTRA_WEATHERBABY_SHARE, Activity.MODE_PRIVATE);
        return share.getInt(WeatherBabyConstants.SHARE_SETTINGS_UPDATE_INTERVAL, 1);
    }

    /**
     * 设置更新时间
     *
     * @param settingsUpdateInterval
     */
    public static void setSettingsUpdateInterval(int settingsUpdateInterval) {
        SharedPreferences share = BaseApplication.getAppContext().getSharedPreferences(
                WeatherBabyConstants.EXTRA_WEATHERBABY_SHARE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putInt(WeatherBabyConstants.SHARE_SETTINGS_UPDATE_INTERVAL, settingsUpdateInterval);
        editor.apply();
    }

    /**
     * 获取温度单位
     *
     * @return
     */
    public static String getSettingsTempUnit() {
        SharedPreferences share = BaseApplication.getAppContext().getSharedPreferences(
                WeatherBabyConstants.EXTRA_WEATHERBABY_SHARE, Activity.MODE_PRIVATE);
        return share.getString(WeatherBabyConstants.SHARE_SETTINGS_TEMP_UNIT, BaseApplication.getAppContext().getString(R.string.Centigrade));
    }

    /**
     * 获取定位地址
     *
     * @return
     */
    public static String getLocationAddress() {
        SharedPreferences share = BaseApplication.getAppContext().getSharedPreferences(
                WeatherBabyConstants.EXTRA_WEATHERBABY_SHARE, Activity.MODE_PRIVATE);
        return share.getString(WeatherBabyConstants.SHARE_LOCATION_ADDRESS, null);
    }

    /**
     * 设置定位地址
     *
     * @param locationAddress
     */
    public static void setLocationAddress(String locationAddress) {
        SharedPreferences share = BaseApplication.getAppContext().getSharedPreferences(
                WeatherBabyConstants.EXTRA_WEATHERBABY_SHARE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString(WeatherBabyConstants.SHARE_LOCATION_ADDRESS, locationAddress);
        editor.apply();
    }

    /**
     * 设置温度单位
     *
     * @param settingsTempUnit
     */
    public static void setSettingsTempUnit(String settingsTempUnit) {
        SharedPreferences share = BaseApplication.getAppContext().getSharedPreferences(
                WeatherBabyConstants.EXTRA_WEATHERBABY_SHARE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString(WeatherBabyConstants.SHARE_SETTINGS_TEMP_UNIT, settingsTempUnit);
        editor.apply();
    }

    /**
     * 获取温度信息
     */
    public static String getTempByUnit(Double temp) {
        String unit = getSettingsTempUnit();
        return String.format("%s %s", getTemp(temp), unit);
    }

    public static String getTemp(Double temp) {
        String unit = getSettingsTempUnit();
        Double currentTemp = temp * 1.8 + 32;
        if (unit.equals(BaseApplication.getAppContext().getString(R.string.Centigrade)))
            currentTemp = temp;
        return String.format("%.1f", currentTemp);
    }

    /**
     * 获取生活指数名称
     *
     * @param type
     * @return
     */
    public static String getLifestyleName(String type) {
        switch (type) {
            case "comf":
                return "舒适度指数";
            case "cw":
                return "洗车指数";
            case "drsg":
                return "穿衣指数";
            case "flu":
                return "感冒指数";
            case "sport":
                return "运动指数";
            case "trav":
                return "旅游指数";
            case "uv":
                return "紫外线指数";
            case "air":
                return "空气污染指数";
            case "ac":
                return "空调开启指数";
            case "ag":
                return "过敏指数";
            case "gl":
                return "太阳镜指数";
            case "mu":
                return "化妆指数";
            case "airc":
                return "晾晒指数";
            case "ptfc":
                return "交通指数";
            case "fsh":
                return "钓鱼指数";
            case "spi":
                return "防晒指数";
            default:
                return type;
        }
    }

    /**
     * 获取生活指数图片
     *
     * @return
     */
    public static Integer getLifestyleIcon(String type) {
        switch (type) {
            case "comf":
                return R.drawable.ic_lifestyle_comfortdegree;
            case "cw":
                return R.drawable.ic_lifestyle_carwash;
            case "drsg":
                return R.drawable.ic_lifestyle_dressing;
            case "flu":
                return R.drawable.ic_lifestyle_cold;
            case "sport":
                return R.drawable.ic_lifestyle_motion;
            case "trav":
                return R.drawable.ic_lifestyle_travel;
            case "uv":
                return R.drawable.ic_lifestyle_ultravioletrays;
            case "air":
                return R.drawable.ic_lifestyle_airpollution;
            case "ac":
                return R.drawable.ic_lifestyle_airconditioner;
            case "ag":
                return R.drawable.ic_lifestyle_allergy;
            case "gl":
                return R.drawable.ic_lifestyle_sunglasses;
            case "mu":
                return R.drawable.ic_lifestyle_makeup;
            case "airc":
                return R.drawable.ic_lifestyle_airing;
            case "ptfc":
                return R.drawable.ic_lifestyle_traffic;
            case "fsh":
                return R.drawable.ic_lifestyle_fishing;
            case "spi":
                return R.drawable.ic_lifestyle_sunscreen;
            default:
                return null;
        }
    }

    /**
     * 日期转时间段
     *
     * @param date
     * @return
     */
    public static String dateToTimeSlot(Date date) {
        SimpleDateFormat sdfHour = new SimpleDateFormat("HH");
        String slot = "";
        int hour = Integer.parseInt(sdfHour.format(date));
        if (2 < hour && hour <= 5) {
            slot = BaseApplication.getAppContext().getString(R.string.time_slot_before_dawn);
        } else if (5 < hour && hour <= 8) {
            slot = BaseApplication.getAppContext().getString(R.string.time_slot_matinal);
        } else if (8 < hour && hour <= 12) {
            slot = BaseApplication.getAppContext().getString(R.string.time_slot_morning);
        } else if (12 < hour && hour <= 14) {
            slot = BaseApplication.getAppContext().getString(R.string.time_slot_noonday);
        } else if (14 < hour && hour <= 18) {
            slot = BaseApplication.getAppContext().getString(R.string.time_slot_afternoon);
        } else if (18 < hour && hour <= 19) {
            slot = BaseApplication.getAppContext().getString(R.string.time_slot_at_nightfall);
        } else if (19 < hour && hour <= 22) {
            slot = BaseApplication.getAppContext().getString(R.string.time_slot_evening);
        } else {
            slot = BaseApplication.getAppContext().getString(R.string.time_slot_late_at_night);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        return String.format("%s %s", slot, sdf.format(date));
    }
}
