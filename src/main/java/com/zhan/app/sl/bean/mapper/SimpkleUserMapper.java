package com.zhan.app.sl.bean.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.zhan.app.sl.bean.User;

public class SimpkleUserMapper implements RowMapper<User> {

	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setUser_id(rs.getLong("user_id"));
		user.setMobile(rs.getString("mobile"));
		user.setPassword(rs.getString("password"));
		user.setName(rs.getString("name"));
		user.setNick_name(rs.getString("nick_name"));
		user.setBirthday(rs.getDate("birthday"));
		user.setSex(rs.getString("sex"));
		user.setAvatar(rs.getString("avatar"));
		user.setSignature(rs.getString("signature"));
		user.setToken(rs.getString("token"));
		user.set_ua(rs.getString("_ua"));
		user.setLat(rs.getString("lat"));
		user.setLng(rs.getString("lng"));
		return user;
	}

	// public Funny mapRow(ResultSet rs, int rowNum) throws SQLException {
	// // Funny user = new User();
	// // user.setId(rs.getInt("id"));
	// // user.setUsername(rs.getString("username"));
	// // user.setPassword(rs.getString("password"));
	//
	// Funny f = null;
	// try {
	// f = (Funny) PropertyMapperUtil.prase(Funny.class, rs);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return f;
	// }

}
