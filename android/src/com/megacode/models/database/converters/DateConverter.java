package com.megacode.models.database.converters;

import java.util.Calendar;
import java.util.Date;

import androidx.room.TypeConverter;

public class DateConverter {

    @TypeConverter
    public Long dateToLong(Date date){
        if (date == null) {
            return null;
        }
        return date.getTime();
    }

    @TypeConverter
    public Date longToDate(Long date){
        return new Date(date);
    }
}
