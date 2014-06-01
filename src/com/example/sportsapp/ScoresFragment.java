/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.sportsapp;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.XML;

import android.app.Activity;
import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.fedorvlasov.lazylist.ImageLoader;
import com.model.sportsapp.Game;
import com.webileapps.navdrawer.R;

public class ScoresFragment extends SherlockListFragment{

	private static final String ARG_POSITION = "position";
	ImageLoader imageLoader=new ImageLoader(getActivity());
	
	private List<Game> myList;
	String sport="";

	private int position;
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		((MainActivity)getActivity()).startGameFragment(myList.get(position));
		
	}

	public static ScoresFragment newInstance(int position) {
		ScoresFragment f = new ScoresFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		position = getArguments().getInt(ARG_POSITION);
		switch(position){
		case 0:
			sport="NBA";
			break;
		case 1:
			sport="NHL";
			break;
		case 2:
			sport="MLB";
			break;
		case 3:
			sport="NFL";
			break;
		}
		new HttpGetTask().execute();
	}
	private class HttpGetTask extends AsyncTask<Void, Void, List<Game>> {

		private static final String TAG = "HttpGetTask";
		
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String todayDate = df.format(today);
		
		
		private String URL = "http://scores.nbcsports.msnbc.com/ticker/data/gamesMSNBC.js.asp?jsonp=true&sport="+sport+"&period="+todayDate;
		//private String URL = "http://scores.nbcsports.msnbc.com/ticker/data/gamesMSNBC.js.asp?jsonp=true&sport="+sport+"&period=20140514";
		
		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

		@Override
		protected List<Game> doInBackground(Void... params) {
			Log.i("date",todayDate);
			HttpGet request = new HttpGet(URL);
			JSONResponseHandler responseHandler = new JSONResponseHandler();
			try {
				return mClient.execute(request, responseHandler);
			} catch (ClientProtocolException e) {
				Log.i("error", "client");
				e.printStackTrace();
			} catch (IOException e) {
				Log.i("error", "IO");
				e.printStackTrace();
			}
			return null;
		}
		

 
		@Override
		protected void onPostExecute(List<Game> result) {
			if (null != mClient)
				mClient.close();
			setListAdapter(new MyAdapter(getActivity(),R.layout.row,result));
		}
	}
	class MyAdapter extends ArrayAdapter{
		
		private Context context;

		public MyAdapter(Context context, int resource, List<Game> result) {
			super(context, resource, result);
			myList = result;
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView home;
			TextView away;
			TextView homeScore;
			TextView awayScore;
			ImageView awayLogo;
			ImageView homeLogo;
			
			
			//reuse converView if you can to speed up scrolling on listview
			if(convertView == null){
				//inflate the listview
				LayoutInflater inflator = ((Activity)context).getLayoutInflater();
				convertView = inflator.inflate(R.layout.row, parent, false);
				convertView.setTag(R.id.homeLabel, convertView.findViewById(R.id.homeLabel));
				convertView.setTag(R.id.awayLabel, convertView.findViewById(R.id.awayLabel));
				convertView.setTag(R.id.homeScore, convertView.findViewById(R.id.homeScore));
				convertView.setTag(R.id.awayScore, convertView.findViewById(R.id.awayScore));
				convertView.setTag(R.id.awayLogo, convertView.findViewById(R.id.awayLogo));
				convertView.setTag(R.id.homeLogo, convertView.findViewById(R.id.homeLogo));
				
				
			}
			
			home = (TextView) convertView.getTag(R.id.homeLabel);
			away = (TextView) convertView.getTag(R.id.awayLabel);
			homeScore = (TextView) convertView.getTag(R.id.homeScore);
			awayScore = (TextView) convertView.getTag(R.id.awayScore);
			homeLogo = (ImageView) convertView.getTag(R.id.homeLogo);
			awayLogo = (ImageView) convertView.getTag(R.id.awayLogo);
			
			if((myList.get(position).getAwayLogo()!=null) && (myList.get(position).getHomeLogo()!=null)){
				imageLoader.DisplayImage(myList.get(position).getAwayLogo(), awayLogo);
				imageLoader.DisplayImage(myList.get(position).getHomeLogo(), homeLogo);
			}
			
			if(sport.equalsIgnoreCase("MLB")){
				String homeTeam = myList.get(position).getHomeTeam();
				String awayTeam = myList.get(position).getAwayTeam();
				
				if(homeTeam.equalsIgnoreCase("angels"))
					homeLogo.setImageResource(R.drawable.angels);
				else if (homeTeam.equalsIgnoreCase("angels"))
					homeLogo.setImageResource(R.drawable.astros);
				else if (homeTeam.equalsIgnoreCase("athletics"))
					homeLogo.setImageResource(R.drawable.athletics);
				else if (homeTeam.equalsIgnoreCase("blue jays"))
					homeLogo.setImageResource(R.drawable.blue_jays);
				else if (homeTeam.equalsIgnoreCase("braves"))
					homeLogo.setImageResource(R.drawable.braves);
				else if (homeTeam.equalsIgnoreCase("brewers"))
					homeLogo.setImageResource(R.drawable.brewers);
				else if (homeTeam.equalsIgnoreCase("cardinals"))
					homeLogo.setImageResource(R.drawable.cardinals);
				else if (homeTeam.equalsIgnoreCase("cubs"))
					homeLogo.setImageResource(R.drawable.cubs);
				else if (homeTeam.equalsIgnoreCase("diamondbacks"))
					homeLogo.setImageResource(R.drawable.diamondbacks);
				else if (homeTeam.equalsIgnoreCase("dodgers"))
					homeLogo.setImageResource(R.drawable.dodgers);
				else if (homeTeam.equalsIgnoreCase("giants"))
					homeLogo.setImageResource(R.drawable.giants);
				else if (homeTeam.equalsIgnoreCase("indians"))
					homeLogo.setImageResource(R.drawable.indians);
				else if (homeTeam.equalsIgnoreCase("mariners"))
					homeLogo.setImageResource(R.drawable.marlins);
				else if (homeTeam.equalsIgnoreCase("mets"))
					homeLogo.setImageResource(R.drawable.mets);
				else if (homeTeam.equalsIgnoreCase("nationals"))
					homeLogo.setImageResource(R.drawable.nationals);
				else if (homeTeam.equalsIgnoreCase("orioles"))
					homeLogo.setImageResource(R.drawable.orioles);
				else if (homeTeam.equalsIgnoreCase("padres"))
					homeLogo.setImageResource(R.drawable.padres);
				else if (homeTeam.equalsIgnoreCase("phillies"))
					homeLogo.setImageResource(R.drawable.phillies);
				else if (homeTeam.equalsIgnoreCase("pirates"))
					homeLogo.setImageResource(R.drawable.pirates);
				else if (homeTeam.equalsIgnoreCase("rangers"))
					homeLogo.setImageResource(R.drawable.rangers);
				else if (homeTeam.equalsIgnoreCase("rays"))
					homeLogo.setImageResource(R.drawable.rays);
				else if (homeTeam.equalsIgnoreCase("red sox"))
					homeLogo.setImageResource(R.drawable.red_sox);
				else if (homeTeam.equalsIgnoreCase("reds"))
					homeLogo.setImageResource(R.drawable.reds);
				else if (homeTeam.equalsIgnoreCase("rockies"))
					homeLogo.setImageResource(R.drawable.rockies);
				else if (homeTeam.equalsIgnoreCase("royals"))
					homeLogo.setImageResource(R.drawable.royals);
				else if (homeTeam.equalsIgnoreCase("tigers"))
					homeLogo.setImageResource(R.drawable.tigers);
				else if (homeTeam.equalsIgnoreCase("twins"))
					homeLogo.setImageResource(R.drawable.twins);
				else if (homeTeam.equalsIgnoreCase("white sox"))
					homeLogo.setImageResource(R.drawable.white_sox);
				else if (homeTeam.equalsIgnoreCase("yankees"))
					homeLogo.setImageResource(R.drawable.yankees);
				
				
				if(awayTeam.equalsIgnoreCase("angels"))
					awayLogo.setImageResource(R.drawable.angels);
				else if (awayTeam.equalsIgnoreCase("angels"))
					awayLogo.setImageResource(R.drawable.astros);
				else if (awayTeam.equalsIgnoreCase("athletics"))
					awayLogo.setImageResource(R.drawable.athletics);
				else if (awayTeam.equalsIgnoreCase("blue jays"))
					awayLogo.setImageResource(R.drawable.blue_jays);
				else if (awayTeam.equalsIgnoreCase("braves"))
					awayLogo.setImageResource(R.drawable.braves);
				else if (awayTeam.equalsIgnoreCase("brewers"))
					awayLogo.setImageResource(R.drawable.brewers);
				else if (awayTeam.equalsIgnoreCase("cardinals"))
					awayLogo.setImageResource(R.drawable.cardinals);
				else if (awayTeam.equalsIgnoreCase("cubs"))
					awayLogo.setImageResource(R.drawable.cubs);
				else if (awayTeam.equalsIgnoreCase("diamondbacks"))
					awayLogo.setImageResource(R.drawable.diamondbacks);
				else if (awayTeam.equalsIgnoreCase("dodgers"))
					awayLogo.setImageResource(R.drawable.dodgers);
				else if (awayTeam.equalsIgnoreCase("giants"))
					awayLogo.setImageResource(R.drawable.giants);
				else if (awayTeam.equalsIgnoreCase("indians"))
					awayLogo.setImageResource(R.drawable.indians);
				else if (awayTeam.equalsIgnoreCase("mariners"))
					awayLogo.setImageResource(R.drawable.marlins);
				else if (awayTeam.equalsIgnoreCase("mets"))
					awayLogo.setImageResource(R.drawable.mets);
				else if (awayTeam.equalsIgnoreCase("nationals"))
					awayLogo.setImageResource(R.drawable.nationals);
				else if (awayTeam.equalsIgnoreCase("orioles"))
					awayLogo.setImageResource(R.drawable.orioles);
				else if (awayTeam.equalsIgnoreCase("padres"))
					awayLogo.setImageResource(R.drawable.padres);
				else if (awayTeam.equalsIgnoreCase("phillies"))
					awayLogo.setImageResource(R.drawable.phillies);
				else if (awayTeam.equalsIgnoreCase("pirates"))
					awayLogo.setImageResource(R.drawable.pirates);
				else if (awayTeam.equalsIgnoreCase("rangers"))
					awayLogo.setImageResource(R.drawable.rangers);
				else if (awayTeam.equalsIgnoreCase("rays"))
					awayLogo.setImageResource(R.drawable.rays);
				else if (awayTeam.equalsIgnoreCase("red sox"))
					awayLogo.setImageResource(R.drawable.red_sox);
				else if (awayTeam.equalsIgnoreCase("reds"))
					awayLogo.setImageResource(R.drawable.reds);
				else if (awayTeam.equalsIgnoreCase("rockies"))
					awayLogo.setImageResource(R.drawable.rockies);
				else if (awayTeam.equalsIgnoreCase("royals"))
					awayLogo.setImageResource(R.drawable.royals);
				else if (awayTeam.equalsIgnoreCase("tigers"))
					awayLogo.setImageResource(R.drawable.tigers);
				else if (awayTeam.equalsIgnoreCase("twins"))
					awayLogo.setImageResource(R.drawable.twins);
				else if (awayTeam.equalsIgnoreCase("white sox"))
					awayLogo.setImageResource(R.drawable.white_sox);
				else if (awayTeam.equalsIgnoreCase("yankees"))
					awayLogo.setImageResource(R.drawable.yankees);
				
				
				
			}
	
			
			home.setText(myList.get(position).getHomeTeam());
			away.setText(myList.get(position).getAwayTeam());
			if(myList.get(position).getHomeScore()==null){
				homeScore.setText("");
			}else{
				homeScore.setText(myList.get(position).getHomeScore().toString());
			}
			if(myList.get(position).getAwayScore()==null){
				awayScore.setText("");
			}else{
				awayScore.setText(myList.get(position).getAwayScore().toString());
			}
			
			return convertView;
		}

	}
	

	private class JSONResponseHandler implements ResponseHandler<List<Game>> {

		@Override
		public List<Game> handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			List<Game> result = new ArrayList<Game>();
			String JSONResponse = new BasicResponseHandler()
					.handleResponse(response);
			Log.i("info", "starting json handler");
			JSONResponse = JSONResponse.replace("shsMSNBCTicker.loadGamesData(", "");
			JSONResponse = JSONResponse.replace(");", "");
			Log.i("info", JSONResponse);
			try {

				Log.i("info", "Still working");
				JSONObject responseObject = (JSONObject) new JSONTokener(
						JSONResponse).nextValue();
				Log.i("info", "after response object");
				
				String sportName = responseObject.getString("sport");
				
		
				// Extract value of "list" key -- a List
				JSONArray games = responseObject
						.getJSONArray("games");

				// Iterate over game list
				for (int idx = 0; idx < games.length(); idx++) {
					String xml = games.getString(idx);
					JSONObject game = XML.toJSONObject(xml);
					String jsonPrettyPrintString = game.toString(4);
		            Log.i("game", jsonPrettyPrintString);
					JSONObject entry = game.getJSONObject("ticker-entry");

					//GET GAME ID
					int id = entry.getInt("gamecode");

					//GET GAMESTATE OBJECT
					JSONObject gamestate = entry.getJSONObject("gamestate");

					//GET GAME STATUS
					String status = gamestate.getString("status");



					//GET GAME TIME
					String time = gamestate.getString("gametime");
					String date = gamestate.getString("gamedate");
					SimpleDateFormat sdf = new SimpleDateFormat("M/d h:mm a", Locale.US);
					Date gameDate = null;
					try {
						gameDate = sdf.parse(date + " " + time);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					//GET TV BROADCAST
					String tv = gamestate.getString("tv");


					//GET HOME AND AWAY TEAM OBJECTS
					JSONObject homeTeam = entry.getJSONObject("home-team");
					JSONObject awayTeam = entry.getJSONObject("visiting-team");

					//CITY NAMES
					String homeCity = homeTeam.getString("display_name");
					String awayCity = awayTeam.getString("display_name");

					//GET NICKNAMES
					String homeNickname = homeTeam.getString("nickname");
					String awayNickname = awayTeam.getString("nickname");

					//GET TEAM LOGOS
					String homeLogo =null;
					String awayLogo = null;
					if(!sport.equalsIgnoreCase("MLB")){
						JSONObject homeLogoObject = homeTeam.getJSONObject("team-logo");
						homeLogo = homeLogoObject.getString("link");
						Log.i("info", homeLogo);

						JSONObject awayLogoObject = awayTeam.getJSONObject("team-logo");
						awayLogo = awayLogoObject.getString("link");
						Log.i("info", awayLogo);
					}

					Integer homeScore;
					Integer awayScore;


					//GET SCORES
					if(!status.equalsIgnoreCase("Pre-Game")){
						JSONArray homeScoreArr = homeTeam.getJSONArray("score");
						homeScore = homeScoreArr.getInt(0);
						Log.i("scores", homeScore.toString());
						JSONArray awayScoreArr = awayTeam.getJSONArray("score");
						awayScore = awayScoreArr.getInt(0);
						Log.i("scores", awayScore.toString());
					}else{
						homeScore =null;
						awayScore = null;
					}

					//GET timeElapsed and state if not pre-game
					String timeElapsed = "";
					String state = "";
					if(!status.equalsIgnoreCase("Pre-Game")){
						timeElapsed = gamestate.getString("display_status1");
						state = gamestate.getString("display_status2");
					}

					Log.i("state", timeElapsed);
					Log.i("state", state);
					

					
					result.add(new Game(sportName, id,homeNickname,awayNickname,homeCity,awayCity,
							homeScore,awayScore,
							status,gameDate,homeLogo,awayLogo,tv,timeElapsed,state));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return result;
		}
		
	}

	

}