package com.example.salesapp;

import android.widget.EditText;
import android.widget.TextView;

public class Customer {
    private String cusAddress, cusBal, cusCode, cusLoc, cusMobile, cusName;

    public Customer() {
    }

    public Customer(String cusCode, String cusName) {
        this.cusCode = cusCode;
        this.cusName = cusName;
    }

    public Customer(String cusAddress, String cusBal, String cusCode, String cusLoc, String cusMobile, String cusName) {
        this.cusAddress = cusAddress;
        this.cusBal = cusBal;
        this.cusCode = cusCode;
        this.cusLoc = cusLoc;
        this.cusMobile = cusMobile;
        this.cusName = cusName;
    }


    public String getCusAddress() {
        return cusAddress;
    }

    public void setCusAddress(String cusAddress) {
        this.cusAddress = cusAddress;
    }

    public String getCusBal() {
        return cusBal;
    }

    public void setCusBal(String cusBal) {
        this.cusBal = cusBal;
    }

    public String getCusCode() {
        return cusCode;
    }

    public void setCusCode(String cusCode) {
        this.cusCode = cusCode;
    }

    public String getCusLoc() {
        return cusLoc;
    }

    public void setCusLoc(String cusLoc) {
        this.cusLoc = cusLoc;
    }

    public String getCusMobile() {
        return cusMobile;
    }

    public void setCusMobile(String cusMobile) {
        this.cusMobile = cusMobile;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }
}
