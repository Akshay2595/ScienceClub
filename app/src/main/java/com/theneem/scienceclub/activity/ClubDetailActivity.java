package com.theneem.scienceclub.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.theneem.scienceclub.R;
import com.theneem.scienceclub.fragments.VideoLogsFragment;
import com.theneem.scienceclub.model.checkTheSubscription;
import com.theneem.scienceclub.response.JoiningClubResponse;
import com.theneem.scienceclub.response.RegisterResponse;
import com.theneem.scienceclub.retrofit.ApiClient;
import com.theneem.scienceclub.retrofit.ApiInterface;
import com.theneem.scienceclub.utils.CustomProgressBar;
import com.theneem.scienceclub.utils.PreferenceManager;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClubDetailActivity extends AppCompatActivity {

    MaterialButton btn_join;
    ImageView club_image_detail;
    TextView txt_club_title_detail, txt_club_address_detail,
            txt_num_member_detail, txt_club_sponserName, txt_club_email, txt_club_phoneNo, txt_desc_detail, txt_club_video;

    String club_id, club_name, club_image, club_address, club_desc, club_member_number, club_sponser_name, club_contact_no, club_email_address,
            user_id , subscribed_club;

    private static String GOOGLE_YOUTUBE_API_KEY = "AIzaSyAdDix7i7a3an-gyXiquTV_14cIsr8-DZg";//here you should use your api key for testing purpose you can use this api also
    private String CHANNEL_ID = ""; //here you should use your channel id for testing purpose you can use this api also
    private String CHANNLE_GET_URL = "";
    PreferenceManager preferenceManager;
    private ProgressDialog progressDialog;
    boolean btnFlag = false;
    boolean isJoinFlag = false;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        club_id = Objects.requireNonNull(getIntent().getExtras()).getString("club_id", "");
        club_name = Objects.requireNonNull(getIntent().getExtras()).getString("club_name", "");
        club_image = Objects.requireNonNull(getIntent().getExtras()).getString("club_image", "");
        club_address = Objects.requireNonNull(getIntent().getExtras()).getString("club_address", "");
        club_desc = Objects.requireNonNull(getIntent().getExtras()).getString("club_desc", "");
        club_member_number = Objects.requireNonNull(getIntent().getExtras()).getString("club_member_number", "");
        club_sponser_name = Objects.requireNonNull(getIntent().getExtras()).getString("club_sponser_name", "");
        club_contact_no = Objects.requireNonNull(getIntent().getExtras()).getString("club_contact_no", "");
        club_email_address = Objects.requireNonNull(getIntent().getExtras()).getString("club_email_address", "");
        CHANNEL_ID = Objects.requireNonNull(getIntent().getExtras()).getString("channel_id", "");


        preferenceManager = new PreferenceManager(ClubDetailActivity.this);
        user_id = preferenceManager.getUserId();
        checkSubscription(user_id, club_id);
        //Toast.makeText(getApplicationContext(),""+user_id,Toast.LENGTH_LONG).show();


        Objects.requireNonNull(getSupportActionBar()).setTitle(club_name);
        toolbar.setTitleTextColor(Color.WHITE);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        club_image_detail = findViewById(R.id.club_image_detail);
        txt_club_title_detail = findViewById(R.id.txt_club_title_detail);
        txt_club_address_detail = findViewById(R.id.txt_club_address_detail);
        txt_num_member_detail = findViewById(R.id.txt_num_member_detail);
        txt_club_sponserName = findViewById(R.id.txt_club_sponserName);
        txt_club_email = findViewById(R.id.txt_club_email);
        txt_club_phoneNo = findViewById(R.id.txt_club_phoneNo);
        txt_desc_detail = findViewById(R.id.txt_desc_detail);
        txt_club_video = findViewById(R.id.txt_club_video);
        btn_join = findViewById(R.id.btn_join);
        builder = new AlertDialog.Builder(this);

        Picasso.get().load(club_image).into(club_image_detail);
        txt_club_title_detail.setText(":  " + club_name);
        txt_club_address_detail.setText(":  " + club_address);
        //txt_num_member_detail.setText("+" + club_member_number);
        txt_club_sponserName.setText(club_sponser_name);
        txt_club_email.setText(club_email_address);
        txt_club_phoneNo.setText(club_contact_no);
        txt_desc_detail.setText(club_desc);

        joinBtnStatus();


        txt_club_video.setOnClickListener(v -> {
            Intent intent = new Intent(ClubDetailActivity.this, ClubVideoChannelList.class);
            intent.putExtra("club_name", club_name);
            intent.putExtra("channel_id", CHANNEL_ID);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        btn_join.setOnClickListener(v -> {

            if (preferenceManager.getLoginSession()) {
                if (!btnFlag) {
                    if(subscribed_club == null || subscribed_club == ""){
                        joinClub(user_id, club_id);
                    } else {
                        builder.setMessage("You are already a member of other club, do you want to replace it with this club?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        joinClub(user_id, club_id);
                                    }
                                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alertDialog = builder.create();

                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
                            }
                        });
                        alertDialog.show();
                    }


                } else {
                    // btn_join.setEnabled(false);
                    Toast.makeText(ClubDetailActivity.this, "You cannot leave the club without joining another", Toast.LENGTH_LONG).show();
                    // joinClub(user_id,"0");
                }

            } else {
                goLogin();
            }

        });

    }

    public void goLogin() {
        Intent intent = new Intent(ClubDetailActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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

    private void joinClub(String id, String clubId) {
        progressDialog = CustomProgressBar.createProgressDialog(ClubDetailActivity.this);
        final ApiInterface api = ApiClient.getApiService();
        Call<JoiningClubResponse> call = api.getJoinClub(id, clubId);

        call.enqueue(new Callback<JoiningClubResponse>() {
            @Override
            public void onResponse(@NonNull Call<JoiningClubResponse> call, @NonNull Response<JoiningClubResponse> response) {

                if (response.code() == 200) {
                    if (response.body() != null) {
                        progressDialog.dismiss();

                        if (response.body().getSuccess()) {


                            //if(response.body().getData().getClubId().equals(clubId)) {
                            btn_join.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                            btn_join.setTextColor(getResources().getColor(R.color.colorAccent));
                            btn_join.setText("Joined");
                            btnFlag = true;
                            Toast.makeText(ClubDetailActivity.this, "You are Proud member of this club", Toast.LENGTH_LONG).show();
							/*}else{
								btn_join.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
								btn_join.setTextColor(getResources().getColor(R.color.black));
								btn_join.setText("Join");
								btnFlag = false;
								Toast.makeText(ClubDetailActivity.this, "You are not member of this club anymore", Toast.LENGTH_LONG).show();
							}*/
                        } else {
                            Toast.makeText(ClubDetailActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ClubDetailActivity.this, "response body() is null", Toast.LENGTH_SHORT).show();
                    }

                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    // Handle unauthorized
                } else if (response.code() == 404) {
                    progressDialog.dismiss();
                    Toast.makeText(ClubDetailActivity.this, "Not Found (From Serverside Error)", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 500) {
                    progressDialog.dismiss();
                    Toast.makeText(ClubDetailActivity.this, "Server Broken", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ClubDetailActivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<JoiningClubResponse> call, Throwable t) {
                Log.e("msg", "Failed");
            }
        });
    }

    private void checkSubscription(String id, String clubid) {
        // progressDialog = CustomProgressBar.createProgressDialog(ClubDetailActivity.this);
        final ApiInterface api = ApiClient.getApiService();
        Call<checkTheSubscription> call = api.getClubSubscription(id, clubid);

        call.enqueue(new Callback<checkTheSubscription>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<checkTheSubscription> call, @NonNull Response<checkTheSubscription> response) {

                if (response.code() == 200) {
                    if (response.body() != null) {
                        // progressDialog.dismiss();

                        if (preferenceManager.getLoginSession()) {
                            if (response.body().getData().getClubId() != null && response.body().getData().getClubId() != "") {
                                if (response.body().getData().getClubId().equals(clubid)) {
                                    subscribed_club = clubid;
                                    // Toast.makeText(ClubDetailActivity.this, "btnFlag = true", Toast.LENGTH_LONG).show();
                                    btnFlag = true;
                                    joinBtnStatus();

                                } else {
                                    subscribed_club = response.body().getData().getClubId();
                                    //Toast.makeText(ClubDetailActivity.this, "btnFlag = false", Toast.LENGTH_LONG).show();
                                    btnFlag = false;
                                    joinBtnStatus();
                                }
                            }
                        }

                    } else {
                        Toast.makeText(ClubDetailActivity.this, "response body() is null", Toast.LENGTH_SHORT).show();
                    }

                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    // Handle unauthorized
                } else if (response.code() == 404) {
                    progressDialog.dismiss();
                    Toast.makeText(ClubDetailActivity.this, "Not Found (From Serverside Error)", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 500) {
                    progressDialog.dismiss();
                    Toast.makeText(ClubDetailActivity.this, "Server Broken", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ClubDetailActivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<checkTheSubscription> call, Throwable t) {
                Log.e("msg", "Failed");
            }
        });
    }

    private void joinBtnStatus() {

        if (btnFlag == false) {
            btn_join.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
            btn_join.setTextColor(getResources().getColor(R.color.black));
            btn_join.setText("Join");


        } else {
            btn_join.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            btn_join.setTextColor(getResources().getColor(R.color.colorAccent));
            btn_join.setText("Joined");

        }
    }

}
