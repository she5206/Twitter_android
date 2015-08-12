package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by mchsieh on 8/9/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter{
    public TweetsArrayAdapter(Context context, List<Tweet>tweets){
        super(context, android.R.layout.simple_list_item_1, tweets);

    }
    private TwitterClient client;
    private int REQUEST_CODE=20;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Tweet tweet = (Tweet) getItem(position);
        client = TwitterApplication.getRestClient();
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet,parent,false);
        }

        // get object
        ImageView ivProfileImage = (ImageView)convertView.findViewById(R.id.ivProfileImg);
        TextView tvName= (TextView)convertView.findViewById(R.id.tvName);
        TextView tvScreemName = (TextView)convertView.findViewById(R.id.tvScreenName);
        TextView tvBody = (TextView)convertView.findViewById(R.id.tvBody);
        TextView tvCreatedAt = (TextView)convertView.findViewById(R.id.tvCreatedAt);
        ImageView ivMedia = (ImageView)convertView.findViewById(R.id.ivMedia);
        ImageButton ibReplay = (ImageButton)convertView.findViewById(R.id.ibtReply);
        ImageButton ibRetweet = (ImageButton)convertView.findViewById(R.id.ibRetweet);
        TextView tvRetweetCount = (TextView)convertView.findViewById(R.id.tvRetweetCount);
        ImageButton ibFavorite = (ImageButton)convertView.findViewById(R.id.ibFavorite);
        TextView tvFavoriteCount = (TextView)convertView.findViewById(R.id.tvFavoriteCount);
        // set value into object
        // name
        tvName.setText(tweet.getUser().getName());
        // screen name
        tvScreemName.setText("@" + tweet.getUser().getScreen_name());
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8")); // give a timezone reference for formating (see comment at the bottom
        tvCreatedAt.setText(timeAgoInWords(date));
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
        // imagebutton - reply
        ibReplay.setImageResource(android.R.color.transparent);
        ibReplay.setImageResource(R.drawable.reply_before);

        // imagebutton - retweet
        ibRetweet.setImageResource(android.R.color.transparent);
        if(!tweet.isRetweeted()) {
            ibRetweet.setImageResource(R.drawable.retweet_before);
        }else{
            ibRetweet.setImageResource(R.drawable.retweet_after);
        }
        // textview - retweet count
        tvRetweetCount.setText(String.valueOf(tweet.getRetweetCount()));

        // imagebutton - favorite
        ibFavorite.setImageResource(android.R.color.transparent);
        if(!tweet.isFavorited()) {
            ibFavorite.setImageResource(R.drawable.favorite_before);
        }else{
            ibFavorite.setImageResource(R.drawable.favorite_after);
        }
        // textview - favorite count
        tvFavoriteCount.setText(String.valueOf(tweet.getFavoriteCount()));

        ibRetweet.setTag(position);
        ibRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tweet clickedTweet  = TimelineActivity.tweets.get(Integer.parseInt(v.getTag().toString()));
                RelativeLayout rl = (RelativeLayout)v.getParent();
                TextView tvRetweetCount = (TextView)rl.findViewById(R.id.tvRetweetCount);
                ImageButton ibRetweet = (ImageButton)rl.findViewById(R.id.ibRetweet);
                int orig_count = Integer.parseInt(tvRetweetCount.getText().toString());
                if(!clickedTweet.isRetweeted()){
                    clickedTweet.setRetweeted(true);
                    ibRetweet.setImageResource(R.drawable.retweet_after);
                    tvRetweetCount.setText(String.valueOf(++orig_count));
                    client.retweetTweet(clickedTweet.getUid(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                            Log.d("Debug", json.toString());
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("Debug", errorResponse.toString());
                        }
                    });
                }else{
                    clickedTweet.setRetweeted(false);
                    ibRetweet.setImageResource(R.drawable.retweet_before);
                    tvRetweetCount.setText(String.valueOf(--orig_count));
                    client.destroyTweet(clickedTweet.getUid(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                            Log.d("Debug", json.toString());
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("Debug", errorResponse.toString());
                        }
                    });
                }
            }
        });

        ibFavorite.setTag(position);
        ibFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tweet clickedTweet  = TimelineActivity.tweets.get(Integer.parseInt(v.getTag().toString()));
                RelativeLayout rl = (RelativeLayout)v.getParent();
                TextView tvFavoriteCount = (TextView)rl.findViewById(R.id.tvFavoriteCount);
                ImageButton ibFavorite = (ImageButton)rl.findViewById(R.id.ibFavorite);
                int orig_count = Integer.parseInt(tvFavoriteCount.getText().toString());
                if(!clickedTweet.isFavorited()){
                    clickedTweet.setFavorited(true);
                    ibFavorite.setImageResource(R.drawable.favorite_after);
                    tvFavoriteCount.setText(String.valueOf(++orig_count));
                    // favorite tweet! - twitter client
                    client.favoriteTweet(clickedTweet.getUid(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                            Log.d("Debug", json.toString());
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("Debug", errorResponse.toString());
                        }
                    });
                }else{
                    clickedTweet.setFavorited(false);
                    ibFavorite.setImageResource(R.drawable.favorite_before);
                    tvFavoriteCount.setText(String.valueOf(--orig_count));
                    // unfavorite tweet! - twitter client
                    client.unfavoriteTweet(clickedTweet.getUid(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                            Log.d("Debug", json.toString());
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("Debug", errorResponse.toString());
                        }
                    });
                }
            }
        });


        convertView.setTag(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tweet clickedTweet  = TimelineActivity.tweets.get(Integer.parseInt(v.getTag().toString()));
                Log.d("Debug", "name=" + clickedTweet.getUser().getName());
                Context context = getContext();
                Intent i = new Intent(context, DetailActivity.class);
                i.putExtra("position", Integer.parseInt(v.getTag().toString()));
                ((Activity) context).startActivityForResult(i, REQUEST_CODE);
            }
        });

        return convertView;
    }

    public static String timeAgoInWords(Date from) {
        Date now = new Date();
        long difference = now.getTime() - from.getTime();
        long distanceInMin = difference / 60000;

        if ( 0 <= distanceInMin && distanceInMin <= 1 ) {
            return "1 minute";
        } else if ( 1 <= distanceInMin && distanceInMin <= 45 ) {
            return distanceInMin + " minutes";
        } else if ( 45 <= distanceInMin && distanceInMin <= 89 ) {
            return "1 hour";
        } else if ( 90 <= distanceInMin && distanceInMin <= 1439 ) {
            return (distanceInMin / 60) + " hours";
        } else if ( 1440 <= distanceInMin && distanceInMin <= 2529 ) {
            return "1 day";
        } else if ( 2530 <= distanceInMin && distanceInMin <= 43199 ) {
            return (distanceInMin / 1440) + "days";
        } else if ( 43200 <= distanceInMin && distanceInMin <= 86399 ) {
            return "1 month";
        } else if ( 86400 <= distanceInMin && distanceInMin <= 525599 ) {
            return (distanceInMin / 43200) + " months";
        } else {
            long distanceInYears = distanceInMin / 525600;
            return distanceInYears + " years ago";
        }
    }




}
