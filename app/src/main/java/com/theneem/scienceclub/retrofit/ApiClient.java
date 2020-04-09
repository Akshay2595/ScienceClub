package com.theneem.scienceclub.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

	private static final String ROOT_URL = "http://scienceclub.in/api/";

	public static ApiInterface getApiService() {
		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.connectTimeout(1, TimeUnit.MINUTES)
				.readTimeout(30, TimeUnit.SECONDS)
				.writeTimeout(15, TimeUnit.SECONDS)
				.build();

		Gson gson = new GsonBuilder().setLenient().create();
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(ROOT_URL)
				.client(okHttpClient)
				.addConverterFactory(GsonConverterFactory.create(gson))
				.build();

		return retrofit.create(ApiInterface.class);
	}
}