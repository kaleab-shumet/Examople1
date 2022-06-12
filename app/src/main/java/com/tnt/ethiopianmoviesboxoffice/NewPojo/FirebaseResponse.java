package com.tnt.ethiopianmoviesboxoffice.NewPojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class FirebaseResponse{

	@SerializedName("documents")
	private List<DocumentsItem> documents;

	public List<DocumentsItem> getDocuments(){
		return documents;
	}
}