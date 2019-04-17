package me.objectyan.weatherbaby.entities.caiyun;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Hourly {
    @SerializedName("status")
    public String status;

    @SerializedName("temperature")
    public List<HourlyKeyValue<Double>> temperature;

    @SerializedName("pres")
    public List<HourlyKeyValue<Double>> pres;

    @SerializedName("humidity")
    public List<HourlyKeyValue<Double>> humidity;

    @SerializedName("wind")
    public List<HourlyKeyValue<Double>> wind;

    @SerializedName("cloudrate")
    public List<HourlyKeyValue<Double>> cloudrate;

    @SerializedName("visibility")
    public List<HourlyKeyValue<Double>> visibility;

    @SerializedName("skycon")
    public List<HourlyKeyValue<String>> skycon;

    @SerializedName("precipitation")
    public List<HourlyKeyValue<Double>> precipitation;

}
