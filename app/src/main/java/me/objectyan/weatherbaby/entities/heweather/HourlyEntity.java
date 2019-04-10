package me.objectyan.weatherbaby.entities.heweather;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HourlyEntity extends BaseWeatherEntity implements Serializable {

    /**
     * 预报时间，格式yyyy-MM-dd hh:mm
     */
    @SerializedName("time")
    public String time;

    /**
     * 露点温度
     */
    @SerializedName("dew")
    public Double dew;

    /**
     * 降水概率，百分比
     */
    @SerializedName("pop")
    public Double precipitationProbability;

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
    public Double temperature;

    /**
     * 云量
     */
    @SerializedName("cloud")
    public Integer cloud;

    public String getDate() {
        return time.substring(0, 10);
    }

    public String getTime() {
        return time.substring(10);
    }
}
