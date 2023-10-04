package com.prox.babyvaccinationtracker.model;

public class Baby {
    private String baby_id;
    private String baby_name;
    private String baby_birthday;
    private String baby_avatar;
    private String baby_gender;

    public Baby(String baby_id, String baby_name, String baby_birthday, String baby_avatar, String baby_gender) {
        this.baby_id = baby_id;
        this.baby_name = baby_name;
        this.baby_birthday = baby_birthday;
        this.baby_avatar = baby_avatar;
        this.baby_gender = baby_gender;
    }

    public Baby() {
    }


    public String getBaby_id() {
        return baby_id;
    }

    public void setBaby_id(String baby_id) {
        this.baby_id = baby_id;
    }

    public String getBaby_name() {
        return baby_name;
    }

    public void setBaby_name(String baby_name) {
        this.baby_name = baby_name;
    }

    public String getBaby_birthday() {
        return baby_birthday;
    }

    public void setBaby_birthday(String baby_birthday) {
        this.baby_birthday = baby_birthday;
    }

    public String getBaby_avatar() {
        return baby_avatar;
    }

    public void setBaby_avatar(String baby_avatar) {
        this.baby_avatar = baby_avatar;
    }

    public String getBaby_gender() {
        return baby_gender;
    }

    public void setBaby_gender(String baby_gender) {
        this.baby_gender = baby_gender;
    }
}
