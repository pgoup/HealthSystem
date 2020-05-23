package com.application.entity.client;

import lombok.Data;

/**
 * @author PG
 */
@Data
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
}
