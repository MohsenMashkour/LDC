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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button Start;
    TextView data;
    Spinner spinner;
    String[] items;
    DataBase dataBase;
    DatabaseReference databaseReference;
    Marker marker;

    //private CountDownTimer countDownTimer;
    //private long timeLeft = 900000;
    //private boolean timerRun;

    Calendar calendar = Calendar.getInstance();
    String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("DataBase");
        dataBase = new DataBase();


        //spinner
        spinner = findViewById(R.id.spinner);
        items = getResources().getStringArray(R.array.LocalizationMethods);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        //start process
        Start = findViewById(R.id.button);

        data = findViewById(R.id.textView);

        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 876);


        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lMtd = spinner.getSelectedItem().toString();


                //Date dat1 = new Date(System.currentTimeMillis());


                switch (lMtd) {
                    case "GPS":
                        // TrackingGps trackingGps = new TrackingGps(getApplicationContext());

                        TrackingGps trackingGps = new TrackingGps(getApplicationContext(), new TrackingGps.LocationChanged() {

                            @Override
                            public void locationChanged(Location location) {
                                if (location != null) {

                                    String info = locationToString(location);
                                    data.setText(info);

                                    // data.setText(location.toString());
                                    //double lat = location.getLatitude();
                                    //double lon = location.getLongitude();


                                    //Toast.makeText(getApplicationContext(), "LAT: " + lat + " \n LON: " + lon, Toast.LENGTH_LONG).show();

                                    dataBase.setId("gps");
                                    dataBase.setData(data.getText().toString().trim());


                                    databaseReference.child(String.format(String.valueOf(System.currentTimeMillis())))
                                            .setValue(dataBase);
                                    onMarkerUpdate();

                                }
                            }
                        });

                        Location location = trackingGps.getLocation();

                        if (location != null) {
                            String info = locationToString(location);
                            data.setText(info);
                            // data.setText(currentDate+ "\n"+ location.toString());
                            // double lat = location.getLatitude();
                            // double lon = location.getLongitude();
                            //Toast.makeText(getApplicationContext(),"LAT: "+ lat +" \n LON: " + lon, Toast.LENGTH_LONG).show();

                            dataBase.setId("gps");
                            dataBase.setData(data.getText().toString().trim());


                            databaseReference.child(String.format(String.valueOf(System.currentTimeMillis())))
                                    .setValue(dataBase);

                        }
                        break;

                    case "Cellular":
                        data.setText(currentDate);
                        Toast.makeText(getApplicationContext(), lMtd + " selected", Toast.LENGTH_SHORT).show();

                        dataBase.setId("Cellular");
                        dataBase.setData(data.getText().toString().trim());


                        databaseReference.child(String.format(String.valueOf(System.currentTimeMillis())))
                                .setValue(dataBase);

                        break;


                    case "Cell+WiFi":
                        data.setText(currentDate);
                        Toast.makeText(getApplicationContext(), lMtd + " selected", Toast.LENGTH_SHORT).show();

                        dataBase.setId("Cell+WiFi");
                        dataBase.setData(data.getText().toString().trim());


                        databaseReference.child(String.format(String.valueOf(System.currentTimeMillis())))
                                .setValue(dataBase);

                        break;


                }


            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    // @Override
    //  public void onLocationChanged(Location location) {
    //      String info = locationToString(location);
    //      data.setText(info);
    //  }

    private String locationToString(Location location) {
        Calendar locationDate = Calendar.getInstance();
        locationDate.setTimeInMillis(location.getTime());
        return String.format(Locale.getDefault(),
                "Lat: %.2f, Lon: %.2f, Accurracy: %.2f, time: %d (%02d.%02d.%d %02d:%02d:%02d)",
                location.getLatitude(), location.getLongitude(), location.getAccuracy(), location.getTime(),
                locationDate.get(Calendar.DAY_OF_MONTH), locationDate.get(Calendar.MONTH), locationDate.get(Calendar.YEAR),
                locationDate.get(Calendar.HOUR_OF_DAY), locationDate.get(Calendar.MINUTE), locationDate.get(Calendar.SECOND));
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
        if (location != null) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            LatLng latLng = new LatLng(lat, lon);
           marker = mMap.addMarker(new MarkerOptions().position(latLng).title("your current position"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        }
    }



    private void onMarkerUpdate() {
        TrackingGps trackingGps = new TrackingGps(getApplicationContext());
        Location location = trackingGps.getLocation();
        if (location != null) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            LatLng latLng = new LatLng(lat, lon);


            if(marker != null)
            {
                marker.remove();

              marker =  mMap.addMarker(new MarkerOptions().position(latLng).title("your current position"));
               // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }

           // Toast.makeText(getApplicationContext(),"marker Updated", Toast.LENGTH_LONG).show();

        }
    }
}