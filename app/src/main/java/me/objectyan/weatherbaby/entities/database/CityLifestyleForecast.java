package me.objectyan.weatherbaby.entities.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CityLifestyleForecast {
    @Id(autoincrement = true)
    private Long id;
    private Long CityID;
    private String date;
    private String briefIntroduction;
    private String description;
    private String type;

    @Generated(hash = 471381911)
    public CityLifestyleForecast(Long id, Long CityID, String date,
                                 String briefIntroduction, String description, String type) {
        this.id = id;
        this.CityID = CityID;
        this.date = date;
        this.briefIntroduction = briefIntroduction;
        this.description = description;
        this.type = type;
    }

    @Generated(hash = 190847302)
    public CityLifestyleForecast() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCityID() {
        return this.CityID;
    }

    public void setCityID(Long CityID) {
        this.CityID = CityID;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBriefIntroduction() {
        return this.briefIntroduction;
    }

    public void setBriefIntroduction(String briefIntroduction) {
        this.briefIntroduction = briefIntroduction;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
