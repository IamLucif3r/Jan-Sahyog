package application.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.text.pdf.codec.BmpImage;

import java.util.Objects;

public class Post extends AppCompatActivity {

    public static final int GALLEREY_REQUEST = 1;
    ImageButton upload;
    Button post,back,home;
    EditText title,description;
    Uri imageUri=null;
    private StorageReference mStorage;
    private ProgressDialog mProgress;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        home=(Button)findViewById(R.id.button17);
        upload=(ImageButton)findViewById(R.id.imageButton2);
        post=(Button)findViewById(R.id.button14);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Blog");
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
                Intent i=new Intent(getApplicationContext(),task_main.class);
                startActivity(i);
                finish();

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, GALLEREY_REQUEST);

            }
        });

        mProgress=new ProgressDialog(this);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });
    }

    private void startPosting() {
        mProgress.setMessage("Posting to Blog....");
        mProgress.show();
        final String title_val=title.getText().toString().trim();
        final String description_val=description.getText().toString().trim();
        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(description_val) && imageUri!=null){
            final StorageReference filepath= mStorage.child("Blog_Images").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   // Uri downloadUrl=taskSnapshot.getDownloadUrl();
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String userid=firebaseAuth.getCurrentUser().getUid();
                            final Uri downloadUrl=uri;

                            DatabaseReference newPost=mDatabase.push();
                            newPost.child("title").setValue(title_val);
                            newPost.child("description").setValue(description_val);
                            newPost.child("image").setValue(downloadUrl.toString());
                            newPost.child("UserID").setValue(userid);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Post.this, "Error While Posting "+e, Toast.LENGTH_SHORT).show();

                        }
                    });

                    mProgress.dismiss();
                    Toast.makeText(Post.this,"Posted Successfuly!",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(getApplicationContext(),task_main.class);
                    startActivity(i);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Post.this, "Error Uploading Image"+e, Toast.LENGTH_SHORT).show();
                }
            });


        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLEREY_REQUEST&& resultCode==RESULT_OK){
             imageUri=data.getData();
            upload.setImageURI(imageUri);
        }
    }
}
