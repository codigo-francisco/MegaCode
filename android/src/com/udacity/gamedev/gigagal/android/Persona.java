package com.udacity.gamedev.gigagal.android;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Francisco on 10/08/2018.
 */

class Persona implements Parcelable {
    private int age;
    private String name;
    private String sex;

    public Persona(int age, String name, String sex){
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.age);
        dest.writeString(this.name);
        dest.writeString(this.sex);
    }

    protected Persona(Parcel in) {
        this.age = in.readInt();
        this.name = in.readString();
        this.sex = in.readString();
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
}
