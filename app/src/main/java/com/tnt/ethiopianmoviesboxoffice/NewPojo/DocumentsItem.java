package com.tnt.ethiopianmoviesboxoffice.NewPojo;

import com.google.gson.annotations.SerializedName;

public class DocumentsItem{

	@SerializedName("createTime")
	private String createTime;

	@SerializedName("name")
	private String name;

	@SerializedName("updateTime")
	private String updateTime;

	@SerializedName("fields")
	private Fields fields;

	public String getCreateTime(){
		return createTime;
	}

	public String getName(){
		return name;
	}

	public String getUpdateTime(){
		return updateTime;
	}

	public Fields getFields(){
		return fields;
	}
}