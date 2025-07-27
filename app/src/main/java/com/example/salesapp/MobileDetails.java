package com.example.salesapp;

public class MobileDetails {
    String contactNo,empId,empName,encryptKey,licControl,mobileId,nickName,userCategory,uuId,vhNo;

    public MobileDetails(String contactNo, String empId, String empName, String encryptKey, String licControl, String mobileId, String nickName, String userCategory, String uuId, String vhNo) {
        this.contactNo = contactNo;
        this.empId = empId;
        this.empName = empName;
        this.encryptKey = encryptKey;
        this.licControl = licControl;
        this.mobileId = mobileId;
        this.nickName = nickName;
        this.userCategory = userCategory;
        this.uuId = uuId;
        this.vhNo = vhNo;
    }

    public MobileDetails(String mobileId, String uuId) {
        this.mobileId = mobileId;
        this.uuId = uuId;
    }

    public MobileDetails(String contactNo, String empId, String empName, String encryptKey, String licControl, String nickName, String userCategory, String vhNo) {
        this.contactNo = contactNo;
        this.empId = empId;
        this.empName = empName;
        this.encryptKey = encryptKey;
        this.licControl = licControl;
        this.nickName = nickName;
        this.userCategory = userCategory;
        this.vhNo = vhNo;
    }

    public MobileDetails() {
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEncryptKey() {
        return encryptKey;
    }

    public void setEncryptKey(String encryptKey) {
        this.encryptKey = encryptKey;
    }

    public String getLicControl() {
        return licControl;
    }

    public void setLicControl(String licControl) {
        this.licControl = licControl;
    }

    public String getMobileId() {
        return mobileId;
    }

    public void setMobileId(String mobileId) {
        this.mobileId = mobileId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserCategory() {
        return userCategory;
    }

    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    public String getVhNo() {
        return vhNo;
    }

    public void setVhNo(String vhNo) {
        this.vhNo = vhNo;
    }
}
