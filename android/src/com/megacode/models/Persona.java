package com.megacode.models;

import android.app.Person;
import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Moshi;

import java.io.IOException;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
/**
 * Created by Francisco on 10/08/2018.
 */

/**
 * Clase que representa a la persona que realiza la sesión en el sistema
 */
public class Persona extends RealmObject implements Parcelable {
    private int edad;
    @PrimaryKey
    private long id;
    private int si;
    private int para;
    private int mientras;
    private int variables;
    public String nombre;
    private String sexo;
    public String email;
    public String contrasena;
    private String token;
    private String fotoPerfil;

    /**
     * Construye una persona vacia (ideal para Realm)
     */
    public Persona(){}

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

    protected Persona(Parcel in) {
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

    public static final Parcelable.Creator<Persona> CREATOR = new Parcelable.Creator<Persona>() {
        @Override
        public Persona createFromParcel(Parcel source) {
            return new Persona(source);
        }

        @Override
        public Persona[] newArray(int size) {
            return new Persona[size];
        }
    };

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

    private static Moshi moshi = new Moshi.Builder().build();

    public String toJson(){
        return moshi.adapter(Persona.class).toJson(this);
    }

    public static Persona buildPersonaFromJson(String json) throws IOException {
        return moshi.adapter(Persona.class).fromJson(json);
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public Persona buildPersonaObj(){
        Persona persona = new Persona();

        persona.setId(getId());
        persona.setNombre(getNombre());
        persona.setEdad(getEdad());
        persona.setSexo(getSexo());
        persona.setEmail(getEmail());
        persona.setContrasena(getContrasena());
        persona.setMientras(getMientras());
        persona.setNombre(getNombre());
        persona.setVariables(getVariables());
        persona.setSi(getSi());
        persona.setPara(getPara());
        persona.setMientras(getMientras());
        persona.setToken(getToken());

        persona.setFotoPerfil(getFotoPerfil());

        return persona;
    }
}
