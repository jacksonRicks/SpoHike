package com.example.spohike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        TextView name = (TextView) findViewById(R.id.title);
        TextView stars = (TextView) findViewById(R.id.Stars);
        TextView summary = (TextView) findViewById(R.id.Summary);
        TextView location = (TextView) findViewById(R.id.Location);
        TextView length = (TextView) findViewById(R.id.length);
        ImageView image = (ImageView) findViewById(R.id.image);

        name.setText(trail.getName());
        stars.setText(trail.getStars());
        summary.setText(trail.getStars());
        location.setText(trail.getLongitude());
        length.setText(trail.getLength());





    }
}
