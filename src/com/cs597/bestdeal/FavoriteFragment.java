package com.cs597.bestdeal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.cs597.bestdeal.contracts.FavoriteContract;
import com.cs597.bestdeal.contracts.ProductContract;
import com.cs597.bestdeal.entities.Favorite;
import com.cs597.bestdeal.entities.Product;
import com.cs597.bestdeal.entities.User;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class FavoriteFragment extends Fragment {

	private static final String ARG_SECTION_TITLE = "section_title";
	
	private User user;
	private Favorite favorite;
	private FavoriteAdapter adapter;
	
	static final private int LOGIN_REQUEST = 1;
	
	public static FavoriteFragment newInstance(String title, User user) {
		FavoriteFragment fragment = new FavoriteFragment();
		fragment.user = user;
		Bundle args = new Bundle();
		args.putString(ARG_SECTION_TITLE, title);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.favorite, container, false);
		ListView favoritelist = (ListView) view.findViewById(R.id.favoritelist);
		adapter = new FavoriteAdapter(getActivity(), null);
		favoritelist.setAdapter(adapter);
		View emptyView = view.findViewById(android.R.id.empty);
		favoritelist.setEmptyView(emptyView);
		
		if(user == null)
		{
			Intent login = new Intent(getActivity(), LoginActivity.class);
			startActivityForResult(login, LOGIN_REQUEST);
		}
		
		ContentResolver resolver = getActivity().getContentResolver();
		Cursor cursor = resolver.query(FavoriteContract.CONTENT_URI, null, "Favorites.user_fk = " + user.id, null, null);
		adapter.changeCursor(cursor);		
		
		return view;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if(requestCode == LOGIN_REQUEST)
		{
			Bundle bundle = intent.getExtras();
			user = bundle.getParcelable("user");
			ContentResolver resolver = getActivity().getContentResolver();
			Cursor cursor = resolver.query(FavoriteContract.CONTENT_URI, null, "Favorites.user_fk = " + user.id, null, null);
			adapter.changeCursor(cursor);
		}
	}
	
	public class FavoriteAdapter extends ResourceCursorAdapter {
		
		protected final static int ROW_LAYOUT = R.layout.favorite_row;
		private ContentResolver resolver;
		
		public FavoriteAdapter(Context context, Cursor cursor) {
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
			ImageView productimg = (ImageView) view.findViewById(R.id.favoriterow_product_img);
			TextView productname = (TextView) view.findViewById(R.id.favoriterow_product_name);
			ImageButton imgBtn = (ImageButton) view.findViewById(R.id.favoriterow_deleteButton);
			
			favorite = new Favorite(cursor);
			resolver = getActivity().getContentResolver();
			Cursor c = resolver.query(ProductContract.CONTENT_URI, null, "Products._id = " + favorite.product_fk, null, null);
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
			
			imgBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
            	    resolver.delete(FavoriteContract.CONTENT_URI(favorite.id), null, null);
       				Cursor cursor = resolver.query(FavoriteContract.CONTENT_URI, null, "Favorites.user_fk = " + user.id, null, null);
       				adapter.changeCursor(cursor);
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
