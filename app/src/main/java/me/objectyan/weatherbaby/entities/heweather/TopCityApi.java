package me.objectyan.weatherbaby.entities.heweather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TopCityApi {
    @SerializedName("HeWeather6")
    @Expose
    public List<TopCity> mTopCity = new ArrayList<>();
}
