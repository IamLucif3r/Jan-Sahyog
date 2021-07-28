package application.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class cab1 extends AppCompatActivity {
Button driver,customer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cab1);
        driver=(Button)findViewById(R.id.button9);
        customer=(Button)findViewById(R.id.button10);
        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),DriverMapActivity.class);
                startActivity(i);
                finish();
                return;
            }
        });
        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),CustomerMapActivity.class);
                startActivity(i);
                finish();
                return;
            }
        });
    }
}
