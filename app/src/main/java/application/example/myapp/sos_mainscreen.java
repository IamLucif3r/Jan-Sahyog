package application.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class sos_mainscreen extends AppCompatActivity {
Button back;
ImageView SOS,emergency,police,ambulance,fire,women,child,accident,animal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos_mainscreen);
        back=(Button)findViewById(R.id.button8);
        SOS=(ImageView)findViewById(R.id.imageView2);
        emergency=(ImageView)findViewById(R.id.imageView3);
        police=(ImageView)findViewById(R.id.imageView4);
        ambulance=(ImageView)findViewById(R.id.imageView5);
        fire=(ImageView)findViewById(R.id.imageView6);
        women=(ImageView)findViewById(R.id.imageView7);
        accident=(ImageView)findViewById(R.id.imageView8);
        child=(ImageView)findViewById(R.id.imageView9);
        animal=(ImageView)findViewById(R.id.imageView10);

        if (ContextCompat.checkSelfPermission(sos_mainscreen.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(sos_mainscreen.this, new String[]{Manifest.permission.SEND_SMS}, 1000);

        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(sos_mainscreen.this,new String[]{Manifest.permission.CALL_PHONE},114);
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Dashboard.class);
                startActivity(i);
                return;
            }
        });
        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),sos_sos.class);
                startActivity(i);
                return;
            }
        });

        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:112"));


                if(ActivityCompat.checkSelfPermission(sos_mainscreen.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    return;
                }
                startActivity(i);
            }
        });

        police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:100"));

                if(ActivityCompat.checkSelfPermission(sos_mainscreen.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    return;
                }
                startActivity(i);
            }
        });
        ambulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:102"));

                if(ActivityCompat.checkSelfPermission(sos_mainscreen.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    return;
                }
                startActivity(i);
            }
        });
        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:101"));

                if(ActivityCompat.checkSelfPermission(sos_mainscreen.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    return;
                }
                startActivity(i);
            }
        });
        women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:1091"));

                if(ActivityCompat.checkSelfPermission(sos_mainscreen.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    return;
                }
                startActivity(i);
            }
        });
        accident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:1033"));

                if(ActivityCompat.checkSelfPermission(sos_mainscreen.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    return;
                }
                startActivity(i);
            }
        });
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:1098"));

                if(ActivityCompat.checkSelfPermission(sos_mainscreen.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    return;
                }
                startActivity(i);
            }
        });
        animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:9820122602"));

                if(ActivityCompat.checkSelfPermission(sos_mainscreen.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    return;
                }
                startActivity(i);
            }
        });




    }
}
