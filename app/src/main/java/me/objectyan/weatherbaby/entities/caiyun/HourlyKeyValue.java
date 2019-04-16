package me.objectyan.weatherbaby.entities.caiyun;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HourlyKeyValue<T> {
    @SerializedName("value")
    public T value;
    @SerializedName("datetime")
    public String datetime;

    public Date getLocalTime() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");//小写的mm表示的是分钟
        return sdf.parse(datetime);
    }
}
