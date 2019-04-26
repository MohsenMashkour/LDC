package com.example.ldc;

import android.Manifest;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    Button Start;
    TextView data;
    Spinner spinner;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        spinner = (Spinner) findViewById(R.id.spinner);
        items = getResources().getStringArray(R.array.LocalizationMethods);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


       /* spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String lMtd = spinner.getSelectedItem().toString();

                Toast.makeText(getApplicationContext(),lMtd, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        Start = (Button) findViewById(R.id.button);
        ActivityCompat.requestPermissions(MapsActivity.this,  new String[]{Manifest.permission.ACCESS_FINE_LOCATION},876);

        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lMtd = spinner.getSelectedItem().toString();
               // String i = lMtd;

                switch (lMtd){
                    case "GPS":
                        TrackingGps trackingGps = new TrackingGps(getApplicationContext());
                        Location location = trackingGps.getLocation();
                        if(location != null){
                            data = (TextView) findViewById(R.id.textView);
                            data.setText(location.toString());
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();
                            Toast.makeText(getApplicationContext(),"LAT: "+ lat +" \n LON: " + lon, Toast.LENGTH_LONG).show();

                        }
                        break;

                    case "Cellular":
                        Toast.makeText(getApplicationContext(),lMtd + " selected", Toast.LENGTH_SHORT).show();

                        break;


                    case "Cell+WiFi":
                        Toast.makeText(getApplicationContext(),lMtd + " selected", Toast.LENGTH_SHORT).show();
                        break;

                }




            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        TrackingGps trackingGps = new TrackingGps(getApplicationContext());
        Location location = trackingGps.getLocation();
        if(location != null){
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            LatLng latLng = new LatLng(lat, lon);
            mMap.addMarker(new MarkerOptions().position(latLng).title("your current position"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }
}
