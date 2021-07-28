package application.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.okhttp.internal.DiskLruCache;

import java.util.ArrayList;
import java.util.List;

public class driver_list extends AppCompatActivity {

    private ListView listView;
    Button back;

    private ArrayList<String> list=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_list);
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        listView=(ListView)findViewById(R.id.driverList);
        back=(Button)findViewById(R.id.button22);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),serviceHome.class);
                startActivity(i);
                finish();
            }
        });

        db.collection("UsersInformation").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                list.clear();
                assert queryDocumentSnapshots != null;

                for (DocumentSnapshot snapshot: queryDocumentSnapshots){

                    String s1= snapshot.getString("FirstName:");
                    String s2=snapshot.getString("LastName:");

                    list.add(s1+" "+s2);

                }
                ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_selectable_list_item,list);

                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 Intent intent=new Intent(getApplicationContext(),driver_info.class);
                intent.putExtra("Name",listView.getItemAtPosition(i).toString());
                startActivity(intent);
            }
        });


    }
}