package application.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public class jobs extends AppCompatActivity {


    ImageButton hire;
    CardView job,list,driverList;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        driverList=(CardView)findViewById(R.id.DriverList);
        driverList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),driver_list.class);
                startActivity(i);
            }
        });



        job=(CardView)findViewById(R.id.jobAvailable);
       // setContentView(R.id.jobAvailable);{
            job.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(getApplicationContext(),jobs_main.class);
                    startActivity(i);
                }
            });
        //}
        hire=(ImageButton)findViewById(R.id.imageButton);
        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),jobs_available.class);
                startActivity(i);
            }
        });
    }
}
