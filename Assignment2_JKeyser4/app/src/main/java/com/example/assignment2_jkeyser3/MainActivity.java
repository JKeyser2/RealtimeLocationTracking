package com.example.assignment2_jkeyser3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import android.Manifest;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener, SensorEventListener {

    // Provides access to device's hardware sensor
    private SensorManager sensorManager;

    // Represents a single sensor on an Android device
    private Sensor accelerometer;

    // Provides access to device's hardware location
    private LocationManager locationManager;

    private Location lastKnownLocation;

    private TextView locationTextView;

    private int counter;

    // Stores all velocities
    private ArrayList<Float> allVelocitiesX = new ArrayList<Float>();
    private ArrayList<Float> allVelocitiesY = new ArrayList<Float>();
    private ArrayList<Float> allVelocitiesZ = new ArrayList<Float>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Initialize fragment
        Fragment fragment = new MapsFragment();


        // Open fragment
        getSupportFragmentManager()
         .beginTransaction()
         .replace(R.id.frame_layout, fragment)
          .commit();




        // Used to obtain an instance of the LocationManager class
        // Provides access to location-related services such as GPS
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Used to obtain instance of the sensorManager class
        // Provides access to sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Used to collect data from accelerometer sensor
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);




        // Method call to register a listener for receiving sensor data updates from an...
        // ... Android device's sensor hardware.
        // Turn on sensors for accelerometer, gyroscope, magnetometer
        //sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        // Links widget to XML file. For displaying
        //locationTextView = (TextView) findViewById(R.id.locationTextView);

        // If we don't have permission to access location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission to access location
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            // If we have permission to access location
        } else {
            // Request location updates
            // First number is minimum time interval between location updates (in milliseconds)
            // Second number is the minimum distance between location updates (in meters)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

            // Gets last known location
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        // Gets last known location
        lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //System.out.println("Last Known Location: " + lastKnownLocation);

        // Turns on the accelerometer
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);


        if(lastKnownLocation != null){
            Bundle bundle = new Bundle();

            bundle.putDouble("Longitude", lastKnownLocation.getLongitude());
            bundle.putDouble("Latitude", lastKnownLocation.getLatitude());

            Fragment fragment2 = new MapsFragment();

            fragment2.setArguments(bundle);

            // Open fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment2)
                    .commit();

        }



    }

    // If we don't have permission, prompts user with a dialog box to either accept or deny permission
    //@Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                // Request location updates
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location){
        //System.out.println("EPIC");
        // Display the location
        //locationTextView.setText("Latitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude());
    }

    // When a sensor detects change in value
    @Override
    public void onSensorChanged(SensorEvent event){

        //System.out.println("AWESOME");

        float currTime2 = System.currentTimeMillis();
        //System.out.println("CURRTIME2: " + currTime2);

        //System.out.println("event.timestamp: "+ event.timestamp);


        // If the sensor is the accelerometer
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            counter += 1;
            //System.out.println("COUNTER"  + counter);
            // Store X,Y, Z instances for accelerometer
            float [] accelXYZValues = event.values;

            float accelX = accelXYZValues[0];
            float accelY = accelXYZValues[1];
            // Subtract out gravity
            accelY = (float) (accelY - 9.8099865254902);

            //System.out.println("ACCELY: " + accelY);

            // I can't account for gravity perfectly
            // If the acceleration for Y is this small, just make it 0
            if(accelY < 0.00001 && accelY > -0.00001){
                accelY = 0;
            }
            float accelZ = accelXYZValues[2];


            //System.out.println("ACCELX: " + accelX);
            //System.out.println("ACCELY: " + accelY);
            //System.out.println("ACCELZ: " + accelZ);
            //System.out.println("lastKnownLocation.getTime(): " + lastKnownLocation.getTime());


            // Multiply the acceleration values by time to get the velocity
            // timestamp / 1000000000 to go from nanoseconds to seconds
            // Should be meters-per-second now
            //float velX = accelX * event.timestamp / 1000000000;
            //float velY = accelY * event.timestamp / 1000000000;
            //float velZ = accelZ * event.timestamp / 1000000000;

            // 16 instances collected every second -> 0.0625
            // Multiply acceleration by time
            float velX = (float) (accelX * 0.0625);
            float velY = (float) (accelY * 0.0625);
            float velZ = (float) (accelZ * 0.0625);


            //System.out.println("event.timestamp: "+ event.timestamp/1000000000);

            //System.out.println("VELX: " + velX);
            //System.out.println("VELY: " + velY);
            //System.out.println("VELZ: " + velZ);

            // Stores all velocities
            allVelocitiesX.add(velX);
            allVelocitiesY.add(velY);
            allVelocitiesZ.add(velZ);

            float meanVelocityX = 0;
            float meanVelocityY = 0;
            float meanVelocityZ = 0;

            // Get mean velocities
            for(int i = 0; i < allVelocitiesX.size(); i++){
                meanVelocityX += allVelocitiesX.get(i);
                meanVelocityY += allVelocitiesY.get(i);
                meanVelocityZ += allVelocitiesZ.get(i);
            }

            meanVelocityX = meanVelocityX / allVelocitiesX.size();
            meanVelocityY = meanVelocityY / allVelocitiesX.size();
            meanVelocityZ = meanVelocityZ / allVelocitiesX.size();

            // Gets current time in milliseconds
            long currTime = System.currentTimeMillis();
            // Gets time since our last known location in seconds
            long timeSinceLastKnownLocation = (long) ((currTime - lastKnownLocation.getTime()) / 1000.0f);

            //System.out.println("Time since last location: " + timeSinceLastKnownLocation);

            // Estimated latitude in degrees
            // 111,110 is used to convert the change in latitude from meters to degrees
            // Circumference of Earth at the equator divided by 360 degrees
            double estimatedLat = lastKnownLocation.getLatitude() + (meanVelocityY * timeSinceLastKnownLocation) / 111110.0;
            // Estimated longitude in degrees
            double estimatedLong = lastKnownLocation.getLongitude() + (meanVelocityX * timeSinceLastKnownLocation) / (111110.0 * Math.cos(Math.toRadians(estimatedLat)));

            // Creates a location for the current estimate location, stores the estimated longitude and latitude
            Location estimatedLocation = new Location(" ");
            estimatedLocation.setLatitude(estimatedLat);
            estimatedLocation.setLongitude(estimatedLong);
            estimatedLocation.setTime(currTime);

            // Get the distance between last known location and estimated current location
            float distanceAway = lastKnownLocation.distanceTo(estimatedLocation);

            //System.out.println("-----------------");
            //System.out.println(lastKnownLocation.getLatitude());
            //System.out.println(lastKnownLocation.getLongitude());
            //System.out.println(estimatedLocation.getLatitude());
            //System.out.println(estimatedLocation.getLongitude());
            //System.out.println("-----------------");

            //System.out.println("DISTANCE AWAY: " + distanceAway);
            //System.out.println("CURR TIME: " + currTime);
            //System.out.println("LAST KNOWN LOCATION TIME: " + lastKnownLocation.getTime());

            // If we are over 5 meters away
            if(distanceAway > 5){
                //System.out.println("WE ARE OVER 5 METERS AWAY");
                //System.out.println(distanceAway);
                // Change the location to the estimated location from the accelerometer
                //System.out.println(lastKnownLocation);
                lastKnownLocation = estimatedLocation;
                onLocationChanged(lastKnownLocation);
                //System.out.println(lastKnownLocation);

                Bundle bundle2 = new Bundle();

                bundle2.putDouble("Longitude", lastKnownLocation.getLongitude());
                bundle2.putDouble("Latitude", lastKnownLocation.getLatitude());

                Fragment fragment3 = new MapsFragment();

                fragment3.setArguments(bundle2);

                // Open fragment
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, fragment3)
                        .commit();


                // Clear so we can start over
                allVelocitiesX.clear();
                allVelocitiesY.clear();
                allVelocitiesZ.clear();
                // If we are not over 5 meters away
            }else{
                // If it has been over 30 seconds
                if(timeSinceLastKnownLocation > 30){
                    //System.out.println("VERY NICE");
                    // Uses GPS to get current location
                    lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    onLocationChanged(lastKnownLocation);

                    Bundle bundle4 = new Bundle();

                    bundle4.putDouble("Longitude", lastKnownLocation.getLongitude());
                    bundle4.putDouble("Latitude", lastKnownLocation.getLatitude());

                    Fragment fragment4 = new MapsFragment();

                    fragment4.setArguments(bundle4);

                    // Open fragment
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_layout, fragment4)
                            .commit();


                    // Clear so we can start over
                    allVelocitiesX.clear();
                    allVelocitiesY.clear();
                    allVelocitiesZ.clear();
                }
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume(){
        super.onResume();
    }

}