package com.example.spohike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

public class SingleTrail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_trail);

        Intent intent = getIntent();
        if (intent != null ) {
            Trail trail = (Trail) intent.getSerializableExtra("trail");


            setCurrentValues(trail);
        }


    }

    // GS: added after class
    public void receivedPhotoBitmap(Bitmap bitmap) {
        //ImageView imageView = findViewById(R.id.imageView);
        //imageView.setImageBitmap(bitmap);
    }

    public void setCurrentValues(Trail trail){

    }
}
