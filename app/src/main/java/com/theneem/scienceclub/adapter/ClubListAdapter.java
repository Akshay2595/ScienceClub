package com.theneem.scienceclub.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;
import com.theneem.scienceclub.R;
import com.theneem.scienceclub.interfaces.ClubItemClickListner;
import com.theneem.scienceclub.response.ClubListResponse;

import java.util.List;


public class ClubListAdapter extends RecyclerView.Adapter<ClubListAdapter.MyViewHolder> {

	private List<ClubListResponse.ClubList> clubList;
	private ClubItemClickListner clubItemClickListner;

	public ClubListAdapter(List<ClubListResponse.ClubList> clubList, ClubItemClickListner clubItemClickListner) {
		this.clubList = clubList;
		this.clubItemClickListner = clubItemClickListner;

	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.row_club_list_adapter, parent, false);

		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, final int position) {
		ClubListResponse.ClubList clubModel = clubList.get(position);

		holder.txt_club_title.setText(":  " + clubModel.getClub_name());
		holder.txt_club_address.setText(":  " + clubModel.getClub_address());
		Picasso.get().load(clubModel.getClub_image()).into(holder.img_toy);

		holder.txt_club_member.setText("+" + clubModel.getClub_member_number());

		holder.club_card.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				clubItemClickListner.click(clubModel);
			}
		});

	}

	@Override
	public int getItemCount() {
		return clubList.size();
	}

	class MyViewHolder extends RecyclerView.ViewHolder {
		TextView txt_club_title, txt_club_address, txt_club_member;
		ImageView img_toy;
		MaterialCardView club_card;

		public MyViewHolder(View view) {
			super(view);
			img_toy = (ImageView) view.findViewById(R.id.img);
			txt_club_title = (TextView) view.findViewById(R.id.txt_club_title);
			club_card = view.findViewById(R.id.club_card);
			txt_club_address = view.findViewById(R.id.txt_club_address);
			txt_club_member = view.findViewById(R.id.txt_club_member);
		}
	}
}

