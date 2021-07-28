package application.example.myapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class About_Driver extends AppCompatActivity {

    String userid;
    TextView name,contactno;
    FirebaseFirestore fstore;
    RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about__driver);
        Intent i = getIntent();
        String Detail =i.getStringExtra("list");
        Toast.makeText(About_Driver.this, "String: " +Detail, Toast.LENGTH_SHORT).show();
        assert Detail != null;
        int length= Detail.length();
        userid = Detail.substring(length-28);
        name=findViewById(R.id.textView1);
        contactno=findViewById(R.id.textView2);
        ratingBar=findViewById(R.id.Rating_bar);
        fstore= FirebaseFirestore.getInstance();
        DocumentReference documentReference = fstore.collection("UsersInformation").document(userid);
        documentReference.addSnapshotListener(About_Driver.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                assert documentSnapshot != null;
                name.setText(documentSnapshot.getString("FirstName:"));
                contactno.setText(documentSnapshot.getString("Contact"));

            }
        });
    }

    public void rate(View view) {
        String str="Your Rating is " + ratingBar.getRating();
        Toast.makeText(About_Driver.this,str,Toast.LENGTH_LONG).show();

    }
}