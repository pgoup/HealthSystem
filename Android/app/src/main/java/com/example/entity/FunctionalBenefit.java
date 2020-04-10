package com.example.entity;

/**
 * @author PG
 */
public class FunctionalBenefit {
    private int id;
    private String benefit;

    public FunctionalBenefit() {
    }

    public FunctionalBenefit(String benefit) {
        this.benefit = benefit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }
}
