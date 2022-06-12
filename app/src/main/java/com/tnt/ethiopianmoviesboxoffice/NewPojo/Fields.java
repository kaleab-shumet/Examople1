package com.tnt.ethiopianmoviesboxoffice.NewPojo;

import com.google.gson.annotations.SerializedName;

public class Fields{

	@SerializedName("yearMovies")
	private YearMovies yearMovies;

	@SerializedName("createdTime")
	private CreatedTime createdTime;

	@SerializedName("monthMovies")
	private MonthMovies monthMovies;

	@SerializedName("uuid")
	private Uuid uuid;

	@SerializedName("publishTime")
	private PublishTime publishTime;

	@SerializedName("imageUrl")
	private ImageUrl imageUrl;

	@SerializedName("videoId")
	private VideoId videoId;

	@SerializedName("title")
	private Title title;

	@SerializedName("statistics")
	private Statistics statistics;

	@SerializedName("thumbnailUrl")
	private ThumbnailUrl thumbnailUrl;

	@SerializedName("likeCount")
	private LikeCount likeCount;

	@SerializedName("viewCount")
	private ViewCount viewCount;

	@SerializedName("favoriteCount")
	private FavoriteCount favoriteCount;

	@SerializedName("commentCount")
	private CommentCount commentCount;

	public YearMovies getYearMovies(){
		return yearMovies;
	}

	public CreatedTime getCreatedTime(){
		return createdTime;
	}

	public MonthMovies getMonthMovies(){
		return monthMovies;
	}

	public Uuid getUuid(){
		return uuid;
	}

	public PublishTime getPublishTime(){
		return publishTime;
	}

	public ImageUrl getImageUrl(){
		return imageUrl;
	}

	public VideoId getVideoId(){
		return videoId;
	}

	public Title getTitle(){
		return title;
	}

	public Statistics getStatistics(){
		return statistics;
	}

	public ThumbnailUrl getThumbnailUrl(){
		return thumbnailUrl;
	}

	public LikeCount getLikeCount(){
		return likeCount;
	}

	public ViewCount getViewCount(){
		return viewCount;
	}

	public FavoriteCount getFavoriteCount(){
		return favoriteCount;
	}

	public CommentCount getCommentCount(){
		return commentCount;
	}
}