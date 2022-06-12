package com.tnt.ethiopianmoviesboxoffice.NewPojo;

import com.google.gson.annotations.SerializedName;

public class Statistics{

	@SerializedName("mapValue")
	private MapValue mapValue;

	public MapValue getMapValue(){
		return mapValue;
	}
}