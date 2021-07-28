package application.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class sosContact extends Activity implements  LocationListener {
    String str1="";
    String Contact1,Contact2,Contact3;
    CheckBox C1,C2,C3,C4;
    FirebaseAuth fAuth;

    private FirebaseDatabase Database = FirebaseDatabase.getInstance();
    private DatabaseReference root,myref;
    protected LocationManager locationManager;
    protected Context context;
    //String number = "9027555638";
    public static String text="hello",str="",text1="city",mesg="problem";
    //double lat,lon;
    boolean number1=false,number2=false,number3=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos_contact);
        fAuth=FirebaseAuth.getInstance();
        C1=(CheckBox)findViewById(R.id.checkbox1);
        C2=(CheckBox)findViewById(R.id.checkbox2);
        C3=(CheckBox)findViewById(R.id.checkbox3);
        C4=(CheckBox)findViewById(R.id.checkbox4);
        Intent i = getIntent();
        mesg = i.getStringExtra("message");
        str1+="Your message:" + mesg ;
        Context context = getApplicationContext();
        CharSequence text = str1;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context,text,duration);
        toast.show();
        //user = auth.getCurrentUser();
        root = Database.getReference();
        String userid=fAuth.getCurrentUser().getUid();
        myref = root.child("UsersInformation").child(userid);
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren())
                {
                    Contact1 = dataSnapshot.child("EmergencyContact1:").getValue().toString();
                    Contact2 = dataSnapshot.child("EmergencyContact2:").getValue().toString();
                    Contact3 = dataSnapshot.child("EmergencyContact3:").getValue().toString();
                    Toast.makeText(sosContact.this,"Ambulance: " + Contact1 + "Fire_station: "
                            + Contact2 + "Police: " + Contact3, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });



        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0.1F, (LocationListener) this);

    }

    private String getCityName(LatLng myCoordinates) {
        String myCity = "";
        Geocoder geocoder = new Geocoder(sosContact.this, Locale.getDefault());
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


    public void sos_contact(View view) {
        Intent intent = new Intent(this,sosconfirm.class);
        intent.putExtra("message",str1);
        startActivity(intent);

        if(number1==true) {
            SmsManager sm1 = SmsManager.getDefault();
            sm1.sendTextMessage(Contact1, null, str+mesg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent successfully to Contact1!", Toast.LENGTH_LONG).show();
        }
        if(number2==true)
        {
            SmsManager sm2 = SmsManager.getDefault();
            sm2.sendTextMessage(Contact2, null, str+mesg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent successfully to Contact2!", Toast.LENGTH_LONG).show();
        }

        if(number3==true) {
            SmsManager sm3 = SmsManager.getDefault();
            sm3.sendTextMessage(Contact3, null, str+mesg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent successfully to Contact3!", Toast.LENGTH_LONG).show();
        }
    }

    public void selectC1(View view)
    {
        if(C1.isChecked())
        {
            C2.setChecked(true);
            C3.setChecked(true);
            C4.setChecked(true);
            str1+= " is transfer to " +C2.getText().toString() + "," + C3.getText().toString() +" and " + C4.getText().toString();
            number1=true;
            number2=true;
            number3=true;
        }
        if(C2.isChecked() && !C1.isChecked())
        {
            str1+= " is transfer to " +C2.getText().toString();
            number1=true;
        }
        if(C3.isChecked() && !C1.isChecked())
        {
            str1+= " is transfer to " +C3.getText().toString();
            number2=true;

        }
        if(C4.isChecked() && !C1.isChecked())
        {
            str1+= " is transfer to " +C4.getText().toString();
            number3=true;
        }

    }
    @Override
    public void onLocationChanged(Location location) {
        text=("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        DecimalFormat df = new DecimalFormat();
        double lat = Double.parseDouble(df.format(location.getLatitude()));
        double lon = Double.parseDouble(df.format(location.getLongitude()));

        LatLng myCoordinates = new LatLng(lat,lon);
        text1 = getCityName(myCoordinates);
        str="Help Require at " + text1;
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
