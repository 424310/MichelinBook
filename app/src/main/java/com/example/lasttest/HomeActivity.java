package com.example.lasttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private TextView nameTextView;
    private  TextView emailTextView;
    private FirebaseAuth auth;
    private Context mContext;

    private FloatingActionButton fab_main, fab_sub1, fab_sub2, fab_sub3;
    private Animation fab_open, fab_close;

    private boolean isFabOpen = false;

    /*DB 불러온 리사이클러뷰
    private RecyclerView mRecyclerView;
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*DB 불러온 리사이클러뷰(시작)
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_categories);
        new FirebaseDatabaseHelper().readCategories(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Category> categories, List<String> keys) {
                new RecyclerView_Config().setConfig(mRecyclerView, HomeActivity.this, categories, keys);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });
        DB 불러온 리사이클러뷰(끝)*/

        mContext = getApplicationContext();

        fab_open = AnimationUtils.loadAnimation(mContext, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(mContext, R.anim.fab_close);

        fab_main = (FloatingActionButton) findViewById(R.id.fab_main);
        fab_sub1 = (FloatingActionButton) findViewById(R.id.fab_sub1);
        fab_sub2 = (FloatingActionButton) findViewById(R.id.fab_sub2);
        fab_sub3 = (FloatingActionButton) findViewById(R.id.fab_sub3);

        fab_main.setOnClickListener(this);
        fab_sub1.setOnClickListener(this);
        fab_sub2.setOnClickListener(this);
        fab_sub3.setOnClickListener(this);

        int img[] = {R.drawable.ic_menu_camera, R.drawable.ic_menu_gallery, R.drawable.ic_menu_share, R.drawable.ic_menu_send};

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        auth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);

        nameTextView = (TextView)view.findViewById(R.id.header_name_textView);
        emailTextView = (TextView)view.findViewById(R.id.header_email_textView);

        nameTextView.setText(auth.getCurrentUser().getDisplayName());
        emailTextView.setText(auth.getCurrentUser().getEmail());

        MyAdapter adapter = new MyAdapter (
                getApplicationContext(),
                R.layout.homeview,       // GridView 항목의 레이아웃 row.xml
                img);    // 데이터

        GridView gv = (GridView)findViewById(R.id.gridView);
        gv.setAdapter(adapter);  // 커스텀 아답타를 GridView 에 적용

        // GridView 아이템을 클릭하면 상단 텍스트뷰에 position 출력
        // JAVA8 에 등장한 lambda expression 으로 구현했습니다. 코드가 많이 간결해지네요

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, CategoryListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_post) {
            Intent intent = new Intent(this, PostList.class);
            startActivity(intent);
        } else if (id == R.id.nav_mapsearch) {
            Intent intent = new Intent(this, Map.class);
            startActivity(intent);
        } else if (id == R.id.nav_mypage) {
            finish();
            Intent intent = new Intent(this, MyPage.class);
            startActivity(intent);
        } else if(id == R.id.nav_logout) {
            auth.signOut();
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_main:
                toggleFab();
                break;
            case R.id.fab_sub1:
                toggleFab();
                Intent intent1 = new Intent(this, AddPost.class);
                startActivity(intent1);
                break;
            case R.id.fab_sub2:
                toggleFab();
                Intent intent2 = new Intent(this, QuickVisit.class);
                startActivity(intent2);
                break;
            case R.id.fab_sub3:
                toggleFab();
                Intent intent3 = new Intent(this, Category_DB_Insert.class);
                startActivity(intent3);
                break;
        }
    }

    private void toggleFab() {
        if (isFabOpen) {
            fab_main.setImageResource(R.drawable.ic_fab_plus);
            fab_sub1.startAnimation(fab_close);
            fab_sub2.startAnimation(fab_close);
            fab_sub3.startAnimation(fab_close);
            fab_sub1.setClickable(false);
            fab_sub2.setClickable(false);
            fab_sub3.setClickable(false);
            isFabOpen = false;
        } else {
            fab_main.setImageResource(R.drawable.ic_fab_close);
            fab_sub1.startAnimation(fab_open);
            fab_sub2.startAnimation(fab_open);
            fab_sub3.startAnimation(fab_open);
            fab_sub1.setClickable(true);
            fab_sub2.setClickable(true);
            fab_sub3.setClickable(true);
            isFabOpen = true;
        }

    }
}

class MyAdapter extends BaseAdapter {
    Context context;
    int layout;
    int img[];
    LayoutInflater inf;

    public MyAdapter(Context context, int layout, int[] img) {
        this.context = context;
        this.layout = layout;
        this.img = img;
        inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return img.length;
    }

    @Override
    public Object getItem(int position) {
        return img[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null)
            convertView = inf.inflate(layout, null);
        ImageView iv = (ImageView)convertView.findViewById(R.id.imageView1);
        iv.setImageResource(img[position]);

        return convertView;
    }
}
