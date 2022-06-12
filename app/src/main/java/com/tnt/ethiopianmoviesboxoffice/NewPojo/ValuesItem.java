package com.tnt.ethiopianmoviesboxoffice.NewPojo;

import com.google.gson.annotations.SerializedName;

public class ValuesItem{

	@SerializedName("mapValue")
	private MapValue mapValue;

	public MapValue getMapValue(){
		return mapValue;
	}
}