package com.megacode.adapters.model.enumators;

import java.util.Locale;

public enum TypeLevel {
    COMANDO,
    SI,
    PARA,
    MIENTRAS;

    @Override
    public String toString() {
        return name().substring(0,1).toUpperCase().concat(name().substring(1).toLowerCase());
    }
}
