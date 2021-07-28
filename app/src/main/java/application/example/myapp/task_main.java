package application.example.myapp;

import androidx.annotation.NonNull;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class task_main extends AppCompatActivity {

    Button add,home;
    private DatabaseReference mDatabase;
    private RecyclerView mBlogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_main);

        add =(Button)findViewById(R.id.button13);
        home=(Button)findViewById(R.id.button16) ;
        mBlogList=(RecyclerView)findViewById(R.id.job_list);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Blog");
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(getApplicationContext()) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        smoothScroller.setTargetPosition(0);


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Dashboard.class);
                startActivity(i);
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Post.class);
                startActivity(i);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<Blog,BlogViewHolder> firebaseRecyclerAdapter =new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder blogViewHolder, Blog blog, int i) {

                blogViewHolder.setTitle(blog.getTitle());
                blogViewHolder.setDesc(blog.getDescription());
                blogViewHolder.setImage(blog.getImage());
            }
        };
        mBlogList.setAdapter(firebaseRecyclerAdapter);
        mBlogList.smoothScrollToPosition(0);

    }
    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public BlogViewHolder(@NonNull View itemView) {
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
        public void setImage(String image){
            ImageView post_image=(ImageView)mView.findViewById(R.id.post_image);
            Picasso.get().load(image).into(post_image);
        }
    }
}
