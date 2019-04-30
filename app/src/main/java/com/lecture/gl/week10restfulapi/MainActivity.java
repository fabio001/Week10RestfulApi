package com.lecture.gl.week10restfulapi;

import android.app.ProgressDialog;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    private static String TAG= "Week10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void cat_api_click(View view) {

        /*FakeDownloader ff = new FakeDownloader();
        ff.execute("FakeDownload");
        */

        Ion.with(this)
                .load("https://api.thecatapi.com/v1/images/search")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        process_cat("{\"randimg\":"+ result+ "}");
                    }
                });

    }


    /*
    *
    {
        "randimg":[
            {
            "breeds":{},
            "id":"bmh",
            "url":"https://cdn2.thecatapi.com/images/bmh.jpg",
            "width":540,
            "height":360
            }
      ]
    }
    */
    private void process_cat(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String url = jsonObject.getJSONArray("randimg")
                    .getJSONObject(0)
                    .getString("url");

            ImageView img = findViewById(R.id.img_catView);
            Picasso.get()
                    .load(url)
                    .into(img);



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void chuck_click(View view) {

        Ion.with(this)
                .load("http://api.icndb.com/jokes/random")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                        process_joke(result);
                    }
                });


    }


    /*
    *
    * {
        "type":"success",
        "value":{
            "id":605,
            "joke":"Chuck Norris doesn't need a keyboard he tells the computer to write something and it does.",
            "categories":[
            "nerdy"
            ]
            }
       }
    * */
    private void process_joke(String jsonString){

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String joke = jsonObject.getJSONObject("value").getString("joke");
            TextView txt_chuck = findViewById(R.id.txt_chuck);
            txt_chuck.setText(joke);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private class FakeDownloader extends AsyncTask<String, Integer, String>{

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "OnPreExecute");

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Downloader");
            progressDialog.setMessage("Fake Download");
            progressDialog.setProgress(0);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            Log.d(TAG, "DoInBackground with param: " + strings[0]);

            int i =0;
            while (i<10){
                try {
                    Thread.currentThread().sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i*10);
                i++;
            }


            return strings[0] + " doinbackground";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            int value = values[0];

            Log.d(TAG, "publishUpdate:" + value);

            progressDialog.setProgress(value);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute");
            progressDialog.dismiss();
            Log.d(TAG, "Result is :" + s);
        }
    }


}
