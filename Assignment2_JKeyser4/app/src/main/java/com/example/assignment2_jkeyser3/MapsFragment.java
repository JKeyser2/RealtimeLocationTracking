package com.example.assignment2_jkeyser3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment {

    private GoogleMap aMap;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            //System.out.println("NICE");
            //LatLng sydney = new LatLng(-34, 151);
            //googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            // Retrieve the arguments
            Bundle args = getArguments();

            if(args != null){
                double longitude = args.getDouble("Longitude");
                double latitude = args.getDouble("Latitude");
                //System.out.println("ABC123");
                //System.out.println(args);

                //System.out.println(longitude);
                //System.out.println(latitude);

                LatLng ourLocation = new LatLng(latitude, longitude);
                //System.out.println(sydney2);
                googleMap.addMarker(new MarkerOptions().position(ourLocation).title(latitude + ", " + longitude));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(ourLocation));

            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        /*
        // Retrieve the arguments
        Bundle args = getArguments();

        if(args != null){
            double longitude = args.getDouble("Longitude");
            double latitude = args.getDouble("Latitude");
            System.out.println("ABC123");
            System.out.println(args);

            System.out.println(longitude);
            System.out.println(latitude);

            LatLng sydney2 = new LatLng(latitude, longitude);
            System.out.println(sydney2);
            aMap.addMarker(new MarkerOptions().position(sydney2).title("Marker in Sydney"));
            aMap.moveCamera(CameraUpdateFactory.newLatLng(sydney2));


            System.out.println("CASH MONEY");
        }
    */
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}