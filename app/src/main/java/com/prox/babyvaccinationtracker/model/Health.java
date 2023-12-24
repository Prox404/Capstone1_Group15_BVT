package com.prox.babyvaccinationtracker.model;

import java.util.Date;

public class Health {
    private String health_id ;

    public Health(String baby_id, double height, double weight, double sleep, String healthCreated_at) {
        this.baby_id = baby_id;
        this.height = height;
        this.weight = weight;
        this.sleep = sleep;
        this.healthCreated_at = healthCreated_at;
    }

    public Health(String health_id, String baby_id, double height, double weight, double sleep, String healthCreated_at) {
        this.health_id = health_id;
        this.baby_id = baby_id;
        this.height = height;
        this.weight = weight;
        this.sleep = sleep;
        this.healthCreated_at = healthCreated_at;
    }

    public Health(double height, double weight) {
        this.height = height;
        this.weight = weight;
    }

    public Health() {
    }

    public String getHealth_id() {
        return health_id;
    }

    public void setHealth_id(String health_id) {
        this.health_id = health_id;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getSleep() {
        return sleep;
    }

    public void setSleep(double sleep) {
        this.sleep = sleep;
    }

    public String getHealthCreated_at() {
        return healthCreated_at;
    }

    public void setHealthCreated_at(String healthCreated_at) {
        this.healthCreated_at = healthCreated_at;
    }

    public String getBaby_id() {
        return baby_id;
    }

    public void setBaby_id(String baby_id) {
        this.baby_id = baby_id;
    }

    private String baby_id ;
    private double height ;
    private double weight ;
    private double sleep ;
    private String healthCreated_at  = new Date().toString();


}
