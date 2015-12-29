package com.cs597.bestdeal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.cs597.bestdeal.contracts.FavoriteContract;
import com.cs597.bestdeal.contracts.ProductContract;
import com.cs597.bestdeal.contracts.ReviewContract;
import com.cs597.bestdeal.contracts.UserContract;
import com.cs597.bestdeal.entities.Favorite;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ProductActivity extends Activity {

	private User user;
	private Product product;
	private long product_id;
	
	private ProductReviewAdapter adapter;
	private ContentResolver resolver;
	
	private RatingBar product_rating;
	private TextView product_review_count;
	
	static final private int ADD_REQUEST = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product);
		
		Intent intent = this.getIntent();
		user = intent.getParcelableExtra("USER");
		product_id = intent.getLongExtra("PRODUCT_ID",0);
		
		ContentResolver resolver = this.getContentResolver();
		Cursor cursor = resolver.query(ProductContract.CONTENT_URI, null, "Products._id = " + product_id , null, null); 
		cursor.moveToFirst();
		product = new Product(cursor);
		
		ImageView product_img = (ImageView) findViewById(R.id.product_image);
		TextView product_name = (TextView) findViewById(R.id.product_name);
		TextView product_price = (TextView) findViewById(R.id.product_price);
		RatingBar product_rating = (RatingBar) findViewById(R.id.product_ratingBar);
		TextView product_review_count = (TextView) findViewById(R.id.product_review_count);
		ListView product_review_listView = (ListView) findViewById(R.id.product_review_listView);
				
		Bitmap bitmap = getLoacalBitmap("/Product/"+ product.id +".jpg");
		if(bitmap == null)
		{
			product_img.setBackgroundResource(R.drawable.default_product_pic);
		}
		else
		{
			product_img.setImageBitmap(bitmap);
		}
		product_name.setText(product.name);
		product_price.setText("$ " + product.price);
		
		resolver = this.getContentResolver();
		Cursor c = resolver.query(ReviewContract.CONTENT_URI, null, "Reviews.product_fk = " + product.id, null, null);
		adapter = new ProductReviewAdapter(this, c);
		product_review_listView.setAdapter(adapter);
		View emptyView = findViewById(android.R.id.empty);
		product_review_listView.setEmptyView(emptyView);
		
		c.moveToFirst();
		int count = c.getCount();
		product_review_count.setText("("+ count +")");
		if(cursor == null || count == 0)
		{
			product_rating.setRating(0);
		}
		else
		{
			int total = 0;
			while(!c.isAfterLast())
			{
				total += ReviewContract.getStar(c);
				c.moveToNext();
			}
			product_rating.setRating(total/count);
		}
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

	public void addReview(View view)
	{
		Intent intent = new Intent(ProductActivity.this, AddReviewActivity.class);
		intent.putExtra("USER", user);
		intent.putExtra("PRODUCT_ID", product.id);
		startActivityForResult(intent, ADD_REQUEST);
	}
	
	public void addFavorite(View view)
	{
		ContentResolver resolver = this.getContentResolver();
		Favorite favorite = new Favorite(user.id,product.id);
		resolver.insert(FavoriteContract.CONTENT_URI, favorite.putValue());
		Toast.makeText(this, "Add success.", Toast.LENGTH_LONG).show();
	}
	
	 @Override
	protected void onActivityResult(int requestCode, int resultCode,
				Intent intent) {
			super.onActivityResult(requestCode, resultCode, intent);
			if(requestCode == ADD_REQUEST)
			{
//				Cursor cur = resolver.query(ReviewContract.CONTENT_URI, null, "Reviews.product_fk = " + product_id, null, null);
//				adapter.changeCursor(cur);
//				cur.moveToFirst();
//				int count = cur.getCount();
	//			product_review_count.setText("("+ count +")");
		//		if(count == 0)
			//	{
				//	product_rating.setRating(0);
//				}
	//			else
		//		{
			//		int total = 0;
				//	while(!cur.isAfterLast())
					//{
//						total += ReviewContract.getStar(cur);
	//					cur.moveToNext();
		//			}
			//		product_rating.setRating(total/count);
				//}
			}
		}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product, menu);
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
	
public class ProductReviewAdapter extends ResourceCursorAdapter {
		
		protected final static int ROW_LAYOUT = R.layout.product_review_row;
		
		public ProductReviewAdapter(Context context, Cursor cursor) {
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
			ImageView user_img = (ImageView) view.findViewById(R.id.product_review_row_user_img);
			TextView user_name = (TextView) view.findViewById(R.id.product_review_row_user_name);
			TextView reviewtext = (TextView) view.findViewById(R.id.product_review_row_text);
			
			Review review = new Review(cursor);
			ContentResolver resolver = getContentResolver();
			Cursor c = resolver.query(UserContract.CONTENT_URI, null, "Users._id = " + review.user_fk, null, null);
			c.moveToFirst();
			if(!(cursor == null || cursor.getCount() == 0))
			{
				User user = new User(c);
				user_name.setText(user.username);
				reviewtext.setText(review.text);
				Bitmap bitmap = getLoacalBitmap("/User/"+ user.id +".jpg");
				if(bitmap == null)
				{
					user_img.setBackgroundResource(R.drawable.default_profile_pic);
				}
				else
				{
					user_img.setImageBitmap(bitmap);
				}
			}
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
