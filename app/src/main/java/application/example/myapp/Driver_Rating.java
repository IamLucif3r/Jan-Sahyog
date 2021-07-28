package application.example.myapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Driver_Rating extends AppCompatActivity {

    String name, contactno, address,Vehicle,userid,userId;
    FirebaseFirestore fstore;
    ListView listView;
    Button back;
    ArrayList<String> addarray = new ArrayList<String>();
    ArrayList<String> arrayList = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver__rating);
        back=(Button)findViewById(R.id.button21);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),serviceHome.class);
                startActivity(i);
                finish();
            }
        });
        //addarray.add("str");
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Driver_Rating.this,android.R.layout.simple_list_item_1,addarray);
        fstore= FirebaseFirestore.getInstance();
        fstore.collection("UsersInformation").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null)
                    Toast.makeText(Driver_Rating.this,"Error: " +e ,Toast.LENGTH_LONG).show();
                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                    userid= doc.getDocument().getId();
                    listView =(ListView)findViewById(R.id.list1);
                    DocumentReference documentReference = fstore.collection("UsersInformation").document(userid);
                    documentReference.addSnapshotListener(Driver_Rating.this, new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            assert documentSnapshot != null;
                            name=documentSnapshot.getString("FirstName:");
                            contactno=documentSnapshot.getString("Contact");
                            address=documentSnapshot.getString("Address:");
                            Vehicle=documentSnapshot.getString("VehicleModelName:");
                            userId=documentSnapshot.getString("UserId");
                            String str ="Name: " +name +"\n"+" Vehicle: "+Vehicle+"\nAddress: "+ address+" Id: " +userId;
                            if(!Vehicle.trim().equals(""))
                            {
                                addarray.add(str);
                                func(addarray);
                            }


                        }
                    });
                }
            }



        });



    }

    private void func(ArrayList<String> addarray) {
        arrayAdapter= new ArrayAdapter<>(Driver_Rating.this,android.R.layout.simple_list_item_1,addarray);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Driver_Rating.this, Ratings.class);
                Toast.makeText(Driver_Rating.this, "position: " +position, Toast.LENGTH_SHORT).show();
                String str = listView.getAdapter().getItem(position).toString();
                Toast.makeText(Driver_Rating.this, "String: " +str, Toast.LENGTH_SHORT).show();
                intent.putExtra("list", str);
                startActivity(intent);
            }
        });
    }

}