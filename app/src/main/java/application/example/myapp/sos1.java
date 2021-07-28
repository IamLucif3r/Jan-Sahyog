package application.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.util.Base64Utils;

public class sos1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos1);

    }
    public void sos_func(View view) {
        Intent intent = new Intent(this,sos2.class);
        startActivity(intent);
    }
    public void emergency_func(View view) {
        Intent intent = new Intent(this,sosEmergency.class);
        /*intent.putExtra("message",str);

        Context context = getApplicationContext();
        CharSequence text = str;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,text,duration);
        toast.show();
        */
        startActivity(intent);
    }

    public void sosContact(View view) {
        Intent intent = new Intent(this,sosEmergencyContacts.class);
        startActivity(intent);

    }


}
