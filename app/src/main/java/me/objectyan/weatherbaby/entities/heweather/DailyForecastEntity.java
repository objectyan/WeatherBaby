package me.objectyan.weatherbaby.entities.heweather;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DailyForecastEntity extends BaseWeatherEntity implements Serializable {
    /**
     * 日出时间
     */
    @SerializedName("sr")
    public String sunRiseTime;

    /**
     * 日落时间
     */
    @SerializedName("ss")
    public String sunSetTime;

    /**
     * 月升时间
     */
    @SerializedName("mr")
    public String monthlyRiseTime;

    /**
     * 月落时间
     */
    @SerializedName("ms")
    public String monthlySetTime;

    /**
     * 白天天气状况代码
     */
    @SerializedName("cond_code_d")
    public String condCodeDay;

    /**
     * 晚间天气状况代码
     */
    @SerializedName("cond_code_n")
    public String condCodeNight;

    /**
     * 白天天气状况描述
     */
    @SerializedName("cond_txt_d")
    public String condTxtDay;

    /**
     * 晚间天气状况描述
     */
    @SerializedName("cond_txt_n")
    public String condTxtNight;

    /**
     * 预报日期
     */
    @SerializedName("date")
    public String date;

    public Date getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);//小写的mm表示的是分钟
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 最高温度
     */
    @SerializedName("tmp_max")
    public Double temperatureMax;

    /**
     * 最低温度
     */
    @SerializedName("tmp_min")
    public Double temperatureMin;

    /**
     * 紫外线强度指数
     */
    @SerializedName("uv_index")
    public Double ultravioletIndex;

    /**
     * 能见度，默认单位：公里
     */
    @SerializedName("vis")
    public Double visibility;

    /**
     * 降水量
     */
    @SerializedName("pcpn")
    public Double precipitation;

    /**
     * 降水概率，百分比
     */
    @SerializedName("pop")
    public Double precipitationProbability;
}
