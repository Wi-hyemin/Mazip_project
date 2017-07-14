package com.example.totoroto.homework2;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        ivLayoutChange = (ImageView) findViewById(R.id.iv_layoutChange);

        aboutTab();
        aboutData();
        aboutLayoutChange();

        StaggeredGridLayoutManager stagLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(stagLayoutManager);
        recyclerView.setHasFixedSize(true);

        myAdapter = new MyAdapter();
        myAdapter.setItemDatas(arrItems);
        recyclerView.setAdapter(myAdapter);
    }

    private void aboutLayoutChange() {
        isLinear = false;
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
