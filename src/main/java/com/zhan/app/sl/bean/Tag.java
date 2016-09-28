package com.zhan.app.sl.bean;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhan.app.sl.annotation.ColumnType;

public class Tag implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int TYPE_JOB = 1;
	public static final int TYPE_TAG = 2;
	public static final int TYPE_INTEREST = 3;
	public static final int TYPE_LIKE_ANIMAL = 4;
	public static final int TYPE_LIKE_MUSIC = 5;
	public static final int TYPE_WEEKDAY = 6;
	public static final int TYPE_FOOTSTEPS = 7;
	// 系统主键 （不入库，不json序列化）
	@JsonIgnore
	@ColumnType
	private int sys_id;
	private int id;
	private String name;
	private int type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		Tag t = (Tag) obj;
		return id == t.getId();
	}
}
