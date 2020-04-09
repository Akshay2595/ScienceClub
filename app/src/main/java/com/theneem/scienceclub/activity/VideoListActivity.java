package com.theneem.scienceclub.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.theneem.scienceclub.R;
import com.theneem.scienceclub.adapter.VideoPostAdapter;
import com.theneem.scienceclub.interfaces.OnItemClickListener;
import com.theneem.scienceclub.model.Club;
import com.theneem.scienceclub.model.YoutubeDataModel;
import com.squareup.picasso.Picasso;

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

public class VideoListActivity extends AppCompatActivity {

	ProgressBar loading_indicator;
	private static String GOOGLE_YOUTUBE_API_KEY = "AIzaSyAdDix7i7a3an-gyXiquTV_14cIsr8-DZg";//here you should use your api key for testing purpose you can use this api also
	private static String CHANNEL_ID = "UCoMdktPbSTixAyNGwb-UYkQ"; //here you should use your channel id for testing purpose you can use this api also
	private static String CHANNLE_GET_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&channelId=" + CHANNEL_ID + "&maxResults=50&key=" + GOOGLE_YOUTUBE_API_KEY + "&pageToken=CDIQAA";

	private RecyclerView mList_videos = null;
	private VideoPostAdapter adapter = null;
	private ArrayList<YoutubeDataModel> mListData = new ArrayList<>();
	TextView txt_clubName;
	ImageView img_clubDetailIcon;
	Dialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_video_list);
		mList_videos = (RecyclerView) findViewById(R.id.mList_videos);
		txt_clubName = findViewById(R.id.txt_clubName);
		loading_indicator = findViewById(R.id.loading_indicator);
		img_clubDetailIcon = findViewById(R.id.img_clubDetailIcon);

		Intent i = getIntent();
		Club club = (Club) i.getSerializableExtra("club");

		img_clubDetailIcon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showDialog(club);
			}
		});

		txt_clubName.setText(club.getClub_name());
		loading_indicator.setVisibility(View.VISIBLE);
		initList(mListData);
		new RequestYoutubeAPI().execute();

	}

	private void initList(ArrayList<YoutubeDataModel> mListData) {
		mList_videos.setLayoutManager(new LinearLayoutManager(VideoListActivity.this));
		adapter = new VideoPostAdapter(VideoListActivity.this, mListData, new OnItemClickListener() {
			@Override
			public void onItemClick(YoutubeDataModel item) {
				YoutubeDataModel youtubeDataModel = item;
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + youtubeDataModel.getVideo_id())));
			}
		});
		mList_videos.setAdapter(adapter);

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
			loading_indicator.setVisibility(View.GONE);
			if (response != null) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					Log.e("response", jsonObject.toString());
					mListData = parseVideoListFromResponse(jsonObject);
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


	public void showDialog(Club club) {

		dialog = new Dialog(VideoListActivity.this);
		dialog.setContentView(R.layout.dialog_detail_club);

		TextView txt_clubName = dialog.findViewById(R.id.txt_clubName);
		TextView txt_desc = dialog.findViewById(R.id.txt_desc);
		TextView txt_address = dialog.findViewById(R.id.txt_address);
		ImageView dialog_image = dialog.findViewById(R.id.dialog_image);

		txt_address.setText(club.getClub_address());
		txt_clubName.setText(club.getClub_name());
		txt_desc.setText(club.getClub_desc());
		Picasso.get().load(club.getClub_image()).into(dialog_image);


		Window window = dialog.getWindow();
		assert window != null;
		window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
		Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();

	}
}
