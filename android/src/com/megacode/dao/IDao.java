package com.megacode.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

public interface IDao<T> {

    @Insert
    void insert(T element);
    @Update
    void update(T element);
    @Delete
    void delete(T element);

    void borrarTodos();
}
