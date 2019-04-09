package me.objectyan.weatherbaby.entities.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

@Entity(indexes = {
        @Index(value = "isDefault DESC, sort DESC")
})
public class CityBase {
    @Id(autoincrement = true)
    private Long id;
    private String location;
    private String cid;
    private Double lat;
    private Double lon;
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

    @Generated(hash = 156737760)
    public CityBase(Long id, String location, String cid, Double lat, Double lon, String parentCity,
                    String adminArea, String country, Float timeZone, @NotNull Boolean isDefault,
                    Double sendibleTemperature, Double temperature, Integer cloud, @NotNull Integer sort) {
        this.id = id;
        this.location = location;
        this.cid = cid;
        this.lat = lat;
        this.lon = lon;
        this.parentCity = parentCity;
        this.adminArea = adminArea;
        this.country = country;
        this.timeZone = timeZone;
        this.isDefault = isDefault;
        this.sendibleTemperature = sendibleTemperature;
        this.temperature = temperature;
        this.cloud = cloud;
        this.sort = sort;
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

    public Double getLat() {
        return this.lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return this.lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
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

    public float getTimeZone() {
        return this.timeZone;
    }

    public void setTimeZone(float timeZone) {
        this.timeZone = timeZone;
    }

    public boolean getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(boolean isDefault) {
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

    public int getCloud() {
        return this.cloud;
    }

    public void setCloud(int cloud) {
        this.cloud = cloud;
    }

    public int getSort() {
        return this.sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setTimeZone(Float timeZone) {
        this.timeZone = timeZone;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public void setCloud(Integer cloud) {
        this.cloud = cloud;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
