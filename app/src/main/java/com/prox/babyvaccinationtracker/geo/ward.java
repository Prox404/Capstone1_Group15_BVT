package com.prox.babyvaccinationtracker.geo;

public class ward {
    String name;
    int code;

    String division_type;
    String codename;
    String district_code;
    public ward(){}

    public ward(String name, int code, String division_type, String codename, String district_code) {
        this.name = name;
        this.code = code;
        this.division_type = division_type;
        this.codename = codename;
        this.district_code = district_code;
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

    public String getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(String district_code) {
        this.district_code = district_code;
    }
}
