package com.example.notebaguion;

import android.annotation.SuppressLint;


import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {
    public static final String KEY_ID = "_id";

    public static final String KEY_NOTE_COLUMN = "NOTE_COLUMN";
    public static final String KEY_NOTE_CREATED_COLUMN = "NOTE_CREATED_COLUMN";

    String note;
    Date created;

    public Note(String note) {
        this.note = note;
        created = new Date(System.currentTimeMillis());
    }

    public String getNote() {

        return note;
    }

    public void setNote(String note) {

        this.note = note;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getCreated() {

        return created;
    }


    @Override
    public String toString() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String timeString = sdf.format(created);
        return "("+ timeString +")" + note;
    }
}
