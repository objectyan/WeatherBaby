package me.objectyan.weatherbaby.entities.caiyun;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DailyAvg {
    @SerializedName("date")
    public String date;
    @SerializedName("max")
    public Double max;
    @SerializedName("avg")
    public Double avg;
    @SerializedName("min")
    public Double min;
    @SerializedName("index")
    public Double index;

    public Date getLocalTime() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);//小写的mm表示的是分钟
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date shanghaiDate = sdf.parse(date);
        return sdf.parse(date);
    }
}
