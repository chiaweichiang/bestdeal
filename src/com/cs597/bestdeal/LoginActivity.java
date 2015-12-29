package com.cs597.bestdeal;

import com.cs597.bestdeal.contracts.UserContract;
import com.cs597.bestdeal.entities.User;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	static final private int SIGNUP_REQUEST = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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

	public void Login(View view) {
		String email = ((EditText) findViewById(R.id.login_email_editText)).getText().toString();
		String password = ((EditText) findViewById(R.id.login_password_editText)).getText().toString();

		if (email.equals("") || password.equals("")) 
		{
			Toast.makeText(this, "Please type in your email and password.",	Toast.LENGTH_LONG).show();
		} 
		else 
		{
			ContentResolver resolver = this.getContentResolver();
			Cursor cursor = resolver.query(UserContract.CONTENT_URI, null, "Users.email = '" + email + "'", null, null);
			if (cursor == null) 
			{
				Toast.makeText(this, "No such Email, please sign up.", Toast.LENGTH_LONG).show();
			} 
			else
			{
				cursor.moveToFirst();
				User user = new User(cursor);
				if (user.password.equals(password)) 
				{
					Intent intent = this.getIntent();
					Bundle bundle = new Bundle();
					bundle.putParcelable("user", user);
					intent.putExtras(bundle);
					setResult(Activity.RESULT_OK, intent);
					finish();
				}
				else
				{
					Toast.makeText(this, "Password is incorrect.", Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	public void Signup(View view) {
		Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
		startActivityForResult(intent,SIGNUP_REQUEST);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);		
		// Use SEARCH_REQUEST and CHECKOUT_REQUEST codes to distinguish the cases.
		// SEARCH: add the book that is returned to the shopping cart.
		if(requestCode == SIGNUP_REQUEST)
		{
			Intent signup = this.getIntent();
			Bundle bundle = intent.getExtras();
			signup.putExtras(bundle);
			setResult(Activity.RESULT_OK,signup);
			finish();
		}
	}
}
