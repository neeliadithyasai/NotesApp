package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class notesDetailsActivity extends AppCompatActivity {

    TextView notesDetails;
    DatabaseReference notesdata;
    Toolbar toolbar;
    String selectednotelat;
    String selectednotelon;
    String id;
    String sname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_details);
        notesDetails = findViewById(R.id.notesDetails);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Intent i = getIntent();
         id = i.getExtras().getString("id");
        sname = i.getExtras().getString("sname");
        notesdata = FirebaseDatabase.getInstance().getReference().child("Notes").child(sname).child("subjectnotes").child(id);

        Toast.makeText(this,String.valueOf(id), Toast.LENGTH_LONG).show();
        Log.d("detailshow", String.valueOf(notesdata.child(id)));

        notesdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                    notesDetails.setText(dataSnapshot.child("content").getValue().toString());
                    selectednotelat = dataSnapshot.child("userlat").getValue().toString();
                    selectednotelon = dataSnapshot.child("userlong").getValue().toString();
                    getSupportActionBar().setTitle(dataSnapshot.child("title").getValue().toString());

                    Log.d("showmedata", dataSnapshot.child("title").getValue().toString());





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notes_edit_menu,menu);
        return true;


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mapsmenu){
            Intent i = new Intent(this, mapsActivity.class);
           i.putExtra("notelat",selectednotelat);
            i.putExtra("notelon",selectednotelon);
            startActivity(i);

        }
        if(item.getItemId() == R.id.save_editednotes){

            notesdata.child("content").setValue(notesDetails.getText().toString());
            onBackPressed();


        }






        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
