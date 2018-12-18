package com.tekken.loadmoresample;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvEmptyView;
    private RecyclerView recyclerView;
    private DataAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private List<Model> ModelList;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvEmptyView = findViewById(R.id.empty_view);
        recyclerView = findViewById(R.id.my_recycler_view);
        handler = new Handler();
        ModelList = new ArrayList<>();
        load();

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new DataAdapter(ModelList, recyclerView);
        recyclerView.setAdapter(adapter);

        if(ModelList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }

        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                ModelList.add(null);
                adapter.notifyItemInserted(ModelList.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ModelList.remove(ModelList.size()-1);
                        adapter.notifyItemRemoved(ModelList.size());

                        int start = ModelList.size();
                        int end = start + 20;

                        for(int i = start + 1; i <= end; i++) {
                            ModelList.add(new Model("House of God " + i, "XYZ" + i + "@amity.com"));
                            adapter.notifyItemInserted(ModelList.size());
                        }
                        adapter.setLoaded();
                    }
                }, 2000);
            }

            @Override
            public void onSelected(int position) {

            }
        });

    }



    public void load(){
        for(int i = 1; i <= 20; i++) {
            ModelList.add(new Model("House of God " + i, "XYZ" + i + "@amity.com"));
        }
    }
}
