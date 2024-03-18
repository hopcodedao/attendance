package com.example.attendance.beans;

public class addstudentdatabase {
    private String FullName, MSSV, Address, Email, BrithofDate, Class, Majors, Faculty, Number, Uid;

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public addstudentdatabase(String fullName, String mssv, String address, String email, String brithofDate, String clazz, String majors, String faculty, String number, String uid) {
        FullName = fullName;
        MSSV = mssv;
        Address = address;
        Email = email;
        BrithofDate = brithofDate;
        Class = clazz;
        Majors = majors;
        Faculty = faculty;
        Number = number;
        Uid = uid;
    }

    public addstudentdatabase() {
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getMSSV() {
        return MSSV;
    }

    public void setMSSV(String mssv) {
        MSSV = mssv;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getBrithofDate() {
        return BrithofDate;
    }

    public void setBrithofDate(String brithofDate) {
        BrithofDate = brithofDate;
    }

    public String getClazz() {
        return Class;
    }

    public void setClass(String clazz) {
        Class = clazz;
    }

    public String getMajors() {
        return Majors;
    }

    public void setMajors(String majors) {
        Majors = majors;
    }

    public String getFaculty() {
        return Faculty;
    }

    public void setFaculty(String faculty) {
        Faculty = faculty;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

}
