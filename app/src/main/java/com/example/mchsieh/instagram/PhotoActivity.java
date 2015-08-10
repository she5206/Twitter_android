package com.example.mchsieh.instagram;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotoActivity extends Activity {

    public static String CLIENT_ID="b5b345b3217c479b81c68e67ac41bb99";
    private ArrayList<PopularPhotos> photosArrayList;
    private PopularPhotosAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        photosArrayList = new ArrayList<>();
        // set adapter
        adapter=new PopularPhotosAdapter(this, photosArrayList);
        ListView listView = (ListView) findViewById(R.id.lvPhotos);
        listView.setAdapter(adapter);
        // get data from API
        fetchPopularPhotos();
    }

    public void fetchPopularPhotos(){

        String url ="https://api.instagram.com/v1/media/popular?client_id="+CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJsonArray;
                try {
                    photosJsonArray = response.getJSONArray("data");
                    for (int i = 0; i < photosJsonArray.length(); i++) {
                        JSONObject photoObject = photosJsonArray.getJSONObject(i);
                        PopularPhotos photo = new PopularPhotos();
                        photo.profile_img_url = photoObject.getJSONObject("user").getString("profile_picture");
                        photo.user_name = photoObject.getJSONObject("user").getString("username");
                        photo.createdTime = photoObject.getString("created_time");
                        photo.caption = photoObject.getJSONObject("caption").getString("text");
                        photo.img_url = photoObject.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.img_height = photoObject.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.like_count = photoObject.getJSONObject("likes").getInt("count");
                        photosArrayList.add(photo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}


