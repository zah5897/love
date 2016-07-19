package com.zhan.app.sl.bean.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.zhan.app.sl.bean.User;

public class FoundUserMapper implements RowMapper<User> {

	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setUser_id(rs.getLong("user_id"));
		user.setNick_name(rs.getString("nick_name"));
		user.setBirthday(rs.getDate("birthday"));
		user.setAvatar(rs.getString("avatar"));
		user.setLat(rs.getString("lat"));
		user.setLng(rs.getString("lng"));
		user.setJob_ids(rs.getString("job_ids"));
		user.setDisc(rs.getString("juli"));
		user.setInterest_ids(rs.getString("interest_ids"));
		return user;
	}

}
