package me.objectyan.weatherbaby.entities.heweather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AirNowApi {
    @SerializedName("HeWeather6")
    @Expose
    public List<AirNow> mAirNow = new ArrayList<>();
}
