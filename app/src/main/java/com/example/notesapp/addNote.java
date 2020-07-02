package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActionBar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class addNote extends AppCompatActivity {

    //view variable initialization

    EditText notesTitle,noteDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        //initialization of views.
        notesTitle = findViewById(R.id.noteTitle);
        noteDetails = findViewById(R.id.noteDetails );



        //noteTitle functions.
        notesTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
}
