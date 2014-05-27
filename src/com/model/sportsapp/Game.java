package com.model.sportsapp;

import java.io.Serializable;
import java.util.Date;


public class Game implements Serializable {
	private int gameid;
	private String homeTeam;
	private String awayTeam;
	private Integer homeScore;
	private Integer awayScore;
	private String status;
	private Date time;
	private String homeLogo;
	private String awayLogo;
	private String tv;
	private String timeElapsed;
	private String state;
	
	
	public Game(int gameid,String homeTeam,String awayTeam,Integer homeScore,
			Integer awayScore,String status,Date time,String homeLogo,
			String awayLogo,String tv,String timeElapsed,String state){
		this.gameid = gameid;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.homeScore = homeScore;
		this.awayScore = awayScore;
		this.status = status;
		this.time = time;
		this.homeLogo = homeLogo;
		this.awayLogo = awayLogo;
		this.tv = tv;
		this.timeElapsed = timeElapsed;
		this.state = state;
	}
	

	public String getTimeElapsed() {
		return timeElapsed;
	}


	public void setTimeElapsed(String timeElapsed) {
		this.timeElapsed = timeElapsed;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public int getGameid() {
		return gameid;
	}


	public void setGameid(int gameid) {
		this.gameid = gameid;
	}


	public String getHomeTeam() {
		return homeTeam;
	}


	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}


	public String getAwayTeam() {
		return awayTeam;
	}


	public void setAwayTeam(String awayTeam) {
		this.awayTeam = awayTeam;
	}


	public Integer getHomeScore() {
		return homeScore;
	}


	public void setHomeScore(Integer homeScore) {
		this.homeScore = homeScore;
	}


	public Integer getAwayScore() {
		return awayScore;
	}


	public void setAwayScore(Integer awayScore) {
		this.awayScore = awayScore;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Date getTime() {
		return time;
	}


	public void setTime(Date time) {
		this.time = time;
	}


	public String getHomeLogo() {
		return homeLogo;
	}


	public void setHomeLogo(String homeLogo) {
		this.homeLogo = homeLogo;
	}


	public String getAwayLogo() {
		return awayLogo;
	}


	public void setAwayLogo(String awayLogo) {
		this.awayLogo = awayLogo;
	}


	public String getTv() {
		return tv;
	}


	public void setTv(String tv) {
		this.tv = tv;
	}
	
	
	

}
