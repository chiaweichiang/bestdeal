package com.cs597.bestdeal;

import com.cs597.bestdeal.contracts.ReviewContract;
import com.cs597.bestdeal.entities.Review;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class EditReviewActivity extends Activity {
	
	private Review review;
	private ContentResolver resolver;
	
	private RatingBar star;
	private EditText review_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_review);
		
		Intent intent = this.getIntent();
		review = intent.getParcelableExtra("REVIEW");
		
		star = (RatingBar)findViewById(R.id.review_ratingBar);
		review_text = (EditText)findViewById(R.id.review_text_editText);
		star.setRating(review.star);
		review_text.setText(review.text);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_review, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void Post(View view)
	{
		int rating = (int)star.getRating();
		String reviewtext = review_text.getText().toString();
		
		if(rating==0||reviewtext.equals(""))
		{
			Toast.makeText(this, "Please rate and type in your review.",	Toast.LENGTH_LONG).show();
		}
		else
		{
			review.star = rating;
			review.text = reviewtext;
			ContentValues cv = review.putValue();
			Uri uri = ReviewContract.CONTENT_URI(review.id);
			resolver = this.getContentResolver();
			resolver.update(uri, cv, null, null);
		}
		Intent intent = this.getIntent();
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
}
