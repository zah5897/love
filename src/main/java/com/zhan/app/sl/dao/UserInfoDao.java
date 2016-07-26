package com.zhan.app.sl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.zhan.app.sl.bean.Image;
import com.zhan.app.sl.bean.User;
import com.zhan.app.sl.bean.mapper.FateUserMapper;
import com.zhan.app.sl.bean.mapper.FoundUserMapper;
import com.zhan.app.sl.bean.mapper.SimpkleUserMapper;
import com.zhan.app.sl.comm.Relationship;
import com.zhan.app.sl.util.DateTimeUtil;

@Repository("userInfoDao")
public class UserInfoDao extends BaseDao {
	public static final String TABLE_USER_IMAGES = "t_user_images";
	@Resource
	private JdbcTemplate jdbcTemplate;

	public long svaeUserImage(long user_id, String imageName) {
		try {
			String sql = " insert into " + TABLE_USER_IMAGES + " ('user_id','name') values ( '" + user_id + "','"
					+ imageName + "') ";
			final String s = sql;
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					PreparedStatement ps = conn.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
					return ps;
				}

			}, keyHolder);
			return keyHolder.getKey().longValue();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	public long saveImage(Image image) {
		return saveObj(jdbcTemplate, TABLE_USER_IMAGES, image);
	}

	public int modify_info(long user_id, String nick_name, String birthday, String job, String height, String weight,
			String signature, String my_tags, String interests, String animals, String musics, String weekday_todo,
			String footsteps, String want_to_where) {

		String sql = "update t_user set ";
		StringBuilder names = new StringBuilder();
		List<Object> values = new ArrayList<Object>();
		if (nick_name != null) {
			names.append("nick_name=?");
			values.add(nick_name);
		}

		Date birthdayDate = DateTimeUtil.parseDate(birthday);

		if (birthdayDate != null) {
			if (values.size() > 0) {
				names.append(",birthday=?");
			} else {
				names.append("birthday=?");
			}
			values.add(birthdayDate);
		}

		// if (job != null) {
		// if (values.size() > 0) {
		// names.append(",job_ids=?");
		// } else {
		// names.append("job_ids=?");
		// }
		// values.add(job);
		// }

		appendSql(names, job, "job_ids", values);
		appendSql(names, height, "height", values);
		appendSql(names, weight, "weight", values);
		appendSql(names, signature, "signature", values);
		appendSql(names, my_tags, "my_tag_ids", values);
		appendSql(names, interests, "interest_ids", values);
		appendSql(names, animals, "animal_ids", values);
		appendSql(names, musics, "music_ids", values);
		appendSql(names, weekday_todo, "weekday_todo_ids", values);
		appendSql(names, footsteps, "footstep_ids", values);
		appendSql(names, want_to_where, "want_to_where", values);

		if (values.size() == 0) {
			return 0;
		}
		values.add(user_id);
		sql += names.toString() + " where user_id=?";

		Object[] params = values.toArray();
		return jdbcTemplate.update(sql, params);
	}

	private void appendSql(StringBuilder sql_str, String param, String sql_field_name, List<Object> values) {
		if (param != null) {
			if (values.size() > 0) {
				sql_str.append("," + sql_field_name + "=?");
			} else {
				sql_str.append(sql_field_name + "=?");
			}
			values.add(param);
		}
	}

	public User getUserInfo(long user_id) {
		List<User> list = jdbcTemplate.query("select *from t_user user where user.user_id=?", new Object[] { user_id },
				new BeanPropertyRowMapper(User.class));
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public List<Image> getUserImages(long user_id) {
		return jdbcTemplate.query(
				"select *from " + TABLE_USER_IMAGES + " user where user_id=? order by id desc limit 5",
				new Object[] { user_id }, new BeanPropertyRowMapper(Image.class));
	}

	public int deleteImage(long user_id, long id) {
		return jdbcTemplate.update("delete from " + TABLE_USER_IMAGES + " where user_id=? and id=?",
				new Object[] { user_id, id });
	}

	public List<User> getRandUsers(long user_id,String lat, String lng, int count) {
		String disc = " ROUND(6378.138*2*ASIN(SQRT(POW(SIN((?*PI()/180-lat*PI()/180)/2),2)+COS(?*PI()/180)*COS(lat*PI()/180)*POW(SIN((?*PI()/180-lng*PI()/180)/2),2)))*1000) AS juli";
		return jdbcTemplate.query(
				"select user_id,avatar, nick_name,birthday,job_ids,lat,lng,interest_ids, " + disc
						+ " from t_user where user_id<>? order by rand() limit ?",
				new Object[] { lat, lat, lng,user_id, count }, new FoundUserMapper());

	}

	public void updateRelationship(long user_id, long with_user_id, int relation_ship) {
		jdbcTemplate.update("delete from t_relationship where user_id=? and with_user_id=?",
				new Object[] { user_id, with_user_id });

		String sql = "insert into t_relationship (user_id,with_user_id,relationship) values (?,?,?)";
		jdbcTemplate.update(sql, new Object[] { user_id, with_user_id, relation_ship });
	}

	public List<User> getLikeMeUsers(long user_id, long last_user_id, int page_size) {
		return jdbcTemplate.query("select user.user_id,user.nick_name,user.avatar,user.sex  from t_user user right join t_relationship rp on user.user_id=rp.user_id where rp.with_user_id=? and relationship=? and user.user_id>? order by user.user_id  limit ?", new Object[] { user_id, Relationship.LIKE.ordinal(), last_user_id, page_size }, new FateUserMapper());
	}
	
	public List<User> getOnlyLikeMeUsers(long user_id, long last_user_id, int page_size) {
		return jdbcTemplate.query("select user.user_id,user.nick_name,user.avatar,user.sex  "
				+ "from t_user user,"
				+ "t_relationship onlyLm "
				+ "where onlyLm.with_user_id=? and "
				+ "user.user_id=onlyLm.user_id  and  "
				+ "onlyLm.relationship=? and "
				+ "onlyLm.user_id not in (select with_user_id from  t_relationship where user_id=? and relationship=?) and "
				+ "user.user_id>? "
				+ "order by user.user_id  limit ?", new Object[] { user_id, Relationship.LIKE.ordinal(),user_id,Relationship.LIKE.ordinal(), last_user_id, page_size }, new FateUserMapper());
	}
	public List<User> getLikeEachUsers(long user_id, long last_user_id, int page_size) {
		
		String sql="select friend.user_id ,user.nick_name ,user.avatar,user.sex "
				+ "from t_relationship me,"
				+ "t_relationship friend ,"
				+ "t_user user "
				+ "where me.user_id=friend.with_user_id "
				+ "and me.with_user_id=friend.user_id "
				+ "and me.relationship=? "
				+ "and me.with_user_id=user.user_id "
				+ "and me.with_user_id>? and me.user_id=? order by me.with_user_id limit ?";
		
		return jdbcTemplate.query(sql, new Object[] { Relationship.LIKE.ordinal(),last_user_id,user_id, page_size }, new FateUserMapper());
	}

	
	
	public int isLikeMe(long user_id, long with_user_id){
		return jdbcTemplate.queryForObject("select count(*) from t_relationship where user_id=? and with_user_id=? and relationship=?",   new Object[] {with_user_id,user_id,Relationship.LIKE.ordinal()}, Integer.class);
	}
	
}
