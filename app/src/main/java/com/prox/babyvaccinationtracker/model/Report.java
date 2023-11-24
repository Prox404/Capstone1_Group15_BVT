package com.prox.babyvaccinationtracker.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Report {
    String report_id;
    String reason;
    int type_report;
    Post post;
    Comment comment;
    int check;

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public Report() {
        this.check = 0;
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
