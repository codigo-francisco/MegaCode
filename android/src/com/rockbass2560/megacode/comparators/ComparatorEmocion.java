package com.rockbass2560.megacode.comparators;

import com.rockbass2560.megacode.models.database.Emocion;

import java.util.Comparator;

public interface ComparatorEmocion extends Comparator<Emocion> {

    @Override
    default int compare(Emocion emocion1, Emocion emocion2) {
        return emocion1.label.compareTo(emocion2.label);
    }
}
