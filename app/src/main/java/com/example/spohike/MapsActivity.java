package com.example.spohike;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.Serializable;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationClickListener {
    private FusedLocationProviderClient fusedLocationProviderClient;
     public GoogleMap mMap;
    List<Trail> TrailList;
    int currPhotoIndex = -1;
    Location mCurrentLocation;
    public LocationRequest locationRequest;
    String lat;
    String lon;
    Double wayLatitude;
    Double wayLongitude;
    LatLng gonzaga = new LatLng(47.6664,-117.40235);
    static final int MY_LOCATION_CODE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        Button trailList = (Button) findViewById(R.id.button2);
        trailList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, HikeListActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable)TrailList);
                intent.putExtra("BUNDLE",args);
                startActivity(intent);
                //intent.putExtra("trailList", TrailList);
            }
        });



//        HikingAPI hikingAPI = new HikingAPI(this);
//        hikingAPI.fetchTrailList(47.666,-117.40235);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // callback executes when user has made a choice on the alert dialogue, allow or deny
        if (requestCode == MY_LOCATION_CODE){
            if (permissions.length == 1 &&
                    permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                grantResults[0] = PackageManager.PERMISSION_GRANTED;
                // we have users permission
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationClickListener(MapsActivity.this);
            }
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        // executes when the user clicks on their blue dot
        Toast.makeText(this,
                "You're at: " + location.getLatitude() + ", " + location.getLongitude(),
                Toast.LENGTH_SHORT).show();
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
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
            }
        });

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                }
            }
        };


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // we have users permission to access their location
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    null);
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location
                            // In some rare situations this can be null
                            if (location != null) {
                                mCurrentLocation = location;
                            }
                        }
                    });

            mMap.setMyLocationEnabled(true);
        } else {
            // we don't have permission and need to request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_LOCATION_CODE);
        }


        fusedLocationProviderClient.getLastLocation();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gonzaga, 10.0f));


        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        float[] dist = new float[1];
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        Location.distanceBetween(47.666, -117.40235, latitude, longitude, dist);
        if (dist[0] / 1000 > 50) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MapsActivity.this);
            alertBuilder.setTitle("Outside Spokane")
                    .setMessage("You are currently not in Spokane. Come back to Spokane soon to experience some of the area's great hikes!")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            alertBuilder.show();
        }

        HikingAPI hikingAPI = new HikingAPI(this);
        hikingAPI.fetchTrailList(latitude,longitude);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.d("LOL", marker.getTitle());
                for (Trail trail : TrailList) {
                    // call Book's containText method
                    if (trail.getName().equals(marker.getTitle())) {
                        // assuming Book has a decent `toString()` override:
                        Intent intent = new Intent(MapsActivity.this, SingleTrail.class);
                        intent.putExtra("trail", trail);
                        startActivity(intent);
                    }
                }
            }

        });
    }

    public void receivedInterestingPhotos(List<Trail> trail) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 10.0f));
        //mMap.addMarker(new MarkerOptions()
        //        .position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
        //        .title("Home")
        //        .icon(BitmapDescriptorFactory.fromResource(R.drawable.user)));
        TrailList = trail;
        for(int i = 0; i < TrailList.size(); i++) {
            nextTrail();
        }
    }



    public void nextTrail() {
        if (TrailList != null & TrailList.size() > 0) {
            currPhotoIndex++;
            currPhotoIndex %= TrailList.size();


            Trail trail = TrailList.get(currPhotoIndex);
            lon = trail.getLongitude();
            lat = trail.getLatitude();
            Double latitude = Double.parseDouble(lat);
            Double longitude = Double.parseDouble(lon);
            String name = trail.getName();
            LatLng cool = new LatLng(latitude,longitude);
            mMap.addMarker(new MarkerOptions().position(cool).title(name));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(cool));


            //titleTextView.setText(interestingPhoto.getTitle());
            //dateTakenTextView.setText(interestingPhoto.getDateTaken());

            // GS: added after class
            //FlickrAPI flickrAPI = new FlickrAPI(this);
            //flickrAPI.fetchPhotoBitmap(interestingPhoto.getPhotoURL());
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public void refresh(View view){
        recreate();
    }

    // GS: added after class
    public void receivedPhotoBitmap(Bitmap bitmap) {
        //ImageView imageView = findViewById(R.id.imageView);
        //imageView.setImageBitmap(bitmap);
    }
}
