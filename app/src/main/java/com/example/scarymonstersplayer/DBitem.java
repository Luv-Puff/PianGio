package com.example.scarymonstersplayer;

public class DBitem {


    // Labels Table Columns names
    public  static final String KEY_ID = "ID";
    public  static final String KEY_NAME="NAME";
    public  static final String KEY_VID = "VID";
    public  static final String KEY_SECOND = "SECOND";
    public  static final String KEY_NOTE = "NOTE";
    public  static final String TIME_STAMP= "TIME_STAMP";

    public int id;
    public String name;
    public String vid;
    public int second;
    public String note;

    public DBitem(){

    }

    public DBitem(String name, String vid, int second, String note) {
        this.name = name;
        this.vid = vid;
        this.second = second;
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
