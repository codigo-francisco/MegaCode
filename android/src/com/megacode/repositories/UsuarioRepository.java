package com.megacode.repositories;

import android.app.Application;

import com.megacode.asynctasks.DeleteAsyncTask;
import com.megacode.asynctasks.InsertAsyncTask;
import com.megacode.asynctasks.UpdateAsyncTask;
import com.megacode.dao.UsuarioDao;
import com.megacode.databases.DataBaseMegaCode;
import com.megacode.models.database.Usuario;

import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;

public class UsuarioRepository {

    private UsuarioDao usuarioDao;

    public UsuarioRepository(Application application){
        DataBaseMegaCode db = DataBaseMegaCode.getDataBaseMegaCode(application);
        usuarioDao = db.usuarioDao();
    }

    public boolean hasUsuario(){
        return usuarioDao.cantidadUsuario()>0;
    }

    public void insert(Usuario usuario){
        Executors.newSingleThreadExecutor().execute(() -> usuarioDao.insert(usuario));
    }

    public void borrarTodos(){
        usuarioDao.borrarTodos();
    }

    public Usuario obtenerUsuarioSync(){
        return usuarioDao.obtenerUsuarioSync();
    }

    public LiveData<Usuario> obtenerUsuario() {
        return usuarioDao.obtenerUsuario();
    }

    public int cantidadUsuario(){
        return usuarioDao.cantidadUsuario();
    }

    public void update(Usuario usuario){
        Executors.newSingleThreadExecutor().execute(()-> usuarioDao.update(usuario));
    }
}
