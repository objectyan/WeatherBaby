package me.objectyan.weatherbaby.entities.heweather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopCity {
    @SerializedName("basic")
    public List<BasicEntity> basic;

    @SerializedName("status")
    public String status;
}
