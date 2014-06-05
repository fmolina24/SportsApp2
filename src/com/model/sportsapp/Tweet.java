package com.model.sportsapp;

public class Tweet {
	private String user;
	private String tweet;
	private String userImgUrl;

	
	public Tweet(String user,String tweet,String userImgUrl){
		this.user = user;
		this.tweet = tweet;
		this.userImgUrl = userImgUrl;

	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getTweet() {
		return tweet;
	}

	public void setTweet(String tweet) {
		this.tweet = tweet;
	}

	public String getUserImgUrl() {
		return userImgUrl;
	}

	public void setUserImgUrl(String userImgUrl) {
		this.userImgUrl = userImgUrl;
	}

}

