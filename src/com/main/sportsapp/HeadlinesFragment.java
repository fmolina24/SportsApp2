package com.main.sportsapp;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.fedorvlasov.lazylist.ImageLoader;
import com.model.sportsapp.Headline;


public class HeadlinesFragment extends SherlockListFragment {
	private String sport ="";
	private String league ="";
	private static final String ARG_POSITION = "position";
	private int position;
	
	
	public static HeadlinesFragment newInstance(int position) {
		HeadlinesFragment f = new HeadlinesFragment();
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
			case 1:
				sport="basketball";
				league="nba";
				break;
			case 2:
				sport="hockey";
				league="nhl";
				break;
			case 3:
				sport="baseball";
				league="mlb";
				break;
			case 4:
				sport="football";
				league="nfl";
				break;
			default :
				sport="";
				league="";
				break;
			
		}
		new HttpGetTask().execute();
		
	}
	
	

	private class HttpGetTask extends AsyncTask<Void, Void, List<Headline>> {

		private static final String TAG = "HttpGetTask";
		private String apiKey =  "5a3kx6j8sb3nvppya2hxx6jz";
		
		private String URL = "";

		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

		@Override
		protected List<Headline> doInBackground(Void... params) {
			//IF SPORTS OR LEAGUE IS MISSING LOOK UP TOP NEWS
			if(sport.isEmpty() || league.isEmpty()){
				URL = "http://api.espn.com/v1/sports/news/headlines/top?apikey="+apiKey;
			}else{
				URL = "http://api.espn.com/v1/sports/"+sport+"/"+league+"/news/headlines?apikey="+apiKey;
			}
			
			
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
		protected void onPostExecute(List<Headline> result) {
			if (null != mClient)
				mClient.close();
			setListAdapter(new MyAdapter(getActivity(),R.layout.headline_fragment,result));
		}
	}
	class MyAdapter extends ArrayAdapter{
		private List<Headline> myList;
		private Context context;
		ImageLoader imageLoader=new ImageLoader(context);

		public MyAdapter(Context context, int resource, List<Headline> result) {
			super(context, resource, result);
			myList = result;
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView headline;
			TextView desc;
			TextView date;
			TextView url;
			ImageView headlinePic;
			
			//reuse converView if you can to speed up scrolling on listview
			if(convertView == null){
				//inflate the listview
				LayoutInflater inflator = ((Activity)context).getLayoutInflater();
				convertView = inflator.inflate(R.layout.headline_fragment, parent, false);
				
				convertView.setTag(R.id.headline, convertView.findViewById(R.id.headline));
				convertView.setTag(R.id.description, convertView.findViewById(R.id.description));
				convertView.setTag(R.id.date, convertView.findViewById(R.id.date));
				convertView.setTag(R.id.seeMore, convertView.findViewById(R.id.seeMore));
				convertView.setTag(R.id.headlinePic, convertView.findViewById(R.id.headlinePic));
			}
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
			
			headline = (TextView) convertView.getTag(R.id.headline);
			desc = (TextView) convertView.getTag(R.id.description);
			date = (TextView) convertView.getTag(R.id.date);
			url = (TextView)convertView.getTag(R.id.seeMore);
			headlinePic = (ImageView)convertView.getTag(R.id.headlinePic);
			imageLoader.DisplayImage(myList.get(position).getImageURL(), headlinePic);
			headline.setText(myList.get(position).getHeadline());
			desc.setText(myList.get(position).getDescription());
			date.setText(df.format(myList.get(position).getPublished()));
			String linkText = "<a href='"+myList.get(position).getUrl()+"'>See more</a>";
			url.setText(Html.fromHtml(linkText));
			url.setMovementMethod(LinkMovementMethod.getInstance());
			
			return convertView;
		}

	}
	

	private class JSONResponseHandler implements ResponseHandler<List<Headline>> {

		@Override
		public List<Headline> handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			List<Headline> result = new ArrayList<Headline>();
			String JSONResponse = new BasicResponseHandler()
					.handleResponse(response);
			
			try {

				
				JSONObject responseObject = (JSONObject) new JSONTokener(
						JSONResponse).nextValue();
				
				
		
				// Extract value of "list" key -- a List
				JSONArray headlines = responseObject
						.getJSONArray("headlines");

				// Iterate over headline list
				for (int idx = 0; idx < headlines.length(); idx++) {
					JSONObject headline = headlines.getJSONObject(idx);
					
					String headLine = headline.getString("headline");
					String description = headline.getString("description");
					
					//GET FIRST IMAGE
					JSONArray images = headline.getJSONArray("images");
					JSONObject img;
					if(images.length() > 1){
						img = images.getJSONObject(1);
					}
					else{
						img = images.getJSONObject(0);
					}
					JSONObject links = headline.getJSONObject("links");
					JSONObject webLinks = links.getJSONObject("web");
					String imgLink = img.getString("url");
					String storyLink = webLinks.getString("href");
					//GET PUBLISHED DATE
					String rawDate = headline.getString("published");
					
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
					Date date = null;
					try {
						date = df.parse(rawDate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					result.add(new Headline(headLine.trim(),description,imgLink,date,storyLink));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return result;
		}
		
	}
	


}
