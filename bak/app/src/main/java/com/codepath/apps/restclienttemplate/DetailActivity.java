package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.activeandroid.Cache.getContext;


public class DetailActivity extends ActionBarActivity {
    private Tweet tweet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        int position = getIntent().getIntExtra("position", 0);
        tweet = TimelineActivity.tweets.get(position);
        Log.d("debug", "name=" + tweet.getUser().getName());

        // get object
        ImageView ivProfileImage = (ImageView)findViewById(R.id.ivProfileImage);
        TextView tvName= (TextView)findViewById(R.id.tvName);
        TextView tvScreemName = (TextView)findViewById(R.id.tvScreenName);
        TextView tvBody = (TextView)findViewById(R.id.tvBody);
        TextView tvCreatedAt = (TextView)findViewById(R.id.tvCreatedAt);
        ImageView ivMedia = (ImageView)findViewById(R.id.ivMedia);
        ImageButton ibReplay = (ImageButton)findViewById(R.id.ibtReply);
        // set value into object
        // name
        tvName.setText(tweet.getUser().getName());
        // screen name
        tvScreemName.setText("@"+tweet.getUser().getScreen_name());
        // body
        tvBody.setText(tweet.getBody());
        tvBody.setMovementMethod(LinkMovementMethod.getInstance());
        // profile image
        ivProfileImage.setImageResource(android.R.color.transparent); // clear image view
        Picasso.with(getContext())
                .load(tweet.getUser().getProfile_img_url())
                .transform(new RoundedTransformation(5, 0))
                .into(ivProfileImage);
        // created at
        Date date = new Date(tweet.getCreated_at());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm a"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8")); // give a timezone reference for formating (see comment at the bottom
        String DateToStr = sdf.format(date);
        tvCreatedAt.setText(DateToStr);
        // reply button
        ibReplay.setImageResource(R.drawable.reply);

        // media
        ivMedia.setImageResource(android.R.color.transparent);  // clear image view
        if(tweet.getMedia_url()!=null) {
            Picasso.with(getContext())
                    .load(tweet.getMedia_url())
                    .transform(new RoundedTransformation(0, 0))
                    .into(ivMedia);
        }else{
            ivMedia.setMaxHeight(0);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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

    public void showReplyPage(View view) {
        Intent i = new Intent(this, ReplyActivity.class);
        i.putExtra("uid", tweet.getUid());
        i.putExtra("profile_image_url",LoginActivity.currentUser.getProfile_img_url());
        i.putExtra("name", tweet.getUser().getName());
        i.putExtra("screenName", tweet.getUser().getScreen_name());
        startActivityForResult(i,20);
    }
}
