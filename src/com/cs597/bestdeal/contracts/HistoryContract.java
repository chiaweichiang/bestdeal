package com.cs597.bestdeal.contracts;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class HistoryContract {
	
	public static final String _ID = "_id";
	public static final int _ID_KEY = 0;
	
	public static final String USER_FK = "user_fk";
	public static final int USER_FK_KEY = 1;

	public static final String PRODUCT_FK = "product_fk";
	public static final int PRODUCT_FK_KEY = 2;
	
	public static final String AUTOHORITY = "com.cs597.bestdeal";
	public static final String path = "historys";
	
	public static final Uri CONTENT_URI = new Uri.Builder().scheme("content").authority(AUTOHORITY).path(path).build();
	
	public static final String CONTENT_PATH = CONTENT_URI.getPath().substring(1);
	public static final String CONTENT_PATH_ITEM = CONTENT_URI.getPath().substring(1) + "/#";
	
	public static long getId(Cursor cursor){
		return	cursor.getLong(cursor.getColumnIndexOrThrow(_ID));
	}
	
	public static long getUserFk(Cursor cursor) {
		return cursor.getLong(cursor.getColumnIndexOrThrow(USER_FK));
	}

	public static void putUserFk(ContentValues values, long fk) {
		values.put(USER_FK, fk);
	}

	public static long getProductFk(Cursor cursor) {
		return cursor.getLong(cursor.getColumnIndexOrThrow(PRODUCT_FK));
	}
	
	public static void putProductFk(ContentValues values, long fk) {
		values.put(PRODUCT_FK, fk);
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
