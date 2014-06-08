package com.main.sportsapp;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import com.actionbarsherlock.app.SherlockFragment;
import com.fedorvlasov.lazylist.ImageLoader;
import com.model.sportsapp.Game;
import com.model.sportsapp.Tweet;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
		
		if(! game.getSport().equalsIgnoreCase("MLB")){
		imageLoader.DisplayImage(game.getAwayLogo().replace("50x33", "90x60"), awayLogo);
		imageLoader.DisplayImage(game.getHomeLogo().replace("50x33", "90x60"), homeLogo);
		}
		
		else{

			MLBLogoFinder mlbLogoFinder = new MLBLogoFinder();
			mlbLogoFinder.setLogo(homeLogo, game.getHomeTeam());
			mlbLogoFinder.setLogo(awayLogo, game.getAwayTeam());
			
		}
		status.setText(game.getStatus());

		new TwitterTask().execute();
		return gameView;
		
	}
	
	private class TwitterTask extends AsyncTask<Void, Void, ArrayList<Tweet>> {


		@Override
		protected ArrayList<Tweet> doInBackground(Void... params) {
			ArrayList<Tweet> myList = new ArrayList<Tweet>();
			ConfigurationBuilder cb = new ConfigurationBuilder();
		    cb.setDebugEnabled(true)
		          .setOAuthConsumerKey("6EIUm62HmNvRxFQZThW23U2CE")
		          .setOAuthConsumerSecret("coewXjaiYHmEK0hIBk3dewxSPcAdrFzWJifyL6dDfB7cVmKfmA")
		          .setOAuthAccessToken("393532597-7sDY8DOM6VQiSiLgX3brFtaJyzCdpwxJNWCLYnnh")
		          .setOAuthAccessTokenSecret("pTagsBX3dWZg7vqD9B2eYBcO6wscnSIDgro0Nnq88Kvu6");
		    TwitterFactory tf = new TwitterFactory(cb.build());
		    Twitter twitter = tf.getInstance();
		    String home = game.getHomeCity()+" " + game.getHomeTeam();
		    String away = game.getAwayCity()+" " + game.getAwayTeam();
		        try {
		            Query query = new Query("(" +home+")" + " OR " + "(" + away + ")");
		            QueryResult result;
		            result = twitter.search(query);
		            List<twitter4j.Status> tweets = result.getTweets();
		            
		            for (twitter4j.Status tweet : tweets) {
		            	myList.add(new Tweet(tweet.getUser().getScreenName(),tweet.getText(),tweet.getUser().getBiggerProfileImageURL()));
		            }
		            
		            
		            
		            return myList;

		        } catch (TwitterException te) {
		            te.printStackTrace();
		            System.out.println("Failed to search tweets: " + te.getMessage());
		        }
		
			
			
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<Tweet> result) {
			ListView list = (ListView) getActivity().findViewById(R.id.tweetList);
			TwitterAdapter adp = new TwitterAdapter(getActivity(),R.layout.twitteradapt,result);
			list.setAdapter(adp);
		}
		

 
		
	}
	public class TwitterAdapter extends ArrayAdapter{
		private Context context;
		ArrayList<Tweet> myList = new ArrayList<Tweet>();

		public TwitterAdapter(Context context, int resource, ArrayList<Tweet> result) {
			super(context, resource, result);
			myList = result;
			this.context = context;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ListView list;
			TextView user;
			TextView tweet;
			ImageView userImg;
			
			
			//reuse converView if you can to speed up scrolling on listview
			if(convertView == null){
				//inflate the listview
				LayoutInflater inflator = ((Activity)context).getLayoutInflater();
				convertView = inflator.inflate(R.layout.twitteradapt, parent, false);
			}
			user = (TextView) convertView.findViewById(R.id.userTweet);
			tweet = (TextView) convertView.findViewById(R.id.tweet);
			userImg = (ImageView) convertView.findViewById(R.id.userImg);
			
			user.setText("@" + myList.get(position).getUser());
			tweet.setText(myList.get(position).getTweet());
			imageLoader.DisplayImage(myList.get(position).getUserImgUrl(), userImg);
			
			
			return convertView;
		}
		public void setItemList(ArrayList<Tweet> itemList) {
			this.myList = itemList;
		}

	}


	
	
	

}
