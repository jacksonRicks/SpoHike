package com.example.spohike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class HikeListActivity extends AppCompatActivity {

    List<Trail> trailList;
    ArrayAdapter<Trail> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GridLayout gridLayout = new GridLayout(this);
        setContentView(gridLayout);
        gridLayout.setColumnCount(1);
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = GridLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
        trailList = (ArrayList<Trail>) getIntent().getSerializableExtra("trailList");
        final ListView listView = new ListView(this);
        arrayAdapter = new ArrayAdapter<Trail>(this, android.R.layout.simple_list_item_1, trailList);
        listView.setAdapter(arrayAdapter);
        gridLayout.addView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Trail trailSelected = trailList.get(position);
                intent.putExtra("trail", trailSelected);
            }
        });
    }
}


//comment