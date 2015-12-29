package com.cs597.bestdeal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

import com.cs597.bestdeal.contracts.UserContract;
import com.cs597.bestdeal.entities.User;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FirstActivity extends Activity {

	static final private int LOGIN_REQUEST = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first);
		getDatabase();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.first, menu);
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
		String email = ((EditText) findViewById(R.id.first_email_editText)).getText().toString();
		String password = ((EditText) findViewById(R.id.first_password_editText)).getText().toString();

		if (email.equals("") || password.equals("")) 
		{
			Toast.makeText(this, "Please type in your email and password.",	Toast.LENGTH_LONG).show();
		} 
		else 
		{
			ContentResolver resolver = this.getContentResolver();
			Cursor cursor = resolver.query(UserContract.CONTENT_URI, null, "Users.email = '" + email + "'", null, null);
			if (cursor.getCount() == 0) 
			{
				Toast.makeText(this, "No such Email, please sign up.", Toast.LENGTH_LONG).show();
			} 
			else
			{
				cursor.moveToFirst();
				User user = new User(cursor);
				if (user.password.equals(password)) 
				{
					Intent intent = new Intent(FirstActivity.this, HomeActivity.class);
					intent.putExtra("USER", user);
					startActivity(intent);
				}
				else
				{
					Toast.makeText(this, "Password is incorrect.", Toast.LENGTH_LONG).show();
				}
			}
		}
	}
	
	public void Nothanks(View view) {
		Intent intent = new Intent(FirstActivity.this, HomeActivity.class);
		startActivity(intent);
	}
	
	public void Signup(View view) {
		Intent intent = new Intent(FirstActivity.this, SignupActivity.class);
		startActivityForResult(intent, LOGIN_REQUEST);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if(requestCode == LOGIN_REQUEST)
		{
			User user = intent.getParcelableExtra("USER");
			Intent next = new Intent(FirstActivity.this, HomeActivity.class);
			next.putExtra("USER", user);
			startActivity(next);
		}
	}
	
	public void getDatabase()
	{
        File dest = new File("/data/data/com.cs597.bestdeal/databases/bestdeal");  
          
        try {  
            if(dest.exists()){  
                dest.delete();  
            }  
            dest.createNewFile();     
            InputStream in = this.getResources().openRawResource(R.raw.bestdeal);  
            int size = in.available();  
            byte buf[] = new byte[size];  
            in.read(buf);  
            in.close();  
            FileOutputStream out = new FileOutputStream(dest);  
            out.write(buf);  
            out.close();
            Toast.makeText(this, "file success", Toast.LENGTH_LONG).show();
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
	}
}
