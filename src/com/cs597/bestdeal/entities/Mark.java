package com.cs597.bestdeal.entities;

public class Mark {
	
	public double latitude;
	public double longitude;
	public String name;
	public String address;
	
	public Mark(double latitude, double longitude, String name, String address)
	{
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.address = address;
	}
}
