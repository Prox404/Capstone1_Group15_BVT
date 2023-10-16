package com.prox.babyvaccinationtracker.model;

import java.io.Serializable;
import java.util.Date;

public class Regimen implements Serializable, Comparable<Regimen> {
    private String content;
    private Date date;
    private boolean vaccinated;
    private String vaccination_type;

    public Regimen() {
    }

    public Regimen(String content, Date date, boolean vaccinated, String vaccination_type) {
        this.content = content;
        this.date = date;
        this.vaccinated = vaccinated;
        this.vaccination_type = vaccination_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        this.vaccinated = vaccinated;
    }

    public String getVaccination_type() {
        return vaccination_type;
    }

    public void setVaccination_type(String vaccination_type) {
        this.vaccination_type = vaccination_type;
    }

    @Override
    public int compareTo(Regimen regimen) {
        return this.date.compareTo(regimen.date);
    }
}
