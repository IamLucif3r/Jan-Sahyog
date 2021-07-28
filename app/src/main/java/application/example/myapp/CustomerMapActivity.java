package application.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public class CustomerMapActivity extends FragmentActivity implements OnMapReadyCallback , GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private Button mlogout,mrequest,msettings;
    private LatLng pickupLocation;
    private  Boolean requestBol=false;
    private Marker pickupMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mrequest=(Button)findViewById(R.id.request);
        msettings=(Button)findViewById(R.id.settings);
        mlogout=(Button)findViewById(R.id.logout);
        mlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(getApplicationContext(),cab1.class);
                startActivity(i);
                finish();
                return;
            }
        });
        mrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(requestBol){
                    requestBol=false;
                    geoQuery.removeAllListeners();
                    driverLocationref.removeEventListener(driverLocationRefListener);

                    if(driverFoundID!=null){
                        DatabaseReference driverRef=FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverFoundID); //.child("customerRequest");
                        driverRef.setValue(true);
                        driverFoundID=null;

                    }
                    driverFound=false;
                    radius=1;
                    String userid=FirebaseAuth.getInstance().getCurrentUser().getUid();

                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference("customerRequest");
                    GeoFire geoFire=new GeoFire(ref);
                    geoFire.removeLocation(userid, new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {

                        }
                    });
                    if(pickupMarker!=null){
                        pickupMarker.remove();
                    }
                    mrequest.setText("Call for a Cab");

                }
                else{

                    requestBol=true;
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference("customerRequest");
                    GeoFire geoFire=new GeoFire(ref);
                    geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {

                        }
                    });

                    pickupLocation= new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                    pickupMarker=mMap.addMarker(new MarkerOptions().position(pickupLocation).title("PickUp Here"));
                    mrequest.setText("Getting Your Driver ");


                    getClosestDriver();
                }

            }
        });
        msettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(CustomerMapActivity.this,CustomerSettingsActivity.class);
                startActivity(i);
                return;
            }
        });


    }
    private int radius=1;
    private Boolean driverFound=false;
    private String driverFoundID;
    GeoQuery geoQuery;
    private void getClosestDriver(){
        DatabaseReference driverLocation=FirebaseDatabase.getInstance().getReference().child("driversAvailable");

        GeoFire geoFire=new GeoFire(driverLocation);
        geoQuery=geoFire.queryAtLocation(new GeoLocation(pickupLocation.latitude,pickupLocation.longitude),radius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if(!driverFound && requestBol){
                    driverFound=true;
                    driverFoundID=key;

                    DatabaseReference driverRef=FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverFoundID); //.child("customerRequest");
                    String customerId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                    HashMap map=new HashMap();
                    map.put("customerRideId",customerId);
                    driverRef.updateChildren(map);
                    getDriverLocation();
                    mrequest.setText("Looking For Driver's Location");

                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

                if(!driverFound)
                {
                    radius++;
                    getClosestDriver();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });


    }
    private Marker mDriverMarker;
    private DatabaseReference driverLocationref;
    private ValueEventListener driverLocationRefListener;
    private void getDriverLocation(){
        driverLocationref=FirebaseDatabase.getInstance().getReference().child("driversWorking").child(driverFoundID).child("l");
        driverLocationRefListener=driverLocationref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()&& requestBol){
                    List<Object> map=(List<Object>) dataSnapshot.getValue();
                    double locationLat=0;
                    double locationLng=0;
                    //  mrequest.setText("Driver Found");
                    if(map.get(0)!=null){
                        locationLat=Double.parseDouble(map.get(0).toString());
                    }
                    if(map.get(1)!=null){
                        locationLng=Double.parseDouble(map.get(1).toString());
                    }
                    LatLng driverLatlng=new LatLng(locationLat,locationLng);
                    if(mDriverMarker!=null){
                        mDriverMarker.remove();
                    }
                    Location loc1=new Location("");
                    loc1.setLatitude(pickupLocation.latitude);
                    loc1.setLongitude(pickupLocation.longitude);

                    Location loc2=new Location("");
                    loc2.setLatitude(driverLatlng.latitude);
                    loc2.setLongitude(driverLatlng.longitude);

                    float distance=loc1.distanceTo(loc2);
                    if(distance<100){
                        mrequest.setText("Driver is here" );
                    }
                    else{
                        mrequest.setText("Driver Found:"+String.valueOf(distance));

                    }



                    mDriverMarker=mMap.addMarker(new MarkerOptions().position(driverLatlng).title("Your Driver"));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            return;
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);

    }
    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation=location;
        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));



    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest=new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}