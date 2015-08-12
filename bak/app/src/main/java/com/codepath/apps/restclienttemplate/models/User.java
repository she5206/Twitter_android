package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mchsieh on 8/9/15.
 */
public class User {

    private String name;
    private long uid;
    private String screen_name;
    private String profile_img_url;

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public String getProfile_img_url() {
        return profile_img_url;
    }


    public static User fromJSON(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.name = jsonObject.getString("name");
        user.uid = jsonObject.getLong("id");
        user.screen_name = jsonObject.getString("screen_name");
        user.profile_img_url = jsonObject.getString("profile_image_url");
        return user;
    }
}
