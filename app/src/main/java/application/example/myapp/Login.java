package application.example.myapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    TextView signup,fpass;
    EditText et,et3;
    Button login;
    FirebaseAuth fAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signup=(TextView)findViewById(R.id.textView);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),signup.class);
                startActivity(i);
            }

        });

        et=findViewById(R.id.editText);
        et3=findViewById(R.id.editText3);
        fAuth=FirebaseAuth.getInstance();
        login=findViewById(R.id.button);
        fpass=findViewById(R.id.textView5);
        if (fAuth.getCurrentUser()!= null) {
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
            finish();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email=et.getText().toString().trim();
                String pass=et3.getText().toString().trim();


                if( TextUtils.isEmpty(email))
                {
                et.setError("Enter you Email !");
                }
                if(TextUtils.isEmpty(pass))
                {
                   et3.setError("Password is Necessary");
                   return;
                }
                if(et3.length()<8)
                {
                    et3.setError("Password must be at least 8 characters long");
                }

                fAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(Login.this,"Login Successful !",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Dashboard.class));
                            finish();

                        }
                        else {
                            Toast.makeText(Login.this,"Error, Try Again!!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                });

            }
        });


        fpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText reset=new EditText(v.getContext());
                AlertDialog.Builder dialog=new AlertDialog.Builder(v.getContext());
                dialog.setTitle("Rest your Password here!");
                dialog.setMessage("Enter your Registered Email to get a password reset link.");
                dialog.setView(reset);
                dialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Extracting Email and sending the password reset link
                        String mail=reset.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this,"Password Reset link is successfully sent to your registered Email ID ! ",Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this,"Error Occurred, Mail not sent!"+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dialog.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Returning to the HomeScreen
                    }
                });

                dialog.create().show();
            }
        });

    }

}
