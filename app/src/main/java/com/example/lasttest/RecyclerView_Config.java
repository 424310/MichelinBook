package com.example.lasttest;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerView_Config {
    private Context mContext;
    private CategoriesAdapter mCategoriesAdapter;
    public void setConfig(RecyclerView recyclerView, Context context, List<Category> categories, List<String> keys){
        mContext = context;
        mCategoriesAdapter = new CategoriesAdapter(categories, keys);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.setAdapter(mCategoriesAdapter);
    }

    //뷰홀더 CategoryItemView
    class CategoryItemView extends RecyclerView.ViewHolder{
        //onclick 위해 추가한 코드
        public final View mView;

        private TextView mName;
        private TextView mAddress;
        private TextView mNumber;

        private String key;

        public CategoryItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.category_list_item, parent, false));

            //onclick 위해 추가한 코드
            mView = itemView;

            mName = (TextView) itemView.findViewById(R.id.name_txtView);
            mAddress = (TextView) itemView.findViewById(R.id.address_txtView);
            mNumber = (TextView) itemView.findViewById(R.id.number_txtView);
        }

        public void bind(Category category, String key){
            mName.setText(category.getName());
            mAddress.setText(category.getAddress());
            mNumber.setText(category.getNumber());

            this.key = key;
        }
    }

    class CategoriesAdapter extends RecyclerView.Adapter<CategoryItemView>{
        private List<Category> mCategoryList;
        private List<String> mKeys;

        public CategoriesAdapter(List<Category> mCategoryList, List<String> mKeys) {
            this.mCategoryList = mCategoryList;
            this.mKeys = mKeys;
        }

        public CategoriesAdapter() {
            super();
        }

        @NonNull
        @Override
        public CategoryItemView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new CategoryItemView(viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryItemView categoryItemView, final int i) {
            categoryItemView.bind(mCategoryList.get(i), mKeys.get(i));

            //here it is simply write onItemClick listener here
            categoryItemView.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Context context = v.getContext();
                    Intent intent =  new Intent(context, CategoryView.class);

                    intent.putExtra("Name", mCategoryList.get(i).getName());
                    intent.putExtra("Address",  mCategoryList.get(i).getAddress());
                    intent.putExtra("Number",  mCategoryList.get(i).getNumber());

                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() { //데이터 수 반환
            return mCategoryList.size();
        }
    }
}
