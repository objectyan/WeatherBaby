package me.objectyan.weatherbaby.entities.heweather;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AirNowCityEntity implements Serializable {
    /**
     * 空气质量指数，AQI和PM25的关系
     */
    @SerializedName("aqi")
    public String aqi;

    /**
     * 一氧化碳
     */
    @SerializedName("co")
    public String co;

    /**
     * 主要污染物
     */
    @SerializedName("main")
    public String main;

    /**
     * 二氧化氮
     */
    @SerializedName("no2")
    public String no2;

    /**
     * 臭氧
     */
    @SerializedName("o3")
    public String o3;

    /**
     * pm10
     */
    @SerializedName("pm10")
    public String pm10;

    /**
     * pm25
     */
    @SerializedName("pm25")
    public String pm25;

    /**
     * 数据发布时间,格式yyyy-MM-dd HH:mm
     */
    @SerializedName("pub_time")
    public String pubTime;

    /**
     * 空气质量，取值范围:优，良，轻度污染，中度污染，重度污染，严重污染
     */
    @SerializedName("qlty")
    public String airQuality;

    /**
     * 二氧化硫
     */
    @SerializedName("so2")
    public String so2;
}
