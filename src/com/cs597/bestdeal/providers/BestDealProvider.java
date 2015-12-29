package com.cs597.bestdeal.providers;

import com.cs597.bestdeal.contracts.FavoriteContract;
import com.cs597.bestdeal.contracts.HistoryContract;
import com.cs597.bestdeal.contracts.ProductContract;
import com.cs597.bestdeal.contracts.ReviewContract;
import com.cs597.bestdeal.contracts.StoreContract;
import com.cs597.bestdeal.contracts.UserContract;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class BestDealProvider  extends ContentProvider {

	private static final String DATABASE_NAME = "bestdeal";
	private static final String USER_TABLE = "Users";
	private static final String PRODUCT_TABLE = "Products";
	private static final String REVIEW_TABLE = "Reviews";
	private static final String STORE_TABLE = "Stores";
	private static final String FAVORITE_TABLE = "Favorites";
	private static final String HISTORY_TABLE = "Historys";
	private static final int DATABASE_VERSION = 1;

	private static final UriMatcher uriMatcher;
	
	private static final int USER_ROW = 1;
	private static final int STORE_ROW = 2;
	private static final int PRODUCT_ROW = 3;
	private static final int REVIEW_ROW = 4;
	private static final int FAVORITE_ROW = 5;
	private static final int HISTORY_ROW = 6;
	private static final int FAVORITE_ROW1 = 7;
	
	private SQLiteDatabase db;
	private DatabaseHelper DBHelper;
	
	private static final String USER_TABLE_CREATE = "create table " + USER_TABLE + " (" 
			+ UserContract._ID + " integer primary key, " 
			+ UserContract.USERNAME + " text not null, "
			+ UserContract.PASSWORD + " text not null, " 
			+ UserContract.EMAIL + " text not null);";
	
	private static final String STORE_TABLE_CREATE = "create table " + STORE_TABLE + " (" 
			+ StoreContract._ID + " integer primary key, " 
			+ StoreContract.NAME + " text not null, "
			+ StoreContract.ADDRESS + " text not null, "
			+ StoreContract.LATITUDE + " double not null, "
			+ StoreContract.LONGITUDE + " double not null, "
			+ StoreContract.TYPE + " integer not null);";
	
	private static final String PRODUCT_TABLE_CREATE = "create table " + PRODUCT_TABLE + " (" 
			+ ProductContract._ID + " integer primary key, " 
			+ ProductContract.NAME + " text not null, "
			+ ProductContract.PRICE + " text not null, "
			+ ProductContract.STORE_FK + " integer not null, "
			+ ProductContract.BAR + " integer not null, "
			+ ProductContract.TYPE + " integer not null, "
			+ "foreign key (" + ProductContract.STORE_FK + ") references " + STORE_TABLE + "(" + StoreContract._ID + ") on delete cascade);";
	
	private static final String REVIEW_TABLE_CREATE = "create table " + REVIEW_TABLE + " (" 
			+ ReviewContract._ID + " integer primary key, " 
			+ ReviewContract.USER_FK + " integer not null, "
			+ ReviewContract.TEXT + " text not null, "
			+ ReviewContract.STAR + " integer not null, "
			+ ReviewContract.PRODUCT_FK + " integer not null, "
			//+ "foreign key (" + ReviewContract.USER_FK + ") references " + USER_TABLE + "(" + UserContract._ID + ") on delete cascade), "
			+ "foreign key (" + ReviewContract.PRODUCT_FK + ") references " + PRODUCT_TABLE + "(" + ProductContract._ID + ") on delete cascade);";
	
	private static final String FAVORITE_TABLE_CREATE = "create table " + FAVORITE_TABLE + " (" 
			+ FavoriteContract._ID + " integer primary key, " 
			+ FavoriteContract.USER_FK + " integer not null, "
			+ FavoriteContract.PRODUCT_FK + " integer not null, "
			//+ "foreign key (" + FavoriteContract.USER_FK + ") references " + USER_TABLE + "(" + UserContract._ID + ") on delete cascade), "
			+ "foreign key (" + FavoriteContract.PRODUCT_FK + ") references " + PRODUCT_TABLE + "(" + ProductContract._ID + ") on delete cascade);";

	private static final String HISTORY_TABLE_CREATE = "create table " + HISTORY_TABLE + " (" 
			+ HistoryContract._ID + " integer primary key, " 
			+ HistoryContract.USER_FK + " integer not null, "
			+ HistoryContract.PRODUCT_FK + " integer not null, "
			//+ "foreign key (" + HistoryContract.USER_FK + ") references " + USER_TABLE + "(" + UserContract._ID + ") on delete cascade), "
			+ "foreign key (" + HistoryContract.PRODUCT_FK + ") references " + PRODUCT_TABLE + "(" + ProductContract._ID + ") on delete cascade);";
	
	private static final String PRODUCT_STORE_INDEX_CREATE = "create index ProductsStoreIndex on " + PRODUCT_TABLE + "(" + ProductContract.STORE_FK + ");";
	private static final String REVIEW_PRODUCT_INDEX_CREATE = "create index ReviewsProductIndex on " + REVIEW_TABLE + "(" + ReviewContract.PRODUCT_FK + ");";
	private static final String REVIEW_USER_INDEX_CREATE = "create index ReviewsUserIndex on " + REVIEW_TABLE + "(" + ReviewContract.USER_FK + ");";
	private static final String FAVORITE_PRODUCT_INDEX_CREATE = "create index FavoritesProductIndex on " + FAVORITE_TABLE + "(" + FavoriteContract.PRODUCT_FK + ");";
	private static final String FAVORITE_USER_INDEX_CREATE = "create index FavoritesUserIndex on " + FAVORITE_TABLE + "(" + FavoriteContract.USER_FK + ");";
	private static final String HISTORY_PRODUCT_INDEX_CREATE = "create index HistorysProductIndex on " + HISTORY_TABLE + "(" + HistoryContract.PRODUCT_FK + ");";
	private static final String HISTORY_USER_INDEX_CREATE = "create index HistorysUserIndex on " + HISTORY_TABLE + "(" + HistoryContract.USER_FK + ");";		
	
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		//uriMatcher.addURI(UserContract.AUTOHORITY, UserContract.CONTENT_PATH, ALL_ROWS);
		uriMatcher.addURI(UserContract.AUTOHORITY, UserContract.CONTENT_PATH, USER_ROW);
		uriMatcher.addURI(StoreContract.AUTOHORITY, StoreContract.CONTENT_PATH, STORE_ROW);
		uriMatcher.addURI(ProductContract.AUTOHORITY, ProductContract.CONTENT_PATH, PRODUCT_ROW);
		uriMatcher.addURI(ReviewContract.AUTOHORITY, ReviewContract.CONTENT_PATH, REVIEW_ROW);
		uriMatcher.addURI(FavoriteContract.AUTOHORITY, FavoriteContract.CONTENT_PATH, FAVORITE_ROW);
		uriMatcher.addURI(FavoriteContract.AUTOHORITY, FavoriteContract.CONTENT_PATH_ITEM, FAVORITE_ROW1);
		uriMatcher.addURI(HistoryContract.AUTOHORITY, HistoryContract.CONTENT_PATH, HISTORY_ROW);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper{

		public DatabaseHelper(Context context, String name,	CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(USER_TABLE_CREATE);
			db.execSQL(STORE_TABLE_CREATE);
			db.execSQL(PRODUCT_TABLE_CREATE);
			db.execSQL(REVIEW_TABLE_CREATE);
			db.execSQL(FAVORITE_TABLE_CREATE);
			db.execSQL(HISTORY_TABLE_CREATE);
			db.execSQL(PRODUCT_STORE_INDEX_CREATE);
			db.execSQL(REVIEW_PRODUCT_INDEX_CREATE);
			db.execSQL(REVIEW_USER_INDEX_CREATE);
			db.execSQL(FAVORITE_PRODUCT_INDEX_CREATE);
			db.execSQL(FAVORITE_USER_INDEX_CREATE);
			db.execSQL(HISTORY_PRODUCT_INDEX_CREATE);
			db.execSQL(HISTORY_USER_INDEX_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("TaskDBAdapter", "Upgrading from version " + oldVersion + " to " + newVersion);
			// Upgrade: drop the old table and create a new one.
			db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_CREATE);
			db.execSQL("DROP TABLE IF EXISTS " + STORE_TABLE_CREATE);
			db.execSQL("DROP TABLE IF EXISTS " + PRODUCT_TABLE_CREATE);
			db.execSQL("DROP TABLE IF EXISTS " + REVIEW_TABLE_CREATE);
			db.execSQL("DROP TABLE IF EXISTS " + FAVORITE_TABLE_CREATE);
			db.execSQL("DROP TABLE IF EXISTS " + HISTORY_TABLE_CREATE);
			// Create a new one.
			onCreate(db);
		}
	}
	
	@Override  
    public boolean onCreate() {
		DBHelper = new DatabaseHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION);
        return (DBHelper == null) ? false : true;
    }
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		db = DBHelper.getWritableDatabase();
		switch (uriMatcher.match(uri)) {
		case USER_ROW:
			String id3 = uri.getPathSegments().get(1);
            db.delete(USER_TABLE, UserContract._ID + "=" + id3 + (!TextUtils.isEmpty(selection) ? " and ("+selection+')':""),
            		selectionArgs);
		case STORE_ROW:
			String id2 = uri.getPathSegments().get(1);
			db.delete(STORE_TABLE, ProductContract._ID + "=" + id2 + (!TextUtils.isEmpty(selection) ? " and ("+selection+')':""),
            		selectionArgs);
            db.delete(PRODUCT_TABLE, ProductContract.STORE_FK + "=" + id2 + (!TextUtils.isEmpty(selection) ? " and ("+selection+')':""),
            		selectionArgs);
            //db.delete(REVIEW_TABLE, ReviewContract.PRODUCT_FK + "=" + id2 + (!TextUtils.isEmpty(selection) ? " and ("+selection+')':""),
            //		selectionArgs);
            return 1;
		case PRODUCT_ROW:
			String id = uri.getPathSegments().get(1);
            db.delete(PRODUCT_TABLE, ProductContract._ID + "=" + id + (!TextUtils.isEmpty(selection) ? " and ("+selection+')':""),
            		selectionArgs);
            db.delete(REVIEW_TABLE, ReviewContract.PRODUCT_FK + "=" + id + (!TextUtils.isEmpty(selection) ? " and ("+selection+')':""),
            		selectionArgs);
            return 1;
		case REVIEW_ROW:
			String id1 = uri.getPathSegments().get(1);
            db.delete(REVIEW_TABLE, ReviewContract._ID + "=" + id1 + (!TextUtils.isEmpty(selection) ? " and ("+selection+')':""),
            		selectionArgs);
            return 1;
		case FAVORITE_ROW1:
			String id4 = uri.getPathSegments().get(1);
            db.delete(FAVORITE_TABLE, FavoriteContract._ID + "=" + id4 + (!TextUtils.isEmpty(selection) ? " and ("+selection+')':""),
            		selectionArgs);
            return 1;
		case HISTORY_ROW:
			String id5 = uri.getPathSegments().get(1);
            db.delete(HISTORY_TABLE, HistoryContract._ID + "=" + id5 + (!TextUtils.isEmpty(selection) ? " and ("+selection+')':""),
            		selectionArgs);
            return 1;
		default: 
			throw new IllegalArgumentException("Unsupported URI:" + uri);	
		}
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case USER_ROW:
			return UserContract.contentItemType("user");
		case STORE_ROW:
			return	StoreContract.contentItemType("store");
		case PRODUCT_ROW:
			return ProductContract.contentItemType("product");
		case REVIEW_ROW:
			return ReviewContract.contentItemType("review");
		default: 
			throw new IllegalArgumentException("Unsupported URI:" + uri);
		}	
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		db = DBHelper.getWritableDatabase();
		long row;
		switch (uriMatcher.match(uri)) {
		case USER_ROW:
			row = db.insert(USER_TABLE, null, values);
			if (row > 0) 
			{
				Uri instanceUri = UserContract.CONTENT_URI(row);
				ContentResolver cr = getContext().getContentResolver();
				cr.notifyChange(instanceUri, null);
				return instanceUri;
			}
		case STORE_ROW:
			row = db.insert(STORE_TABLE, null, values);
			if (row > 0) 
			{
				Uri instanceUri = StoreContract.CONTENT_URI(row);
				ContentResolver cr = getContext().getContentResolver();
				cr.notifyChange(instanceUri, null);
				return instanceUri;
			}
		case PRODUCT_ROW:
			row = db.insert(PRODUCT_TABLE, null, values);
			if (row > 0) 
			{
				Uri instanceUri = ProductContract.CONTENT_URI(row);
				ContentResolver cr = getContext().getContentResolver();
				cr.notifyChange(instanceUri, null);
				return instanceUri;
			}
		case REVIEW_ROW:
			row = db.insert(REVIEW_TABLE, null, values);
			if (row > 0) 
			{
				Uri instanceUri = ReviewContract.CONTENT_URI(row);
				ContentResolver cr = getContext().getContentResolver();
				cr.notifyChange(instanceUri, null);
				return instanceUri;
			}
		case FAVORITE_ROW:
			row = db.insert(FAVORITE_TABLE, null, values);
			if (row > 0) 
			{
				Uri instanceUri = FavoriteContract.CONTENT_URI(row);
				ContentResolver cr = getContext().getContentResolver();
				cr.notifyChange(instanceUri, null);
				return instanceUri;
			}
		case HISTORY_ROW:
			row = db.insert(HISTORY_TABLE, null, values);
			if (row > 0) 
			{
				Uri instanceUri = HistoryContract.CONTENT_URI(row);
				ContentResolver cr = getContext().getContentResolver();
				cr.notifyChange(instanceUri, null);
				return instanceUri;
			}
		default:
			throw new SQLException("Insertion failed");
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		db = DBHelper.getWritableDatabase();
		switch (uriMatcher.match(uri)) {
		case USER_ROW:
			String sql1 = "SELECT * FROM Users";
			if(selection != null)
			{
				sql1 += " WHERE " + selection;
			}
			return db.rawQuery(sql1, null);
		case STORE_ROW:
			String sql2 = "SELECT * FROM Stores";
			if(selection != null)
			{
				sql2 += " WHERE " + selection;
			}
			return db.rawQuery(sql2, null);
		case PRODUCT_ROW:
			String sql3 = "SELECT * FROM Products";
			if(selection != null)
			{
				sql3 += " WHERE " + selection;
			}
			return db.rawQuery(sql3, null);
		case REVIEW_ROW:
			String sql4 = "SELECT * FROM Reviews";
			if(selection != null)
			{
				sql4 += " WHERE " + selection;
			}
			return db.rawQuery(sql4, null);
		case FAVORITE_ROW:
			String sql5 = "SELECT * FROM Favorites";
			if(selection != null)
			{
				sql5 += " WHERE " + selection;
			}
			return db.rawQuery(sql5, null);
		case HISTORY_ROW:
			String sql6 = "SELECT * FROM Historys";
			if(selection != null)
			{
				sql6 += " WHERE " + selection;
			}
			return db.rawQuery(sql6, null);
		default:
			throw new IllegalArgumentException("Unsupported URI:" + uri);
		}
		
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		db = DBHelper.getWritableDatabase();
		int count;
		switch (uriMatcher.match(uri)) {
		case USER_ROW:
			String id = uri.getPathSegments().get(1);
			count = db.update(USER_TABLE, values, UserContract._ID + "=" + id, selectionArgs);
			return count;
		case STORE_ROW:
			count = db.update(STORE_TABLE, values, selection, selectionArgs);
			return count;
		case PRODUCT_ROW:
			count = db.update(PRODUCT_TABLE, values, selection, selectionArgs);
			return count;
		case REVIEW_ROW:
			count = db.update(REVIEW_TABLE, values, selection, selectionArgs);
			return count;
		case FAVORITE_ROW:
			count = db.update(FAVORITE_TABLE, values, selection, selectionArgs);
			return count;
		case HISTORY_ROW:
			count = db.update(HISTORY_TABLE, values, selection, selectionArgs);
			return count;
		default: 
			throw new IllegalArgumentException("Unsupported URI:" + uri);	
		}
	}
}
