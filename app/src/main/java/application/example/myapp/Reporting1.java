package application.example.myapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.protobuf.StringValue;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.lang.String.valueOf;

@SuppressWarnings("ALL")
public class Reporting1 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final int CAMERA_PERM_CODE=101;
    public static final int CAMERA_REQUEST_CODE=102;
    public static final int GALLEREY_REQUEST_CODE = 105;
    private static final int PICK_VIDEO_REQUEST =1;
    private static final String LOG_TAG="Record_log";
    public static final int STORAGE_CODE = 1000;
    private MediaRecorder recorder;
    private String mfileName=null;
    ProgressBar loading;
    TextView longitude, latitude, wordCount,recording;
    Spinner category;
    ImageView camera,gallery,video;
    EditText addit,address;
    FirebaseFirestore fStore;
    CardView mCamera,btnreco;
    StorageReference storageReference;
    FirebaseAuth fauth;
    FusedLocationProviderClient fusedLocationProviderClient;
    String currentPhotoPath;
    MediaController mediaController;
    Uri videoUri;
    Button rec,submit;
    ProgressDialog progressDialog;
    DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporting1);
        fauth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        submit=(Button)findViewById(R.id.button11);
        progressDialog= new ProgressDialog(Reporting1.this);
        String userid=fauth.getCurrentUser().getUid();
address=(EditText)findViewById(R.id.editText24);
        mfileName = getExternalCacheDir().getAbsolutePath();
        mfileName += "/audiorecordtest.3gp";
        recording=(TextView)findViewById(R.id.textView57);
        btnreco=(CardView)findViewById(R.id.reco);
        addit= (EditText) findViewById(R.id.explain);
        mCamera = (CardView) findViewById(R.id.camera);
        gallery=(ImageView)findViewById(R.id.imageView5);
        camera = (ImageView) findViewById(R.id.imageView4);
        video=(ImageView)findViewById(R.id.imageView6);
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Reports").child(userid);



        //AuDIO
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Reporting1.this,new String[]{Manifest.permission.RECORD_AUDIO},111);
        }

        btnreco.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    startRecording();
                   // recordinglabel.setText("start");
                    recording.setVisibility(View.VISIBLE);
                    v.setScaleX(0.95f);
                    v.setScaleY(0.95f);
                }
                else if(event.getAction()==MotionEvent.ACTION_UP){
                    stopRecording();
                    recording.setVisibility(View.INVISIBLE);
                    v.animate().cancel();
                    v.animate().scaleX(1f).setDuration(1000).start();
                    v.animate().scaleY(1f).setDuration(1000).start();
                }


                return true;
            }
        });
        //AuDIO


        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Reporting1.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Reporting1.this, new String[]{Manifest.permission.CAMERA}, 100);

                }else{
                    dispatchTakePictureIntent();
                }
              //  Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               // startActivityForResult(i, 100);

            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallereyIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallereyIntent, GALLEREY_REQUEST_CODE);

            }
        });


//Counting Letters
        wordCount = (TextView) findViewById(R.id.textView46);
        addit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                wordCount.setText(s.toString().length() + "/1000");
            }
        });

        //Date And Time
        final Calendar calendar = Calendar.getInstance();
        final String date = DateFormat.getInstance().format(calendar.getTime());
        TextView tvDate = findViewById(R.id.textView43);
        tvDate.setText(date);
        //End of Date and Time
        latitude = (TextView) findViewById(R.id.textView52);
        longitude = (TextView) findViewById(R.id.textView53);
        address= (EditText) findViewById(R.id.editText24);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(Reporting1.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(Reporting1.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        //Location end
        category = (Spinner) findViewById(R.id.categorySelector);
        category.setOnItemSelectedListener(this);

        //Video
        storageReference= FirebaseStorage.getInstance().getReference();
        loading=(ProgressBar)findViewById(R.id.progressBar);
        mediaController=new MediaController(this);
        storageReference.child("Reports/").child(fauth.getInstance().getCurrentUser().getUid()).child(category.getSelectedItem().toString()).child("videos/");
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVideo();
                uploadVideo();

            }
        });

        //video
        //FINAL SUBMISSION:
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String details=addit.getText().toString();
                String dat= date.toString();
                String lat=latitude.getText().toString();
                String lng=longitude.getText().toString();
                String addd=address.getText().toString();
                String userid=fauth.getCurrentUser().getUid();
                String cate=category.getSelectedItem().toString();
              DocumentReference dr=fStore.collection("Reports").document(userid);

                DatabaseReference save=mDatabase.push();
                save.child("DateTime").setValue(dat);
                save.child("Details").setValue(details);
                save.child("Latitude").setValue(lat);
                save.child("Longitude").setValue(lng);
                save.child("Address").setValue(addd);
                save.child("UserId").setValue(userid);
                save.child("Category").setValue(cate);



                final Map<String,Object> rep=new HashMap<>();

                rep.put("DateTime",dat);
                rep.put("Details",details);
                rep.put("Latitude",lat);
                rep.put("Longitude",lng);
                rep.put("Address",addd);
                rep.put("UserId",userid);
                rep.put("category",cate);
                rep.put("UserId",userid);
               dr.set(rep).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Reporting1.this,"Successfully Save",Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Reporting1.this,"ERROR"+e,Toast.LENGTH_SHORT).show();

                    }
                });

