package com.example.notesapp.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class notes {

    private String id;
    private String title;
    private String content;
    private String date;
    private String time;
    private String subjectname;
    private double userlat;
    private double userlong;
    private ArrayList<notes> colist = new ArrayList<notes>();

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    private String imagename;



    public notes(String id, String title, String content, String date, String time, String subjectname,double userlat,double userlong) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.subjectname = subjectname;
        this.userlat = userlat;
        this.userlong = userlong;
    }

    public notes() {
    }

    public notes(String title, String content, String date, String time) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
    }

    public notes(String imagename) {
        this.imagename = imagename;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public double getUserlat() {
        return userlat;
    }

    public void setUserlat(double userlat) {
        this.userlat = userlat;
    }

    public double getUserlong() {
        return userlong;
    }

    public void setUserlong(double userlong) {
        this.userlong = userlong;
    }

    public ArrayList<notes> getColist() {
        return colist;
    }

    public void setColist(ArrayList<notes> colist) {
        this.colist = colist;
    }
}
