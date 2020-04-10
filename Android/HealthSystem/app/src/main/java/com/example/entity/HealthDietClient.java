package com.example.entity;


/**
 * @author PG
 */

public class HealthDietClient {
    /**
     * 饮食类型
     * 有人群膳食、疾病调理、功能性调理、肝脏调理
     */
    private String dietKind;

    /**
     * 健康饮食的对象
     */
    private String healthDietName;

    /**
     * 饮食方式
     */
    private String dietMethod;

    /**
     * 适宜食材
     */
    private String suitable;

    /**
     * 适宜的具体食材
     */
    private String suitableFoods;

    /**
     * 禁忌食材
     */
    private String taboo;

    /**
     * 禁忌的具体食材
     */
    private String tabooFoods;

    public HealthDietClient() {
    }

    public HealthDietClient(String dietKind, String healthDietName, String dietMethod, String suitable, String suitableFoods, String taboo, String tabooFoods) {
        this.dietKind = dietKind;
        this.healthDietName = healthDietName;
        this.dietMethod = dietMethod;
        this.suitable = suitable;
        this.suitableFoods = suitableFoods;
        this.taboo = taboo;
        this.tabooFoods = tabooFoods;
    }

    public String getDietKind() {
        return dietKind;
    }

    public void setDietKind(String dietKind) {
        this.dietKind = dietKind;
    }

    public String getHealthDietName() {
        return healthDietName;
    }

    public void setHealthDietName(String healthDietName) {
        this.healthDietName = healthDietName;
    }

    public String getDietMethod() {
        return dietMethod;
    }

    public void setDietMethod(String dietMethod) {
        this.dietMethod = dietMethod;
    }

    public String getSuitable() {
        return suitable;
    }

    public void setSuitable(String suitable) {
        this.suitable = suitable;
    }

    public String getSuitableFoods() {
        return suitableFoods;
    }

    public void setSuitableFoods(String suitableFoods) {
        this.suitableFoods = suitableFoods;
    }

    public String getTaboo() {
        return taboo;
    }

    public void setTaboo(String taboo) {
        this.taboo = taboo;
    }

    public String getTabooFoods() {
        return tabooFoods;
    }

    public void setTabooFoods(String tabooFoods) {
        this.tabooFoods = tabooFoods;
    }
}
