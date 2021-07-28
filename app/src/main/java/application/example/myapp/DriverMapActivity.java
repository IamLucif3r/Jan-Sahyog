package application.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.GoogleApiAvailabilityCache;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.common.net.InternetDomainName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public class DriverMapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener   //,RoutingListener
        {

            FirebaseAuth fAuth;
            private GoogleMap mMap;
            GoogleApiClient mGoogleApiClient;
            Location mLastLocation;
            LocationRequest mLocationRequest;
            private Button mlogout;
            private  String customerId="";
            private boolean isLoggingOut=false;
            private LinearLayout mCustomerInfo;
            private ImageView mCustomerProfileImage;
            private TextView mCustomerName,mCustomerPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        fAuth=FirebaseAuth.getInstance();
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(DriverMapActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},111);
        }else{
            mapFragment.getMapAsync(this);
        }

        mCustomerInfo=(LinearLayout) findViewById(R.id.customerInfo);

        mCustomerProfileImage=(ImageView) findViewById(R.id.customerProfileImage);

        mCustomerName=(TextView) findViewById(R.id.customerName);

        mCustomerPhone=(TextView) findViewById(R.id.customerPhone);




        mlogout=(Button)findViewById(R.id.logout);
        mlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoggingOut=true;
                disconnectDriver();
                FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(getApplicationContext(),cab1.class);
                startActivity(i);
                finish();
                return;
            }
        });
        getAssignedCustomer();
    }
            private void getAssignedCustomer(){
                String driverId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                final DatabaseReference assignedCustomerRef=FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverId).child("customerRideId");
                assignedCustomerRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            customerId=dataSnapshot.getValue().toString();
                            getAssignedCustomerPickupLocation();
                            getAssignedCustomerInfo();

                        }else
                        {
                            customerId="";
                            if(pickupMarker!=null){
                                pickupMarker.remove();
                            }
                            if(assignedCustomerPickupLocationRefListener!=null) {
                                assignedCustomerPickupLocationRef.removeEventListener(assignedCustomerPickupLocationRefListener);
                            }
                            mCustomerInfo.setVisibility(View.GONE);
                            mCustomerName.setText("");
                            mCustomerPhone.setText("");
                            mCustomerProfileImage.setImageResource(R.drawable.ic_account_box_black_24dp);


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }); }

            private void getAssignedCustomerInfo(){

                mCustomerInfo.setVisibility(View.VISIBLE);

                DatabaseReference mCustomerDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(customerId);
                mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()&&dataSnapshot.getChildrenCount()>0){
                            Map<String ,Object> map =(Map<String, Object>)dataSnapshot.getValue();
                            if(map.get("name")!=null){
                                mCustomerName.setText(map.get("name").toString());
                            }
                            if(map.get("phone")!=null) {
                                mCustomerPhone.setText( map.get("phone").toString());
                            }
                            if(map.get("ProfileImageUrl")!=null){
                                //Any ISsues related to CUSTOMER PROFILE IMAGE ?? HERE IS THE PROBLEM
                                StorageReference storageReference =  FirebaseStorage.getInstance().getReference();
                                final StorageReference profileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "/ProfilePic");

                                // Picasso.get().load("ProfileImageUrl").into(mCustomerProfileImage);
                                Glide.with(getApplicationContext()).load(map.get("ProfileImageUrl").toString()).into(mCustomerProfileImage);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }




            Marker pickupMarker;
            private DatabaseReference assignedCustomerPickupLocationRef;
            private  ValueEventListener assignedCustomerPickupLocationRefListener;
            private  void  getAssignedCustomerPickupLocation(){
                assignedCustomerPickupLocationRef=FirebaseDatabase.getInstance().getReference().child("customerRequest").child(customerId).child("l");
                assignedCustomerPickupLocationRefListener=assignedCustomerPickupLocationRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists() && !customerId.equals("")){
                            List<Object> map=(List<Object>) dataSnapshot.getValue();
                            double locationLat=0;
                            double locationLng=0;
                            if(map.get(0)!=null){
                                locationLat=Double.parseDouble(map.get(0).toString());
                            }
                            if(map.get(1)!=null){
                                locationLng=Double.parseDouble(map.get(1).toString());
                            }
                            LatLng driverLatlng=new LatLng(locationLat,locationLng);
                            pickupMarker= mMap.addMarker(new MarkerOptions().position(driverLatlng).title("pickUp location"));
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
                if(getApplicationContext()!=null) {
                    mLastLocation = location;
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

                    String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference refAvailable = FirebaseDatabase.getInstance().getReference("driversAvailable");
                    DatabaseReference refWorking = FirebaseDatabase.getInstance().getReference("driversWorking");
                    GeoFire geoFireAvailable = new GeoFire(refAvailable);
                    GeoFire geoFireWorking = new GeoFire(refWorking);



                    switch (customerId){
                        case "":
                            geoFireWorking.removeLocation(userid, new GeoFire.CompletionListener() {
                                @Override
                                public void onComplete(String key, DatabaseError error) {

                                }
                            });

                            geoFireAvailable.setLocation(userid, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
                                @Override
                                public void onComplete(String key, DatabaseError error) {

                                }
                            });
                            break;
                        default:
                            geoFireAvailable.removeLocation(userid, new GeoFire.CompletionListener() {
                                @Override
                                public void onComplete(String key, DatabaseError error) {

                                }
                            });

                            geoFireWorking.setLocation(userid, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
                                @Override
                                public void onComplete(String key, DatabaseError error) {

                                }
                            });

                            break;
                    }




                }
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



            private void disconnectDriver(){
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("driversAvailable");
                GeoFire geoFire = new GeoFire(ref);
                geoFire.removeLocation(userid, new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {

                    }
                });
            }
            @Override
            protected void onStop() {
                super.onStop();
                if (!isLoggingOut) {
                    disconnectDriver();

                }
            }

        }