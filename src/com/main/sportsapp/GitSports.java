package com.main.sportsapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.os.Handler;

public class GitSports extends Activity {

	private static int SPLASH_TIME_OUT = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);

		ImageView imageView = (ImageView) findViewById(R.id.splash);
		Bitmap bm = BitmapFactory.decodeResource(getResources(),
				R.drawable.splash_logo);
		imageView.setImageBitmap(bm);
		
		new Handler().postDelayed(new Runnable() {
	
 
            @Override
            public void run() {
                Intent i = new Intent(GitSports.this, MainActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);

	}

}
