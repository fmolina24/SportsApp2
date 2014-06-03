package com.example.sportsapp;

import android.widget.ImageView;

import com.webileapps.navdrawer.R;

public class MLBLogoFinder {
	
	public MLBLogoFinder(){}
	
	public void setLogo(ImageView teamLogo, String teamName){
		
		if(teamName.equalsIgnoreCase("angels"))
			teamLogo.setImageResource(R.drawable.angels);
		else if (teamName.equalsIgnoreCase("angels"))
			teamLogo.setImageResource(R.drawable.astros);
		else if (teamName.equalsIgnoreCase("athletics"))
			teamLogo.setImageResource(R.drawable.athletics);
		else if (teamName.equalsIgnoreCase("blue jays"))
			teamLogo.setImageResource(R.drawable.blue_jays);
		else if (teamName.equalsIgnoreCase("braves"))
			teamLogo.setImageResource(R.drawable.braves);
		else if (teamName.equalsIgnoreCase("brewers"))
			teamLogo.setImageResource(R.drawable.brewers);
		else if (teamName.equalsIgnoreCase("cardinals"))
			teamLogo.setImageResource(R.drawable.cardinals);
		else if (teamName.equalsIgnoreCase("cubs"))
			teamLogo.setImageResource(R.drawable.cubs);
		else if (teamName.equalsIgnoreCase("diamondbacks"))
			teamLogo.setImageResource(R.drawable.diamondbacks);
		else if (teamName.equalsIgnoreCase("dodgers"))
			teamLogo.setImageResource(R.drawable.dodgers);
		else if (teamName.equalsIgnoreCase("giants"))
			teamLogo.setImageResource(R.drawable.giants);
		else if (teamName.equalsIgnoreCase("indians"))
			teamLogo.setImageResource(R.drawable.indians);
		else if (teamName.equalsIgnoreCase("mariners"))
			teamLogo.setImageResource(R.drawable.mariners);
		else if (teamName.equalsIgnoreCase("marlins"))
			teamLogo.setImageResource(R.drawable.marlins);
		else if (teamName.equalsIgnoreCase("mets"))
			teamLogo.setImageResource(R.drawable.mets);
		else if (teamName.equalsIgnoreCase("nationals"))
			teamLogo.setImageResource(R.drawable.nationals);
		else if (teamName.equalsIgnoreCase("orioles"))
			teamLogo.setImageResource(R.drawable.orioles);
		else if (teamName.equalsIgnoreCase("padres"))
			teamLogo.setImageResource(R.drawable.padres);
		else if (teamName.equalsIgnoreCase("phillies"))
			teamLogo.setImageResource(R.drawable.phillies);
		else if (teamName.equalsIgnoreCase("pirates"))
			teamLogo.setImageResource(R.drawable.pirates);
		else if (teamName.equalsIgnoreCase("rangers"))
			teamLogo.setImageResource(R.drawable.rangers);
		else if (teamName.equalsIgnoreCase("rays"))
			teamLogo.setImageResource(R.drawable.rays);
		else if (teamName.equalsIgnoreCase("red sox"))
			teamLogo.setImageResource(R.drawable.red_sox);
		else if (teamName.equalsIgnoreCase("reds"))
			teamLogo.setImageResource(R.drawable.reds);
		else if (teamName.equalsIgnoreCase("rockies"))
			teamLogo.setImageResource(R.drawable.rockies);
		else if (teamName.equalsIgnoreCase("royals"))
			teamLogo.setImageResource(R.drawable.royals);
		else if (teamName.equalsIgnoreCase("tigers"))
			teamLogo.setImageResource(R.drawable.tigers);
		else if (teamName.equalsIgnoreCase("twins"))
			teamLogo.setImageResource(R.drawable.twins);
		else if (teamName.equalsIgnoreCase("white sox"))
			teamLogo.setImageResource(R.drawable.white_sox);
		else if (teamName.equalsIgnoreCase("yankees"))
			teamLogo.setImageResource(R.drawable.yankees);

	}

}
