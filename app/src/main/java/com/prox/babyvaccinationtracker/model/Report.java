package com.prox.babyvaccinationtracker.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Report {
    String report_id;
    String reason;
    int type_report; // 1: post || 0: Comment
    Post post; // trường hợp 1
    Comment comment; // trường hợp 0
    String post_id; // trường hợp 0

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public Report() {
    }

    public Report(String report_id, String reason, int type_report, Post post, Comment comment) {
        this.report_id = report_id;
        this.reason = reason;
        this.type_report = type_report;
        this.post = post;
        this.comment = comment;
    }

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getType_report() {
        return type_report;
    }

    public void setType_report(int type_report) {
        this.type_report = type_report;
    }


    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
