package application.example.myapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

@SuppressWarnings("ALL")
public class ProfileView extends AppCompatActivity {

    StorageReference storageReference;
    TextView name,email,contactno,address,lisno,adharno,dob;
    FirebaseAuth fauth;
    FirebaseFirestore fStore;
    ImageView dp;
    Button update;
    Uri imageuri;
    String userid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        dp = findViewById(R.id.dp);
        name = findViewById(R.id.textView20);
        email = findViewById(R.id.textView21);
        contactno = findViewById(R.id.textView23);
        address = findViewById(R.id.textView24);
        lisno = findViewById(R.id.textView25);
        update = (Button) findViewById(R.id.button7);
        adharno = findViewById(R.id.textView26);
        dob = findViewById(R.id.textView27);
        fauth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userid = fauth.getCurrentUser().getUid();

        storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference profileRef = storageReference.child("users/" + fauth.getCurrentUser().getUid() + "/ProfilePic");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(dp);

            }
        });


        DocumentReference documentReference = fStore.collection("UsersInformation").document(userid);
        documentReference.addSnapshotListener(ProfileView.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                name.setText(documentSnapshot.getString("FirstName:"));

                contactno.setText(documentSnapshot.getString("Contact"));
                address.setText(documentSnapshot.getString("Address:"));
                lisno.setText(documentSnapshot.getString("LicenseNo"));

            }


        });
        documentReference = fStore.collection("users").document(userid);
        documentReference.addSnapshotListener(ProfileView.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                email.setText(documentSnapshot.getString("Email"));
                adharno.setText(documentSnapshot.getString("ID"));
                dob.setText(documentSnapshot.getString("DOB"));

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), profile1.class);
                startActivity(i);
                finish();
            }
        });

    }
    }

