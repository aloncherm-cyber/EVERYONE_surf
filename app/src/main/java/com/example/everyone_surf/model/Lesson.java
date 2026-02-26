package com.example.everyone_surf.model;

public class Lesson {
    private String lessonId;      // מזהה ייחודי לשיעור

    private Instructor instructor; // שם המדריך (כדי לא לשלוף שוב מה-DB)
    private String region;        // צפון / מרכז / דרום
    private String date;          // תאריך
    private String time;          // שעה
    private double price;         // מחיר
         // טלפון ליצירת קשר

    protected  String status;
    public Lesson() { } // חובה עבור Firebase

    public Lesson(String lessonId, Instructor instructor, String region, String date, String time, double price, String status) {
        this.lessonId = lessonId;
        this.instructor = instructor;
        this.region = region;
        this.date = date;
        this.time = time;
        this.price = price;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getters and Setters
    public String getLessonId() { return lessonId; }

    public String getRegion() { return region; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public double getPrice() { return price; }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "lessonId='" + lessonId + '\'' +
                ", instructor=" + instructor +
                ", region='" + region + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                '}';
    }
}