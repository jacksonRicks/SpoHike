package com.example.spohike;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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

public class HikingAPI {
    // wrapper class for all of our flickr API related members
    static final String BASE_URL = "https://www.hikingproject.com/data/";
    static final String API_KEY = "200643790-27fe3d25d62d9f26d57f602eb33040d5"; // BAD PRACTICE!!
    static final String TAG = "Hiking";

    MapsActivity mainActivity; // for callbacks, because our code
    // is going to run asynchronously

    public HikingAPI(MapsActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void fetchTrailList(double lat, double lon) {
        // need url for request
        String url = constructTrailListURL(lat,lon);
        Log.d(TAG, "fetchInterestingPhotoList: " + url);

        // start the background task to fetch the photos
        // we have to use a background task!
        // Android will not let you do any network activity
        // on the main UI thread
        // define a subclass of AsyncTask
        // (async asynchronous which means doesn't wait/block)
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
        //url += "&sort=distance";
        url += "&key=" + API_KEY;
        //url += "&api_key=" + API_KEY;
        //url += "&format=json";
       // url += "&nojsoncallback=1";
       // url += "&extras=date_taken,url_h";
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
            // executes on the main UI thread
            // we can update the UI here
            // later, we will show an indeterminate progress bar
            //ProgressBar progressBar = mainActivity.findViewById(R.id.progressBar);
            //progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Trail> doInBackground(String... strings) {
            // executes on the background thread
            // CANNOT update the UI thread
            // this is where we do 3 things
            // 1. open the url request
            // 2. download the JSON response
            // 3. parse the JSON response in to InterestingPhoto objects

            List<Trail> TrailList = new ArrayList<>();
            String url = strings[0]; // ... is call var args, treat like an array

            try {
                // 1. open url request
                URL urlObject = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) urlObject.openConnection();
                // successfully opened url over HTTP protocol

                // 2. download JSON response
                String jsonResult = "";
                // character by character, we are going to build the json string from an input stream
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    jsonResult += (char) data;
                    data = reader.read();
                }
                Log.d(TAG, "doInBackground: " + jsonResult);

                // 3. parse the JSON
                JSONObject jsonObject = new JSONObject(jsonResult);
                // grab the "root" photos jsonObject
                //JSONObject ListObject = jsonObject.getJSONObject("trails"); // photos is the key
                JSONArray trailArray = jsonObject.getJSONArray("trails"); // photo is the key
                for (int i = 0; i < trailArray.length(); i++) {
                    JSONObject singleTrailObject = trailArray.getJSONObject(i);
                    // try to a parse a single photo info
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
                // do nothing
            }

            return trail;
        }

        @Override
        protected void onPostExecute(List<Trail> trail) {
            super.onPostExecute(trail);
            // executes on the main UI thread
            // after doInBackground() is done
            // update the main UI thread with the result of doInBackground()
            // interestingPhotos
            Log.d(TAG, "onPostExecute: " + trail);
            Log.d(TAG, "onPostExecute: " + trail.size());
            mainActivity.receivedInterestingPhotos(trail);

           // ProgressBar progressBar = mainActivity.findViewById(R.id.progressBar);
            //progressBar.setVisibility(View.GONE);
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
