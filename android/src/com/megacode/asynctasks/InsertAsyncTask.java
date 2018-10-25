package com.megacode.asynctasks;

import android.os.AsyncTask;

import com.megacode.dao.IDao;

public class InsertAsyncTask<T> extends BaseAsyncTask<T>{


    public InsertAsyncTask(IDao<T> dao) {
        super(dao);
    }

    @Override
    protected Void doInBackground(T... elements) {
        dao.insert(elements[0]);
        return null;
    }
}
