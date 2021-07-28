package application.example.myapp;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class GetReward extends AppCompatActivity {
    RadioButton r1,r2,r3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_reward);
        r1=findViewById(R.id.radioButton1);
        r2=findViewById(R.id.radioButton2);
        r3=findViewById(R.id.radioButton3);
    }

    public void reward_decide(View view) {
        Intent i = new Intent(this,DecideReward.class);
        if(r1.isChecked())
            i.putExtra("reward1","true");
        if(r2.isChecked())
            i.putExtra("reward2","true");
        if(r3.isChecked())
            i.putExtra("reward3","true");
        startActivity(i);
        finish();

    }
}