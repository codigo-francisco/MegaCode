package com.megacode.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.megacode.adapters.model.SkillNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ParcelableLinkedList implements Parcelable {
    public LinkedList<List<SkillNode>> nodes;

    public ParcelableLinkedList(LinkedList<List<SkillNode>> nodes){
        this.nodes=nodes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.nodes);
    }

    protected ParcelableLinkedList(Parcel in) {
        this.nodes = new LinkedList<>(new ArrayList<>());
        in.readList(this.nodes, ArrayList.class.getClassLoader());
    }

    public static final Parcelable.Creator<ParcelableLinkedList> CREATOR = new Parcelable.Creator<ParcelableLinkedList>() {
        @Override
        public ParcelableLinkedList createFromParcel(Parcel source) {
            return new ParcelableLinkedList(source);
        }

        @Override
        public ParcelableLinkedList[] newArray(int size) {
            return new ParcelableLinkedList[size];
        }
    };
}
