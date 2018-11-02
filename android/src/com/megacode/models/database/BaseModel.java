package com.megacode.models.database;

import androidx.room.Ignore;

public abstract class BaseModel {

    @Ignore
    protected int errorCode = 0;

    @Ignore
    protected Throwable error;

    @Ignore
    protected String errorMessage;

    public void setError(Throwable t){
        error = t;
    }

    public Throwable getError(){
        return error;
    }

    public boolean hasError() {
        return error!=null || errorCode != 0;
    }

    public int getErrorCode(){
        return errorCode;
    }

    public void setErrorCode(int errorCode){
        this.errorCode = errorCode;
    }

    public String getErrorMessage(){
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage){
        this.errorMessage = errorMessage;
    }
}
