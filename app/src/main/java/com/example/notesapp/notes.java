package com.example.notesapp;

public class notes {

    private String KEY_id;
    private String KEY_title;
    private String KEY_content;
    private String KEY_date;
    private String KEY_time;

    public notes(String KEY_id, String KEY_title, String KEY_content, String KEY_date, String KEY_time) {
        this.KEY_id = KEY_id;
        this.KEY_title = KEY_title;
        this.KEY_content = KEY_content;
        this.KEY_date = KEY_date;
        this.KEY_time = KEY_time;
    }


    public String getKEY_id() {
        return KEY_id;
    }

    public void setKEY_id(String KEY_id) {
        this.KEY_id = KEY_id;
    }

    public String getKEY_title() {
        return KEY_title;
    }

    public void setKEY_title(String KEY_title) {
        this.KEY_title = KEY_title;
    }

    public String getKEY_content() {
        return KEY_content;
    }

    public void setKEY_content(String KEY_content) {
        this.KEY_content = KEY_content;
    }

    public String getKEY_date() {
        return KEY_date;
    }

    public void setKEY_date(String KEY_date) {
        this.KEY_date = KEY_date;
    }

    public String getKEY_time() {
        return KEY_time;
    }

    public void setKEY_time(String KEY_time) {
        this.KEY_time = KEY_time;
    }
}
