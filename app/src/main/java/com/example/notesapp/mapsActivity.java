package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class mapsActivity extends AppCompatActivity {

    SupportMapFragment supportMapFragment;

    String lat;
    String lon;
    LatLng secondlatlon;
    DatabaseReference notesdata;
    HashMap<String,Double> getlatlong;
    Double newlat;
    Double newlong;
    GoogleMap mymap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.google_map);

        Intent i = getIntent();
             lat = i.getExtras().getString("notelat");
             lon = i.getExtras().getString("notelon");
             final String  id = i.getExtras().getString("id");
             final String sname = i.getExtras().getString("sname");
//        HashMap<String,Double> hm = (HashMap<String, Double>) i.getExtras().get("hashmap");
//        Log.d("myhash", String.valueOf(hm));

        notesdata = FirebaseDatabase.getInstance().getReference().child("Notes").child(sname).child("subjectnotes").child(id);

        notesdata.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(String.valueOf(dataSnapshot.child("mulmaps").getValue()).equals("0")){
                    newlat = 0.0;
                    newlong = 0.0;
                    getcurrentLoaction();
                }else {

                    Log.d("tryingtodouble", String.valueOf(dataSnapshot.child("mulmaps").getValue()));
                   getlatlong = (HashMap<String, Double>) dataSnapshot.child("mulmaps").getValue();

                    newlat = getlatlong.get("latitude");
                    newlong = getlatlong.get("longitude");

                    getcurrentLoaction();

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void getcurrentLoaction() {

                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            mymap = googleMap;
                            LatLng latLng = new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));

                            MarkerOptions options = new MarkerOptions().position(latLng).title("Created here!");

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));

                            googleMap.addMarker(options);

                            if(newlat != 0.0 && newlong !=0.0) {




                                LatLng edited = new LatLng(newlat, newlong);

                                MarkerOptions edtmark = new MarkerOptions().position(edited).title("edited here");

                                //   googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));


                                googleMap.addMarker(edtmark);

                                drawPolyline(latLng,edited);

                            }

                        }
                    });


    }

    private void drawPolyline(LatLng latLng, LatLng latLng1) {
        Polyline line = mymap.addPolyline(new PolylineOptions()
                .add(latLng,latLng1)
                .width(10)
                .color(Color.BLUE));
        line.setClickable(true);


    }

}
