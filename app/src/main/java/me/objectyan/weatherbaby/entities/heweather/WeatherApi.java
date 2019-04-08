package me.objectyan.weatherbaby.entities.heweather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WeatherApi {
    @SerializedName("HeWeather6")
    @Expose
    public List<Weather> mWeather = new ArrayList<>();
}
