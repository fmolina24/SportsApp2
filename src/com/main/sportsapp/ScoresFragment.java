
package com.main.sportsapp;

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
			
			HttpGet request = new HttpGet(URL);
			JSONResponseHandler responseHandler = new JSONResponseHandler();
			try {
				return mClient.execute(request, responseHandler);
			} catch (ClientProtocolException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			return null;
		}
		

 
		@Override
		protected void onPostExecute(List<Game> result) {
			if (null != mClient)
				mClient.close();
			setListAdapter(new MyAdapter(getActivity(),R.layout.row,result));
			ListView listView = getListView();
			
			
			
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
				
				MLBLogoFinder mlbLogoFinder = new MLBLogoFinder();
				
				mlbLogoFinder.setLogo(homeLogo, myList.get(position).getHomeTeam());
				mlbLogoFinder.setLogo(awayLogo, myList.get(position).getAwayTeam());

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
			
			JSONResponse = JSONResponse.replace("shsMSNBCTicker.loadGamesData(", "");
			JSONResponse = JSONResponse.replace(");", "");
			
			try {

				
				JSONObject responseObject = (JSONObject) new JSONTokener(
						JSONResponse).nextValue();
				
				
				String sportName = responseObject.getString("sport");
				
		
				// Extract value of "list" key -- a List
				JSONArray games = responseObject
						.getJSONArray("games");

				// Iterate over game list
				for (int idx = 0; idx < games.length(); idx++) {
					String xml = games.getString(idx);
					JSONObject game = XML.toJSONObject(xml);
					String jsonPrettyPrintString = game.toString(4);
		            
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
						

						JSONObject awayLogoObject = awayTeam.getJSONObject("team-logo");
						awayLogo = awayLogoObject.getString("link");
						
					}

					Integer homeScore;
					Integer awayScore;


					//GET SCORES
					if(!status.equalsIgnoreCase("Pre-Game")){
						JSONArray homeScoreArr = homeTeam.getJSONArray("score");
						homeScore = homeScoreArr.getInt(0);
						
						JSONArray awayScoreArr = awayTeam.getJSONArray("score");
						awayScore = awayScoreArr.getInt(0);
						
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