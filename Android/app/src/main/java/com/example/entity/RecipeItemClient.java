package com.example.entity;

/**
 * @author PG
 */

public class RecipeItemClient {
    private String num;
    private String name;
    private byte[] pic;
    private String recipeKind;
    private int viewCount;
    private int collectCount;

    public RecipeItemClient() {
    }

    public RecipeItemClient(String num, String name, byte[] pic, String recipeKind, int viewCount, int collectCount) {
        this.num = num;
        this.name = name;
        this.pic = pic;
        this.recipeKind = recipeKind;
        this.viewCount = viewCount;
        this.collectCount = collectCount;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public String getRecipeKind() {
        return recipeKind;
    }

    public void setRecipeKind(String recipeKind) {
        this.recipeKind = recipeKind;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }
}
