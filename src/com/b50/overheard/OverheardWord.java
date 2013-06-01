package com.b50.overheard;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.b50.gesticulate.SwipeDetector;

public class OverheardWord extends Activity {

	private static final String APP = "overheardword";
	private GestureDetector gestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(APP, "onCreated Invoked");

		setContentView(R.layout.activity_overheard_word);

		initializeGestures();
	}

	private void initializeGestures() {
		gestureDetector = initGestureDetector();

		View view = findViewById(R.id.LinearLayout1);

		view.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
			}
		});

		view.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		});
	}

	private GestureDetector initGestureDetector() {
		return new GestureDetector(new SimpleOnGestureListener() {

			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				try {
					final SwipeDetector detector = new SwipeDetector(e1, e2, velocityX, velocityY);
					if (detector.isDownSwipe()) {
						return false;
					} else if (detector.isUpSwipe()) {
						finish();
					} else if (detector.isLeftSwipe()) {
						Toast.makeText(getApplicationContext(), "Left Swipe", Toast.LENGTH_SHORT).show();
					} else if (detector.isRightSwipe()) {
						Toast.makeText(getApplicationContext(), "Right Swipe", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					// nothing
				}
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.overheard_word, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.quit_item:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(APP, "onResume Invoked");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(APP, "onStart Invoked");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(APP, "onPause Invoked");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(APP, "onRestart Invoked");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(APP, "onStop Invoked");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(APP, "onDestroy Invoked");
	}
}
