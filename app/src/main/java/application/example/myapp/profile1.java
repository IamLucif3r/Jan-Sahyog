package application.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.drm.DrmManagerClient;
import android.media.Image;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public class profile1 extends AppCompatActivity {

    ImageView dp,adhar,license;
    EditText fname,lname,address,contact,ec1,ec2,ec3,mname,owname,regno,regdate,lno,vfrom,vto;
    Button cpass,resendCode,save;
    FirebaseAuth fauth;
    FirebaseUser user;
    ProgressBar pbar;
    FirebaseFirestore fStore;
    DatabaseReference dr;
    TextView verifymsg;
    String userid;
    StorageReference storageReference;
    private Uri imageuri2,imageuri3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile1);
        verifymsg=findViewById(R.id.textView11);
        fauth=FirebaseAuth.getInstance();
        pbar=(ProgressBar)findViewById(R.id.progressBar3);
        fStore=FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();

        final StorageReference profileRef=storageReference.child("users/"+fauth.getCurrentUser().getUid()+"/ProfilePic");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(dp);
            }
        });
        StorageReference adharRef=storageReference.child("users/"+fauth.getCurrentUser().getUid()+"/AdhaarCard");
        adharRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(adhar);
            }
        });
        final StorageReference licenseRef=storageReference.child("users/"+fauth.getCurrentUser().getUid()+"/License");
        licenseRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(license);
            }
        });


        resendCode=(Button)findViewById(R.id.button3);
        cpass=(Button)findViewById(R.id.button4);
        dp=(ImageView)findViewById(R.id.dp);
        ec1=(EditText)findViewById(R.id.editText10);
        ec2=(EditText)findViewById(R.id.editText20);
        ec3=(EditText)findViewById(R.id.editText23);
        adhar=(ImageView)findViewById(R.id.adhar);
        license=(ImageView)findViewById(R.id.license);
        fname=(EditText)findViewById(R.id.editText11);
        lname=(EditText)findViewById(R.id.editText12);
        contact=(EditText)findViewById(R.id.editText13);
        address=(EditText)findViewById(R.id.editText14);
        mname=(EditText)findViewById(R.id.editText15);
        owname=(EditText)findViewById(R.id.editText16);
        regno=(EditText)findViewById(R.id.editText17);
        regdate=(EditText)findViewById(R.id.editText18);
        lno=(EditText)findViewById(R.id.editText19);
        vfrom=(EditText)findViewById(R.id.editText21);
        vto=(EditText)findViewById(R.id.editText22);
        fStore=FirebaseFirestore.getInstance();
        save=(Button)findViewById(R.id.button6);
        userid=fauth.getCurrentUser().getUid();
        user=fauth.getCurrentUser();
        //start


        //end
        if(!user.isEmailVerified()){
            verifymsg.setVisibility(View.VISIBLE);
            resendCode.setVisibility(View.VISIBLE);
            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    FirebaseUser fUser=fauth.getCurrentUser();
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(),"Verification Email has been resent to the registered Email id",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),profile1.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag","onFailure: Error Occurred!"+e.getMessage());
                        }
                    });
                }
            });


        }




        cpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetpass = new EditText(v.getContext());
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("Change your Password here!");
                dialog.setMessage("Enter Your new Password : ");
                dialog.setView(resetpass);
                dialog.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Extracting Email and sending the password reset link
                        String newpass = cpass.getText().toString();
                        user.updatePassword(newpass).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(profile1.this, "OnSuccess: Password Changed Successfully!!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(profile1.this, "Error Occurred!!" + e, Toast.LENGTH_SHORT).show();

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
        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallereyIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallereyIntent,1000);

            }
        });
        adhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallereyIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallereyIntent,1001);
            }
        });
        license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallereyIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallereyIntent,1002);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName=fname.getText().toString();
                String lastName=lname.getText().toString();
                String phone=contact.getText().toString();
                String Address=address.getText().toString();
                String emergency_contact1=ec1.getText().toString();
                String emergency_contact2=ec2.getText().toString();
                String emergency_contact3=ec3.getText().toString();
                String modelName=mname.getText().toString();
                String ownerName=owname.getText().toString();
                String regNo=regno.getText().toString();
                String regDate=regdate.getText().toString();
                String licenseNo=lno.getText().toString();
                String lValidfrom=vfrom.getText().toString();
                String lValidto=vto.getText().toString();
                DocumentReference documentReference = fStore.collection("UsersInformation").document(userid);
                final Map<String,Object> user=new HashMap<>();
                user.put("FirstName:",firstName);
                user.put("LastName:",lastName);
                user.put("Contact",phone);
                user.put("Address:",Address);
                user.put("EmergencyContact1:",emergency_contact1);
                user.put("EmergencyContact2:",emergency_contact2);
                user.put("EmergencyContact3:",emergency_contact3);
                user.put("VehicleModelName:",modelName);
                user.put("OwnerofVehicle:",ownerName);
                user.put("VehicleRegisterationNo:",regNo);
                user.put("RegisterationDate:",regDate);
                user.put("LicenseNo",licenseNo);
                user.put("LicenseValidFrom",lValidfrom);
                user.put("LicensceValidUpto:",lValidto);
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       Toast.makeText(profile1.this,"Successfully Updated",Toast.LENGTH_SHORT).show();
                       Intent i=new Intent(getApplicationContext(),Dashboard.class);
                       startActivity(i);
                       finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(profile1.this,"Error:"+e,Toast.LENGTH_SHORT).show();
