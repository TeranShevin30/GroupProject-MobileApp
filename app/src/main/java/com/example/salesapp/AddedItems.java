package com.example.salesapp;

public class AddedItems {
    String no,description,rate,fIssue,qty,value;

    public AddedItems() {
    }

    public AddedItems(String no, String description, String rate, String qty, String value) {
        this.no = no;
        this.description = description;
        this.rate = rate;
        this.qty = qty;
        this.value = value;
    }

    public AddedItems(String no, String description, String rate, String fIssue, String qty, String value) {
        this.no = no;
        this.description = description;
        this.rate = rate;
        this.fIssue = fIssue;
        this.qty = qty;
        this.value = value;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getfIssue() {
        return fIssue;
    }

    public void setfIssue(String fIssue) {
        this.fIssue = fIssue;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
