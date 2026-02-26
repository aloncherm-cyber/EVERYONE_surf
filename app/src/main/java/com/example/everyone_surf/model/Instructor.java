package com.example.everyone_surf.model;

public class Instructor  extends  User{

    int experience_year;

    String pic;
    String status;

    public Instructor(String id, String fname, String lname, String phone, String gender, String age, String email, String password, int experience_year, String pic, String status) {
        super(id, fname, lname, phone, gender, age, email, password);
        this.experience_year = experience_year;
        this.pic = pic;
        this.status = status;
    }

    public Instructor(int experience_year, String pic, String status) {
        this.experience_year = experience_year;
        this.pic = pic;
        this.status = status;
    }

    public Instructor(int experience_year) {
        this.experience_year = experience_year;
    }


    public Instructor() {
    }

    public Instructor(Instructor instructor) {
        super(instructor.id, instructor.fname, instructor.lname, instructor.phone, instructor.gender, instructor.age, instructor.email);
        this.experience_year = experience_year;
        this.pic = pic;



    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
                ", pic='" + pic + '\'' +
                ", status='" + status + '\'' +
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
