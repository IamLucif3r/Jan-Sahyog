package application.example.myapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("ALL")
public class QRCodeScanner extends AppCompatActivity {

    CodeScanner codeScanner;
    CodeScannerView scannView;
    TextView resultData,name,regNum;
    FirebaseAuth fauth;
    FirebaseUser user;

    FirebaseFirestore fStore;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_code_scanner);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(QRCodeScanner.this,new String[]{Manifest.permission.CAMERA},111);
        }


        scannView = findViewById(R.id.scannerView);
        codeScanner = new CodeScanner(this,scannView);
        resultData = findViewById(R.id.resultsOfQr);
        name=findViewById(R.id.resultsOfQr2);
        fauth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
       regNum=(TextView)findViewById(R.id.resultsOfQr3);
        final Calendar calendar = Calendar.getInstance();
        final String date = DateFormat.getInstance().format(calendar.getTime());
       // TextView tvDate = findViewById(R.id.textView43);
       // tvDate.setText(date);



        String userid=fauth.getCurrentUser().getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("TravelHistory").child(userid);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultData.setText(result.getText());

                      final String userID=result.getText().toString();
                        String dat=date.toString();
                        DatabaseReference save=mDatabase.push();
                        save.child("DateTime").setValue(dat);
                        save.child("UserID").setValue(userID);


                        FirebaseFirestore q=FirebaseFirestore.getInstance();
                        final DocumentReference documentReference= q.collection("UsersInformation").document(userID);
                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                name.setText(documentSnapshot.getString("FirstName:"));
                                regNum.setText(documentSnapshot.getString("VehicleRegisterationNo:"));
                               // String rating= (String) documentSnapshot.get("Rating");

                               // ratingBar.setRating(Float.parseFloat(rating));
//                                ratingBar.setRating(Float.parseFloat(documentSnapshot.getString("Rating")));
                            }
                        });

                    }
                });

            }
        });


        scannView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //codeScanner.startPreview();
       requestForCamera();

    }

   public void requestForCamera() {
       Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
           @Override
           public void onPermissionGranted(PermissionGrantedResponse response) {
               codeScanner.startPreview();
           }

           @Override
           public void onPermissionDenied(PermissionDeniedResponse response) {
               Toast.makeText(QRCodeScanner.this, "Camera Permission is Required.", Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
               token.continuePermissionRequest();

           }
       }).check();

   }}