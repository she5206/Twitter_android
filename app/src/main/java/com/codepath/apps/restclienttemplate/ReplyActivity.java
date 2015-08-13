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
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

import static com.activeandroid.Cache.getContext;


public class ReplyActivity extends ActionBarActivity {
    private TwitterClient client;
    private EditText edReplyContent;
    private Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        client = TwitterApplication.getRestClient();
        // get object
        ImageView ivProfileImage = (ImageView)findViewById(R.id.ivProfileImage);
        TextView tvReplyToWho = (TextView)findViewById(R.id.tvReplyToWho);
        edReplyContent = (EditText)findViewById(R.id.edReplyContent);
        // set object
        // profile image
        ivProfileImage.setImageResource(android.R.color.transparent); // clear image view
        Picasso.with(getContext())
                .load(getIntent().getStringExtra("profile_image_url"))
                .transform(new RoundedTransformation(5, 0))
                .into(ivProfileImage);
        // text reply to who
        tvReplyToWho.setText("Reply to "+getIntent().getStringExtra("name"));
        // edit text reply content
        edReplyContent.setText("@" + getIntent().getStringExtra("screenName") + " ");
        edReplyContent.setSelection(edReplyContent.length());

        // Post Content
        // 1. set Filter to 140 characters
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(140);
        edReplyContent.setFilters(filters);
        // 2. Words Counter
        edReplyContent.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = 140 - edReplyContent.getText().length();
                ActionMenuItemView ivWords = (ActionMenuItemView) findViewById(R.id.action_words);
                ivWords.setText(String.valueOf(length));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reply, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem settingsItem = menu.findItem(R.id.action_words);
        int length = 140 - edReplyContent.getText().length();
        settingsItem.setTitle(String.valueOf(length));

        return super.onPrepareOptionsMenu(menu);
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


    public void onReplyButton(MenuItem item) {
        client.replyTweet(getIntent().getLongExtra("uid", 0),edReplyContent.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.d("Debug", json.toString());
                setResult(RESULT_OK); // set result code and bundle data for response
                Toast.makeText(getContext(), "reply successful", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Debug", errorResponse.toString());
            }
        });

    }
}
