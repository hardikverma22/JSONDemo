package com.example.android.jsondemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    public class DownloadTask extends AsyncTask<String,Void,String> {
        String result="";
        @Override
        protected String doInBackground(String... urls) {
            //Log.i("URL",params[0]);

            URL url;
            HttpURLConnection urlConnection=null;

            try
            {
                url = new URL(urls[0]);
                urlConnection= (HttpURLConnection) url.openConnection();
                //stream to hold the data
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1){
                    char current =(char)data;
                    result+=current;
                    data=reader.read();
                }
                return result;
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return "Something Went Wrong";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String weather = jsonObject.getString("weather");
                //Log.i("Weather Info",weather);
                JSONArray arr = new JSONArray(weather);
                for(int i=0;i<arr.length();i++){
                    JSONObject jsonpart = arr.getJSONObject(i);
                    Log.i("main : ",jsonpart.getString("main"));
                    Log.i("description : ",jsonpart.getString("description"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask downloadTask = new DownloadTask();
        String result=null;
        try {
            result = downloadTask.execute("http://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=bfad1ae5c2f17db0c3fe5e85b57ce50a").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.i("result : ",result);
    }

}
