package com.tnt.ethiopianmoviesboxoffice.NewPojo;

import com.google.gson.annotations.SerializedName;

public class FavoriteCount{

	@SerializedName("stringValue")
	private String stringValue;

	public String getStringValue(){
		return stringValue;
	}
}