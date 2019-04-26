package me.objectyan.weatherbaby.common;

/**
 * 公共常量类
 */
public class WeatherBabyConstants {
    /**
     * 主页城市管理界面
     */
    public static final int MAIN_REQUEST_CITY_MANAGE = 1;

    /**
     * 主页城市地图定位
     */
    public static final int REQUEST_PERMISSION_LOCATION = 2;

    /**
     * 城市编号
     */
    public static final String CITY_ID = "CURRENT_CITY_ID";

    /**
     * sharedPreferences属性信息文件
     */
    public static final String EXTRA_WEATHERBABY_SHARE = "EXTRA_WEATHERBABY_SHARE";

    /**
     * sharedPreferences属性 默认城市编号
     */
    public static final String SHARE_DEFAULT_CITY_ID = "DEFAULT_CITY_ID";

    /**
     * sharedPreferences属性 热门城市分组
     */
    public static final String SHARE_TOP_CITY_GROUP = "TOP_CITY_GROUP";

    /**
     * sharedPreferences属性 热门城市数量
     */
    public static final String SHARE_TOP_CITY_NUMBER = "TOP_CITY_NUMBER";

    /**
     * sharedPreferences属性 更新时间
     */
    public static final String SHARE_SETTINGS_UPDATE_INTERVAL = "SETTINGS_UPDATE_INTERVAL";

    /**
     * sharedPreferences属性 温度单位
     */
    public static final String SHARE_SETTINGS_TEMP_UNIT = "SETTINGS_TEMP_UNIT";

    /**
     * sharedPreferences属性 温度单位
     */
    public static final String SHARE_LOCATION_ADDRESS = "LOCATION_ADDRESS";

    /**
     * receiver 天气更新
     */
    public static final String RECEIVER_UPDATE_WEATHER = "me.objectyan.weatherbaby.receiver.updateweather";

    /**
     * 更新当前定位
     */
    public static final int RECEIVE_UPDATE_TYPE_LOCATION = 1;

    /**
     * 更新首页城市名称
     */
    public static final int RECEIVE_UPDATE_TYPE_CITY_NAMR = 2;
}
