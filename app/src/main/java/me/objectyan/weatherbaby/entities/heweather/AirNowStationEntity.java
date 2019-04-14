package me.objectyan.weatherbaby.entities.heweather;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AirNowStationEntity extends AirNowCityEntity implements Serializable {
    /**
     * 站点名称
     */
    @SerializedName("air_sta")
    public String airStation;

    /**
     * 站点ID
     */
    @SerializedName("asid")
    public String airStationID;

    /**
     * 站点纬度
     */
    @SerializedName("lat")
    public Double latitude;

    /**
     * 站点经度
     */
    @SerializedName("lon")
    public Double longitude;
}
