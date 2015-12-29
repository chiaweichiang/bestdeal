package com.cs597.bestdeal.entities;

import com.cs597.bestdeal.contracts.HistoryContract;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class History implements Parcelable {
	
	public long id;
	
	public long user_fk;
	
	public long product_fk;
	
	
	public History(int user_fk, int product_fk)
	{
		this.user_fk = user_fk;
		this.product_fk = product_fk;
	}
	
	public History(Parcel in)
	{
		id = in.readInt();
		user_fk = in.readInt();
		product_fk = in.readInt();
	}
	
	public History(Cursor cursor) {
		this.id = HistoryContract.getId(cursor);
		this.user_fk = HistoryContract.getUserFk(cursor);
		this.product_fk = HistoryContract.getProductFk(cursor);
	}
	
	
	public ContentValues putValue() {
		ContentValues value = new ContentValues();
		HistoryContract.putUserFk(value, user_fk);
		HistoryContract.putProductFk(value, product_fk);
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
	
	public static final Parcelable.Creator<History> CREATOR = new Parcelable.Creator<History>() {
		public History createFromParcel(Parcel in) {
			return new History(in);
		}

		public History[] newArray(int size) {
			return new History[size];
		}
	};
}
