package com.megacode.models.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Moshi;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by Francisco on 10/08/2018.
 */

/**
 * Clase que representa a la persona que realiza la sesión en el sistema
 */
@Entity(tableName = "Usuario")
public class Usuario implements Parcelable {

    private int edad;
    @PrimaryKey
    private long id;
    private int si;
    private int para;
    private int mientras;
    private int variables;
    @NonNull
    public String nombre;
    @NonNull
    private String sexo;
    @NonNull
    public String email;
    @NonNull
    public String contrasena;
    private String token;
    private String fotoPerfil;

    public Usuario(){

    }

    @Ignore
    public Usuario(long id, int edad, int si, int para, int mientras, int variables,@NonNull String nombre,@NonNull String sexo,@NonNull String email,@NonNull String contrasena){
        this.id = id;
        this.edad = edad;
        this.si = si;
        this.para = para;
        this.mientras = mientras;
        this.variables = variables;
        this.nombre = nombre;
        this.sexo = sexo;
        this.email = email;
        this.contrasena = contrasena;
    }

    @Ignore
    public Usuario(long id, int edad, int si, int para, int mientras, int variables, String nombre, String sexo, String email, String contrasena, String fotoPerfil){
        this(id, edad, si, para, mientras, variables, nombre, sexo, email, contrasena);

        this.fotoPerfil = fotoPerfil;
    }

    public Integer getEdad() {
        return edad;
    }
    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSexo() {
        return sexo;
    }
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public int getSi() {
        return si;
    }
    public void setSi(int si) {
        this.si = si;
    }

    public int getPara() {
        return para;
    }
    public void setPara(int para) {
        this.para = para;
    }

    public int getMientras() {
        return mientras;
    }
    public void setMientras(int mientras) {
        this.mientras = mientras;
    }

    public int getVariables() {
        return variables;
    }
    public void setVariables(int variables) {
        this.variables = variables;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getToken(){
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.edad);
        dest.writeLong(this.id);
        dest.writeInt(this.si);
        dest.writeInt(this.para);
        dest.writeInt(this.mientras);
        dest.writeInt(this.variables);
        dest.writeString(this.nombre);
        dest.writeString(this.sexo);
        dest.writeString(this.email);
        dest.writeString(this.contrasena);
        dest.writeString(this.fotoPerfil);
    }

    protected Usuario(Parcel in) {
        this.edad = in.readInt();
        this.id = in.readLong();
        this.si = in.readInt();
        this.para = in.readInt();
        this.mientras = in.readInt();
        this.variables = in.readInt();
        this.nombre = in.readString();
        this.sexo = in.readString();
        this.setEmail(in.readString());
        this.setContrasena(in.readString());
        this.fotoPerfil = in.readString();
    }

    public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel source) {
            return new Usuario(source);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };
}
