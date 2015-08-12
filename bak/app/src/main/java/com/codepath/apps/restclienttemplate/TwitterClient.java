package com.codepath.apps.restclienttemplate;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	// key : wf57y68hBfWmdLEOs6MLsxLFM
	// secret : 2oclWdP9euYThB1GymyIgvqWLY5DY4VmscoaJtoZF0GVPGeV1C
	public static final String REST_CONSUMER_KEY = "JYgJcHLbSfNTxSdE8npwKKu5T";       // Change this
	public static final String REST_CONSUMER_SECRET = "7GpQrXxRyrSSMwDlA5QAoHIO62THpdblr6C7yG2a3lHJlYo0Yn"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}


	// Home Timeline
	// statuses/home_timeline.json
	// count = 25
	// since_id=1
	public void getHomeTimeline(int page, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 20*page);
		params.put("since_id", 1);
		params.put("include_entities","true");
		// execute
		getClient().get(apiUrl, params, handler);
	}

	// Get Current User
	// 1.1/account/verify_credentials.json
	public void getUserInfo(AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("account/verify_credentials.json");
		// execute
		getClient().get(apiUrl, null, handler);
	}

	// Compose a Tweet
	// 1.1/account/verify_credentials.json
	public void composeTweet(String content, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", content);
		// execute
		getClient().post(apiUrl, params, handler);

	}

	// Reply a Tweet
	// 1.1/statuses/update.json
	public void replyTweet(long uid, String content, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", content);
		params.put("in_reply_to_status_id",uid);
		// execute
		getClient().post(apiUrl, params, handler);
	}

	// Favorite a tweet
	// 1.1/favorites/create.json
	public void favoriteTweet(long uid, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("favorites/create.json");
		RequestParams params = new RequestParams();
		params.put("id",uid);
		// execute
		getClient().post(apiUrl, params, handler);
	}

	// UnFavorite a tweet
	// 1.1/favorites/destroy.json
	public void unfavoriteTweet(long uid, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("favorites/destroy.json");
		RequestParams params = new RequestParams();
		params.put("id",uid);
		// execute
		getClient().post(apiUrl, params, handler);
	}

	// Retweet a tweet
	// 1.1/statuses/retweet/%@.json
	public void retweetTweet(long uid, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/retweet/"+uid+".json");
		// execute
		getClient().post(apiUrl, null, handler);
	}

	// Destoryed a tweet
	// 1.1/statuses/destroy/:id.json
	public void destroyTweet(long uid, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/destroy/"+uid+".json");
		// execute
		getClient().post(apiUrl, null, handler);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}