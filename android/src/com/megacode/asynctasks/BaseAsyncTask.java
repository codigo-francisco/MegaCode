package com.megacode.asynctasks;

import android.os.AsyncTask;

import com.megacode.dao.IDao;

public abstract class BaseAsyncTask<T> extends AsyncTask<T, Void, Void> {

    protected IDao<T> dao;

    public BaseAsyncTask(IDao<T> dao){
        this.dao = dao;
    }
}
