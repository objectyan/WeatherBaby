package me.objectyan.weatherbaby.entities.heweather;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class UpdateEntity implements Serializable {
    /**
     * 当地时间，24小时制，格式yyyy-MM-dd HH:mm
     */
    @SerializedName("loc")
    public String localTime;

    /**
     * UTC时间，24小时制，格式yyyy-MM-dd HH:mm
     */
    @SerializedName("utc")
    public String utcTime;

    /**
     * 获取当地时间
     *
     * @return
     */
    public Date getLocalTime() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");//小写的mm表示的是分钟
        return sdf.parse(localTime);
    }

    /**
     * 获取UTC时间
     *
     * @return
     */
    public Date getUtcTime() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");//小写的mm表示的是分钟
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.parse(utcTime);
    }
}
