package com.cs597.bestdeal;

import com.cs597.bestdeal.entities.User;
import com.cs597.bestdeal.R;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class UserAdapter extends ResourceCursorAdapter {
	
	protected final static int ROW_LAYOUT = R.layout.user_row;
	
	public UserAdapter(Context context, Cursor cursor) {
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
		TextView username = (TextView) view.findViewById(R.id.user_name);
		User user = new User(cursor);
		username.setText(user.username);
	}
}