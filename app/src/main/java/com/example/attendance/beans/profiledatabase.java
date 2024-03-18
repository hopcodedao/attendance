package com.example.attendance.beans;

public class profiledatabase {

    private String FullName, MSSV, Address, Email, BrithofDate, Class, Majors, Faculty, Number, Uid, Facedata, Password, Usertype, Year, Degree;

    public profiledatabase() {
    }

    public profiledatabase(String fullname, String mssv, String address, String email, String birthofdate, String clazz, String majors, String faculty, String number, String uid, String facedata, String password, String usertype, String year, String degree) {
        FullName = fullname;
        Email = email;
        BrithofDate = birthofdate;
        Number = number;
        Uid = uid;
        Address = address;
        Password = password;
        Usertype = usertype;
        MSSV = mssv;
        Class = clazz;
        Year = year;
        Degree = degree;
        Majors = majors;
        Faculty = faculty;
        Facedata = facedata;
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

    public void setMSSV(String MSSV) {
        this.MSSV = MSSV;
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

    public void setClazz(String clazz) {
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

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getFacedata() {
        return Facedata;
    }

    public void setFacedata(String facedata) {
        Facedata = facedata;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUsertype() {
        return Usertype;
    }

    public void setUsertype(String usertype) {
        Usertype = usertype;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getDegree() {
        return Degree;
    }

    public void setDegree(String degree) {
        Degree = degree;
    }
}
