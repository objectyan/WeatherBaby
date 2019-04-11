package me.objectyan.weatherbaby.entities.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CityDailyForecast {
    @Id(autoincrement = true)
    private Long id;
    private Long cityID;
    private String date;
    private String sunRise;
    private String sunSet;
    private String monthlyRise;
    private String monthlySet;
    private Double maximumTemperature;
    private Double minimumTemperature;
    private String condCodeDay;
    private String condCodeEvening;
    private String condTxtDay;
    private String condTxtEvening;
    private Double windDirectionAngle;
    private String windDirection;
    private String windPower;
    private Double windSpeed;
    private Double relativeHumidity;
    private Double precipitation;
    private Double probability;
    private Double pressure;
    private Double ultravioletIntensity;
    private Double visibility;

    @Generated(hash = 2078776974)
    public CityDailyForecast(Long id, Long cityID, String date, String sunRise,
                             String sunSet, String monthlyRise, String monthlySet,
                             Double maximumTemperature, Double minimumTemperature,
                             String condCodeDay, String condCodeEvening, String condTxtDay,
                             String condTxtEvening, Double windDirectionAngle, String windDirection,
                             String windPower, Double windSpeed, Double relativeHumidity,
                             Double precipitation, Double probability, Double pressure,
                             Double ultravioletIntensity, Double visibility) {
        this.id = id;
        this.cityID = cityID;
        this.date = date;
        this.sunRise = sunRise;
        this.sunSet = sunSet;
        this.monthlyRise = monthlyRise;
        this.monthlySet = monthlySet;
        this.maximumTemperature = maximumTemperature;
        this.minimumTemperature = minimumTemperature;
        this.condCodeDay = condCodeDay;
        this.condCodeEvening = condCodeEvening;
        this.condTxtDay = condTxtDay;
        this.condTxtEvening = condTxtEvening;
        this.windDirectionAngle = windDirectionAngle;
        this.windDirection = windDirection;
        this.windPower = windPower;
        this.windSpeed = windSpeed;
        this.relativeHumidity = relativeHumidity;
        this.precipitation = precipitation;
        this.probability = probability;
        this.pressure = pressure;
        this.ultravioletIntensity = ultravioletIntensity;
        this.visibility = visibility;
    }

    @Generated(hash = 2031457849)
    public CityDailyForecast() {
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

    public Double getMaximumTemperature() {
        return this.maximumTemperature;
    }

    public void setMaximumTemperature(Double maximumTemperature) {
        this.maximumTemperature = maximumTemperature;
    }

    public Double getMinimumTemperature() {
        return this.minimumTemperature;
    }

    public void setMinimumTemperature(Double minimumTemperature) {
        this.minimumTemperature = minimumTemperature;
    }

    public String getCondCodeDay() {
        return this.condCodeDay;
    }

    public void setCondCodeDay(String condCodeDay) {
        this.condCodeDay = condCodeDay;
    }

    public String getCondCodeEvening() {
        return this.condCodeEvening;
    }

    public void setCondCodeEvening(String condCodeEvening) {
        this.condCodeEvening = condCodeEvening;
    }

    public String getCondTxtDay() {
        return this.condTxtDay;
    }

    public void setCondTxtDay(String condTxtDay) {
        this.condTxtDay = condTxtDay;
    }

    public String getCondTxtEvening() {
        return this.condTxtEvening;
    }

    public void setCondTxtEvening(String condTxtEvening) {
        this.condTxtEvening = condTxtEvening;
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

    public Double getPrecipitation() {
        return this.precipitation;
    }

    public void setPrecipitation(Double precipitation) {
        this.precipitation = precipitation;
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

    public Double getUltravioletIntensity() {
        return this.ultravioletIntensity;
    }

    public void setUltravioletIntensity(Double ultravioletIntensity) {
        this.ultravioletIntensity = ultravioletIntensity;
    }

    public Double getVisibility() {
        return this.visibility;
    }

    public void setVisibility(Double visibility) {
        this.visibility = visibility;
    }
}
