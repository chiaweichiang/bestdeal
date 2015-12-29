package com.cs597.bestdeal.contracts;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class UserContract {
	
	public static final String _ID = "_id";
	public static final int _ID_KEY = 0;
	
	public static final String USERNAME = "username";
	public static final int USERNAME_KEY = 1;
	
	public static final String PASSWORD = "password";
	public static final int PASSWORD_KEY = 2;
	
	public static final String EMAIL = "email";
	public static final int EMAIL_KEY = 3;
	
	public static final String AUTOHORITY = "com.cs597.bestdeal";
	public static final String path = "users";
	
	public static final Uri CONTENT_URI = new Uri.Builder().scheme("content").authority(AUTOHORITY).path(path).build();
	
	public static final String CONTENT_PATH = CONTENT_URI.getPath().substring(1);
	public static final String CONTENT_PATH_ITEM = CONTENT_URI.getPath().substring(1) + "/#";
	
	public static long getId(Cursor cursor){
		return	cursor.getLong(cursor.getColumnIndexOrThrow(_ID));
	}
	
	public static String getUserName(Cursor cursor) {
		return cursor.getString(cursor.getColumnIndexOrThrow(USERNAME));
	}

	public static void putUserName(ContentValues values, String username) {
		values.put(USERNAME, username);
	}
	
	public static String getPassword(Cursor cursor) {
		return cursor.getString(cursor.getColumnIndexOrThrow(PASSWORD));
	}

	public static void putPassword(ContentValues values, String password) {
		values.put(PASSWORD, password);
	}
	
	public static String getEmail(Cursor cursor) {
		return cursor.getString(cursor.getColumnIndexOrThrow(EMAIL));
	}

	public static void putEmail(ContentValues values, String email) {
		values.put(EMAIL, email);
	}
	
	public static Uri CONTENT_URI(long id) {
		return ContentUris.withAppendedId(CONTENT_URI, id);
	}
	
	public static String contentType(String content) {
		return "vnd.android.cursor/vnd." + AUTOHORITY + "." + content + "s";
	}
	
	public static String contentItemType(String content) {
		return "vnd.android.cursor.item/vnd." + AUTOHORITY + "." + content;
	}

}
