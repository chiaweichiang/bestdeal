package com.cs597.bestdeal.entities;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.cs597.bestdeal.contracts.StoreContract;

public class Store implements Parcelable {
	
	public int id;
	
	public String name;
	
	public String address;
	
	public double latitude;
	
	public double longitude;
	
	public int type; // 0 is online, and 1 is offline.
	
	
	public Store(String name, String address, double latitude, double longitude, int type)
	{
		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.type = type;
	}
	
	public Store(Parcel in)
	{
		id = in.readInt();
		name = in.readString();
		address = in.readString();
		latitude = in.readDouble();
		longitude = in.readDouble();
		type = in.readInt();
	}
	
	public ContentValues putValue() {
		ContentValues value = new ContentValues();
		StoreContract.putName(value, name);
		StoreContract.putAddress(value, address);
		StoreContract.putLatitude(value, latitude);
		StoreContract.putLongitude(value, longitude);
		StoreContract.putType(value, type);
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
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(address);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		dest.writeInt(type);
	}
	
	public static final Parcelable.Creator<Store> CREATOR = new Parcelable.Creator<Store>() {
		public Store createFromParcel(Parcel in) {
			return new Store(in);
		}

		public Store[] newArray(int size) {
			return new Store[size];
		}
	};
}
