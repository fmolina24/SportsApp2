package com.example.sportsapp;

import com.actionbarsherlock.app.SherlockFragment;
import com.fedorvlasov.lazylist.ImageLoader;
import com.model.sportsapp.Game;
import com.webileapps.navdrawer.R;

import android.app.Fragment;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class GameFragment extends SherlockFragment{
	Game game;
	ImageLoader imageLoader=new ImageLoader(getActivity());

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		game = (Game) getArguments().getSerializable("game");
		
		
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View gameView = inflater.inflate(R.layout.game_fragment, container,false);
		TextView home = (TextView)gameView.findViewById(R.id.homeTeam_single);
		TextView away = (TextView)gameView.findViewById(R.id.awayTeam_single);
		TextView state = (TextView)gameView.findViewById(R.id.state_single);
		TextView time = (TextView)gameView.findViewById(R.id.time_single);
		
		TextView homeScore = (TextView)gameView.findViewById(R.id.homeScore_single);
		TextView awayScore = (TextView)gameView.findViewById(R.id.awayScore_single);
		
		ImageView homeLogo = (ImageView)gameView.findViewById(R.id.homeLogo_single);
		ImageView awayLogo = (ImageView)gameView.findViewById(R.id.awayLogo_single);
		
		
		TextView tv = (TextView)gameView.findViewById(R.id.tv_single);
		
		TextView status = (TextView)gameView.findViewById(R.id.status_single);
		
		home.setText(game.getHomeTeam());
		away.setText(game.getAwayTeam());
		
		if(!game.getStatus().equalsIgnoreCase("pre-game")){
			homeScore.setText(game.getHomeScore().toString());
			awayScore.setText(game.getAwayScore().toString());
		}
		
		tv.setText(game.getTv());
		state.setText(game.getState());
		time.setText(game.getTimeElapsed());
		
		imageLoader.DisplayImage(game.getAwayLogo().replace("50x33", "90x60"), awayLogo);
		imageLoader.DisplayImage(game.getHomeLogo().replace("50x33", "90x60"), homeLogo);
		status.setText(game.getStatus());
		
		Log.i("info", "done with gamefrag");
		return gameView;
		
	}


	
	
	

}