//Submit
            /*    Intent i = new Intent(Reporting1.this,Rewardsoption.class);
                i.putExtra("count","yes");
                startActivity(i);
                finish();
*/
                //End

            }
        });
        //FINAL SUBMISSION
    }



    private void chooseVideo() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

 /*       Intent i=new Intent();
        i.setType("video/*");
        i.setAction(i.ACTION_GET_CONTENT);
*/
       startActivityForResult(i,PICK_VIDEO_REQUEST);

    }


    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(Reporting1.this, Locale.getDefault());

                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        latitude.setText(Html.fromHtml("<Lat:>" + addresses.get(0).getLatitude()));
                        longitude.setText(Html.fromHtml("<Lat:>" + addresses.get(0).getLongitude()));
                        address.setText(addresses.get(0).getAddressLine(0));



                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode==Activity.RESULT_OK) {
              File f=new File(currentPhotoPath);
              camera.setImageURI(Uri.fromFile(f));
            Log.d("tag","Absolute Url :"+Uri.fromFile(f));

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);

            uploadImageToFirebase(f.getName(),contentUri);

        }
        if (requestCode == GALLEREY_REQUEST_CODE && resultCode==Activity.RESULT_OK) {

            Uri contentUri=data.getData();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "."+getFileExt(contentUri);
            Log.d("tag","onActivityResult: Gallerey Image Uri:  "+imageFileName);
            gallery.setImageURI(contentUri);

            uploadImageToFirebase(imageFileName,contentUri);



        }
        //VIDEO
        if(requestCode==PICK_VIDEO_REQUEST && resultCode==RESULT_OK && data != null && data.getData()!=null){
            videoUri=data.getData();

        }

        //VIDEO


    }
    //VIDEO
    private String getFileExtension(Uri videoUri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(videoUri));
    }
    private void uploadVideo(){
        loading.setVisibility(View.VISIBLE);
        if(videoUri!=null){
            //StorageReference reference=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(videoUri));
            StorageReference reference=storageReference.child("Reports/").child(fauth.getInstance().getCurrentUser().getUid()).child(category.getSelectedItem().toString()).child("videos/").child(System.currentTimeMillis()+"."+getFileExtension(videoUri));;

            reference.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   loading.setVisibility(View.INVISIBLE);
                   Toast.makeText(Reporting1.this,"Upload Successful! ",Toast.LENGTH_SHORT).show();

               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
              Toast.makeText(Reporting1.this,"Upload Failed"+e,Toast.LENGTH_SHORT).show();
               }
           });

        }
    }
    //VIDEO

    private void uploadImageToFirebase(String name, Uri contentUri) {
        final StorageReference image=storageReference.child("Reports/").child((String) category.getSelectedItem()).child(fauth.getInstance().getCurrentUser().getUid()).child("images/"+name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("tag","onSuccess:Uploaded Image URL is:"+uri.toString());

                    }
                });
                Toast.makeText(Reporting1.this,"Images Is Uploaded",Toast.LENGTH_SHORT).show();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Reporting1.this,"Upload failed"+e,Toast.LENGTH_SHORT);
            }
        });
    }

    private String getFileExt(Uri contentUri) {

        ContentResolver c=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
//AUDIO
    private void startRecording() {
    recorder = new MediaRecorder();
    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
    recorder.setOutputFile(mfileName);
    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

    try {
        recorder.prepare();
    } catch (IOException e) {
        Log.e(LOG_TAG, "prepare() failed");
    }

    recorder.start();
}

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        uploadAudio();
    }

    private void uploadAudio() {
        progressDialog.setMessage("Uploading Audio ...");
        progressDialog.show();

        StorageReference filepath=storageReference.child("Reports/").child((String) category.getSelectedItem()).child("Audio").child(System.currentTimeMillis()+"."+"3gp");  //.child("new_audio.3gp");
        Uri uri=Uri.fromFile(new File(mfileName));
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
              recording.setVisibility(View.INVISIBLE);
                Toast.makeText(Reporting1.this,"Uploaded Successfuly",Toast.LENGTH_SHORT).show();

               // recording.setText("Stopped");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
           Toast.makeText(Reporting1.this,"Error Occurred"+e,Toast.LENGTH_SHORT).show();
            }
        });
    }

    //AUDIO


}
