package com.cs597.bestdeal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.cs597.bestdeal.contracts.ProductContract;
import com.cs597.bestdeal.contracts.ReviewContract;
import com.cs597.bestdeal.entities.Product;
import com.cs597.bestdeal.entities.Review;
import com.cs597.bestdeal.entities.User;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class ReviewActivity extends Activity {
	
	private User user;
	private Review review;
	private ReviewAdapter adapter;
	
	static final private int LOGIN_REQUEST = 1;
	static final private int EDIT_REQUEST = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review);
		
		Intent intent = this.getIntent();
		user = intent.getParcelableExtra("USER");
		
		if(user == null)
		{
			Intent login = new Intent(ReviewActivity.this, LoginActivity.class);
			startActivityForResult(login, LOGIN_REQUEST);
		}
		
		ContentResolver resolver = this.getContentResolver();
		Cursor cursor = resolver.query(ReviewContract.CONTENT_URI, null, "Reviews.user_fk = " + user.id, null, null);
		
		ListView reviewlist = (ListView)findViewById(R.id.reviewlist);
		adapter = new ReviewAdapter(this, cursor);
		reviewlist.setAdapter(adapter);
		View emptyView = findViewById(android.R.id.empty);
		reviewlist.setEmptyView(emptyView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.review, menu);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if(requestCode == LOGIN_REQUEST)
		{
			Bundle bundle = intent.getExtras();
			user = bundle.getParcelable("user");
			ContentResolver resolver = this.getContentResolver();
			Cursor cursor = resolver.query(ReviewContract.CONTENT_URI, null, "Reviews.user_fk = " + user.id, null, null);
			
			ListView reviewlist = (ListView)findViewById(R.id.reviewlist);
			adapter = new ReviewAdapter(this, cursor);
			reviewlist.setAdapter(adapter);
		}
		if(requestCode == EDIT_REQUEST)
		{
			ContentResolver resolver = this.getContentResolver();
			Cursor cursor = resolver.query(ReviewContract.CONTENT_URI, null, "Reviews.user_fk = " + user.id, null, null);
			adapter.changeCursor(cursor);
		}
	}
	
	public class ReviewAdapter extends ResourceCursorAdapter {
		
		protected final static int ROW_LAYOUT = R.layout.review_row;
		
		public ReviewAdapter(Context context, Cursor cursor) {
			super(context, ROW_LAYOUT, cursor, 0);
		}
		
		@Override
		public View newView(Context context, Cursor cur, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater)
					context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			return inflater.inflate(ROW_LAYOUT, parent, false);
		}
		
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			ImageView productimg = (ImageView) view.findViewById(R.id.reviewrow_product_img);
			TextView productname = (TextView) view.findViewById(R.id.reviewrow_product_name);
			TextView reviewtext = (TextView) view.findViewById(R.id.reviewrow_text);
			ImageButton imgBtn = (ImageButton) view.findViewById(R.id.reviewrow_editButton);
			
			review = new Review(cursor);
			ContentResolver resolver = getContentResolver();
			Cursor c = resolver.query(ProductContract.CONTENT_URI, null, "Products._id = " + review.product_fk, null, null);
			c.moveToFirst();
			if(c != null)
			{
				Product product = new Product(c);
				productname.setText(product.name);
				reviewtext.setText(review.text);
				Bitmap bitmap = getLoacalBitmap("/Product/"+ product.id +".jpg");
				if(bitmap == null)
				{
					productimg.setBackgroundResource(R.drawable.default_product_pic);
				}
				else
				{
					productimg.setImageBitmap(bitmap);
				}
			}
			
			imgBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
            	   Intent intent = new Intent(ReviewActivity.this, EditReviewActivity.class);
            	   intent.putExtra("REVIEW", review);
            	   startActivityForResult(intent,EDIT_REQUEST);
               }
           });
		}
		
		public Bitmap getLoacalBitmap(String url) {
		     try {
		          FileInputStream fis = new FileInputStream(url);
		          return BitmapFactory.decodeStream(fis);
		     } catch (FileNotFoundException e) {
		          e.printStackTrace();
		          return null;
		     }
		}
	}
}
