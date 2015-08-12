package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mchsieh on 8/9/15.
 */
public class Tweet {

    private long uid;
    private User user;
    private String body;
    private String created_at;
    private String media_url;
    private int retweet_count;
    private int favorite_count;
    private boolean favorited;

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    private boolean retweeted;

    public boolean isRetweeted() {
        return retweeted;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public int getRetweet_count() {
        return retweet_count;
    }

    public int getFavorite_count() {
        return favorite_count;
    }


    public long getUid() {return uid;}
    public User getUser() {
        return user;
    }
    public String getBody() { return body;}
    public String getCreated_at() {return created_at;}
    public String getMedia_url() { return media_url;}
    public int getFavoriteCount() {return favorite_count;}
    public int getRetweetCount() {return retweet_count;}


    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.created_at=jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.retweet_count = jsonObject.getInt("retweet_count");
        tweet.favorite_count = jsonObject.getInt("favorite_count");
        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.retweeted = jsonObject.getBoolean("retweeted");
        JSONObject entities = jsonObject.getJSONObject("entities");
        if(entities.has("media")){
            JSONArray medias = entities.getJSONArray("media");
            JSONObject media;
            for (int i = 0; i < 1; i++) {
                media = (JSONObject) medias.get(i);
                tweet.media_url = media.getString("media_url");
            }
        }else{
            tweet.media_url=null;
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(int page, JSONArray jsonArray) throws JSONException {
        ArrayList<Tweet> tweets = new ArrayList<>();
        Log.d("Debug", "here page=" + String.valueOf(page));
        for(int i =(page-1)*20; i<jsonArray.length(); i++){
            JSONObject jsonobj = jsonArray.getJSONObject(i);
            Tweet tweet = fromJSON(jsonobj);
            if(tweet != null){
                tweets.add(tweet);
            }
        }
        return tweets;
    }
}
