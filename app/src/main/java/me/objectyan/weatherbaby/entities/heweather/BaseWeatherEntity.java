package me.objectyan.weatherbaby.entities.heweather;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BaseWeatherEntity implements Serializable {

    /**
     * 相对湿度
     */
    @SerializedName("hum")
    public String humidity;

    /**
     * 大气压强
     */
    @SerializedName("pres")
    public String pressure;

    /**
     * 风向360角度
     */
    @SerializedName("wind_deg")
    public String windDirectionAngle;

    /**
     * 风向
     */
    @SerializedName("wind_dir")
    public String windDirection;

    /**
     * 风力
     */
    @SerializedName("wind_sc")
    public String windPower;

    /**
     * 风速，公里/小时
     */
    @SerializedName("wind_spd")
    public String windSpeed;

}
