package com.admin.babyvaccinationtracker.model;

import java.util.ArrayList;

public class Report {
    Post post;
    ArrayList<String> reasons;

    public Report() {
    }

    public Report(Post post, ArrayList<String> reasons) {
        this.post = post;
        this.reasons = reasons;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public ArrayList<String> getReasons() {
        return reasons;
    }

    public void setReasons(ArrayList<String> reasons) {
        this.reasons = reasons;
    }
}
