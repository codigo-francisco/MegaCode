package com.megacode.asynctasks;

import com.megacode.dao.IDao;

public class UpdateAsyncTask<T> extends BaseAsyncTask<T> {

    public UpdateAsyncTask(IDao<T> dao) {
        super(dao);
    }

    @Override
    protected Void doInBackground(T... elements) {
        dao.update(elements[0]);
        return null;
    }
}
