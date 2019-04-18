package me.objectyan.weatherbaby.entities.caiyun;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Hourly {
    @SerializedName("status")
    public String status;

    @SerializedName("temperature")
    @Expose
    public List<HourlyKeyValue<Double>> temperature;

    @SerializedName("pres")
    @Expose
    public List<HourlyKeyValue<Double>> pres;

    @SerializedName("humidity")
    @Expose
    public List<HourlyKeyValue<Double>> humidity;

    @SerializedName("wind")
    @Expose
    public List<HourlyKeyValue<Double>> wind;

    @SerializedName("cloudrate")
    @Expose
    public List<HourlyKeyValue<Double>> cloudrate;

    @SerializedName("visibility")
    @Expose
    public List<HourlyKeyValue<Double>> visibility;

    @SerializedName("skycon")
    @Expose
    public List<HourlyKeyValue<String>> skycon;

    @SerializedName("precipitation")
    @Expose
    public List<HourlyKeyValue<Double>> precipitation;

}
