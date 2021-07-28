package application.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class sosEmergencyContacts extends AppCompatActivity {
    EditText e1,e2,e3;
    private DatabaseReference myref,root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos_emergency_contacts);
        e1=(EditText)findViewById(R.id.cont1);
        e2=(EditText)findViewById(R.id.cont2);
        e3=(EditText)findViewById(R.id.cont3);
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        root = database.getReference("user");
        myref = root.child("Family_Contacts");

    }

    public void save_contact2(View view) {
        String con1= e1.getText().toString();
        String con2= e2.getText().toString();
        String con3= e3.getText().toString();
        Family_contacts ob = new Family_contacts();
        ob.setContact1(con1);
        ob.setContact2(con2);
        ob.setContact3(con3);

        DatabaseReference myref1 = myref.child("contact1");
        myref1.setValue(con1);
        DatabaseReference myref2 = myref.child("contact2");
        myref2.setValue(con2);
        DatabaseReference myref3 = myref.child("contact3");
        myref3.setValue(con3);

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }
}
