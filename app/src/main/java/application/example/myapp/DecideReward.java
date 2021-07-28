package application.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DecideReward extends AppCompatActivity {
    FirebaseFirestore fStore;
    String userid,first,second,third;
    FirebaseAuth fauth;
    TextView txt1;
    EditText ed1;

    String str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decide_reward);
        Intent i = getIntent();

        first = i.getStringExtra("reward1");
        second = i.getStringExtra("reward2");
        third = i.getStringExtra("reward3");
        fStore = FirebaseFirestore.getInstance();
        fauth = FirebaseAuth.getInstance();
        userid = fauth.getCurrentUser().getUid();
        txt1 = findViewById(R.id.textView1);
        ed1=findViewById(R.id.editText);
        str = "You are claiming : ";
        if(first=="" || first==null)
            first="false";
        if(second=="" || second==null)
            second="false";
        if(third=="" || third==null)
            third="false";
        assert first != null;
        if (first.equals("true"))
            str += "You reported 5 incidents ";
        assert second != null;
        if (second.equals("true"))
            str += ".....You are having more than 4 stars in your performance rating ";
        if (third.equals("true"))
            str += ".....You work for extra hours";
        txt1.setText(str);
    }


    public void reward_decide(View view) {
        DocumentReference dr = fStore.collection("Rewards").document(userid);
        final Map<String, Object> rep = new HashMap<>();
        rep.put("claim_for", str);
        rep.put("Account_Number",ed1.getText().toString());

        dr.set(rep).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(DecideReward.this, "Successfully Save", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DecideReward.this, "ERROR" + e, Toast.LENGTH_SHORT).show();

                Intent i = new Intent(DecideReward.this, Dashboard.class);
                startActivity(i);
            }
        });
    }
}