package com.b50.overheard;

import com.b50.overheard.util.SwipeDetector;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class OverheardWord extends Activity {

	private GestureDetector gestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overheard_word);

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
			
			private SwipeDetector detector = new SwipeDetector();
			
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				try {
					if (detector.isSwipeDown(e1, e2, velocityY)) {
						return false;
					} else if (detector.isSwipeUp(e1, e2, velocityY)) {
						showToast("Up Swipe");
					}else if (detector.isSwipeLeft(e1, e2, velocityX)) {
						showToast("Left Swipe");
					} else if (detector.isSwipeRight(e1, e2, velocityX)) {
						showToast("Right Swipe");
					}
				} catch (Exception e) {
					// nothing
				}
				return false;
			}
			
			private void showToast(String phrase){
				Toast.makeText(getApplicationContext(), phrase, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.overheard_word, menu);
		return true;
	}
}
