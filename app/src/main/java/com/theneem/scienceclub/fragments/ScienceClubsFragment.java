package com.theneem.scienceclub.fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.theneem.scienceclub.R;
import com.theneem.scienceclub.activity.ClubDetailActivity;
import com.theneem.scienceclub.activity.MainActivity;
import com.theneem.scienceclub.adapter.ClubListAdapter;
import com.theneem.scienceclub.interfaces.ClubItemClickListner;
import com.theneem.scienceclub.response.ClubListResponse;
import com.theneem.scienceclub.retrofit.ApiClient;
import com.theneem.scienceclub.retrofit.ApiInterface;
import com.theneem.scienceclub.utils.CustomProgressBar;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScienceClubsFragment extends Fragment implements ClubItemClickListner {

	private ArrayList<ClubListResponse.ClubList> clubList = new ArrayList<>();
	private RecyclerView rv_clubList;
	private ClubListAdapter clubListAdapter;
	private ProgressDialog progressDialog;

	public ScienceClubsFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_science_clubs, container, false);


		rv_clubList = view.findViewById(R.id.rv_clubList);

		retrofit();

		return view;
	}

	private void retrofit() {
		progressDialog = CustomProgressBar.createProgressDialog(getContext());
		final ApiInterface api = ApiClient.getApiService();
		Call<ClubListResponse> call = api.getClubList();

		call.enqueue(new Callback<ClubListResponse>() {
			@SuppressLint("SetTextI18n")
			@Override
			public void onResponse(@NonNull Call<ClubListResponse> call, @NonNull Response<ClubListResponse> response) {
				if (response.code() == 200) {
					if (response.body() != null) {
						progressDialog.dismiss();

						if (response.body().isSuccess()) {
							clubList = response.body().getClublist();
							clubListAdapter = new ClubListAdapter(clubList, ScienceClubsFragment.this);
							rv_clubList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
							rv_clubList.setItemAnimator(new DefaultItemAnimator());
							rv_clubList.setAdapter(clubListAdapter);
						} else {
							Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
						}


					} else {
						Toast.makeText(getContext(), "response body() is null", Toast.LENGTH_SHORT).show();
					}

				} else if (response.code() == 401) {
					progressDialog.dismiss();
					// Handle unauthorized
				} else if (response.code() == 404) {
					progressDialog.dismiss();
					Toast.makeText(getContext(), "Not Found (From Serverside Error)", Toast.LENGTH_SHORT).show();
				} else if (response.code() == 500) {
					progressDialog.dismiss();
					Toast.makeText(getContext(), "Server Broken", Toast.LENGTH_SHORT).show();
				} else {
					progressDialog.dismiss();
					Toast.makeText(getContext(), "Unknown Error", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<ClubListResponse> call, Throwable t) {
				progressDialog.dismiss();
				Toast.makeText(getContext(), "onFailer:  " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void click(ClubListResponse.ClubList club) {

		Intent intent = new Intent(getContext(), ClubDetailActivity.class);
		intent.putExtra("club_id", club.getClub_id());
		intent.putExtra("club_name", club.getClub_name());
		intent.putExtra("club_image", club.getClub_image());
		intent.putExtra("club_address", club.getClub_address());
		intent.putExtra("club_desc", club.getClub_desc());
		intent.putExtra("club_member_number", club.getClub_member_number());
		intent.putExtra("club_sponser_name", club.getClub_sponser_name());
		intent.putExtra("club_contact_no", club.getClub_contact_no());
		intent.putExtra("club_email_address", club.getClub_email_address());
		intent.putExtra("channel_id", club.getChannel_id());
		startActivity(intent);

	}


	@Override
	public void onResume() {
		super.onResume();
		((MainActivity) Objects.requireNonNull(getActivity()))
				.setActionBarTitle("Science Club");
	}
}
