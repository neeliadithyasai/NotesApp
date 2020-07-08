package com.example.notesapp.model;

import com.example.notesapp.model.notes;

import java.util.ArrayList;

public class subject {

   private String SubjectID;
   private ArrayList<notes> subjectnotes = new ArrayList<notes>();



    public subject(String subjectID ) {
        SubjectID = subjectID;


    }

    public subject() {
    }



    public String getSubjectID() {
        return SubjectID;
    }

    public void setSubjectID(String subjectID) {
        SubjectID = subjectID;
    }




    public void addnotes(notes note)
    {
        this.subjectnotes.add(note);
    }

    public ArrayList<notes> getSubjectnotes() {
        return subjectnotes;
    }

    public void setSubjectnotes(ArrayList<notes> subjectnotes) {
        this.subjectnotes = subjectnotes;
    }
}
