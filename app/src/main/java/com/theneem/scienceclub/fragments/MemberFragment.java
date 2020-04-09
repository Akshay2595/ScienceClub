package com.theneem.scienceclub.fragments;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.theneem.scienceclub.R;
import com.theneem.scienceclub.activity.MainActivity;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemberFragment extends Fragment {


	public MemberFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_member, container, false);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		((MainActivity) Objects.requireNonNull(getActivity()))
				.setActionBarTitle("Members");
	}
}
