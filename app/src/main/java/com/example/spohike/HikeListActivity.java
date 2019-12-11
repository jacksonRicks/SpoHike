package com.example.spohike;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;

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

        //trailList = (ArrayList<Trail>) getIntent().getSerializableExtra("trailList");
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        trailList = (ArrayList<Trail>) args.getSerializable("ARRAYLIST");

        final ListView listView = new ListView(this);
        arrayAdapter = new ArrayAdapter<Trail>(this, android.R.layout.simple_list_item_2, android.R.id.text1, trailList){

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view =  super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                Trail trail = trailList.get(position);
                text1.setText(trail.getName());
                text2.setText("Stars: " + trail.getStars());
                return view;
            }
        };
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