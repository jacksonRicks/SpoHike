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

import java.io.InputStream;

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
        TextView name = (TextView) findViewById(R.id.Name);
        TextView stars = (TextView) findViewById(R.id.Stars);
        TextView summary = (TextView) findViewById(R.id.Summary);
        TextView location = (TextView) findViewById(R.id.Location);
        TextView length = (TextView) findViewById(R.id.length);
        ImageView image = (ImageView) findViewById(R.id.image);
        name.setText(trail.getName());
        stars.setText("Stars: " + trail.getStars());
        summary.setText(trail.getSummary());
        location.setText("Lon: " + trail.getLongitude());
        length.setText(trail.getLength() + " miles");
        new DownloadImageTask((ImageView) findViewById(R.id.image)).execute(trail.getPhotoURL());
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
//            progressBar.setVisibility(View.VISIBLE);
//        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
//            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
//            progressBar.setVisibility(View.GONE);
            bmImage.setImageBitmap(result);
        }
    }
}