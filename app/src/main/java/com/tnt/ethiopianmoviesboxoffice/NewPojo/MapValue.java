package com.tnt.ethiopianmoviesboxoffice.NewPojo;

import com.google.gson.annotations.SerializedName;

public class MapValue{

	@SerializedName("fields")
	private Fields fields;

	public Fields getFields(){
		return fields;
	}
}