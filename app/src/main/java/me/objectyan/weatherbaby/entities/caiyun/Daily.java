package me.objectyan.weatherbaby.entities.caiyun;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.nio.FloatBuffer;
import java.util.List;

public class Daily {
    @SerializedName("status")
    public String status;

    @SerializedName("temperature")
    @Expose
    public List<DailyAvg> temperature;

    @SerializedName("pres")
    @Expose
    public List<DailyAvg> pres;

    @SerializedName("humidity")
    @Expose
    public List<DailyAvg> humidity;

    @SerializedName("visibility")
    @Expose
    public List<DailyAvg> visibility;

    @SerializedName("skycon")
    @Expose
    public List<HourlyKeyValue<String>> skycon;

    @SerializedName("precipitation")
    @Expose
    public List<DailyAvg> precipitation;

    @SerializedName("ultraviolet")
    @Expose
    public List<DailyAvg> ultraviolet;
}
