package com.admin.babyvaccinationtracker.model;

import java.util.ArrayList;

public class Report {
    // trường hợp post
    Post post;
    // trường hợp comment
    Comment comment;
    String post_id;

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    ArrayList<String> reasons;
    String report_id;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

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
