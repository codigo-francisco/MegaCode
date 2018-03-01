package com.udacity.gamedev.gigagal.android;

/**
 * Created by Francisco on 28/02/2018.
 */

public interface CustomCallback<T> {
    void processResponse(T response);
}
