package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.EndlessScrollListener;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.activeandroid.Cache.getContext;


public class TimelineActivity extends ActionBarActivity {

    private TwitterClient client;
    public static ArrayList<Tweet> tweets;
    private TweetsArrayAdapter atweet;
    private ListView lvTweets;
    private static int page =0;
    private SwipeRefreshLayout swipeContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        // set logo
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.logo2);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // set content
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        lvTweets = (ListView)findViewById(R.id.lvTweets);
        tweets = new ArrayList<>();
        atweet = new TweetsArrayAdapter(this, tweets);
        lvTweets.setAdapter(atweet);
        client = TwitterApplication.getRestClient();
        popularTimeline();

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                popularTimeline();
            }
        });

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                popularTimeline();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void popularTimeline(){
        page++;
        if(page==1){
            atweet.clear();
        }
        Log.d("Debug", "page="+String.valueOf(page));
        client.getHomeTimeline(page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("Debug", "json=" + json.toString());
                Log.d("Debug", "length=" + String.valueOf(json.length()));
                try {
                    atweet.addAll(Tweet.fromJSONArray(page, json));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                atweet.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Debug", errorResponse.toString());
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void showPostPage(MenuItem item) {
        Intent i = new Intent(this, PostActivity.class);
        startActivityForResult(i,REQUEST_CODE);
    }

    public void showReplyPage(View view) {

        int position = getIntent().getIntExtra("position", 0);
        Tweet tweet = tweets.get(position);
        Intent i = new Intent(this, ReplyActivity.class);
        Log.d("dddddd",String.valueOf(position));
        Log.d("dddddd",tweet.getUser().getName());
        i.putExtra("uid", tweet.getUid());
        i.putExtra("profile_image_url",LoginActivity.currentUser.getProfile_img_url());
        i.putExtra("name", tweet.getUser().getName());
        i.putExtra("screenName", tweet.getUser().getScreen_name());
        startActivityForResult(i,REQUEST_CODE);
    }



    private static int retweet_counter=0;
    public void onRetweetClicked(View view) {
        retweet_counter++;
        ImageButton ibRetweet = (ImageButton)findViewById(R.id.ibRetweet);
        TextView tvRetweetCount = (TextView)findViewById(R.id.tvRetweetCount);
        String tvValue = tvRetweetCount.getText().toString();
        int orig_count = Integer.parseInt(tvValue);
        if(retweet_counter%2==1) {
            ibRetweet.setImageResource(R.drawable.retweet_after);
            tvRetweetCount.setText(String.valueOf(++orig_count));
        }else{
            ibRetweet.setImageResource(R.drawable.retweet_before);
            tvRetweetCount.setText(String.valueOf(--orig_count));
        }
    }

    private final int REQUEST_CODE = 20;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // reload page content
            page = 0;
            popularTimeline();
        }
    }


    public void refreshTimelinePage(MenuItem item) {
        page = 0;
        popularTimeline();
        Toast.makeText(getContext(), "refresh completed!", Toast.LENGTH_SHORT).show();
    }
}
