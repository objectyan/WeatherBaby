package me.objectyan.weatherbaby.entities.heweather;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AirNow implements Serializable {
    @SerializedName("air_now_city")
    public AirNowCityEntity airNow;

    @SerializedName("basic")
    public BasicEntity basic;

    @SerializedName("air_now_station")
    public List<AirNowStationEntity> airNowStationEntities;

    @SerializedName("status")
    public String status;

    @SerializedName("update")
    public UpdateEntity updateEntity;
}
