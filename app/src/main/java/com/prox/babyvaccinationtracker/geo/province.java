package com.prox.babyvaccinationtracker.geo;

import java.util.ArrayList;

public class province {
    String name;
    int code;
    String division_type;
    String codename;
    int phone_code;
    ArrayList<district> districts;
    public  province(){}

    public province(String name, int code, String division_type, String codename, int phone_code, ArrayList<district> districts) {
        this.name = name;
        this.code = code;
        this.division_type = division_type;
        this.codename = codename;
        this.phone_code = phone_code;
        this.districts = districts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDivision_type() {
        return division_type;
    }

    public void setDivision_type(String division_type) {
        this.division_type = division_type;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public int getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(int phone_code) {
        this.phone_code = phone_code;
    }

    public ArrayList<district> getDistricts() {
        return districts;
    }

    public void setDistricts(ArrayList<district> districts) {
        this.districts = districts;
    }
}
