package me.objectyan.weatherbaby.entities.heweather;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Weather implements Serializable {

    @SerializedName("basic")
    public BasicEntity basic;

    @SerializedName("status")
    public String status;

    @SerializedName("update")
    public UpdateEntity updateEntity;

    @SerializedName("now")
    public NowEntity nowEntity;

    @SerializedName("hourly")
    public List<HourlyEntity> hourlyEntities;

    @SerializedName("daily_forecast")
    public List<DailyForecastEntity> dailyForecastEntities;

    @SerializedName("lifestyle")
    public List<LifestyleEntity> lifestyleEntities;
}
