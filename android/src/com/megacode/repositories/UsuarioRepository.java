package com.megacode.repositories;

import android.app.Application;

import com.megacode.asynctasks.DeleteAsyncTask;
import com.megacode.asynctasks.InsertAsyncTask;
import com.megacode.asynctasks.UpdateAsyncTask;
import com.megacode.dao.UsuarioDao;
import com.megacode.databases.DataBaseMegaCode;
import com.megacode.models.database.Usuario;

import androidx.lifecycle.LiveData;

public class UsuarioRepository {

    private UsuarioDao usuarioDao;
    private LiveData<Usuario> usuario;

    public UsuarioRepository(Application application){
        DataBaseMegaCode db = DataBaseMegaCode.getDataBaseMegaCode(application);
        usuarioDao = db.usuarioDao();
        usuario = usuarioDao.obtenerUsuario();
    }

    public void insert(Usuario usuario){
        new InsertAsyncTask<>(usuarioDao).execute(usuario);
    }

    public void borrarTodos(){
        new DeleteAsyncTask<>(usuarioDao).execute();
    }

    public void update(Usuario usuario){
        new UpdateAsyncTask<>(usuarioDao).execute();
    }

    public LiveData<Usuario> obtenerUsuario() {
        return usuario;
    }
}
