package application.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.protobuf.StringValue;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class sos_sos extends AppCompatActivity {

    CheckBox police,ambulance,fStation,c1,c2,c3;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    Button send;
    protected LocationManager locationManager;
    double latitude,longitude;
    String address;
    FusedLocationProviderClient fusedLocationProviderClient;
    String message="HELP ME! I Need Your Help here:\n";
    String exloc="\n",space=" ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos_sos);

        fAuth=FirebaseAuth.getInstance();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        fStore=FirebaseFirestore.getInstance();
        String userid=fAuth.getCurrentUser().getUid();
        send= findViewById(R.id.button12);

        police= findViewById(R.id.checkBox2);
        ambulance= findViewById(R.id.checkBox3);
        fStation= findViewById(R.id.checkBox4);
        c1= findViewById(R.id.checkBox5);
        c2= findViewById(R.id.checkBox6);
        c3= findViewById(R.id.checkBox7);

        c1.setChecked(true);
        c2.setChecked(true);
        c3.setChecked(true);
        police.setChecked(true);
        ambulance.setChecked(true);
        fStation.setChecked(true);


        fStore=FirebaseFirestore.getInstance();
        DocumentReference documentReference ;
        documentReference = fStore.collection("UsersInformation").document(userid);
        documentReference.addSnapshotListener(sos_sos.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                assert documentSnapshot != null;
                c1.setText(documentSnapshot.getString("EmergencyContact1:"));
                c2.setText(documentSnapshot.getString("EmergencyContact2:"));
                c3.setText(documentSnapshot.getString("EmergencyContact3:"));

            }

        });
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(sos_sos.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        try {
                            Geocoder geocoder = new Geocoder(sos_sos.this, Locale.getDefault());

                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                            latitude = addresses.get(0).getLatitude();
                            longitude=addresses.get(0).getLongitude();
                            address=addresses.get(0).getAddressLine(0).trim();



                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        } else {
            ActivityCompat.requestPermissions(sos_sos.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

    }


    public void Send_message(View view) {
        String lat= String.valueOf(latitude).trim();
        String lng=String.valueOf(longitude).trim();

        if(c1.isChecked()==true) {
            SmsManager sm1 = SmsManager.getDefault();
            sm1.sendTextMessage( "+91"+c1.getText().toString(), null, message+address+exloc+lat+space+lng, null, null);
            Toast.makeText(this,"Sent to "+c1.getText().toString(),Toast.LENGTH_SHORT).show();
        }
        if(c2.isChecked()==true) {
            SmsManager sm1 = SmsManager.getDefault();
            sm1.sendTextMessage("+91"+c2.getText().toString(), null, message+address+exloc+lat+space+lng, null, null);
            Toast.makeText(this,"Sent to "+c2.getText().toString(),Toast.LENGTH_SHORT).show();

        }
        if(c3.isChecked()==true) {
            SmsManager sm1 = SmsManager.getDefault();
            sm1.sendTextMessage("+91"+c3.getText().toString(), null, message+address+exloc+lat+space+lng, null, null);
            Toast.makeText(this,"Sent to "+c3.getText().toString(),Toast.LENGTH_SHORT).show();

        }
        if(police.isChecked()==true) {
            SmsManager sm1 = SmsManager.getDefault();
            sm1.sendTextMessage("+91"+"9696227125", null, message+address+exloc+lat+space+lng, null, null);
            Toast.makeText(this,"Sent to "+"Police Station",Toast.LENGTH_SHORT).show();

        }
        if(ambulance.isChecked()==true) {
            SmsManager sm1 = SmsManager.getDefault();
            sm1.sendTextMessage("+91"+"8573928440", null, message+address+exloc+lat+space+lng, null, null);
            Toast.makeText(this,"Sent to "+"Hospital",Toast.LENGTH_SHORT).show();

        }
        if(fStation.isChecked()==true) {
            SmsManager sm1 = SmsManager.getDefault();
            sm1.sendTextMessage("+91"+"8573928440", null, message+address+exloc+lat+space+lng, null, null);
            Toast.makeText(this,"Sent to "+"Fire Station",Toast.LENGTH_SHORT).show();

        }

        Intent i=new Intent(this,sos_mainscreen.class);
        startActivity(i);
        finish();
        return;
    }
}
