package com.example.salesapp;

public class InvoiceDetails {
    private String vehicleNo, repName, repCode, invNo, invDate, cusRoute, cusName, cusCode, invStatus;
    private String balance, cashReceipt, invAmount, previousBal, returnedAmount;

    public InvoiceDetails() {
    }

    public InvoiceDetails(String balance, String cashReceipt, String invAmount, String previousBal, String returnedAmount) {
        this.balance = balance;
        this.cashReceipt = cashReceipt;
        this.invAmount = invAmount;
        this.previousBal = previousBal;
        this.returnedAmount = returnedAmount;
    }

    public InvoiceDetails(String vehicleNo, String repName, String repCode, String invNo, String invDate, String cusRoute, String cusName, String cusCode, String invStatus, String balance, String cashReceipt, String invAmount, String previousBal, String returnedAmount) {
        this.vehicleNo = vehicleNo;
        this.repName = repName;
        this.repCode = repCode;
        this.invNo = invNo;
        this.invDate = invDate;
        this.cusRoute = cusRoute;
        this.cusName = cusName;
        this.cusCode = cusCode;
        this.invStatus = invStatus;
        this.balance = balance;
        this.cashReceipt = cashReceipt;
        this.invAmount = invAmount;
        this.previousBal = previousBal;
        this.returnedAmount = returnedAmount;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getRepName() {
        return repName;
    }

    public void setRepName(String repName) {
        this.repName = repName;
    }

    public String getRepCode() {
        return repCode;
    }

    public void setRepCode(String repCode) {
        this.repCode = repCode;
    }

    public String getInvNo() {
        return invNo;
    }

    public void setInvNo(String invNo) {
        this.invNo = invNo;
    }

    public String getInvDate() {
        return invDate;
    }

    public void setInvDate(String invDate) {
        this.invDate = invDate;
    }

    public String getCusRoute() {
        return cusRoute;
    }

    public void setCusRoute(String cusRoute) {
        this.cusRoute = cusRoute;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getCusCode() {
        return cusCode;
    }

    public void setCusCode(String cusCode) {
        this.cusCode = cusCode;
    }

    public String getInvStatus() {
        return invStatus;
    }

    public void setInvStatus(String invStatus) {
        this.invStatus = invStatus;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCashReceipt() {
        return cashReceipt;
    }

    public void setCashReceipt(String cashReceipt) {
        this.cashReceipt = cashReceipt;
    }

    public String getInvAmount() {
        return invAmount;
    }

    public void setInvAmount(String invAmount) {
        this.invAmount = invAmount;
    }

    public String getPreviousBal() {
        return previousBal;
    }

    public void setPreviousBal(String previousBal) {
        this.previousBal = previousBal;
    }

    public String getReturnedAmount() {
        return returnedAmount;
    }

    public void setReturnedAmount(String returnedAmount) {
        this.returnedAmount = returnedAmount;
    }
}