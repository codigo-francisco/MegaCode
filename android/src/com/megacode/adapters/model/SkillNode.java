package com.megacode.adapters.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.megacode.models.TypeLevel;

public class SkillNode implements Parcelable {
    private int ID;
    private int imageResource;
    private String levelPath;
    private TypeLevel typeLevel;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getLevelPath() {
        return levelPath;
    }

    public void setLevelPath(String levelPath) {
        this.levelPath = levelPath;
    }

    public TypeLevel getTypeLevel() {
        return typeLevel;
    }

    public void setTypeLevel(TypeLevel typeLevel) {
        this.typeLevel = typeLevel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ID);
        dest.writeInt(this.imageResource);
        dest.writeString(this.levelPath);
        dest.writeInt(this.typeLevel == null ? -1 : this.typeLevel.ordinal());
    }

    public SkillNode() {
    }

    protected SkillNode(Parcel in) {
        this.ID = in.readInt();
        this.imageResource = in.readInt();
        this.levelPath = in.readString();
        int tmpTypeLevel = in.readInt();
        this.typeLevel = tmpTypeLevel == -1 ? null : TypeLevel.values()[tmpTypeLevel];
    }

    public static final Parcelable.Creator<SkillNode> CREATOR = new Parcelable.Creator<SkillNode>() {
        @Override
        public SkillNode createFromParcel(Parcel source) {
            return new SkillNode(source);
        }

        @Override
        public SkillNode[] newArray(int size) {
            return new SkillNode[size];
        }
    };
}
