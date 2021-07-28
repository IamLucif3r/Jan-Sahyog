package application.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class serviceHome extends AppCompatActivity {

    LinearLayout  maps;
    ImageButton qr;
    ImageView rating,job;
    CardView khata;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_home);

        khata=(CardView)findViewById(R.id.taxi);
        rating=(ImageView)findViewById(R.id.driverList);
        maps=(LinearLayout)findViewById(R.id.driver);
        job=(ImageView)findViewById(R.id.jobs);

        qr=(ImageButton)findViewById(R.id.imageButton3);
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),QRCode.class);
                startActivity(i);
            }
        });
        job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),jobs.class);
                startActivity(i);
            }
        });

        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),Driver_Rating.class);
                startActivity(i);
            }
        });




        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(i);

            }
        });

        khata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),cab1.class);
                startActivity(i);

            }
        });


    }




}
