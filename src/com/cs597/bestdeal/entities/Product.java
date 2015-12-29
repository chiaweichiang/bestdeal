package com.cs597.bestdeal.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.cs597.bestdeal.contracts.ProductContract;

public class Product implements Parcelable {
	
	public long id;
	
	public String name;
	
	public String price;
	
	public long store_fk;
	
	public long bar;
	
	public int type;
	
	public Product(String name, String price, long store_fk, long bar, int type)
	{
		this.name = name;
		this.price = price;
		this.store_fk = store_fk;
		this.bar = bar;
		this.type = type;
	}
	
	public Product(Parcel in)
	{
		id = in.readInt();
		name = in.readString();
		price = in.readString();
		store_fk = in.readLong();
		bar = in.readLong();
		type = in.readInt();
	}
	
	public Product(Cursor cursor)
	{
		this.id = ProductContract.getId(cursor);
		this.name = ProductContract.getName(cursor);
		this.price = ProductContract.getPrice(cursor);
		this.store_fk = ProductContract.getStorefk(cursor);
		this.bar = ProductContract.getBar(cursor);
		this.type = ProductContract.getType(cursor);
	}
	
	public ContentValues putValue() {
		ContentValues value = new ContentValues();
		ProductContract.putName(value, name);
		ProductContract.putPrice(value, price);
		ProductContract.putStorefk(value, store_fk);
		ProductContract.putBar(value, bar);
		ProductContract.putType(value, type);
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
		dest.writeString(name);
		dest.writeString(price);
		dest.writeLong(store_fk);
		dest.writeLong(bar);
		dest.writeInt(type);
	}
	
	public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
		public Product createFromParcel(Parcel in) {
			return new Product(in);
		}

		public Product[] newArray(int size) {
			return new Product[size];
		}
	};
}
