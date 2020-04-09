package com.theneem.scienceclub.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.theneem.scienceclub.R;
import com.theneem.scienceclub.adapter.VideoPostAdapter;
import com.theneem.scienceclub.interfaces.OnItemClickListener;
import com.theneem.scienceclub.model.YoutubeDataModel;
import com.theneem.scienceclub.utils.CustomProgressBar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ClubVideoChannelList extends AppCompatActivity {

	private ProgressDialog progressDialog;
	private RecyclerView mList_videos = null;
	private VideoPostAdapter adapter = null;
	private ArrayList<YoutubeDataModel> mListData = new ArrayList<>();

	private static String GOOGLE_YOUTUBE_API_KEY = "AIzaSyAdDix7i7a3an-gyXiquTV_14cIsr8-DZg";//here you should use your api key for testing purpose you can use this api also
	String CHANNEL_ID = ""; //here you should use your channel id for testing purpose you can use this api also
	private String CHANNLE_GET_URL = "";
	String club_name = "";
	TextView txt_no_data_found;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_club_video_channel_list);
		progressDialog = CustomProgressBar.createProgressDialog(ClubVideoChannelList.this);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		mList_videos = findViewById(R.id.mList_videos);
		toolbar.setTitleTextColor(Color.WHITE);
		txt_no_data_found = findViewById(R.id.txt_no_data_found);
		final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
		upArrow.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
		getSupportActionBar().setHomeAsUpIndicator(upArrow);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		club_name = Objects.requireNonNull(getIntent().getExtras()).getString("club_name", "");
		CHANNEL_ID = Objects.requireNonNull(getIntent().getExtras()).getString("channel_id", "");
		Log.e("CHAAAAA", "" + CHANNEL_ID);
		Objects.requireNonNull(getSupportActionBar()).setTitle(club_name);
		initList(mListData);
		new RequestYoutubeAPI().execute();


	}

	private void initList(ArrayList<YoutubeDataModel> mListData) {
		mList_videos.setLayoutManager(new LinearLayoutManager(ClubVideoChannelList.this));
		adapter = new VideoPostAdapter(ClubVideoChannelList.this, mListData, new OnItemClickListener() {
			@Override
			public void onItemClick(YoutubeDataModel item) {
				YoutubeDataModel youtubeDataModel = item;
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + youtubeDataModel.getVideo_id())));
			}
		});
		mList_videos.setAdapter(adapter);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@SuppressLint("StaticFieldLeak")
	private class RequestYoutubeAPI extends AsyncTask<Void, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			HttpClient httpClient = new DefaultHttpClient();
			CHANNLE_GET_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&channelId=" + CHANNEL_ID + "&maxResults=50&key=" + GOOGLE_YOUTUBE_API_KEY + "&pageToken=CDIQAA";
			HttpGet httpGet = new HttpGet(CHANNLE_GET_URL);
			Log.e("URL", CHANNLE_GET_URL);
			try {
				HttpResponse response = httpClient.execute(httpGet);
				HttpEntity httpEntity = response.getEntity();
				String json = EntityUtils.toString(httpEntity);
				return json;
			} catch (IOException e) {
				e.printStackTrace();
			}


			return null;
		}

		@Override
		protected void onPostExecute(String response) {
			super.onPostExecute(response);
			progressDialog.dismiss();
			if (response != null) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					Log.e("response", jsonObject.toString());
					mListData = parseVideoListFromResponse(jsonObject);
					if (mListData.isEmpty()) {
						txt_no_data_found.setVisibility(View.VISIBLE);
					}
					Log.e("AAAGGGGGG", "" + mListData);
					initList(mListData);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public ArrayList<YoutubeDataModel> parseVideoListFromResponse(JSONObject jsonObject) {
		ArrayList<YoutubeDataModel> mList = new ArrayList<>();

		if (jsonObject.has("items")) {
			try {
				JSONArray jsonArray = jsonObject.getJSONArray("items");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject json = jsonArray.getJSONObject(i);
					if (json.has("id")) {
						JSONObject jsonID = json.getJSONObject("id");
						String video_id = "";
						if (jsonID.has("videoId")) {
							video_id = jsonID.getString("videoId");
						}
						if (jsonID.has("kind")) {
							if (jsonID.getString("kind").equals("youtube#video")) {
								YoutubeDataModel youtubeObject = new YoutubeDataModel();
								JSONObject jsonSnippet = json.getJSONObject("snippet");
								String title = jsonSnippet.getString("title");
								String description = jsonSnippet.getString("description");
								String publishedAt = jsonSnippet.getString("publishedAt");
								String thumbnail = jsonSnippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");

								youtubeObject.setTitle(title);
								youtubeObject.setDescription(description);
								youtubeObject.setPublishedAt(publishedAt);
								youtubeObject.setThumbnail(thumbnail);
								youtubeObject.setVideo_id(video_id);
								mList.add(youtubeObject);

							}
						}
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return mList;

	}
}
