package com.cs597.bestdeal.contracts;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class ProductContract {
	
	public static final String _ID = "_id";
	public static final int _ID_KEY = 0;
	
	public static final String NAME = "name";
	public static final int NAME_KEY = 1;

	public static final String STORE_FK = "store_fk";
	public static final int STORE_FK_KEY = 2;
	
	public static final String PRICE = "price";
	public static final int PRICE_KEY = 3;
	
	public static final String BAR = "bar";
	public static final int BAR_KEY = 4;
	
	public static final String TYPE = "type";
	public static final int TYPE_KEY = 5;
	
	public static final String AUTOHORITY = "com.cs597.bestdeal";
	public static final String path = "products";
	
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
	
	public static long getStorefk(Cursor cursor){
		return	cursor.getLong(cursor.getColumnIndexOrThrow(STORE_FK));
	}
	
	public static void putStorefk(ContentValues values, long store_fk){
		values.put(STORE_FK, store_fk);
	}
	
	public static String getPrice(Cursor cursor){
		return	cursor.getString(cursor.getColumnIndexOrThrow(PRICE));
	}
	public static void putPrice(ContentValues values, String price){
		values.put(PRICE, price);
	}
	
	public static long getBar(Cursor cursor){
		return	cursor.getLong(cursor.getColumnIndexOrThrow(BAR));
	}
	
	public static void putBar(ContentValues values, long bar){
		values.put(BAR, bar);
	}
	
	public static int getType(Cursor cursor){
		return	cursor.getInt(cursor.getColumnIndexOrThrow(TYPE));
	}
	
	public static void putType(ContentValues values, int type){
		values.put(TYPE, type);
	}
	
	public static Uri withExtendedPath(Uri uri, String[] path) {
		Uri.Builder builder = uri.buildUpon();
		for (String p : path)
			builder.appendPath(p);
		return builder.build();
	}
	
	public static Uri CONTENT_URI(long id) {
		return ContentUris.withAppendedId(CONTENT_URI, id);
	}
	
	public static long getID(Uri uri) {
		return Long.parseLong(uri.getLastPathSegment());
	}
	
	public static String contentType(String content) {
		return "vnd.android.cursor/vnd." + AUTOHORITY + "." + content + "s";
	}
	
	public static String contentItemType(String content) {
		return "vnd.android.cursor.item/vnd." + AUTOHORITY + "." + content;
	}

}
