package com.theneem.scienceclub.fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.theneem.scienceclub.R;
import com.theneem.scienceclub.activity.MainActivity;
import com.theneem.scienceclub.adapter.VideoPostAdapter;
import com.theneem.scienceclub.interfaces.OnItemClickListener;
import com.theneem.scienceclub.model.YoutubeDataModel;
import com.theneem.scienceclub.utils.CustomProgressBar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoLogsFragment extends Fragment {

	private ProgressDialog progressDialog;
	private static String GOOGLE_YOUTUBE_API_KEY = "AIzaSyAdDix7i7a3an-gyXiquTV_14cIsr8-DZg";//here you should use your api key for testing purpose you can use this api also
	private static String CHANNEL_ID = "UCoMdktPbSTixAyNGwb-UYkQ"; //here you should use your channel id for testing purpose you can use this api also
	private static String CHANNLE_GET_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&channelId=" + CHANNEL_ID + "&maxResults=50&key=" + GOOGLE_YOUTUBE_API_KEY + "&pageToken=CDIQAA";
	private static ArrayList<String> CHANNLE_GET_URL_1 = new ArrayList<>();
	private RecyclerView mList_videos = null;
	private VideoPostAdapter adapter = null;
	private ArrayList<YoutubeDataModel> mListData = new ArrayList<>();


	public VideoLogsFragment() {
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_video_logs, container, false);

		CHANNLE_GET_URL_1.add("https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&channelId=" + CHANNEL_ID + "&maxResults=50&key=" + GOOGLE_YOUTUBE_API_KEY + "&pageToken=CDIQAA");
		CHANNLE_GET_URL_1.add("https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&channelId=" + CHANNEL_ID + "&maxResults=50&key=" + GOOGLE_YOUTUBE_API_KEY + "&pageToken=CDIQAA");

		mList_videos = (RecyclerView) view.findViewById(R.id.mList_videos);
		progressDialog = CustomProgressBar.createProgressDialog(getContext());
		initList(mListData);
		new RequestYoutubeAPI().execute(CHANNLE_GET_URL_1);
		return view;
	}


	private void initList(ArrayList<YoutubeDataModel> mListData) {
		mList_videos.setLayoutManager(new LinearLayoutManager(getContext()));
		adapter = new VideoPostAdapter(getContext(), mListData, new OnItemClickListener() {
			@Override
			public void onItemClick(YoutubeDataModel item) {
				YoutubeDataModel youtubeDataModel = item;
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + youtubeDataModel.getVideo_id())));
			}
		});
		mList_videos.setAdapter(adapter);

	}

	@SuppressLint("StaticFieldLeak")
	class RequestYoutubeAPI extends AsyncTask<ArrayList<String>, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(ArrayList<String>... lists) {

			for (int i = 0; i < CHANNLE_GET_URL_1.size(); i++) {
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = null;
				try {
					httpGet = new HttpGet(new URI(String.valueOf(lists[i].get(i))));
					HttpResponse response = httpClient.execute(httpGet);
					HttpEntity httpEntity = response.getEntity();
					String json = EntityUtils.toString(httpEntity);
					return json;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
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
					initList(mListData);
				} catch (Exception e) {

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


	@Override
	public void onResume() {
		super.onResume();
		((MainActivity) Objects.requireNonNull(getActivity()))
				.setActionBarTitle("Video Logs");
	}
}
