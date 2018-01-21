package com.example.totoroto.homework2;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    Toolbar toolbar;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    ArrayList<ItemData> arrItems;
    ImageView ivLayoutChange;
    boolean isLinear;
    private DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    GpsInfo gps;
    myUtil util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        init();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_3bar_grey);
        actionBar.setDisplayHomeAsUpEnabled(true); //햄버거 아이콘이 클릭되게만 해줌
        //onOptionsItemSelected 을 구현해줘야..
        aboutTab();
        aboutData();
        aboutLayoutChange();
        aboutNavigation();

        StaggeredGridLayoutManager stagLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(stagLayoutManager);
        recyclerView.setHasFixedSize(true);
    }

    private void aboutNavigation() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) { //네비게이션 아이템에 대한
                item.setChecked(true);
                mDrawerLayout.closeDrawers();

                switch (item.getItemId()) {
                    case R.id.tab_destination:
                        Toast.makeText(getApplicationContext(), "거리순", Toast.LENGTH_SHORT).show();
                        AscendingDistance descendingDis = new AscendingDistance(gps.getLatitude(), gps.getLongitude());
                        Collections.sort(arrItems, descendingDis);
                        myAdapter.notifyDataSetChanged();
                        break;
                    case R.id.tab_popular:
                        Toast.makeText(getApplicationContext(), "인기순", Toast.LENGTH_SHORT).show();
                        Collections.sort(arrItems, new Comparator<ItemData>() {
                            @Override
                            public int compare(ItemData itemData, ItemData t1) {
                                return itemData.numPopular - t1.numPopular;
                            }
                        });
                        myAdapter.notifyDataSetChanged();
                        break;
                    case R.id.tab_recent:
                        Toast.makeText(getApplicationContext(), "최근순", Toast.LENGTH_SHORT).show();
                        Collections.sort(arrItems, new Comparator<ItemData>() {
                            @Override
                            public int compare(ItemData itemData, ItemData t1) {
                                return itemData.getDay().compareTo(t1.getDay());
                            }
                        });
                        myAdapter.notifyDataSetChanged();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){ //햄버거 아이콘을 클릭했을 때
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void init() {
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        ivLayoutChange = (ImageView) findViewById(R.id.iv_layoutChange);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        gps = new GpsInfo(this);
        util = new myUtil();
    }

    private void aboutLayoutChange() {
        isLinear = false; //초기 화면이 Staggered 이므로 Linear가 아님
        ivLayoutChange.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(!isLinear) {
                    ivLayoutChange.setImageResource(R.drawable.ic_staggered);
                    isLinear = true;

                    LinearLayoutManager layoutManager =  new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                }else{
                    ivLayoutChange.setImageResource(R.drawable.ic_linear);
                    isLinear = false;

                    StaggeredGridLayoutManager stagLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(stagLayoutManager);
                }
            }
        });
    }

    class BackThread extends Thread{
        @Override
        public void run() {
            arrItems = util.create_item();
            for(ItemData item : arrItems){
                System.out.println(item);
                item.setBitmap(util.getBitmapFromURL(item.getStoreImage()));
            }
            handler.sendEmptyMessage(0);
        } // end run()
    } // end class BackThread

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){   // Message id 가 0 이면
                myAdapter = new MyAdapter();
                myAdapter.setContext(getApplicationContext());
                myAdapter.setItemDatas(arrItems);
                recyclerView.setAdapter(myAdapter);
            }
        }
    };

    private void aboutData(){
        // 스레즈 생성하고 시작
        BackThread thread = new BackThread();
        thread.setDaemon(true);
        thread.start();
    }

    private void aboutTab() {
        tabLayout.setSelectedTabIndicatorHeight(0);
        tabLayout.addTab(tabLayout.newTab().setText("거리순"));
        tabLayout.addTab(tabLayout.newTab().setText("인기순"));
        tabLayout.addTab(tabLayout.newTab().setText("최근순"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) { //탭에 따라 아이템 소팅해주는
                switch (tab.getPosition()){
                    case 0:
                        AscendingDistance descendingDis = new AscendingDistance(gps.getLatitude(), gps.getLongitude());
                        Collections.sort(arrItems, descendingDis);
                        myAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        Collections.sort(arrItems, new Comparator<ItemData>() {
                            @Override
                            public int compare(ItemData itemData, ItemData t1) {
                                return itemData.numPopular - t1.numPopular;
                            }
                        });
                        myAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        Collections.sort(arrItems, new Comparator<ItemData>() {
                            @Override
                            public int compare(ItemData itemData, ItemData t1) {
                                return itemData.getDay().compareTo(t1.getDay());
                            }
                        });
                        myAdapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


}
import android.Manifest;
        import android.content.pm.PackageManager;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.Build;
        import android.os.Handler;
        import android.os.Message;
        import android.support.annotation.NonNull;
        import android.support.design.widget.NavigationView;
        import android.support.design.widget.TabLayout;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.StaggeredGridLayoutManager;
        import android.support.v7.widget.Toolbar;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.Toast;

        import java.io.InputStream;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.Comparator;


public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    Toolbar toolbar;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    ArrayList<ItemData> arrItems;
    ImageView ivLayoutChange;
    boolean isLinear;
    private DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    GpsInfo gps;
    myUtil util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        init();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_3bar_grey);
        actionBar.setDisplayHomeAsUpEnabled(true); //햄버거 아이콘이 클릭되게만 해줌
        //onOptionsItemSelected 을 구현해줘야..
        aboutTab();
        aboutData();
        aboutLayoutChange();
        aboutNavigation();

        StaggeredGridLayoutManager stagLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(stagLayoutManager);
        recyclerView.setHasFixedSize(true);
    }

    private void aboutNavigation() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) { //네비게이션 아이템에 대한
                item.setChecked(true);
                mDrawerLayout.closeDrawers();

                switch (item.getItemId()) {
                    case R.id.tab_destination:
                        Toast.makeText(getApplicationContext(), "거리순", Toast.LENGTH_SHORT).show();
                        AscendingDistance descendingDis = new AscendingDistance(gps.getLatitude(), gps.getLongitude());
                        Collections.sort(arrItems, descendingDis);
                        myAdapter.notifyDataSetChanged();
                        break;
                    case R.id.tab_popular:
                        Toast.makeText(getApplicationContext(), "인기순", Toast.LENGTH_SHORT).show();
                        Collections.sort(arrItems, new Comparator<ItemData>() {
                            @Override
                            public int compare(ItemData itemData, ItemData t1) {
                                return itemData.numPopular - t1.numPopular;
                            }
                        });
                        myAdapter.notifyDataSetChanged();
                        break;
                    case R.id.tab_recent:
                        Toast.makeText(getApplicationContext(), "최근순", Toast.LENGTH_SHORT).show();
                        Collections.sort(arrItems, new Comparator<ItemData>() {
                            @Override
                            public int compare(ItemData itemData, ItemData t1) {
                                return itemData.getDay().compareTo(t1.getDay());
                            }
                        });
                        myAdapter.notifyDataSetChanged();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){ //햄버거 아이콘을 클릭했을 때
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void init() {
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        ivLayoutChange = (ImageView) findViewById(R.id.iv_layoutChange);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        gps = new GpsInfo(this);
        util = new myUtil();
    }

    private void aboutLayoutChange() {
        isLinear = false; //초기 화면이 Staggered 이므로 Linear가 아님
        ivLayoutChange.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(!isLinear) {
                    ivLayoutChange.setImageResource(R.drawable.ic_staggered);
                    isLinear = true;

                    LinearLayoutManager layoutManager =  new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                }else{
                    ivLayoutChange.setImageResource(R.drawable.ic_linear);
                    isLinear = false;

                    StaggeredGridLayoutManager stagLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(stagLayoutManager);
                }
            }
        });
    }

    class BackThread extends Thread{
        @Override
        public void run() {
            arrItems = util.create_item();
            for(ItemData item : arrItems){
                System.out.println(item);
                item.setBitmap(util.getBitmapFromURL(item.getStoreImage()));
            }
            handler.sendEmptyMessage(0);
        } // end run()
    } // end class BackThread

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){   // Message id 가 0 이면
                myAdapter = new MyAdapter();
                myAdapter.setContext(getApplicationContext());
                myAdapter.setItemDatas(arrItems);
                recyclerView.setAdapter(myAdapter);
            }
        }
    };

    private void aboutData(){
        // 스레즈 생성하고 시작
        BackThread thread = new BackThread();
        thread.setDaemon(true);
        thread.start();
    }

    private void aboutTab() {
        tabLayout.setSelectedTabIndicatorHeight(0);
        tabLayout.addTab(tabLayout.newTab().setText("거리순"));
        tabLayout.addTab(tabLayout.newTab().setText("인기순"));
        tabLayout.addTab(tabLayout.newTab().setText("최근순"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) { //탭에 따라 아이템 소팅해주는
                switch (tab.getPosition()){
                    case 0:
                        AscendingDistance descendingDis = new AscendingDistance(gps.getLatitude(), gps.getLongitude());
                        Collections.sort(arrItems, descendingDis);
                        myAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        Collections.sort(arrItems, new Comparator<ItemData>() {
                            @Override
                            public int compare(ItemData itemData, ItemData t1) {
                                return itemData.numPopular - t1.numPopular;
                            }
                        });
                        myAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        Collections.sort(arrItems, new Comparator<ItemData>() {
                            @Override
                            public int compare(ItemData itemData, ItemData t1) {
                                return itemData.getDay().compareTo(t1.getDay());
                            }
                        });
                        myAdapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


}