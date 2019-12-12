package com.example.spohike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * SingleTrail.java
 * Jackson Ricks, Alex Weaver
 * SpoHike
 */

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


    public void setCurrentValues(Trail trail){
        TextView name = (TextView) findViewById(R.id.Name);
        TextView stars = (TextView) findViewById(R.id.Stars);
        TextView summary = (TextView) findViewById(R.id.Summary);
        TextView location = (TextView) findViewById(R.id.Location);
        TextView length = (TextView) findViewById(R.id.length);
        ImageView image = (ImageView) findViewById(R.id.image);
        name.setText(trail.getName());
        stars.setText("Stars: " + trail.getStars());
        summary.setText(trail.getSummary());
        location.setText("(Lat, Lon): (" + trail.getLatitude() + ", " + trail.getLongitude() + ")");
        length.setText(trail.getLength() + " miles");
        new GetImage((ImageView) findViewById(R.id.image)).execute(trail.getPhotoURL());
    }

    private class GetImage extends AsyncTask<String, Void, Bitmap> {
        ImageView image;

        public GetImage(ImageView image) {
            this.image = image;
        }


        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)
                        url.openConnection();

                InputStream in = urlConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {

            image.setImageBitmap(result);
        }
    }
}