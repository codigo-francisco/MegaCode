package com.megacode.models.database.converters;

import java.util.Date;

import androidx.room.TypeConverter;

public class DateConverter {

    @TypeConverter
    public long dateToLong(Date date){
        return date.getTime();
    }

    @TypeConverter
    public Date longToDate(long date){
        return new Date(date);
    }
}
