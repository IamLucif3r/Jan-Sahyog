package application.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
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

public class Ratings extends AppCompatActivity {

    RatingBar safety,behavior,ride,rules,avg;
    Button submit;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        firebaseAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        safety=(RatingBar)findViewById(R.id.ratingBar);
        behavior=(RatingBar)findViewById(R.id.ratingBar2);
        ride=(RatingBar)findViewById(R.id.ratingBar3);
        rules=(RatingBar)findViewById(R.id.ratingBar4);
        avg=(RatingBar)findViewById(R.id.ratingBar5);

        submit=(Button)findViewById(R.id.button19);

        safety.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String a=String.valueOf(safety.getRating());
                Toast.makeText(getApplicationContext(),a+" Star for Driving Safety",Toast.LENGTH_SHORT).show();

            }
        });
        behavior.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String b=String.valueOf(behavior.getRating());
                Toast.makeText(getApplicationContext(),b+" Star for Driver's Behavior",Toast.LENGTH_SHORT).show();
            }
        });
        ride.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String c=String.valueOf(ride.getRating());
                Toast.makeText(getApplicationContext(),c+" Star for overall Ride",Toast.LENGTH_SHORT).show();

            }
        });

        rules.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String d=String.valueOf(rules.getRating());
                Toast.makeText(getApplicationContext(),d+" Star for following Traffic Rules",Toast.LENGTH_SHORT).show();

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String a=String.valueOf(safety.getRating());
              String b=String.valueOf(behavior.getRating());
              String c=String.valueOf(ride.getRating());
              String d=String.valueOf(rules.getRating());
              Double sum=(Double.parseDouble(a))+(Double.parseDouble(b))+(Double.parseDouble(c))+(Double.parseDouble(d));
                final float average= (float) (sum/4);
                avg.setRating(average);
                avg.setVisibility(View.VISIBLE);
                String userid=firebaseAuth.getCurrentUser().getUid();

                fStore.collection("UsersInformation").document(userid).update("Rating",average);
            }
        });
    }

}
