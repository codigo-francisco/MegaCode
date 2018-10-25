package com.megacode.asynctasks;

import android.os.AsyncTask;

import com.megacode.dao.IDao;

public class DeleteAsyncTask<T> extends BaseAsyncTask<T> {

    public DeleteAsyncTask(IDao<T> dao){
        super(dao);
    }

    @Override
    protected Void doInBackground(T... elements) {
        dao.borrarTodos();
        return null;
    }
}
