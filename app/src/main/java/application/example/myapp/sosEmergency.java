package application.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class sosEmergency extends AppCompatActivity implements LocationListener {
    protected LocationManager locationManager;
    protected Context context;
    //String lat,str="";
    String provider;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    String number = "8573928440";
    public static String text="hello",str="",text1="city";
    double lat,lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos_emergency);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0.1F, (LocationListener) this);
        //str="Help Require at " + text +
        //       "Please go to the link https://gps-coordinates.org/coordinate-converter.php to check the location";

    }
    private String getCityName(LatLng myCoordinates) {
        String myCity = "";
        Geocoder geocoder = new Geocoder(sosEmergency.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            //myCity=addresses.get(0).getFeatureName();
            //myCity+=" ";
            myCity= addresses.get(0).getLocality();
            myCity+=" ";
            myCity+=addresses.get(0).getSubAdminArea();
            myCity+=" ";
            myCity+=addresses.get(0).getAdminArea();
            myCity+=" ";
            myCity+=addresses.get(0).getCountryName();
            myCity+=" ";
            myCity+=text;

            Log.d("mylog", "Complete Address: " + addresses.toString());
            Log.d("mylog", "Address: " + address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCity;
    }

    public void fire(View view) {
        SmsManager sm = SmsManager.getDefault();
        sm.sendTextMessage(number, null, str, null, null);
        Toast.makeText(getApplicationContext(), "Message Sent successfully to fire_station!",Toast.LENGTH_LONG).show();

    }

    public void ambulance(View view) {
        Context context = getApplicationContext();
        CharSequence text = str;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        if (str.length() > 20) {
            SmsManager sm = SmsManager.getDefault();
            sm.sendTextMessage(number, null, str, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent successfully to ambulance!", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Message not Sent to ambulance!", Toast.LENGTH_LONG).show();
        }
    }

    public void police(View view) {
        SmsManager sm = SmsManager.getDefault();
        sm.sendTextMessage(number, null, str, null, null);
        Toast.makeText(getApplicationContext(), "Message Sent successfully to police_station!",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onLocationChanged(Location location) {
        text=("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        DecimalFormat df = new DecimalFormat();
        double lat = Double.parseDouble(df.format(location.getLatitude()));
        double lon = Double.parseDouble(df.format(location.getLongitude()));

        LatLng myCoordinates = new LatLng(lat,lon);
        text1 = getCityName(myCoordinates);
        str="Emergency!! Immediate Help Required at " + text1;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }
}
