package application.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class signup extends AppCompatActivity {

    RadioButton r1, r2,driver,customer;
    TextView tv10;
    EditText et, et2, et3, et4, et5, et6, et7, et8, et9;
    Button btn, btn2;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        driver=(RadioButton)findViewById(R.id.radioButton);
        customer=(RadioButton)findViewById(R.id.radioButton5);
        et = findViewById(R.id.editText);
        et2 = findViewById(R.id.editText2);
        et3 = findViewById(R.id.editText3);
        et4 = findViewById(R.id.editText4);
        et5 = findViewById(R.id.editText5);
        et6 = findViewById(R.id.editText6);
        et7 = findViewById(R.id.editText7);
        et8 = findViewById(R.id.editText8);
        et9 = findViewById(R.id.editText9);
        r1 = findViewById(R.id.radioButton2);
        r2 = findViewById(R.id.radioButton3);
        tv10 = findViewById(R.id.textView10);
        btn2 = findViewById(R.id.button2);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    final String fullname = et.getText().toString().trim();
                final String email = et2.getText().toString().trim();
                final String password = et3.getText().toString().trim();
                final String dob = et4.getText().toString().trim();
                final String contact = et5.getText().toString().trim();
                final String address = et6.getText().toString().trim();
                final String city = et7.getText().toString().trim();
                final String pincode = et8.getText().toString().trim();
                final String id = et9.getText().toString().trim();
String profession = null;
if(customer.isChecked()){
    profession="Customer";
}
if(driver.isChecked()) {
    profession="Driver";
}
                String gender = null;
                if(r1.isChecked()){
                   gender="Male";
                }
                if(r2.isChecked()) {
                   gender="Female";
                }


                if (TextUtils.isEmpty(email)) {
                    et2.setError("Enter you Email !");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    et3.setError("Password is Necessary");
                    return;
                }
                if (password.length() < 8) {
                    et3.setError("Password must be at least 8 characters long");
                    return;
                }


                final String finalProfession = profession;
                final String finalGender = gender;
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //
                            if(finalProfession=="Customer") {
                                String user_id = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user_id);
                                current_user_db.setValue(true);
                            }
                            if(finalProfession=="Driver"){
                                String user_id = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(user_id);
                                current_user_db.setValue(true);
                            }
                            //
                             FirebaseUser fuser=fAuth.getCurrentUser();
                                fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(signup.this,"Verification Email has been Sent",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), Dashboard.class) );
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Log.d("TAG","onFailure: Email not Sent !"+e.getMessage());
                                    }
                                });


                            Toast.makeText(signup.this, "User Created Successfully!", Toast.LENGTH_SHORT).show();
                            userid = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userid);
                            startActivity(new Intent(getApplicationContext(), Login.class));
                            Map<String, Object> user = new HashMap<>();
                            user.put("fName", fullname);
                            user.put("Gender", finalGender);
                            user.put("Email", email);
                            user.put("Password", password);
                            user.put("DOB", dob);
                            user.put("Contact", contact);
                            user.put("Address", address);
                            user.put("City", city);
                            user.put("Pincode", pincode);
                            user.put("ID", id);
                            user.put("Profession", finalProfession);


                            fStore.collection("Users")
                                    .add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("TAG", "onSuccess: UserId Created" + documentReference.getId());

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("TAG", "Error Adding Document", e);
                                        }
                                    });

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "onSuccess: UserId created!" + userid);
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "onFailure;" + e.toString());
                                }
                            });


                        } else {
                            Toast.makeText(signup.this, "Error, Try Again!!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });


        tv10 = (TextView) findViewById(R.id.textView10);
        tv10.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        }));

        /* directly home screeen was showing as we click on the ceate account

        btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), home.class);
                startActivity(i);
            }
        });*/


        tv10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Login.class);
                startActivity(i);
            }
        });

    }

    public void selectDriver(View v){
        if(customer.isChecked()){
            customer.setChecked(false);
        }
        else {
            driver.setChecked(true);
        }
    }
    public void selectCustomer(View v){
        if(driver.isChecked()){
            driver.setChecked(false);
        }
        else {
            customer.setChecked(true);
        }
    }

}


   /* public void onRadioButtonClicked(View view){
        r1=(RadioButton)findViewById(R.id.radioButton2);
        r2=(RadioButton)findViewById(R.id.radioButton3);
        boolean checked=((RadioButton)view).isChecked();
        switch (view.getId()){
            case R.id.radioButton2:
                if(checked){
                    r2.setChecked(false);

                }
                break;
            case R.id.radioButton3:
                if(checked){
                    r1.setChecked(false);

                }
                break;

        }
    tv10=(TextView)findViewById(R.id.textView10);
        tv10.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Login.class);
                startActivity(i);
            }
        }));

        }
}*/
