package application.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
//import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("ALL")
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    int PROXIMITY_RADIUS=10000;
    double latitude,longitude;
    private GoogleMap mMap;
    private LocationRequest locationRequest;
    private  Location lastLocation;
    private Marker currentLocationMarker;
    public static final int REQUEST_LOCATION_CODE=99;
    GoogleApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_LOCATION_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        if (client==null){
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }else
                {
                    Toast.makeText(MapsActivity.this,"Permission Denied",Toast.LENGTH_SHORT).show();
                }
                return;
        }
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

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {

            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }
    protected synchronized void buildGoogleApiClient(){
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        client.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation= location;
        LatLng latLng=new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
        if(currentLocationMarker!=null){
            currentLocationMarker.remove();
        }

        MarkerOptions markerOptions =new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Your Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentLocationMarker=mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));


        if(client!=null){
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);

        }
    }


    public void onClick(View v) {
        Object dataTransfer[]=new Object[2];

        GetNearbyPlacesData getNearbyPlacesData=new GetNearbyPlacesData();

        switch (v.getId())
        {
            case R.id.B_search:
             {
                EditText tf_Location = (EditText) findViewById(R.id.TF_location);
                String location = tf_Location.getText().toString();
                List<Address> addressList = null;
                MarkerOptions markerOptions = new MarkerOptions();

                if (!location.equals(""))
                {
                Geocoder geocoder = new Geocoder(this);
                try {
                    addressList = geocoder.getFromLocationName(location, 5);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < addressList.size(); i++) {
                    mMap.clear();
                    Address myAddress = addressList.get(i);
                    LatLng latLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                    markerOptions.position(latLng);
                    markerOptions.title("Here You go! ");
                    mMap.addMarker(markerOptions);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                }

            }
        }break;

            case R.id.B_hospital:
                mMap.clear();
                latitude=lastLocation.getLatitude();
                longitude=lastLocation.getLongitude();
                //LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());

                String hospital="Hospital";
                String url=getUrl(latitude,longitude,hospital);
                dataTransfer[0]=mMap;
                dataTransfer[1]=url;
                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this,"Showing Nearby Hospitals",Toast.LENGTH_LONG).show();
                break;

            case R.id.B_police:
                mMap.clear();
                String police="Police";
                url=getUrl(latitude,longitude,police);

                dataTransfer[0]=mMap;
                dataTransfer[1]=url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this,"Showing Nearby Police Stations ",Toast.LENGTH_LONG).show();
                break;

            case R.id.B_petrol:
                mMap.clear();
                String petrolpump="Petrol";
                url=getUrl(latitude,longitude,petrolpump);

                dataTransfer[0]=mMap;
                dataTransfer[1]=url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this,"Showing Nearby Petrol Pumps",Toast.LENGTH_LONG).show();
                break;

    }
    }
    @NotNull
    private String getUrl(double latitude, double longitude, String nearbyPlace){
        StringBuilder googlePlaceUrl= new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
   //     googlePlaceUrl.append("location="+25.435801+","+81.846313);

        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&name="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyCAnCtTyVC8rDm2d8C_Qsg0RtVmjsZ8LoI");

        return googlePlaceUrl.toString();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest=new LocationRequest();

        locationRequest.setInterval(15*1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(client,locationRequest, this);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){

        }
    }

    @Override
    public void onConnectionSuspended(int i) {


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    public boolean checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
     if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
         ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
     }
     else{
         ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);

     }return false;
        }
        return false;
    }

}
