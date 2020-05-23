package com.example.entity;


/**
 * @author PG
 */

public class HealthDietClient {
    private String dietKind;
    private String healthDietName;
    private String dietMethod;
    private String suitable;
    private String suitableFoods;
    private String taboo;
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
