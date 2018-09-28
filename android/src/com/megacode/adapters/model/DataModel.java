package com.megacode.adapters.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

public class DataModel extends RealmObject implements Parcelable {

    private int imagen;
    private String title;
    private String content;
    private int _id;

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.imagen);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeInt(this._id);
    }

    public DataModel() {
    }

    protected DataModel(Parcel in) {
        this.imagen = in.readInt();
        this.title = in.readString();
        this.content = in.readString();
        this._id = in.readInt();
    }

    public static final Parcelable.Creator<DataModel> CREATOR = new Parcelable.Creator<DataModel>() {
        @Override
        public DataModel createFromParcel(Parcel source) {
            return new DataModel(source);
        }

        @Override
        public DataModel[] newArray(int size) {
            return new DataModel[size];
        }
    };
}
