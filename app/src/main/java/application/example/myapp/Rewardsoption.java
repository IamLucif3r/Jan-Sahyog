package application.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Rewardsoption extends AppCompatActivity {
    String str, userid, number_report;
    FirebaseFirestore fStore;
    FirebaseAuth fauth;
    TextView textView1,textView2;
    int num;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewardsoption);
        Intent i = getIntent();
        str=i.getStringExtra("count");
        fauth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userid = Objects.requireNonNull(fauth.getCurrentUser()).getUid();
        if (str!= null && str.equalsIgnoreCase("yes")) {
            documentReference = fStore.collection("Reports").document(userid);
            documentReference.addSnapshotListener(Rewardsoption.this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    assert documentSnapshot != null;
                    number_report = documentSnapshot.getString("NumberOfReports");

                    Toast.makeText(Rewardsoption.this,number_report,Toast.LENGTH_LONG).show();

                    num = Integer.parseInt(number_report);
                    num+=1;
                    number_report = String.valueOf(num);
                    num=5-num%5;
                }
            });
        }
        String show = "You Currently Report " + number_report + " number of  reports" ;

        textView1 = findViewById(R.id.text1);
        textView2 = findViewById(R.id.text2);
        textView1.setText(show);

        show = "You need to report " + num + " number of incidents  to get rewarded";
        textView2.setText(show);

        final Map<String,Object> rep=new HashMap<>();
        rep.put("NumberOfReports",number_report);
        documentReference.set(rep).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Rewardsoption.this,"Successfully Save",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Rewardsoption.this,"ERROR"+e,Toast.LENGTH_SHORT).show();

            }
        });

    }
}

