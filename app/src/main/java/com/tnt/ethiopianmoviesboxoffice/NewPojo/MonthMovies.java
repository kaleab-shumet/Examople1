package com.tnt.ethiopianmoviesboxoffice.NewPojo;

import com.google.gson.annotations.SerializedName;

public class MonthMovies{

	@SerializedName("arrayValue")
	private ArrayValue arrayValue;

	public ArrayValue getArrayValue(){
		return arrayValue;
	}
}