package me.objectyan.weatherbaby.entities.database;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;

@Entity(indexes = {
        @Index(value = "isDefault DESC, sort DESC")
})
public class CityBase {
    @Id(autoincrement = true)
    private Long id;
    private String location;
    private String cid;
    private Double latitude;
    private Double longitude;
    private String parentCity;
    private String adminArea;
    private String country;
    private Float timeZone;
    @NotNull
    private Boolean isDefault;
    private Double sendibleTemperature;
    private Double temperature;
    private Integer cloud;
    @NotNull
    private Integer sort;
    @NotNull
    private Boolean isLocation;
    private Date updateTime;
    private String condCode;
    private String condTxt;
    private Double tempHigh;
    private Double tempLow;
    private String windDirection;
    private String windPower;
    private Double windSpeed;
    private Double humidity;
    private Double pressure;
    private Double precipitation;
    private Double visibility;
    private Double ultravioletIndex;
    private Double precipitationProbability;
    private String sunRise;
    private String sunSet;
    private String monthlyRise;
    private String monthlySet;
    private Double aqi;
    private String majorPollutants;
    private String airQuality;
    private Double pm10;
    private Double pm25;
    private Double no2;
    private Double so2;
    private Double co;
    private Double o3;

    @Generated(hash = 1276293912)
    public CityBase(Long id, String location, String cid, Double latitude,
                    Double longitude, String parentCity, String adminArea, String country,
                    Float timeZone, @NotNull Boolean isDefault, Double sendibleTemperature,
                    Double temperature, Integer cloud, @NotNull Integer sort,
                    @NotNull Boolean isLocation, Date updateTime, String condCode,
                    String condTxt, Double tempHigh, Double tempLow, String windDirection,
                    String windPower, Double windSpeed, Double humidity, Double pressure,
                    Double precipitation, Double visibility, Double ultravioletIndex,
                    Double precipitationProbability, String sunRise, String sunSet,
                    String monthlyRise, String monthlySet, Double aqi,
                    String majorPollutants, String airQuality, Double pm10, Double pm25,
                    Double no2, Double so2, Double co, Double o3) {
        this.id = id;
        this.location = location;
        this.cid = cid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.parentCity = parentCity;
        this.adminArea = adminArea;
        this.country = country;
        this.timeZone = timeZone;
        this.isDefault = isDefault;
        this.sendibleTemperature = sendibleTemperature;
        this.temperature = temperature;
        this.cloud = cloud;
        this.sort = sort;
        this.isLocation = isLocation;
        this.updateTime = updateTime;
        this.condCode = condCode;
        this.condTxt = condTxt;
        this.tempHigh = tempHigh;
        this.tempLow = tempLow;
        this.windDirection = windDirection;
        this.windPower = windPower;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.pressure = pressure;
        this.precipitation = precipitation;
        this.visibility = visibility;
        this.ultravioletIndex = ultravioletIndex;
        this.precipitationProbability = precipitationProbability;
        this.sunRise = sunRise;
        this.sunSet = sunSet;
        this.monthlyRise = monthlyRise;
        this.monthlySet = monthlySet;
        this.aqi = aqi;
        this.majorPollutants = majorPollutants;
        this.airQuality = airQuality;
        this.pm10 = pm10;
        this.pm25 = pm25;
        this.no2 = no2;
        this.so2 = so2;
        this.co = co;
        this.o3 = o3;
    }

    @Generated(hash = 1122040241)
    public CityBase() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCid() {
        return this.cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
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

    public String getParentCity() {
        return this.parentCity;
    }

    public void setParentCity(String parentCity) {
        this.parentCity = parentCity;
    }

    public String getAdminArea() {
        return this.adminArea;
    }

    public void setAdminArea(String adminArea) {
        this.adminArea = adminArea;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Float getTimeZone() {
        return this.timeZone;
    }

    public void setTimeZone(Float timeZone) {
        this.timeZone = timeZone;
    }

    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Double getSendibleTemperature() {
        return this.sendibleTemperature;
    }

    public void setSendibleTemperature(Double sendibleTemperature) {
        this.sendibleTemperature = sendibleTemperature;
    }

    public Double getTemperature() {
        return this.temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getCloud() {
        return this.cloud;
    }

    public void setCloud(Integer cloud) {
        this.cloud = cloud;
    }

    public Integer getSort() {
        return this.sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getIsLocation() {
        return this.isLocation;
    }

    public void setIsLocation(Boolean isLocation) {
        this.isLocation = isLocation;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCondCode() {
        return this.condCode;
    }

    public void setCondCode(String condCode) {
        this.condCode = condCode;
    }

    public String getCondTxt() {
        return this.condTxt;
    }

    public void setCondTxt(String condTxt) {
        this.condTxt = condTxt;
    }

    public Double getTempHigh() {
        return this.tempHigh;
    }

    public void setTempHigh(Double tempHigh) {
        this.tempHigh = tempHigh;
    }

    public Double getTempLow() {
        return this.tempLow;
    }

    public void setTempLow(Double tempLow) {
        this.tempLow = tempLow;
    }

    public String getWindDirection() {
        return this.windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindPower() {
        return this.windPower;
    }

    public void setWindPower(String windPower) {
        this.windPower = windPower;
    }

    public Double getWindSpeed() {
        return this.windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Double getHumidity() {
        return this.humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getPressure() {
        return this.pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getPrecipitation() {
        return this.precipitation;
    }

    public void setPrecipitation(Double precipitation) {
        this.precipitation = precipitation;
    }

    public Double getVisibility() {
        return this.visibility;
    }

    public void setVisibility(Double visibility) {
        this.visibility = visibility;
    }

    public Double getUltravioletIndex() {
        return this.ultravioletIndex;
    }

    public void setUltravioletIndex(Double ultravioletIndex) {
        this.ultravioletIndex = ultravioletIndex;
    }

    public Double getPrecipitationProbability() {
        return this.precipitationProbability;
    }

    public void setPrecipitationProbability(Double precipitationProbability) {
        this.precipitationProbability = precipitationProbability;
    }

    public String getSunRise() {
        return this.sunRise;
    }

    public void setSunRise(String sunRise) {
        this.sunRise = sunRise;
    }

    public String getSunSet() {
        return this.sunSet;
    }

    public void setSunSet(String sunSet) {
        this.sunSet = sunSet;
    }

    public String getMonthlyRise() {
        return this.monthlyRise;
    }

    public void setMonthlyRise(String monthlyRise) {
        this.monthlyRise = monthlyRise;
    }

    public String getMonthlySet() {
        return this.monthlySet;
    }

    public void setMonthlySet(String monthlySet) {
        this.monthlySet = monthlySet;
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
}
