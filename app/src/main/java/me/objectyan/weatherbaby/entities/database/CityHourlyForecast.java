package me.objectyan.weatherbaby.entities.database;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Date;

import me.objectyan.weatherbaby.entities.database.converter.DatePropertyConverter;

@Entity
public class CityHourlyForecast {
    @Id(autoincrement = true)
    private Long id;
    private Long cityID;
    private String date;
    private String time;
    @Convert(converter = DatePropertyConverter.class, columnType = Long.class)
    private Date dateTime;
    private Double temperature;
    private String condCode;
    private String condTxt;
    private Double windDirectionAngle;
    private String windDirection;
    private String windPower;
    private Double windSpeed;
    private Double relativeHumidity;
    private Double probability;
    private Double pressure;
    private Double dewPoint;
    private Integer cloud;

    @Generated(hash = 2091677909)
    public CityHourlyForecast(Long id, Long cityID, String date, String time, Date dateTime,
                              Double temperature, String condCode, String condTxt, Double windDirectionAngle,
                              String windDirection, String windPower, Double windSpeed, Double relativeHumidity,
                              Double probability, Double pressure, Double dewPoint, Integer cloud) {
        this.id = id;
        this.cityID = cityID;
        this.date = date;
        this.time = time;
        this.dateTime = dateTime;
        this.temperature = temperature;
        this.condCode = condCode;
        this.condTxt = condTxt;
        this.windDirectionAngle = windDirectionAngle;
        this.windDirection = windDirection;
        this.windPower = windPower;
        this.windSpeed = windSpeed;
        this.relativeHumidity = relativeHumidity;
        this.probability = probability;
        this.pressure = pressure;
        this.dewPoint = dewPoint;
        this.cloud = cloud;
    }

    @Generated(hash = 1711267168)
    public CityHourlyForecast() {
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

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getTemperature() {
        return this.temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
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

    public Double getWindDirectionAngle() {
        return this.windDirectionAngle;
    }

    public void setWindDirectionAngle(Double windDirectionAngle) {
        this.windDirectionAngle = windDirectionAngle;
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

    public Double getRelativeHumidity() {
        return this.relativeHumidity;
    }

    public void setRelativeHumidity(Double relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    public Double getProbability() {
        return this.probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }

    public Double getPressure() {
        return this.pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getDewPoint() {
        return this.dewPoint;
    }

    public void setDewPoint(Double dewPoint) {
        this.dewPoint = dewPoint;
    }

    public Integer getCloud() {
        return this.cloud;
    }

    public void setCloud(Integer cloud) {
        this.cloud = cloud;
    }

    public Date getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
