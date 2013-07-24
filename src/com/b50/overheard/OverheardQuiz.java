package com.b50.overheard;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.b50.gesticulate.SwipeDetector;
import com.b50.savvywords.TestableWord;
import com.b50.savvywords.Word;
import com.b50.savvywords.WordTestEngine;

public class OverheardQuiz extends Activity {
	
	private static final String APP = "overheardword";
	private GestureDetector gestureDetector;
	private static WordTestEngine engine;
	private int quizNumber;
	final private String QUIZ_NUM = "quiz_num";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overheard_quiz);
		initializeGestures();
		
		final Bundle previous = getIntent().getExtras();
		quizNumber = (previous != null) ? previous.getInt(QUIZ_NUM) : 1;

		if (quizNumber > 10) {
			quizNumber = 1;
			CharSequence text = "Great Job! You made it through 10 questions. Here's another 10!";
			Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
		}

		final TextView quizCounter = (TextView)findViewById(R.id.quiz_number);
		quizCounter.setText(quizNumber + " of 10");
		
		List<Word> words = buildWordList();

		if (engine == null) {
			engine = WordTestEngine.getInstance(words);
		}

		final TestableWord firstWord = engine.getTestableWord();
		final TextView testDefinition = (TextView)findViewById(R.id.quiz_definition);
		testDefinition.setText(formatDefinition(firstWord.getValidDefinition()));

		final List<String> possibleAnswers = possibleAnswersFrom(firstWord);

		final int[] radios = { R.id.quiz_answer_1, R.id.quiz_answer_2, R.id.quiz_answer_3 };
		for (int x = 0; x < radios.length; x++) {
			final RadioButton rButton = (RadioButton)findViewById(radios[x]);
			rButton.setText(possibleAnswers.get(x));
		}
		
		final RadioGroup group = (RadioGroup)findViewById(R.id.quiz_answers);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(final RadioGroup group, final int checkedId) {
				final RadioButton selected = (RadioButton)findViewById(checkedId);
				final String answer = (String) selected.getText();
				if (answer.equals(firstWord.getSpelling())) {
					final TextView result = (TextView)findViewById(R.id.quiz_result);
					result.setTextColor(Color.parseColor("#228b22"));
					result.setText("Correct!");
					final Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						public void run() {
							final Intent nextQuiz = new Intent(getApplicationContext(), OverheardQuiz.class);
							nextQuiz.putExtra(QUIZ_NUM, ++quizNumber);
							startActivity(nextQuiz);
							result.setText("");
							finish();
						}
					}, 2500);
				} else {
					final TextView result = (TextView)findViewById(R.id.quiz_result);
					result.setTextColor(Color.parseColor("#ff0000"));
					result.setText("Nope, that's not it! Try again.");
					final Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						public void run() {
							selected.setChecked(false);
							result.setText("");
						}
					}, 2000);
				}
			}
		});
	}
	
	public List<String> possibleAnswersFrom(final TestableWord word){
		List<String> possibleAnswers = Arrays.asList(
				word.getInvalidWordAnswers().get(0), 
				word.getInvalidWordAnswers().get(1), 
				word.getSpelling());

		Collections.shuffle(possibleAnswers);
		return possibleAnswers;
	}
	
	private String formatDefinition(final String definition) {
		String firstChar = definition.substring(0, 1).toUpperCase(Locale.ENGLISH);
		StringBuffer buff = new StringBuffer(firstChar);
		buff.append(definition.substring(1, (definition.length() + 0)));
		if (!definition.endsWith(".")) {
			buff.append(".");
		}
		return buff.toString();
	}

	private List<Word> buildWordList() {
		InputStream resource = getApplicationContext().getResources().openRawResource(R.raw.words);
		List<Word> words = new ArrayList<Word>();
		try {
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(resource));
			String read = br.readLine();

			while (read != null) {
				sb.append(read);
				read = br.readLine();
			}

			JSONObject document = new JSONObject(sb.toString());
			JSONArray allWords = document.getJSONArray("words");
			for (int i = 0; i < allWords.length(); i++) {
				words.add(Word.manufacture(allWords.getJSONObject(i)));
			}

		} catch (Exception e) {
			Log.e(APP, "Exception in getInstance for WordEngine: " + e.getLocalizedMessage());
		}
		return words;
	}

	private void initializeGestures() {
		gestureDetector = initGestureDetector();

		View view = findViewById(R.id.widget33);

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
						finish();
					} else if (detector.isUpSwipe()) {
						return false;
					} else if (detector.isLeftSwipe()) {
						return false;
					} else if (detector.isRightSwipe()) {
						return false;
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
		getMenuInflater().inflate(R.menu.overheard_quiz, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.quit_quiz:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
