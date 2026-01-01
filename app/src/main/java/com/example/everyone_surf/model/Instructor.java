package com.example.everyone_surf.model;

public class Instructor  extends  User{

    int experience_year;

    public Instructor(String id, String fname, String lname, String phone, String gender, String age, String email, String password, int experience_year) {
        super(id, fname, lname, phone, gender, age, email, password);
        this.experience_year = experience_year;
    }

    public Instructor(int experience_year) {
        this.experience_year = experience_year;
    }


    public Instructor() {
    }

    public int getExperience_year() {
        return experience_year;
    }

    public void setExperience_year(int experience_year) {
        this.experience_year = experience_year;
    }

    @Override
    public String toString() {
        return "Instructor{" +
                "experience_year=" + experience_year +
                ", id='" + id + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", age='" + age + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
