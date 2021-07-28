package application.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.itextpdf.text.pdf.parser.Line;

import java.util.ArrayList;
import java.util.List;

public class onBoarding1 extends AppCompatActivity {

    private onBoardingAdapter onBoardingAdapter;
    private LinearLayout layoutOnboardingIndicators;
    private MaterialButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding1);

        layoutOnboardingIndicators=findViewById(R.id.onboardingindicators);
        button=findViewById(R.id.buttonOnBoarding);

        setOnBoardingAdapter();

        final ViewPager2 onBoardingViewPager=findViewById(R.id.onBoardViewPager);
        onBoardingViewPager.setAdapter(onBoardingAdapter);

        setOnBoardingAdapter();
        setCurrentOnBoardingIndicator(0);
        onBoardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnBoardingIndicator(position);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onBoardingViewPager.getCurrentItem()+1<onBoardingAdapter.getItemCount()){
                    onBoardingViewPager.setCurrentItem(onBoardingViewPager.getCurrentItem()+1);
                }else{
                    startActivity(new Intent(getApplicationContext(),Login.class));
                    finish();
                }

            }
        });

    }
    private void setOnBoardingAdapter(){
        List<onBoardingItem> onBoardingItems=new ArrayList<>();

        onBoardingItem itemSOS=new onBoardingItem();
        itemSOS.setTitle("SOS Feature");
        itemSOS.setDescription("In Trouble ? Send SOS Signals to your dear ones in one touch");
        itemSOS.setImage(R.drawable.sos);

        onBoardingItem itemService=new onBoardingItem();
        itemService.setTitle("Jobs");
        itemService.setDescription("Need A Job or Want to Hire ? Look for appropriate jobs or Post your needs");
        itemService.setImage(R.drawable.onboardingcar);

        onBoardingItem itemReport=new onBoardingItem();
        itemReport.setTitle("Report an Incident");
        itemReport.setDescription("Anything making Trouble ? Report Us here");
        itemReport.setImage(R.drawable.reportingonboard);

        onBoardingAdapter=new onBoardingAdapter(onBoardingItems);
    }
    private void setupOnboardingIndicators(){
        ImageView[] indicators=new ImageView[onBoardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        for(int i=0;i<indicators.length;i++){
            indicators[i]=new ImageView(getApplicationContext());
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicators.addView(indicators[i]);
        }

    }
    private void setCurrentOnBoardingIndicator(int index){
        int childcount=layoutOnboardingIndicators.getChildCount();
        for(int i=0;i<childcount;i++){
            ImageView imageView=(ImageView) layoutOnboardingIndicators.getChildAt(i);
            if(i==index){
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_active));
            }
            else{
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_inactive));
            }
        }
        if(index==onBoardingAdapter.getItemCount()-1){
            button.setText("Start");
        }else{
            button.setText("Next");
        }
    }
}
