package application.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class sos2 extends AppCompatActivity {
    RadioButton R1, R2, R3, R4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos2);
        R1 = (RadioButton)findViewById(R.id.radioButton1);
        R2 = (RadioButton)findViewById(R.id.radioButton2);
        R3 = (RadioButton)findViewById(R.id.radioButton3);
        R4 = (RadioButton)findViewById(R.id.radioButton4);
    }

    public void sos_message(View view) {
        String out;
        if(R1.isChecked())
            out = R1.getText().toString();
        else if(R2.isChecked())
            out = R2.getText().toString();
        else if(R3.isChecked())
            out = R3.getText().toString();
        else
            out = R4.getText().toString();

        Context context = getApplicationContext();
        CharSequence text = out;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,text,duration);
        toast.show();

        Intent intent = new Intent(this,sosContact.class);
        intent.putExtra("message",out);
        startActivity(intent);
    }

    public void selectR1(View view) {
        if(R2.isChecked() || R3.isChecked() || R4.isChecked())
        {
            R2.setChecked(false);
            R3.setChecked(false);
            R4.setChecked(false);

        }
        else
            R1.setChecked(true);
    }
    public void selectR2(View view) {
        if(R1.isChecked() || R3.isChecked() || R4.isChecked())
        {
            R1.setChecked(false);
            R3.setChecked(false);
            R4.setChecked(false);
        }
        else
            R2.setChecked(true);
    }
    public void selectR3(View view) {
        if(R1.isChecked() || R2.isChecked() || R4.isChecked())
        {
            R1.setChecked(false);
            R2.setChecked(false);
            R4.setChecked(false);
        }
        else
            R3.setChecked(true);
    }
    public void selectR4(View view) {
        if(R2.isChecked() || R3.isChecked() || R1.isChecked())
        {
            R1.setChecked(false);
            R2.setChecked(false);
            R3.setChecked(false);
        }
        else
            R4.setChecked(true);
    }
}
