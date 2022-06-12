package com.tnt.ethiopianmoviesboxoffice.NewPojo;

import com.google.gson.annotations.SerializedName;

public class YearMovies{

	@SerializedName("arrayValue")
	private ArrayValue arrayValue;

	public ArrayValue getArrayValue(){
		return arrayValue;
	}
}