package me.objectyan.weatherbaby.entities.caiyun;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HourlyKeyValue<T> {
    @SerializedName("value")
    public T value;
    @SerializedName("datetime")
    public String datetime;
    @SerializedName("direction")
    public Double direction;
    @SerializedName("speed")
    public Double speed;

    public Date getLocalTime() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);//小写的mm表示的是分钟
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date shanghaiDate = sdf.parse(datetime);
        return sdf.parse(datetime);
    }

    public String getDate() {
        return datetime.substring(0, 10);
    }

    public String getTime() {
        return datetime.substring(11);
    }

    public String getCondTxt() {
        switch (String.valueOf(value)) {
            case "CLEAR_DAY":
            case "CLEAR_NIGHT":
                return "晴";
            case "PARTLY_CLOUDY_DAY":
            case "PARTLY_CLOUDY_NIGHT":
                return "多云";
            case "CLOUDY":
                return "阴";
            case "WIND":
                return "大风";
            case "HAZE":
                return "雾霾";
            case "RAIN":
                return "雨";
            case "SNOW":
                return "雪";
        }
        return "晴";
    }

    public String getCondCode() {
        switch (String.valueOf(value)) {
            case "CLEAR_DAY":
            case "CLEAR_NIGHT":
                return "100";
            case "PARTLY_CLOUDY_DAY":
            case "PARTLY_CLOUDY_NIGHT":
                return "101";
            case "CLOUDY":
                return "104";
            case "WIND":
                return "207";
            case "HAZE":
                return "501";
            case "RAIN":
                return "399";
            case "SNOW":
                return "499";
        }
        return "100";
    }
}
