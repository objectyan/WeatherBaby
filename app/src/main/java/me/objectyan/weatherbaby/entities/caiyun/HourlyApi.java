package me.objectyan.weatherbaby.entities.caiyun;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import me.objectyan.weatherbaby.entities.heweather.AirNow;

public class HourlyApi {
    @SerializedName("result")
    public Hourly mHourly;
}
