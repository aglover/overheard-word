package com.b50.overheard;

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
	
	private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    	
    private GestureDetector gestureDetector;
    
    private boolean isSwipeDistance(float coordinateA, float coordinateB){
    	return (coordinateA - coordinateB) > SWIPE_MIN_DISTANCE;
    }
    
    private boolean isSwipeSpeed(float velocity){
    	return  Math.abs(velocity) > SWIPE_THRESHOLD_VELOCITY;
    }
    
    private boolean isSwipe(float coordinateA, float coordinateB, float velocity){
    	return isSwipeDistance(coordinateA, coordinateB) && isSwipeSpeed(velocity);    		
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overheard_word);
                        
        gestureDetector = new GestureDetector(new SimpleOnGestureListener(){
        	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                try {
                	
                	if (isSwipe(e2.getY(), e1.getY(), velocityY)) {
                    	return false;
                	} else if(isSwipe(e1.getY(), e2.getY(), velocityY)) {
                        Toast.makeText(getApplicationContext(), "up Swipe", Toast.LENGTH_SHORT).show();
                    } 
                	
                	if(isSwipe(e1.getX(), e2.getX(), velocityX)) {
                        Toast.makeText(getApplicationContext(), "Left Swipe", Toast.LENGTH_SHORT).show();
                    }  else if (isSwipe(e2.getX(), e1.getX(), velocityX)) {
                        Toast.makeText(getApplicationContext(), "Right Swipe", Toast.LENGTH_SHORT).show();
                    }
                	
//                	if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
//                    	return false;
//                	} else if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
//                        Toast.makeText(getApplicationContext(), "down? Swipe", Toast.LENGTH_SHORT).show();
//                    } 
//                	
//                	if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                        Toast.makeText(getApplicationContext(), "Left Swipe", Toast.LENGTH_SHORT).show();
//                    }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                        Toast.makeText(getApplicationContext(), "Right Swipe", Toast.LENGTH_SHORT).show();
//                    }
                } catch (Exception e) {
                    // nothing
                }
                return false;
            }
        });
        
        View view = findViewById(R.id.LinearLayout1);

        view.setOnClickListener(new OnClickListener(){
        	public void onClick(View arg0) {}
        });
        
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
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
