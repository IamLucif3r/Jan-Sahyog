package application.example.myapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class onBoardingAdapter extends RecyclerView.Adapter<onBoardingAdapter.onBoardingViewHolder>{
    public onBoardingAdapter(List<onBoardingItem> onBoardingItems) {
        this.onBoardingItems = onBoardingItems;
    }

    private List<onBoardingItem> onBoardingItems;
    @NonNull
    @Override
    public onBoardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new onBoardingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_onboarding,parent,false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull onBoardingViewHolder holder, int position) {

        holder.setonBoardingData(onBoardingItems.get(position));
    }

    @Override
    public int getItemCount() {
        return onBoardingItems.size();
    }

    class onBoardingViewHolder extends RecyclerView.ViewHolder{
        private TextView textTitle;
        private TextView textDescription;
        private ImageView imageOnBoarding;

        public onBoardingViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle=itemView.findViewById(R.id.textTitle);
            textDescription=itemView.findViewById(R.id.textDescription);
            imageOnBoarding=itemView.findViewById(R.id.imageOnboarding);

        }
        void setonBoardingData(onBoardingItem onBoardingItem){
            textTitle.setText(onBoardingItem.getTitle());
            textDescription.setText(onBoardingItem.getDescription());
            imageOnBoarding.setImageResource(onBoardingItem.getImage());
        }

    }
}
