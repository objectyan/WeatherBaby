package me.objectyan.weatherbaby.entities.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CityAirNowStation {
    @Id(autoincrement = true)
    private Long id;
    private Long cityID;
    private Double aqi;
    private String majorPollutants;
    private String airQuality;
    private Double pm10;
    private Double pm25;
    private Double no2;
    private Double so2;
    private Double co;
    private Double o3;
    private Date pubTime;
    private String airStation;
    private String airStationID;
    private Double latitude;
    private Double longitude;

    @Generated(hash = 939985975)
    public CityAirNowStation(Long id, Long cityID, Double aqi,
                             String majorPollutants, String airQuality, Double pm10, Double pm25,
                             Double no2, Double so2, Double co, Double o3, Date pubTime,
                             String airStation, String airStationID, Double latitude,
                             Double longitude) {
        this.id = id;
        this.cityID = cityID;
        this.aqi = aqi;
        this.majorPollutants = majorPollutants;
        this.airQuality = airQuality;
        this.pm10 = pm10;
        this.pm25 = pm25;
        this.no2 = no2;
        this.so2 = so2;
        this.co = co;
        this.o3 = o3;
        this.pubTime = pubTime;
        this.airStation = airStation;
        this.airStationID = airStationID;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Generated(hash = 1364535315)
    public CityAirNowStation() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCityID() {
        return this.cityID;
    }

    public void setCityID(Long cityID) {
        this.cityID = cityID;
    }

    public Double getAqi() {
        return this.aqi;
    }

    public void setAqi(Double aqi) {
        this.aqi = aqi;
    }

    public String getMajorPollutants() {
        return this.majorPollutants;
    }

    public void setMajorPollutants(String majorPollutants) {
        this.majorPollutants = majorPollutants;
    }

    public String getAirQuality() {
        return this.airQuality;
    }

    public void setAirQuality(String airQuality) {
        this.airQuality = airQuality;
    }

    public Double getPm10() {
        return this.pm10;
    }

    public void setPm10(Double pm10) {
        this.pm10 = pm10;
    }

    public Double getPm25() {
        return this.pm25;
    }

    public void setPm25(Double pm25) {
        this.pm25 = pm25;
    }

    public Double getNo2() {
        return this.no2;
    }

    public void setNo2(Double no2) {
        this.no2 = no2;
    }

    public Double getSo2() {
        return this.so2;
    }

    public void setSo2(Double so2) {
        this.so2 = so2;
    }

    public Double getCo() {
        return this.co;
    }

    public void setCo(Double co) {
        this.co = co;
    }

    public Double getO3() {
        return this.o3;
    }

    public void setO3(Double o3) {
        this.o3 = o3;
    }

    public Date getPubTime() {
        return this.pubTime;
    }

    public void setPubTime(Date pubTime) {
        this.pubTime = pubTime;
    }

    public String getAirStation() {
        return this.airStation;
    }

    public void setAirStation(String airStation) {
        this.airStation = airStation;
    }

    public String getAirStationID() {
        return this.airStationID;
    }

    public void setAirStationID(String airStationID) {
        this.airStationID = airStationID;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
