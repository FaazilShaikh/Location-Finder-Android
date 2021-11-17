package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public RecyclerView rView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper  d = new DatabaseHelper(getApplicationContext()); //get database

        ImageView imageAddNoteMain = findViewById(R.id.add);
        imageAddNoteMain.setOnClickListener( v ->AddLocation() ); //click listener for add button

        rView = findViewById(R.id.recyclerView);
        rView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        ArrayList<Locations> temp = d.getDataArr(); //read data from database
        Random rand = new Random();

        if (temp.size() < 50){ //if there aren't 50 addresses keep adding until there is at least 50, takes a few seconds to complete
            Toast.makeText(getApplicationContext(),"Less than 50 addresses... Loading random address... please wait...",Toast.LENGTH_LONG).show();
            for (int i = temp.size();i<50;i++){
                if (Geocoder.isPresent()) {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    Double LAT = 43 + (44 - 43) * rand.nextDouble();
                    Double LONG = (78 + (79 - 78) * rand.nextDouble()) *-1;
                    try {
                        List<Address> ls= geocoder.getFromLocation(LAT,LONG,1);
                        d.addData(ls.get(0).getAddressLine(0), LAT.toString(), LONG.toString());
                    }
                    catch (Exception e) {
                        i--;
                    }
                }
            }
        }

        //put data in adapter
        LocationAdapter adapter = new LocationAdapter(d.getDataArr());

        rView.setAdapter(adapter);

        //listen to character presses in search box
        EditText inputSearch = findViewById(R.id.search);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.cancelTimer(); //cancel the debounce timer (500 ms)
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (adapter.getItemCount() != 0) {
                    adapter.searchNotes(s.toString()); //search for notes
                }
            }

        });

    }

    public void AddLocation(){
        //send to the create activity to add new location
        Intent intent = new Intent (getApplicationContext(), Create.class);

        startActivity(intent);
    }
}