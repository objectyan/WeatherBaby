package me.objectyan.weatherbaby.entities.heweather;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BasicEntity implements Serializable {
    /**
     * 地区／城市ID
     */
    @SerializedName("cid")
    public String code;

    /**
     * 地区／城市名称
     */
    @SerializedName("location")
    public String Name;

    /**
     * 该地区／城市的上级城市
     */
    @SerializedName("parent_city")
    public String parentCity;

    /**
     * 该地区／城市所属行政区域
     */
    @SerializedName("admin_area")
    public String adminArea;

    /**
     * 该地区／城市所属国家名称
     */
    @SerializedName("cnty")
    public String countryName;

    /**
     * 地区／城市纬度
     */
    @SerializedName("lat")
    public String latitude;

    /**
     * 地区／城市经度
     */
    @SerializedName("lon")
    public String longitude;

    /**
     * 该地区／城市所在时区
     */
    @SerializedName("tz")
    public String timeZone;
}