package com.tnt.ethiopianmoviesboxoffice.NewPojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ArrayValue{

	@SerializedName("values")
	private List<ValuesItem> values;

	public List<ValuesItem> getValues(){
		return values;
	}
}