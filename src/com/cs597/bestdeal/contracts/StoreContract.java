package com.cs597.bestdeal.contracts;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class StoreContract {

	public static final String _ID = "_id";
	public static final int _ID_KEY = 0;
	
	public static final String NAME = "name";
	public static final int NAME_KEY = 1;
	
	public static final String ADDRESS = "address";
	public static final int ADDRESS_KEY = 2;
	
	public static final String LATITUDE = "latitude";
	public static final int LATITUDE_KEY = 3;
	
	public static final String LONGITUDE = "longitude";
	public static final int LONGITUDE_KEY = 4;

	public static final String TYPE = "type";
	public static final int TYPE_KEY = 5;
	
	public static final String AUTOHORITY = "com.cs597.bestdeal";
	public static final String path = "stores";
	
	public static final Uri CONTENT_URI = new Uri.Builder().scheme("content").authority(AUTOHORITY).path(path).build();
	
	public static final String CONTENT_PATH = CONTENT_URI.getPath().substring(1);
	public static final String CONTENT_PATH_ITEM = CONTENT_URI.getPath().substring(1) + "/#";
	
	public static long getId(Cursor cursor){
		return	cursor.getLong(cursor.getColumnIndexOrThrow(_ID));
	}
	
	public static String getName(Cursor cursor){
		return	cursor.getString(cursor.getColumnIndexOrThrow(NAME));
	}
	
	public static void putName(ContentValues values, String name){
		values.put(NAME, name);
	}
	
	public static String getAddress(Cursor cursor){
		return	cursor.getString(cursor.getColumnIndexOrThrow(ADDRESS));
	}
	
	public static void putAddress(ContentValues values, String address){
		values.put(ADDRESS, address);
	}
	
	public static double getLatitude(Cursor cursor){
		return	cursor.getDouble(cursor.getColumnIndexOrThrow(LATITUDE));
	}
	
	public static void putLatitude(ContentValues values, double latitude){
		values.put(LATITUDE, latitude);
	}
	
	public static double getLongitude(Cursor cursor){
		return	cursor.getDouble(cursor.getColumnIndexOrThrow(LONGITUDE));
	}
	
	public static void putLongitude(ContentValues values, double longitude){
		values.put(LONGITUDE, longitude);
	}
	
	public static int getType(Cursor cursor){
		return	cursor.getInt(cursor.getColumnIndexOrThrow(TYPE));
	}
	
	public static void putType(ContentValues values, int type){
		values.put(TYPE, type);
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
