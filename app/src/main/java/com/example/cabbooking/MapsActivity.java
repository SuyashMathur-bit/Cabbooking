package com.example.cabbooking;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.cabbooking.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private AutoCompleteTextView etPickup, etDrop;
    private RadioGroup rgCabType;
    private Button btnBookRide;
    private ImageButton btnMenu;

    private LatLng pickupLocation = null;
    private LatLng dropLocation = null;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        etPickup = findViewById(R.id.etPickup);
        etDrop = findViewById(R.id.etDrop);
        rgCabType = findViewById(R.id.rgCabType);
        btnBookRide = findViewById(R.id.btnBookRide);
        btnMenu = findViewById(R.id.btnMenu);

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationView);

        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_bookings) {
                Toast.makeText(this, "Booking History Clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MapsActivity.this, BookingHistory.class));
            } else if (id == R.id.nav_logout) {
                Toast.makeText(this, "Logout Clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MapsActivity.this, LoginPage1.class));
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        binding.etPickup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                pickupLocation = getLocationFromAddress(s.toString().trim());
                updateMapAndFare();
            }
        });

        binding.etDrop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                dropLocation = getLocationFromAddress(s.toString().trim());
                updateMapAndFare();
            }
        });
        binding.rgCabType.setOnCheckedChangeListener((group, checkedId) -> updateMapAndFare());



        btnBookRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pickup = etPickup.getText().toString().trim();
                String drop = etDrop.getText().toString().trim();
                String cabType = getSelectedCabType();
                String price = getSelectedFare();

                if (pickup.isEmpty() || drop.isEmpty() || cabType == null || price == null) {
                    Toast.makeText(MapsActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                String url = "http://10.0.2.2/cabbooking1/book_ride.php";

                StringRequest request = new StringRequest(Request.Method.POST, url,
                        response -> {
                            Toast.makeText(MapsActivity.this, "Ride booked!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MapsActivity.this, BookSucessfull.class));
                        },
                        error -> Toast.makeText(MapsActivity.this, "Booking failed: " + error.getMessage(), Toast.LENGTH_SHORT).show()
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("user_id", "123"); // fixed user ID
                        params.put("pickup_location", pickup);
                        params.put("drop_location", drop);
                        params.put("cab_type", cabType);
                        params.put("price", price);
                        return params;
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
                queue.add(request);

            }
        });

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng India = new LatLng( 22.9734, 78.6569);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(India, 5.0f));

        mMap.addMarker(new MarkerOptions().position(India).title("Marker in India"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(India));
    }
    private LatLng getLocationFromAddress(String address) {
        if(address == null||address.isEmpty()){
            return null;
        }
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                return new LatLng(location.getLatitude(), location.getLongitude());
            }
            else{
                Log.e("Geocoder", "No location found for: " + address);
            }
        } catch (IOException e) {
            Log.e("Geocoder", "Geocoding failed: " + e.getMessage());

        }
        return null;
    }
    private void updateMapAndFare() {
        if (pickupLocation != null && dropLocation != null) {
            mMap.clear();

            mMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup"));
            mMap.addMarker(new MarkerOptions().position(dropLocation).title("Drop"));

            mMap.addPolyline(new PolylineOptions()
                    .add(pickupLocation, dropLocation)
                    .width(8f)
                    .color(Color.BLUE)
                    .geodesic(true));

            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(pickupLocation)
                    .include(dropLocation)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));

            calculateFare(pickupLocation,dropLocation);

        }
    }


    private void calculateFare(LatLng pickup, LatLng drop) {
        float[] results = new float[1];
        Location.distanceBetween(
                pickup.latitude, pickup.longitude,
                drop.latitude, drop.longitude,
                results
        );

        float distanceInKm = results[0] / 1000f;
        float baseFare = 50;

        float fareMini = baseFare + (distanceInKm * 10);
        float fareSedan = baseFare + (distanceInKm * 15);
        float fareSUV = baseFare + (distanceInKm * 20);

        binding.rbMini.setText("Mini – ₹" + String.format("%.2f", fareMini));
        binding.rbSedan.setText("Sedan – ₹" + String.format("%.2f", fareSedan));
        binding.rbSUV.setText("SUV – ₹" + String.format("%.2f", fareSUV));


    }
    private String getSelectedCabType() {
        int selectedId = rgCabType.getCheckedRadioButtonId();
        if (selectedId == binding.rbMini.getId()) return "Mini";
        if (selectedId == binding.rbSedan.getId()) return "Sedan";
        if (selectedId == binding.rbSUV.getId()) return "SUV";
        return null; // No selection
    }
    private String getSelectedFare() {
        int selectedId = rgCabType.getCheckedRadioButtonId();
        RadioButton selectedButton = findViewById(selectedId);
        if (selectedButton != null) {
            String text = selectedButton.getText().toString();
            if (text.contains("₹")) {
                return text.substring(text.indexOf("₹") + 1).trim(); // Extract fare only
            }
        }
        return null;
    }


}