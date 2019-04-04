package me.objectyan.weatherbaby.entities;

import java.util.List;

public class CityInfo {
    private int id;

    private int type = 0;

    private String cityName;

    private String latitude;

    private String longitude;

    private String spell;

    private String tempHigh;

    private String tempLow;

    private String weatherType;

    private String weatherTypeDay;

    private String weatherTypeNight;

    private String weatherCode;

    private List<String> fullPath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type |= type;
    }

    public void removeType(int type) {
        this.type &= ~type;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTempHigh() {
        return tempHigh;
    }

    public void setTempHigh(String tempHigh) {
        this.tempHigh = tempHigh;
    }

    public String getTempLow() {
        return tempLow;
    }

    public void setTempLow(String tempLow) {
        this.tempLow = tempLow;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public String getWeatherTypeDay() {
        return weatherTypeDay;
    }

    public void setWeatherTypeDay(String weatherTypeDay) {
        this.weatherTypeDay = weatherTypeDay;
    }

    public String getWeatherTypeNight() {
        return weatherTypeNight;
    }

    public void setWeatherTypeNight(String weatherTypeNight) {
        this.weatherTypeNight = weatherTypeNight;
    }

    public String getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(String weatherCode) {
        this.weatherCode = weatherCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public List<String> getFullPath() {
        return fullPath;
    }

    public void setFullPath(List<String> fullPath) {
        this.fullPath = fullPath;
    }

    public CityInfo() {
    }

    public CityInfo(String cityName) {
        this.cityName = cityName;
    }

    public CityInfo(int type) {
        this.type = type;
    }

    public enum CityType {
        /**
         * 默认
         */
        None(1 << 0),
        /**
         * 定位
         */
        Location(1 << 1),
        /**
         * 默认
         */
        Default(1 << 2),
        /**
         * 添加
         */
        Add(1 << 3);

        private final int key;

        private CityType(int key) {
            this.key = key;
        }

        public static CityType getEnumByKey(int key) {
            for (CityType temp : CityType.values()) {
                if (temp.getKey() == key) {
                    return temp;
                }
            }
            return null;
        }

        public int getKey() {
            return key;
        }
    }
}
