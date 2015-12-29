package com.cs597.bestdeal.entities;

import com.cs597.bestdeal.contracts.FavoriteContract;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class Favorite implements Parcelable {
	
	public long id;
	
	public long user_fk;
	
	public long product_fk;
	
	
	public Favorite(long id2, long id3)
	{
		this.user_fk = id2;
		this.product_fk = id3;
	}
	
	public Favorite(Parcel in)
	{
		id = in.readLong();
		user_fk = in.readLong();
		product_fk = in.readLong();
	}
	
	public Favorite(Cursor cursor) {
		this.id = FavoriteContract.getId(cursor);
		this.user_fk = FavoriteContract.getUserFk(cursor);
		this.product_fk = FavoriteContract.getProductFk(cursor);
	}
	
	
	public ContentValues putValue() {
		ContentValues value = new ContentValues();
		FavoriteContract.putUserFk(value, user_fk);
		FavoriteContract.putProductFk(value, product_fk);
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
		dest.writeLong(user_fk);
		dest.writeLong(product_fk);
	}
	
	public static final Parcelable.Creator<Favorite> CREATOR = new Parcelable.Creator<Favorite>() {
		public Favorite createFromParcel(Parcel in) {
			return new Favorite(in);
		}

		public Favorite[] newArray(int size) {
			return new Favorite[size];
		}
	};
}
