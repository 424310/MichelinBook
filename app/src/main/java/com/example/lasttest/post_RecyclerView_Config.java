package com.example.lasttest;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class post_RecyclerView_Config {
    private Context mContext;
    private PostsAdapter mPostsAdapter;
    public void setConfig(RecyclerView recyclerView, Context context, List<Post> posts, List<String> keys){
        mContext = context;
        mPostsAdapter = new PostsAdapter(posts, keys);

        //recyclerView.setLayoutManager(new LinearLayoutManager(context));
        //위 코드(리사이클러뷰)의 역순 정렬
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(mPostsAdapter);
    }

    //뷰홀더 PostItemView
    class PostItemView extends RecyclerView.ViewHolder{

        private TextView mPost_date;
        private ImageView mPost_image;
        private TextView mPost_content;

        private String key, url;

        public PostItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.post_list_item, parent, false));

            mPost_date= (TextView) itemView.findViewById(R.id.post_date_txtView);
            mPost_image = (ImageView) itemView.findViewById(R.id.post_image_imageView);
            mPost_content = (TextView) itemView.findViewById(R.id.post_content_txtView);

            /*here it is simply write onItemClick listener here
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =  new Intent(mContext, PostUpdateDelete.class);

                    intent.putExtra("key", key);
                    intent.putExtra("Post_date", mPost_date.getText().toString());
                    intent.putExtra("Post_content", mPost_content.getText().toString());

                    mContext.startActivity(intent);
                }
            });
            */
        }

        public void bind(Post post, String key){
            mPost_date.setText(post.getPost_date());
            mPost_content.setText(post.getPost_content());
            this.key = key;

            //이미지뷰!!(시작)
            url = post.getPost_image();
            Glide.with(mContext).load(url).into(mPost_image);
            //이미지뷰!!(끝)
        }
    }

    class PostsAdapter extends RecyclerView.Adapter<PostItemView>{
        private List<Post> mPostList;
        private List<String> mKeys;

        public PostsAdapter(List<Post> mPostList, List<String> mKeys) {
            this.mPostList = mPostList;
            this.mKeys = mKeys;
        }

        public PostsAdapter() {
            super();
        }

        @NonNull
        @Override
        public PostItemView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new PostItemView(viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull PostItemView menuItemView, int i) {
            menuItemView.bind(mPostList.get(i), mKeys.get(i));
        }

        @Override
        public int getItemCount() { //데이터 수 반환
            return mPostList.size();
        }
    }
}