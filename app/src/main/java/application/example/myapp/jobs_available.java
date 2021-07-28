package application.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class jobs_available extends AppCompatActivity {

    Button post,back,home;
    EditText title,description;
    private StorageReference mStorage;
    private ProgressDialog mProgress;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_available);
        home=(Button)findViewById(R.id.button17);

        mProgress=new ProgressDialog(this);
        post=(Button)findViewById(R.id.button14);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Jobs");
        title=(EditText)findViewById(R.id.editText27);
        description=(EditText)findViewById(R.id.editText28);
        back=(Button)findViewById(R.id.button15);
        mStorage= FirebaseStorage.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Dashboard.class);
                startActivity(i);
                finish();

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),jobs_main.class);
                startActivity(i);
                finish();

            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });
    }

    private void startPosting() {
        mProgress.setMessage("Posting this Job....");
        mProgress.show();
        final String title_val=title.getText().toString().trim();
        final String description_val=description.getText().toString().trim();
        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(description_val)){
            String userid=firebaseAuth.getCurrentUser().getUid();
            DatabaseReference newPost=mDatabase.push();
            newPost.child("title").setValue(title_val);
            newPost.child("description").setValue(description_val);
            newPost.child("UserID").setValue(userid);
            mProgress.dismiss();
            Toast.makeText(jobs_available.this,"Posted Successfuly!",Toast.LENGTH_SHORT).show();

    }


}
}