//
                    }
                });
            }
        });

        final DocumentReference documentReference= fStore.collection("UsersInformation").document(userid);
        documentReference.addSnapshotListener(profile1.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                fname.setText(documentSnapshot.getString("FirstName:"));
                lname.setText(documentSnapshot.getString("LastName:"));
                contact.setText(documentSnapshot.getString("Contact"));
                address.setText(documentSnapshot.getString("Address:"));
                ec1.setText(documentSnapshot.getString("EmergencyContact1:"));
                ec2.setText(documentSnapshot.getString("EmergencyContact2:"));
                ec3.setText(documentSnapshot.getString("EmergencyContact3:"));
                fname.setText(documentSnapshot.getString("FirstName:"));
                mname.setText(documentSnapshot.getString("VehicleModelName:"));
                owname.setText(documentSnapshot.getString("OwnerofVehicle:"));
                regno.setText(documentSnapshot.getString("VehicleRegisterationNo:"));
                regdate.setText(documentSnapshot.getString("RegisterationDate:"));
                lno.setText(documentSnapshot.getString("LicenseNo"));
                vfrom.setText(documentSnapshot.getString("LicenseValidFrom"));
                vto.setText(documentSnapshot.getString("LicensceValidUpto:"));

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000)
        {
            if(resultCode== Activity.RESULT_OK){
                Uri imageuri=data.getData();
         //       dp.setImageURI(imageuri);

              uploadImage(imageuri);

            }
        }
        if(requestCode==1001)
        {
            if(resultCode==Activity.RESULT_OK){
              Uri  imageuri2=data.getData();
                adhar.setImageURI(imageuri2);
               //uploadImage(imageuri2);
                final StorageReference fileRef2=storageReference.child("users/"+fauth.getCurrentUser().getUid()+"/AdhaarCard");
                fileRef2.putFile(imageuri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       // Toast.makeText(profile1.this,"Adhaar Card Uploaded Successfuly",Toast.LENGTH_SHORT).show();
                        fileRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(adhar);
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(profile1.this,"Error in Uploading Adhaar Card "+e,Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }
        if(requestCode==1002)
        {
            if(resultCode==Activity.RESULT_OK){
              Uri imageuri3=data.getData();
                license.setImageURI(imageuri3);
              //  uploadImage(imageuri3);
                final StorageReference fileRef3=storageReference.child("users/"+fauth.getCurrentUser().getUid()+"/License");
                fileRef3.putFile(imageuri3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Toast.makeText(profile1.this,"License  Uploaded Successfuly",Toast.LENGTH_SHORT).show();
                        fileRef3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).into(license);
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(profile1.this,"Error in Uploading License "+e,Toast.LENGTH_SHORT).show();

                    }
                });


            }
        }

    }

    private void uploadImage(Uri imageuri) {
        pbar.setVisibility(View.VISIBLE);
        final StorageReference fileRef=   storageReference.child("users/"+fauth.getCurrentUser().getUid()+"/ProfilePic");
       // imageuri=fileRef.getDownloadUrl().getResult();

        fileRef.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(profile1.this,"Profile Image Uploaded Successfuly",Toast.LENGTH_SHORT).show();
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final Uri downloadUrl=uri;
                        Picasso.get().load(uri).into(dp);
                        pbar.setVisibility(View.INVISIBLE);

                        String userID=fauth.getCurrentUser().getUid();
                        dr= FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(userID);
                        Map newImage=new HashMap();
                        newImage.put("ProfileImageUrl",uri.toString());
                        dr.updateChildren(newImage);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profile1.this,"Error in Uploading Profile Image"+e,Toast.LENGTH_SHORT).show();
            }
        });




    }
}
