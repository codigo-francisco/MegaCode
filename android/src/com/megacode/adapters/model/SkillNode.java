package com.megacode.adapters.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.megacode.models.TypeLevel;
import com.megacode.models.response.NivelesResponse;
import com.megacode.screens.R;

import java.util.Arrays;
import java.util.Collection;

public class SkillNode implements Parcelable {
    private int ID;
    private int imageResource;
    private String levelPath;
    private TypeLevel typeLevel;
    private String nombreNivel;
    private int para;
    private int comando;
    private int si;
    private int mientras;

    public int getPara() {
        return para;
    }

    public void setPara(int para) {
        this.para = para;
    }

    public String getNombreNivel() {
        return nombreNivel;
    }

    public void setNombreNivel(String nombreNivel) {
        this.nombreNivel = nombreNivel;
    }

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

    public SkillNode() {
    }

    private void chooseImageResource(){
        switch (typeLevel){
            case COMANDO:
                imageResource = R.drawable.ic_c;
                break;
            case SI:
                imageResource = R.drawable.ic_s;
                break;
            case PARA:
                imageResource = R.drawable.ic_p;
                break;
            case MIENTRAS:
                imageResource=R.drawable.ic_m;
                break;
            default:
                imageResource = R.drawable.megacode;
        }
    }

    public SkillNode(String levelPath, TypeLevel typeLevel){
        this.levelPath = levelPath;
        this.typeLevel = typeLevel;
        chooseImageResource();
    }

    public SkillNode(NivelesResponse nivelResponse){
        ID = nivelResponse.getId();
        levelPath = nivelResponse.getRuta();
        typeLevel = TypeLevel.values()[nivelResponse.getTipoNivel()-1];
        comando = nivelResponse.getVariables();
        si = nivelResponse.getSi();
        para = nivelResponse.getPara();
        mientras = nivelResponse.getMientras();
        nombreNivel = nivelResponse.getNombre();
        //Seleccionar imagen
        chooseImageResource();

    }

    public SkillNode(int imageResource, String levelPath, TypeLevel typeLevel){
        this.imageResource = imageResource;
        this.levelPath = levelPath;
        this.typeLevel = typeLevel;
    }

    public static Collection<SkillNode> buildNodes(SkillNode ...nodes){
        int index=1;

        //Agregar indices
        for (SkillNode node: nodes){
            node.setID(index);
        }

        return Arrays.asList(nodes);
    };

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
        dest.writeString(this.nombreNivel);
        dest.writeInt(this.getComando());
        dest.writeInt(this.getSi());
        dest.writeInt(this.para);
        dest.writeInt(this.getMientras());
    }

    protected SkillNode(Parcel in) {
        this.ID = in.readInt();
        this.imageResource = in.readInt();
        this.levelPath = in.readString();
        int tmpTypeLevel = in.readInt();
        this.typeLevel = tmpTypeLevel == -1 ? null : TypeLevel.values()[tmpTypeLevel];
        this.nombreNivel = in.readString();
        this.setComando(in.readInt());
        this.setSi(in.readInt());
        this.para = in.readInt();
        this.setMientras(in.readInt());
    }

    public static final Creator<SkillNode> CREATOR = new Creator<SkillNode>() {
        @Override
        public SkillNode createFromParcel(Parcel source) {
            return new SkillNode(source);
        }

        @Override
        public SkillNode[] newArray(int size) {
            return new SkillNode[size];
        }
    };

    public int getComando() {
        return comando;
    }

    public void setComando(int comando) {
        this.comando = comando;
    }

    public int getSi() {
        return si;
    }

    public void setSi(int si) {
        this.si = si;
    }

    public int getMientras() {
        return mientras;
    }

    public void setMientras(int mientras) {
        this.mientras = mientras;
    }
}
