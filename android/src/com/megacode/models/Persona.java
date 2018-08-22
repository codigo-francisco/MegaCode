package com.megacode.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Francisco on 10/08/2018.
 */

/**
 * Clase que representa a la persona que realiza la sesión en el sistema
 */
@IgnoreExtraProperties
public class Persona implements Parcelable {
    public int age;
    private String id;
    public int si;
    public int para;
    public int mientras;
    public int variables;
    public String name;
    public String sex;
    public String email;
    public String contrasena;
    private String token;

    public Persona(){

    }

    /**
     *
     * @param id ID generado en firebase (es Unique ID)
     * @param age Edad de la persona
     * @param name Nombre Completo de la persona
     * @param sex Sexo de la persona
     */
    public Persona(String id, int age, String name, String sex){
        this.id = id;
        this.age = age;
        this.name = name;
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        dest.writeInt(this.age);
        dest.writeString(this.id);
        dest.writeInt(this.si);
        dest.writeInt(this.para);
        dest.writeInt(this.mientras);
        dest.writeInt(this.variables);
        dest.writeString(this.name);
        dest.writeString(this.sex);
        dest.writeString(this.getEmail());
        dest.writeString(this.getContrasena());
    }

    protected Persona(Parcel in) {
        this.age = in.readInt();
        this.id = in.readString();
        this.si = in.readInt();
        this.para = in.readInt();
        this.mientras = in.readInt();
        this.variables = in.readInt();
        this.name = in.readString();
        this.sex = in.readString();
        this.setEmail(in.readString());
        this.setContrasena(in.readString());
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
}
