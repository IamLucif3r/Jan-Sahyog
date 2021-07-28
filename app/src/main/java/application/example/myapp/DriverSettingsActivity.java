package application.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DriverSettingsActivity extends  AppCompatActivity {

    private EditText mNameField,mPhoneField,mCarField;
    private Button mBack,mConfirm;
    private FirebaseAuth mAuth;
    private DatabaseReference mDriverDatabase;
    private String userID;
    private String  mName,mPhone,mCar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_settings);

        mCarField=(EditText)findViewById(R.id.car);
        mNameField=(EditText)findViewById(R.id.name);
        mPhoneField=(EditText)findViewById(R.id.phone);
        mBack=(Button) findViewById(R.id.back);
        mConfirm=(Button) findViewById(R.id.confirm);

        mAuth=FirebaseAuth.getInstance();
        userID=mAuth.getCurrentUser().getUid();
        mDriverDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(userID);
        getUserInfo();
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserInformation();

            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }
    private void getUserInfo(){
        mDriverDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()&&dataSnapshot.getChildrenCount()>0){
                    Map<String ,Object> map =(Map<String, Object>)dataSnapshot.getValue();
                    if(map.get("name")!=null){
                        mName=map.get("name").toString();
                        mNameField.setText(mName);
                    }
                    if(map.get("phone")!=null) {
                        mPhone = map.get("phone").toString();
                        mPhoneField.setText(mPhone);
                    }
                    if(map.get("car")!=null) {
                        mCar = map.get("car").toString();
                        mCarField.setText(mCar);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void setUserInformation() {
        mName=mNameField.getText().toString();
        mPhone=mPhoneField.getText().toString();
        mCar=mCarField.getText().toString();

        Map userInfo=new HashMap();
        userInfo.put("name",mName);
        userInfo.put("phone",mPhone);
        userInfo.put("car",mCar);

        mDriverDatabase.updateChildren(userInfo);

        finish();
    }
}
