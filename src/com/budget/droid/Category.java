package com.budget.droid;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable{
	private int id;
	private String name;

	public Category() {
	}

	public Category(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Parcelable.Creator<Category> CREATOR = new Creator<Category>() {
		
		@Override
		public Category[] newArray(int size) {
			return new Category[size];
		}
		
		@Override
		public Category createFromParcel(Parcel source) {			
			return new Category(source.readInt(), source.readString());
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
