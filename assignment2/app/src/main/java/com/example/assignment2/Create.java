package com.example.assignment2;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class Create extends AppCompatActivity {
    DatabaseHelper  d;
    EditText address;

    EditText longitude;

    EditText latitude;

    private static int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_stuff);
        d = new DatabaseHelper(getApplicationContext());
        //get edit texts
        address = (EditText) findViewById(R.id.address);
        latitude = (EditText) findViewById(R.id.latitude);
        longitude = (EditText) findViewById(R.id.longitude);

        Bundle bundle = getIntent().getExtras();
        //if bundle exists it means the data can be edited so enter all previous values in
        if (bundle != null) {
            address.setText(bundle.getString("address"));
            latitude.setText(bundle.getString("latitude"));
            longitude.setText(bundle.getString("longitude"));
            id = bundle.getInt("Id");
        }

        //on click listeners for buttons
        ImageView done = findViewById(R.id.done);
        done.setOnClickListener(v -> saveNote(bundle));

        ImageView delete = findViewById(R.id.delete);
        delete.setOnClickListener(v -> delete(bundle));

    }

    public void saveNote(Bundle bundle){
        //if lat or long is missing tell the user via toast, address is not required as it'll be found via geocoding

        if (longitude.getText().toString().equals("") || latitude.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Enter at least a longitude and latitude!",Toast.LENGTH_LONG).show();
            return;
        }
        //get long and lat in double values
        Double LONG = Double.parseDouble(longitude.getText().toString());
        Double LAT = Double.parseDouble(latitude.getText().toString());

        //if bundle exists it means that an existing item was clicked
        if (bundle!= null){ //update if exists
            if (address.getText().toString().equals("")){ //if custom address is not entered, the address will be generated via geocoding
                if (Geocoder.isPresent()) {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    try {
                        List<Address> ls= geocoder.getFromLocation(LAT,LONG,1); //find  address via geocoding
                        d.update(bundle.getInt("Id"), ls.get(0).getAddressLine(0), LAT.toString(), LONG.toString()); //update database
                    }
                    catch (Exception e) {
                        Toast.makeText(getApplicationContext(),"Address not found, try entering a new longitude and latitude",Toast.LENGTH_LONG).show();
                        return; //return if address could not be found (likely due to invalid lat and long)
                    }
                }
            }
            else{
                //if address already exists no need to find via geocoding
                d.update(bundle.getInt("Id"), address.getText().toString(), LAT.toString(), LONG.toString()); //update database
            }


        }
        else { //bundle doesnt exist so this is a new location
            if (Geocoder.isPresent()) {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> ls= geocoder.getFromLocation(LAT,LONG,1); //find  address via geocoding
                    d.addData(ls.get(0).getAddressLine(0), LAT.toString(), LONG.toString()); //add new data
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"Address not found, try entering a new longitude and latitude",Toast.LENGTH_LONG).show();
                    return; //return if address could not be found (likely due to invalid lat and long)
                }
            }


        }


        startActivity(new Intent(getApplicationContext(), MainActivity.class)); //return to main activity
    }

    public void delete(Bundle bundle){


        if (id != 0){ //delete if id exists
            d.delete(id);
        }

        startActivity(new Intent(getApplicationContext(), MainActivity.class)); //return to main activity
    }

}