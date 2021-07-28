package application.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class jobs_main extends AppCompatActivity {

    Button home;
    private DatabaseReference mDatabase;
    private RecyclerView mJoblist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_main);

        home=(Button)findViewById(R.id.button16) ;
        mJoblist=(RecyclerView)findViewById(R.id.job_list);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Jobs");
        mJoblist.setHasFixedSize(true);
        mJoblist.setLayoutManager(new LinearLayoutManager(this));

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Dashboard.class);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<job,JobViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<job, JobViewHolder>(job.class,
                R.layout.job_row,
                JobViewHolder.class,
                mDatabase) {
            @Override
            protected void populateViewHolder(JobViewHolder jobViewHolder, job job, int i) {
                jobViewHolder.setTitle(job.getTitle());
                jobViewHolder.setDesc(job.getDescription());
            }
        };
        mJoblist.setAdapter(firebaseRecyclerAdapter);
      /*  FirebaseRecyclerAdapter<job, task_main.jobViewHolder> firebaseRecyclerAdapter =new FirebaseRecyclerAdapter<job, task_main.jobViewHolder>(
                job.class,
                R.layout.job_row,
                jobs_main.jobViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(task_main.jobViewHolder jobViewHolder, job job, int i) {

                jobViewHolder.setTitle(job.getTitle());
                jobViewHolder.setDesc(job.getDescription());
            }

           /* @Override
            protected void populateViewHolder(jobs_main.jobViewHolder jobViewHolder, job job, int i) {

                jobViewHolder.setTitle(job.getTitle());
                jobViewHolder.setDesc(job.getDescription());


        };
        mJoblist.setAdapter(firebaseRecyclerAdapter);*/

    }
    public static class JobViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setTitle(String title){
            TextView job_title=(TextView) mView.findViewById(R.id.post_title);
            job_title.setText(title);
        }
        public void setDesc(String desc){
            TextView job_desc=(TextView)mView.findViewById(R.id.post_text);
            job_desc.setText(desc);
        }
    }
   /* public static class jobViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public jobViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;

        }
        public void setTitle(String title){
            TextView post_title=(TextView)mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }
        public void setDesc(String desc){
            TextView post_desc=(TextView)mView.findViewById(R.id.post_text);
            post_desc.setText(desc);
        }

    }*/

}
