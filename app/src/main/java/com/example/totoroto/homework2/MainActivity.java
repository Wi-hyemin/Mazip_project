package com.example.totoroto.homework2;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        myAdapter = new MyAdapter();
        myAdapter.setItemDatas(arrItems);
        recyclerView.setAdapter(myAdapter);
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
                        break;
                    case R.id.tab_popular:
                        Toast.makeText(getApplicationContext(), "인기순", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tab_recent:
                        Toast.makeText(getApplicationContext(), "최근순", Toast.LENGTH_SHORT).show();
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

    private void aboutData() {
        arrItems = new ArrayList<>();

        arrItems.add(new ItemData(R.drawable.store_img1, "해우소","꿀막걸리가 인기메뉴고 맛있는 음식을 저렴하게 즐길 수 있어요", 1, 6, 5, false));
        arrItems.add(new ItemData(R.drawable.store_img2, "삼교리동치미막국수","고객이 만들어 먹는 막국수", 2, 5, 4, false));
        arrItems.add(new ItemData(R.drawable.store_img3, "흥부네 화덕","구워 나오는 화덕 피자", 3, 4, 1, false));
        arrItems.add(new ItemData(R.drawable.store_img4, "삼송빵집","삼송에서 운영하는 빵집", 4, 3, 3, false));
        arrItems.add(new ItemData(R.drawable.store_img5, "청년다방","차돌박이 떡볶이가 맛있는 체인점", 5, 2, 6, false));
        arrItems.add(new ItemData(R.drawable.store_img6, "충무김밥","정성으로 직접 만든 미가 충무김밥", 6, 1, 2, false));
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
                       Collections.sort(arrItems, new Comparator<ItemData>() {
                            @Override
                            public int compare(ItemData itemData, ItemData t1) {
                                return itemData.numDistance - t1.numDistance;
                            }
                        });
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
                                return itemData.numRecent - t1.numRecent;
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
