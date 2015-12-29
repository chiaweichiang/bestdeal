package com.cs597.bestdeal.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.cs597.bestdeal.contracts.UserContract;

public class User implements Parcelable {
	
	public long id;
	
	public String username;
	
	public String password;
	
	public String email;	
	
	public User(String username, String password, String email)
	{
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	public User(Parcel in)
	{
		id = in.readLong();
		username = in.readString();
		password = in.readString();
		email = in.readString();
	}
	
	public User(Cursor cursor) {
		this.id = UserContract.getId(cursor);
		this.username = UserContract.getUserName(cursor);
		this.password = UserContract.getPassword(cursor);
		this.email = UserContract.getEmail(cursor);
	}
	
	public ContentValues putValue() {
		ContentValues value = new ContentValues();
		UserContract.putUserName(value, username);
		UserContract.putPassword(value, password);
		UserContract.putEmail(value, email);
		return value;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeLong(id);
		dest.writeString(username);
		dest.writeString(password);
		dest.writeString(email);
	}
	
	public static final Creator<User> CREATOR = new Creator<User>() {
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		public User[] newArray(int size) {
			return new User[size];
		}
	};
}
