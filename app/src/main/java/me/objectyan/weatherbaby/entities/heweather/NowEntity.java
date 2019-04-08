package me.objectyan.weatherbaby.entities.heweather;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NowEntity extends BaseWeatherEntity implements Serializable {
    /**
     * 体感温度，默认单位：摄氏度
     */
    @SerializedName("fl")
    public String somatosensory;

    /**
     * 降水量
     */
    @SerializedName("pcpn")
    public String precipitation;

    /**
     * 能见度，默认单位：公里
     */
    @SerializedName("vis")
    public String visibility;

    /**
     * 实况天气状况代码
     */
    @SerializedName("cond_code")
    public String condCode;

    /**
     * 实况天气状况描述
     */
    @SerializedName("cond_txt")
    public String condTxt;

    /**
     * 温度，默认单位：摄氏度
     */
    @SerializedName("tmp")
    public String temperature;

    /**
     * 云量
     */
    @SerializedName("cloud")
    public String cloud;

}
