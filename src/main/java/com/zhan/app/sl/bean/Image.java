package com.zhan.app.sl.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhan.app.sl.annotation.ColumnType;

public class Image {
	@ColumnType
	public long id; // 主键
	@JsonIgnore
	public String name; // 数据库短名称 ，json 不序列号
	@JsonIgnore
	public long user_id; // 外键，关联user id字段，不json 序列化

	@ColumnType
	private String thumb; // 无关数据库，主要json展示 缩略图
	@ColumnType
	private String origin; // 无关数据库，主要json展示 原始图

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

}
