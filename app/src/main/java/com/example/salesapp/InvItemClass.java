package com.example.salesapp;

public class InvItemClass {

    private String exchange,freeIssue,inStock,itemCode,itemName,lineTot,qty,returnPrice,returnVal,returnValue,unitPrice;

    public InvItemClass() {
    }

    public InvItemClass(String exchange, String freeIssue, String inStock, String itemCode, String itemName, String lineTot, String qty, String returnPrice, String returnVal, String returnValue, String unitPrice) {
        this.exchange = exchange;
        this.freeIssue = freeIssue;
        this.inStock = inStock;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.lineTot = lineTot;
        this.qty = qty;
        this.returnPrice = returnPrice;
        this.returnVal = returnVal;
        this.returnValue = returnValue;
        this.unitPrice = unitPrice;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getFreeIssue() {
        return freeIssue;
    }

    public void setFreeIssue(String freeIssue) {
        this.freeIssue = freeIssue;
    }

    public String getInStock() {
        return inStock;
    }

    public void setInStock(String inStock) {
        this.inStock = inStock;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getLineTot() {
        return lineTot;
    }

    public void setLineTot(String lineTot) {
        this.lineTot = lineTot;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getReturnPrice() {
        return returnPrice;
    }

    public void setReturnPrice(String returnPrice) {
        this.returnPrice = returnPrice;
    }

    public String getReturnVal() {
        return returnVal;
    }

    public void setReturnVal(String returnVal) {
        this.returnVal = returnVal;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }
}
