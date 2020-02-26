package com.example.entity;

import java.util.List;

/**
 * @author PG
 */
public class RecipeClient {

    private long number;

    private String kind;


    private String recipeName;
    /**
     * 图片的字节流形式
     */
    private byte[] pic;

    /**
     * 主食材
     */
    private String mainIngredient;

    /**
     * 辅食材
     */
    private String accessories;

    /**
     * 制作方法
     */
    private List<RecipeMeasureItem> measure;
    private int viewCount;
    private int collectNum;

    //是否收藏
    private boolean isCollected;
    //是否关注
    private boolean isConcerned;
    private byte[] authorImage;
    private String author;
    private Long authorAccount;

    public RecipeClient() {
    }

    public RecipeClient(long number, String kind, String recipeName, byte[] pic, String mainIngredient, String accessories, List<RecipeMeasureItem> measure, int viewCount, int collectNum, String author) {
        this.number = number;
        this.kind = kind;
        this.recipeName = recipeName;
        this.pic = pic;
        this.mainIngredient = mainIngredient;
        this.accessories = accessories;
        this.measure = measure;
        this.viewCount = viewCount;
        this.collectNum = collectNum;
        this.author = author;
    }

    public RecipeClient(long number, String kind, String recipeName, byte[] pic, String mainIngredient, String accessories, List<RecipeMeasureItem> measure, int viewCount, int collectNum, boolean isCollected, boolean isConcerned, byte[] authorImage, String author, Long authorAccount) {
        this.number = number;
        this.kind = kind;
        this.recipeName = recipeName;
        this.pic = pic;
        this.mainIngredient = mainIngredient;
        this.accessories = accessories;
        this.measure = measure;
        this.viewCount = viewCount;
        this.collectNum = collectNum;
        this.isCollected = isCollected;
        this.isConcerned = isConcerned;
        this.authorImage = authorImage;
        this.author = author;
        this.authorAccount = authorAccount;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public String getMainIngredient() {
        return mainIngredient;
    }

    public void setMainIngredient(String mainIngredient) {
        this.mainIngredient = mainIngredient;
    }

    public String getAccessories() {
        return accessories;
    }

    public void setAccessories(String accessories) {
        this.accessories = accessories;
    }

    public List<RecipeMeasureItem> getMeasure() {
        return measure;
    }

    public void setMeasure(List<RecipeMeasureItem> measure) {
        this.measure = measure;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(int collectNum) {
        this.collectNum = collectNum;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public boolean isConcerned() {
        return isConcerned;
    }

    public void setConcerned(boolean concerned) {
        isConcerned = concerned;
    }

    public byte[] getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(byte[] authorImage) {
        this.authorImage = authorImage;
    }

    public Long getAuthorAccount() {
        return authorAccount;
    }

    public void setAuthorAccount(Long authorAccount) {
        this.authorAccount = authorAccount;
    }
}
