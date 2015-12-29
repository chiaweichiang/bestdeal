package com.cs597.bestdeal;

import com.cs597.bestdeal.contracts.ReviewContract;
import com.cs597.bestdeal.entities.Product;
import com.cs597.bestdeal.entities.Review;
import com.cs597.bestdeal.entities.User;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
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

public class AddReviewActivity extends Activity {
	
	private User user;
	private long product_id;
	
	private ContentResolver resolver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_review);
		
		Intent intent = this.getIntent();
		user = intent.getParcelableExtra("USER");
		product_id = intent.getLongExtra("PRODUCT_ID",0);
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
		int rating = (int)((RatingBar)findViewById(R.id.review_ratingBar)).getRating();
		String reviewtext = ((EditText)findViewById(R.id.review_text_editText)).getText().toString();
		
		if(rating==0||reviewtext.equals(""))
		{
			Toast.makeText(this, "Please rate and type in your review.",	Toast.LENGTH_LONG).show();
		}
		else
		{
			Review review = new Review(user.id, reviewtext, rating, product_id);
			ContentValues cv = review.putValue();
			Uri uri = ReviewContract.CONTENT_URI;
			resolver = this.getContentResolver();
			review.id = ContentUris.parseId(resolver.insert(uri, cv));
		}
		
		Intent intent = this.getIntent();
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
}
