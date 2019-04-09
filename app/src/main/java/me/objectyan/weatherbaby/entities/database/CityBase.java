package me.objectyan.weatherbaby.entities.database;

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

    @Generated(hash = 1562302126)
    public CityBase(Long id, String location, String cid, Double latitude, Double longitude,
                    String parentCity, String adminArea, String country, Float timeZone,
                    @NotNull Boolean isDefault, Double sendibleTemperature, Double temperature, Integer cloud,
                    @NotNull Integer sort, @NotNull Boolean isLocation, Date updateTime) {
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
}
