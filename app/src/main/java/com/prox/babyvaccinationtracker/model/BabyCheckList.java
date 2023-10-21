package com.prox.babyvaccinationtracker.model;

public class BabyCheckList {
    public String getBaby_id() {
        return baby_id;
    }

    public void setBaby_id(String baby_id) {
        this.baby_id = baby_id;
    }

    private String baby_id;
    private boolean tuberculosis = false;
    private boolean hepatitis_b = false;
    private boolean diphtheria_whooping_cough_poliomyelitis = false;
    private boolean paralysis = false;
    private boolean pneumonia_hib_meningitis = false;
    private boolean rotavirus_diarrhea = false;
    private boolean pneumonia_meningitis_otitis_media_caused_by_streptococcus = false;
    private boolean meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c = false;
    private boolean influenza = false;
    private boolean measles = false;
    private boolean meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y = false;
    private boolean japanese_encephalitis = false;
    private boolean measles_mumps_rubella = false;
    private boolean chickenpox = false;
    private boolean hepatitis_a = false;
    private boolean hepatitis_a_b = false;
    private boolean tetanus = false;
    private boolean anthrax = false;


    public BabyCheckList(boolean tuberculosis, boolean hepatitis_b, boolean diphtheria_whooping_cough_poliomyelitis, boolean paralysis, boolean pneumonia_hib_meningitis, boolean rotavirus_diarrhea, boolean pneumonia_meningitis_otitis_media_caused_by_streptococcus, boolean meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c, boolean influenza, boolean measles, boolean meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y, boolean japanese_encephalitis, boolean measles_mumps_rubella, boolean chickenpox, boolean hepatitis_a, boolean hepatitis_a_b, boolean tetanus, boolean anthrax) {
        this.tuberculosis = tuberculosis;
        this.hepatitis_b = hepatitis_b;
        this.diphtheria_whooping_cough_poliomyelitis = diphtheria_whooping_cough_poliomyelitis;
        this.paralysis = paralysis;
        this.pneumonia_hib_meningitis = pneumonia_hib_meningitis;
        this.rotavirus_diarrhea = rotavirus_diarrhea;
        this.pneumonia_meningitis_otitis_media_caused_by_streptococcus = pneumonia_meningitis_otitis_media_caused_by_streptococcus;
        this.meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c = meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c;
        this.influenza = influenza;
        this.measles = measles;
        this.meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y = meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y;
        this.japanese_encephalitis = japanese_encephalitis;
        this.measles_mumps_rubella = measles_mumps_rubella;
        this.chickenpox = chickenpox;
        this.hepatitis_a = hepatitis_a;
        this.hepatitis_a_b = hepatitis_a_b;
        this.tetanus = tetanus;
        this.anthrax = anthrax;
    }

    public BabyCheckList() {
    }

    public boolean isTuberculosis() {
        return tuberculosis;
    }

    public void setTuberculosis(boolean tuberculosis) {
        this.tuberculosis = tuberculosis;
    }

    public boolean isHepatitis_b() {
        return hepatitis_b;
    }

    public void setHepatitis_b(boolean hepatitis_b) {
        this.hepatitis_b = hepatitis_b;
    }

    public boolean isDiphtheria_whooping_cough_poliomyelitis() {
        return diphtheria_whooping_cough_poliomyelitis;
    }

    public void setDiphtheria_whooping_cough_poliomyelitis(boolean diphtheria_whooping_cough_poliomyelitis) {
        this.diphtheria_whooping_cough_poliomyelitis = diphtheria_whooping_cough_poliomyelitis;
    }

    public boolean isParalysis() {
        return paralysis;
    }

    public void setParalysis(boolean paralysis) {
        this.paralysis = paralysis;
    }

    public boolean isPneumonia_hib_meningitis() {
        return pneumonia_hib_meningitis;
    }

    public void setPneumonia_hib_meningitis(boolean pneumonia_hib_meningitis) {
        this.pneumonia_hib_meningitis = pneumonia_hib_meningitis;
    }

    public boolean isRotavirus_diarrhea() {
        return rotavirus_diarrhea;
    }

    public void setRotavirus_diarrhea(boolean rotavirus_diarrhea) {
        this.rotavirus_diarrhea = rotavirus_diarrhea;
    }

    public boolean isPneumonia_meningitis_otitis_media_caused_by_streptococcus() {
        return pneumonia_meningitis_otitis_media_caused_by_streptococcus;
    }

    public void setPneumonia_meningitis_otitis_media_caused_by_streptococcus(boolean pneumonia_meningitis_otitis_media_caused_by_streptococcus) {
        this.pneumonia_meningitis_otitis_media_caused_by_streptococcus = pneumonia_meningitis_otitis_media_caused_by_streptococcus;
    }

    public boolean isMeningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c() {
        return meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c;
    }

    public void setMeningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c(boolean meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c) {
        this.meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c = meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c;
    }

    public boolean isInfluenza() {
        return influenza;
    }

    public void setInfluenza(boolean influenza) {
        this.influenza = influenza;
    }

    public boolean isMeasles() {
        return measles;
    }

    public void setMeasles(boolean measles) {
        this.measles = measles;
    }

    public boolean isMeningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y() {
        return meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y;
    }

    public void setMeningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y(boolean meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y) {
        this.meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y = meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y;
    }

    public boolean isJapanese_encephalitis() {
        return japanese_encephalitis;
    }

    public void setJapanese_encephalitis(boolean japanese_encephalitis) {
        this.japanese_encephalitis = japanese_encephalitis;
    }

    public boolean isMeasles_mumps_rubella() {
        return measles_mumps_rubella;
    }

    public void setMeasles_mumps_rubella(boolean measles_mumps_rubella) {
        this.measles_mumps_rubella = measles_mumps_rubella;
    }

    public boolean isChickenpox() {
        return chickenpox;
    }

    public void setChickenpox(boolean chickenpox) {
        this.chickenpox = chickenpox;
    }

    public boolean isHepatitis_a() {
        return hepatitis_a;
    }

    public void setHepatitis_a(boolean hepatitis_a) {
        this.hepatitis_a = hepatitis_a;
    }

    public boolean isHepatitis_a_b() {
        return hepatitis_a_b;
    }

    public void setHepatitis_a_b(boolean hepatitis_a_b) {
        this.hepatitis_a_b = hepatitis_a_b;
    }

    public boolean isTetanus() {
        return tetanus;
    }

    public void setTetanus(boolean tetanus) {
        this.tetanus = tetanus;
    }

    public boolean isAnthrax() {
        return anthrax;
    }

    public void setAnthrax(boolean anthrax) {
        this.anthrax = anthrax;
    }
}