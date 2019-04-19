package me.objectyan.weatherbaby.entities.database.converter;

import android.icu.text.SimpleDateFormat;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Date;

public class DatePropertyConverter implements PropertyConverter<Date, Long> {
    @Override
    public Date convertToEntityProperty(Long databaseValue) {
        if (databaseValue == null || databaseValue == 0) return null;
        return new Date(databaseValue);
    }

    @Override
    public Long convertToDatabaseValue(Date entityProperty) {
        if (entityProperty == null) return null;
        return entityProperty.getTime();
    }
}
