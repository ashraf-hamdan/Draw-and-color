package net.ashraf.drawingapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

public class Splash extends Activity implements AnimationListener {

	Button splsh;
	// Animation
	Animation animFadein;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);

		if (getIntent().getBooleanExtra("EXIT", false)) {
			finish();
		}
		splsh = (Button) findViewById(R.id.btn_splash);

		// load the animation
		animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.zoom_in);

		// set animation listener
		animFadein.setAnimationListener(this);

		splsh.setVisibility(View.VISIBLE);

		// start the animation
		splsh.startAnimation(animFadein);

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// Take any action after completing the animation

		// check for fade in animation
		if (animation == animFadein) {
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
		}

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

}
