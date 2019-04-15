package com.iman.keepword;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.iman.keepword.adpter.HomeAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private RecyclerView recyclerView ;
    private GridLayoutManager gridLayoutManager;
    private HomeAdapter homeAdapter;
    private final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.RV);
        init();
    }

    private void init() {
        gridLayoutManager = new GridLayoutManager(activity,2);
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Add");
        arrayList.add("Remind");
        arrayList.add("Dictionary");
        arrayList.add("Wall");
        arrayList.add("Spell");
        arrayList.add("new feature");
        homeAdapter = new HomeAdapter(activity,arrayList);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(homeAdapter);
    }
}
