package com.example.attendance.beans;

public class QRCodeResultModel {
    private String course;
    private String year;
    private String division;
    private String subject;
    private String start_time;
    private String end_time;

    private double latitude;

    private double longitude;

    // Constructors
    public QRCodeResultModel() {
    }

    public QRCodeResultModel(String course, String year, String division, String subject, String start_time, String end_time) {
        this.course = course;
        this.year = year;
        this.division = division;
        this.subject = subject;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    // Getters
    public String getCourse() {
        return course;
    }

    public String getYear() {
        return year;
    }

    public String getDivision() {
        return division;
    }

    public String getSubject() {
        return subject;
    }

    public String getStartTime() {
        return start_time;
    }

    public String getEndTime() {
        return end_time;
    }

    // Setters
    public void setCourse(String course) {
        this.course = course;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setStartTime(String start_time) {
        this.start_time = start_time;
    }

    public void setEndTime(String end_time) {
        this.end_time = end_time;
    }
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
