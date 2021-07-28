package application.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Dashboard extends AppCompatActivity {

    
    FirebaseAuth fauth;
    /* FirebaseFirestore fStore;
     Button resendCode;
     TextView verifymsg;
     String userid;*/
    Button logout;
    LinearLayout msos,mservice,mprofile,mreward,mtask,mreport;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* setContentView(R.layout.activity_dashboard);
        fauth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        verifymsg=findViewById(R.id.textView11);
        resendCode=findViewById(R.id.button3);
        userid=fauth.getCurrentUser().getUid();
        final FirebaseUser user=fauth.getCurrentUser();
        if(!user.isEmailVerified()){
            verifymsg.setVisibility(View.VISIBLE);
            resendCode.setVisibility(View.VISIBLE);

            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    FirebaseUser fuser=fauth.getCurrentUser();
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(),"Verification Email has been resent to the registered Email id",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Dashboard.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag","onFailure: Error Occured!"+e.getMessage());
                        }
                    });
                }
            });
        }*/
        setContentView(R.layout.activity_dashboard);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Dashboard.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},111);
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Dashboard.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},112);
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Dashboard.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},113);
        }



        Toast.makeText(Dashboard.this,"Always Keep Your Profile Updated ;)",Toast.LENGTH_SHORT).show();


        msos=(LinearLayout)findViewById(R.id.sos);
        mservice=(LinearLayout)findViewById(R.id.services);

        mprofile=(LinearLayout)findViewById(R.id.profile);
        mreport=(LinearLayout)findViewById(R.id.report);
        mreward=(LinearLayout)findViewById(R.id.reward);
        mtask=(LinearLayout)findViewById(R.id.task);
        logout=(Button)findViewById(R.id.button5);

        msos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),sos_mainscreen.class);
                startActivity(i);
                return;

            }
        });

        mprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),ProfileView.class);
                startActivity(i);

                return;

            }
        });
        mservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),serviceHome.class);
                startActivity(i);
            }
        });


        mtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),task_main.class);
                startActivity(i);
            }
        });

       logout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               FirebaseAuth.getInstance().signOut();
               //Intent i=new Intent(getApplicationContext(),Login.class);
               startActivity( new Intent(getApplicationContext(),Login.class));
               finish();

           }
       });
       mreport.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i=new Intent(getApplicationContext(),Reporting1.class);
               startActivity(i);
               return;
           }
       });
        mreward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),GetReward.class);
                startActivity(i);

            }
        });

    
    }
}
