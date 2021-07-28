package application.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class sosconfirm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sosconfirm);

        Intent i = getIntent();
        String mesg = i.getStringExtra("message");
        TextView ed1 = findViewById(R.id.editText);
        ed1.setText(mesg);
    }
}
