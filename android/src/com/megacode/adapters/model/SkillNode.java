package com.megacode.adapters.model;

import com.megacode.models.TypeLevel;

public class SkillNode {
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
}
