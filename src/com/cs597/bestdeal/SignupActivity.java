package com.cs597.bestdeal;

import com.cs597.bestdeal.R;
import com.cs597.bestdeal.contracts.UserContract;
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

public class SignupActivity extends Activity {

	private ContentResolver resolver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.signup, menu);
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
	
	public void addUser(View view){
		String username = ((EditText)findViewById(R.id.username_editText)).getText().toString();
		String email = ((EditText)findViewById(R.id.email_editText)).getText().toString();
		String password = ((EditText)findViewById(R.id.password_editText)).getText().toString();
		
		User user = new User(username,password,email);
		ContentValues cv = user.putValue();
		Uri uri = UserContract.CONTENT_URI;
		resolver = this.getContentResolver();
		user.id = ContentUris.parseId(resolver.insert(uri, cv));
		
		Intent intent = this.getIntent();
		intent.putExtra("USER", user);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
}
