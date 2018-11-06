package com.megacode.models.database.converters;

import com.megacode.adapters.model.enumators.TypeLevel;

import androidx.room.TypeConverter;

public class TypeLevelConverter {

    @TypeConverter
    public TypeLevel integerToTypeLevel(int typeLevel){
        return TypeLevel.values()[typeLevel-1];
    }

    @TypeConverter
    public int typeLevelToInteger(TypeLevel typeLevel){
        return typeLevel.ordinal();
    }

}
