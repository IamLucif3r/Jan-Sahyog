package application.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class home extends AppCompatActivity {

    FirebaseAuth fauth;
    FirebaseFirestore fStore;
    Button resendCode;
    TextView verifymsg;
    String userid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
                            startActivity(new Intent(getApplicationContext(),home.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag","onFailure: Error Occured!"+e.getMessage());
                        }
                    });
                }
            });
        }
    }
}
