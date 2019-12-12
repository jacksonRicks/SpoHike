package com.example.spohike;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * HikingAPI.java
 * Jackson Ricks, Alex Weaver
 * SpoHike
 */

public class HikingAPI {
    static final String BASE_URL = "https://www.hikingproject.com/data/";
    static final String API_KEY = "200643790-27fe3d25d62d9f26d57f602eb33040d5";
    static final String TAG = "Hiking";

    MapsActivity mainActivity;

    public HikingAPI(MapsActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void fetchTrailList(double lat, double lon) {
        // need url for request
        String url = constructTrailListURL(lat,lon);
        Log.d(TAG, "fetchInterestingPhotoList: " + url);

        FetchTrailListAsyncTask asyncTask = new FetchTrailListAsyncTask();
        asyncTask.execute(url);

    }

    public String constructTrailListURL(double lat, double lon) {
        // https://www.hikingproject.com/data/get-trails?lat=40.1274&lon=-105.2519&maxDistance=10&key=200643790-27fe3d25d62d9f26d57f602eb33040d5
        String url = BASE_URL;

        url += "get-trails";
        url += "?lat=" + lat;
        url += "&lon=" + lon;
        url += "&maxDistance=10";
        url += "&maxResults=20";
        url += "&sort=distance";
        url += "&key=" + API_KEY;
        return url;
    }

    // parameterized types are
    // argument to doInBackground(): String url
    // argument to publish(): Void (we are not going to update the UI mid task)
    // return value of doInBackground() and the argument to onPostExecute():
    // is our list of sucessfully parsed and created InterestingPhoto objects
    class FetchTrailListAsyncTask
            extends AsyncTask<String, Void, List<Trail>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected List<Trail> doInBackground(String... strings) {


            List<Trail> TrailList = new ArrayList<>();
            String url = strings[0]; // ... is call var args, treat like an array

            try {
                URL urlObject = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) urlObject.openConnection();

                String jsonResult = "";

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    jsonResult += (char) data;
                    data = reader.read();
                }
                Log.d(TAG, "doInBackground: " + jsonResult);


                JSONObject jsonObject = new JSONObject(jsonResult);

                JSONArray trailArray = jsonObject.getJSONArray("trails");
                for (int i = 0; i < trailArray.length(); i++) {
                    JSONObject singleTrailObject = trailArray.getJSONObject(i);

                    Trail trail = parseTrail(singleTrailObject);
                    if (trail != null) {
                        TrailList.add(trail);
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return TrailList;
        }

        private Trail parseTrail(JSONObject singleTrailObject) {
            Trail trail = null;

            try {
                String id = singleTrailObject.getString("id"); // id is the key
                String name = singleTrailObject.getString("name");
                String summary = singleTrailObject.getString("summary");
                String longitude = singleTrailObject.getString("longitude");
                String latitude = singleTrailObject.getString("latitude");
                String stars = singleTrailObject.getString("stars");
                String length = singleTrailObject.getString("length");
                String photoURL = singleTrailObject.getString("imgMedium");
                trail = new Trail(id, name, summary, longitude, latitude, stars, length, photoURL);
            } catch (JSONException e) {

            }

            return trail;
        }

        @Override
        protected void onPostExecute(List<Trail> trail) {
            super.onPostExecute(trail);
            mainActivity.receivedInterestingPhotos(trail);

        }
    }

   // GS: added after class
    public void fetchPhotoBitmap(String photoURL) {
        PhotoRequestAsyncTask asyncTask = new PhotoRequestAsyncTask();
        asyncTask.execute(photoURL);
    }

    // GS: added after class
    class PhotoRequestAsyncTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
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

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);


            //mainActivity.receivedPhotoBitmap(bitmap);
        }
    }


}
