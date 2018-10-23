package com.megacode.adapters.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.megacode.models.TypeFeed;

public class DataModel implements Parcelable {

    private int imagen;
    private String title;
    private String content;
    private int _id;

    private TypeFeed typeFeed;

    public TypeFeed getTypeFeed() {
        return typeFeed;
    }

    public void setTypeFeed(TypeFeed typeFeed) {
        this.typeFeed = typeFeed;
    }

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

    public DataModel() {
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
        dest.writeInt(this.typeFeed == null ? -1 : this.typeFeed.ordinal());
    }

    protected DataModel(Parcel in) {
        this.imagen = in.readInt();
        this.title = in.readString();
        this.content = in.readString();
        this._id = in.readInt();
        int tmpTypeFeed = in.readInt();
        this.typeFeed = tmpTypeFeed == -1 ? null : TypeFeed.values()[tmpTypeFeed];
    }

    public static final Creator<DataModel> CREATOR = new Creator<DataModel>() {
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
