package com.zhan.app.sl.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.zhan.app.sl.bean.User;
import com.zhan.app.sl.bean.mapper.SimpkleUserMapper;

@SuppressWarnings("unchecked")
@Repository("userDao")
public class UserDao extends BaseDao {
	@Resource
	private JdbcTemplate jdbcTemplate;

	public User getUser(long id) {
		List<User> list = jdbcTemplate.query("select *from t_user user where user.user_id=?", new Object[] { id },
				new SimpkleUserMapper());
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	// public int update(String nickName, String info, short sex, String avater,
	// long id) {
	// return jdbcTemplate.update("update t_user set
	// nick_name=?,info=?,sex=?,avatar=? where user_id=?",
	// new Object[] { nickName, info, sex, avater, id });
	// }

	public List<?> getList() {
		List<User> list = jdbcTemplate.query("select *from t_user user", new SimpkleUserMapper());
		return list;
	}

	public User findUserByMobile(String mobile) {
		List<User> list = jdbcTemplate.query("select *from t_user user where user.mobile=?", new Object[] { mobile },
				new SimpkleUserMapper());
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

	public Serializable insert(User user) {
		return saveObj(jdbcTemplate, "t_user", user);
	}

	public void delete(long id) {
		jdbcTemplate.update("delete from t_user where user_id=?", new Object[] { id });
	}

	public int getUserCountByMobile(String mobile) {
		int count = jdbcTemplate.queryForObject("select count(*) from t_user user where user.mobile=?",
				new String[] { mobile }, Integer.class);
		return count;
	}

	public int updateToken(long userId, String token, String _ua) {
		return jdbcTemplate.update("update t_user set token=?,_ua=? where user_id=?",
				new Object[] { token, _ua, userId });
	}

	public int updatePassword(String mobile, String password) {
		return jdbcTemplate.update("update t_user set password=? where mobile=?", new Object[] { password, mobile });
	}

	public int updateAvatar(long userId, String newAcaar) {
		return jdbcTemplate.update("update t_user set avatar=? where user_id=?", new Object[] { newAcaar, userId });
	}

	public int updateLocation(long user_id, String lat, String lng) {
		return jdbcTemplate.update("update t_user set lat=?,lng=? where user_id=?", new Object[] { lat, lng, user_id });
	}
}
