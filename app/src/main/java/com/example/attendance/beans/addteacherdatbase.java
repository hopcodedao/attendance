package com.example.attendance.beans;

public class addteacherdatbase {
    private String Fullname, Email, Faculty, Degree, Birthofdate, Numbers,  Uid, Addresss;

    public addteacherdatbase() {
    }

    public addteacherdatbase(String fullname, String email, String faculty, String degree, String birthofdate, String numbers, String uid, String addresss) {
        Fullname = fullname;
        Email = email;
        Faculty = faculty;
        Degree = degree;
        Birthofdate = birthofdate;
        Numbers = numbers;
        Uid = uid;
        Addresss = addresss;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setFaculty(String faculty) {
        Faculty = faculty;
    }

    public void setDegree(String degree) {
        Degree = degree;
    }

    public void setBirthofdate(String birthofdate) {
        Birthofdate = birthofdate;
    }

    public void setNumbers(String numbers) {
        Numbers = numbers;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public void setAddresss(String addresss) {
        Addresss = addresss;
    }

    public String getFullname() {
        return Fullname;
    }

    public String getEmail() {
        return Email;
    }

    public String getFaculty() {
        return Faculty;
    }
    public String getDegree() {
        return Degree;
    }

    public String getBirthofdate() {
        return Birthofdate;
    }

    public String getNumbers() {
        return Numbers;
    }

    public String getUid() {
        return Uid;
    }

    public String getAddresss() {
        return Addresss;
    }
}
