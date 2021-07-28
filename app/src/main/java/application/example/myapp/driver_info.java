package application.example.myapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class driver_info extends AppCompatActivity {
    private ListView listView;
    Button back;
    private ArrayList<String> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_info);
        back=(Button)findViewById(R.id.button20);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),driver_list.class);
                startActivity(i);
                finish();
            }
        });
        FirebaseFirestore db= FirebaseFirestore.getInstance();

        listView=(ListView)findViewById(R.id.driverList1);
        final Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {

            db.collection("UsersInformation").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    list.clear();
                    assert queryDocumentSnapshots != null;
                    list.add( "Name: "+bundle.getString("Name"));

                    for (DocumentSnapshot snapshot: queryDocumentSnapshots){
                        String s1= snapshot.getString("FirstName:");
                        String s2=snapshot.getString("LastName:");
                        String s4=s1+" "+s2;
                        if(bundle.getString("Name").equals(s4)){
                            String s3=snapshot.getString("Contact");
                            list.add("Contact: "+s3);
                            String s5=snapshot.getString("Address:");
                            list.add("Address: "+s5);
                            String S6=snapshot.getString("VehicleModelName:");
                            list.add("VehicleModelName: "+S6);
                            String S7=snapshot.getString("OwnerofVehicle:");
                            list.add("OwnerofVehicle: "+S7);
                            String S8=snapshot.getString("VehicleRegisterationNo:");
                            list.add("VehicleRegisterationNo: "+S8);
                            String S9=snapshot.getString("RegisterationDate:");
                            list.add("RegisterationDate: "+S9);
                            String S10=snapshot.getString("LicenseNo");
                            list.add("LicenseNo: "+S10);


                        }

                    }
                    ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_selectable_list_item,list);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);

                }
            });
        }
    }
}