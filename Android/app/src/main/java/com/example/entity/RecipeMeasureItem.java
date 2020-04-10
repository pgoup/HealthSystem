package com.example.entity;

/**
 * @author PG
 */

public class RecipeMeasureItem {
    private long  recipeNum;
    private int num;
    private String measure;
    private byte[] pic;

    public RecipeMeasureItem() {
    }

    public RecipeMeasureItem(long recipeNum, int num, String measure, byte[] pic) {
        this.recipeNum = recipeNum;
        this.num = num;
        this.measure = measure;
        this.pic = pic;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public long getRecipeNum() {
        return recipeNum;
    }

    public void setRecipeNum(long recipeNum) {
        this.recipeNum = recipeNum;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
