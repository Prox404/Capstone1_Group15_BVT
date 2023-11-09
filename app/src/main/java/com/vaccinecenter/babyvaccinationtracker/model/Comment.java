package com.vaccinecenter.babyvaccinationtracker.model;

public class Comment {
    private String content;
    private Customer customer;
    private String created_at;
    private String comment_id;

    public Comment(String content, Customer customer, String created_at) {
        this.content = content;
        this.customer = customer;
        this.created_at = created_at;
    }

    public Comment() {

    }

    public String getContent() {
        return content;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getComment_id() {
        return comment_id;
    }

}
