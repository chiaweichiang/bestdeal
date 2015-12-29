package com.cs597.bestdeal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.cs597.bestdeal.contracts.HistoryContract;
import com.cs597.bestdeal.contracts.ProductContract;
import com.cs597.bestdeal.entities.History;
import com.cs597.bestdeal.entities.Product;
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
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class HistoryActivity extends Activity {
	
	private User user;
	private History history;
	private HistoryAdapter adapter;

	static final private int LOGIN_REQUEST = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		
		Intent intent = this.getIntent();
		user = intent.getParcelableExtra("USER");
		
		if(user == null)
		{
			Intent login = new Intent(HistoryActivity.this, LoginActivity.class);
			startActivityForResult(login, LOGIN_REQUEST);
		}
		
		ContentResolver resolver = this.getContentResolver();
		Cursor cursor = resolver.query(HistoryContract.CONTENT_URI, null, "Historys.user_fk = " + user.id, null, null);
		
		ListView historylist = (ListView)findViewById(R.id.historylist);
		adapter = new HistoryAdapter(this, cursor);
		historylist.setAdapter(adapter);
		View emptyView = findViewById(android.R.id.empty);
		historylist.setEmptyView(emptyView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history, menu);
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
		// Use SEARCH_REQUEST and CHECKOUT_REQUEST codes to distinguish the cases.
		// SEARCH: add the book that is returned to the shopping cart.
		if(requestCode == LOGIN_REQUEST)
		{
			ContentResolver resolver = this.getContentResolver();
			Cursor cursor = resolver.query(HistoryContract.CONTENT_URI, null, "Historys.user_fk = " + user.id, null, null);
			
			ListView historylist = (ListView)findViewById(R.id.historylist);
			adapter = new HistoryAdapter(this, cursor);
			historylist.setAdapter(adapter);
		}
	}
	
	public class HistoryAdapter extends ResourceCursorAdapter {
		
		protected final static int ROW_LAYOUT = R.layout.history_row;
		private ContentResolver resolver;
		
		public HistoryAdapter(Context context, Cursor cursor) {
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
			ImageView productimg = (ImageView) view.findViewById(R.id.historyrow_product_img);
			TextView productname = (TextView) view.findViewById(R.id.historyrow_product_name);
			
			history = new History(cursor);
			resolver = getContentResolver();
			Cursor c = resolver.query(ProductContract.CONTENT_URI, null, "Products._id = " + history.product_fk, null, null);
			c.moveToFirst();
			if(c != null)
			{
				Product product = new Product(c);
				productname.setText(product.name);
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
