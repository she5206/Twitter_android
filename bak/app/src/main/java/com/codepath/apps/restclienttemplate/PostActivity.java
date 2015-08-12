package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import static com.activeandroid.Cache.getContext;


public class PostActivity extends ActionBarActivity {
    private TwitterClient client;
    private static User currentUser;
    private static EditText etPostContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        client = TwitterApplication.getRestClient();
        currentUser=LoginActivity.currentUser;
        if(currentUser==null) {
            currentUser=new User();
            findCurrentUser();
        }
        // get object
        ImageView ivProfileImage = (ImageView)findViewById(R.id.ivProfileImage);
        TextView tvScreenName = (TextView)findViewById(R.id.tvScreenName);
        TextView tvName = (TextView)findViewById(R.id.tvName);
        etPostContent = (EditText)findViewById(R.id.etvPostContent);


        // set object
        // profile Image
        ivProfileImage.setImageResource(android.R.color.transparent); // clear image view
        Picasso.with(getContext())
                .load(currentUser.getProfile_img_url())
                .transform(new RoundedTransformation(5, 0))
                .into(ivProfileImage);
        // username
        tvName.setText(currentUser.getName());
        // screen name
        tvScreenName.setText("@" + currentUser.getScreen_name());



        // Post Content
        // 1. set Filter to 140 characters
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(140);
        etPostContent.setFilters(filters);
        // 2. Words Counter
        etPostContent.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = 140-etPostContent.getText().length();
                ActionMenuItemView ivWords=(ActionMenuItemView) findViewById(R.id.action_words);
                ivWords.setText(String.valueOf(length));
            }
        });

    }


    private void findCurrentUser() {
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                try {
                    Log.d("Debug", json.toString());
                    currentUser = User.fromJSON(json);
                    Log.d("Debug", currentUser.getName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        getMenuInflater().inflate(R.menu.menu_post, menu);
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

    public void onTweetButton(MenuItem item) {
        client.composeTweet(etPostContent.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.d("Debug", json.toString());
                setResult(RESULT_OK); // set result code and bundle data for response
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Debug", errorResponse.toString());
            }
        });

    }
}
