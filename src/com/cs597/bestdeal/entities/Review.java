package com.cs597.bestdeal.entities;

import com.cs597.bestdeal.contracts.ReviewContract;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
	
	public long id;
	
	public long user_fk;
	
	public String text;
	
	public int star;
	
	public long product_fk;
	
	
	public Review(long user_fk, String text, int star, long product_fk)
	{
		this.user_fk = user_fk;
		this.text = text;
		this.star = star;
		this.product_fk = product_fk;
	}
	
	public Review(Parcel in)
	{
		id = in.readInt();
		user_fk = in.readInt();
		text = in.readString();
		star = in.readInt();
		product_fk = in.readInt();
	}
	
	public Review(Cursor cursor) {
		this.id = ReviewContract.getId(cursor);
		this.user_fk = ReviewContract.getUserFk(cursor);
		this.text = ReviewContract.getText(cursor);
		this.star = ReviewContract.getStar(cursor);
		this.product_fk = ReviewContract.getProductFk(cursor);
	}
	
	public ContentValues putValue() {
		ContentValues value = new ContentValues();
		ReviewContract.putUserFk(value, user_fk);
		ReviewContract.putText(value, text);
		ReviewContract.putStar(value, star);
		ReviewContract.putProductFk(value, product_fk);
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
		dest.writeString(text);
		dest.writeInt(star);
		dest.writeLong(product_fk);
	}
	
	public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
		public Review createFromParcel(Parcel in) {
			return new Review(in);
		}

		public Review[] newArray(int size) {
			return new Review[size];
		}
	};
}
