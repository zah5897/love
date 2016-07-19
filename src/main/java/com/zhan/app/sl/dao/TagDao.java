package com.zhan.app.sl.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.zhan.app.sl.bean.Tag;

@Repository("tagDao")
public class TagDao extends BaseDao {
	public static final String TABLE_USER_IMAGES = "t_sys_tag";
	@Resource
	private JdbcTemplate jdbcTemplate;

	public List<Tag> getTagsByType(int type) {
		return jdbcTemplate.query("select *from " + TABLE_USER_IMAGES + " where type=?", new Object[] { type },
				new BeanPropertyRowMapper(Tag.class));
	}

	public List<Tag> getTags() {
		return jdbcTemplate.query("select *from " + TABLE_USER_IMAGES, new Object[] {},
				new BeanPropertyRowMapper(Tag.class));
	}

	// @SuppressWarnings({ "unchecked", "rawtypes" })
	// public List<Funny> list(long lastID, int pageSize) {
	// List<Funny> list = jdbcTemplate.query(
	// "select funny.*,user.name,user.info,user.avatar " + "from t_funny funny "
	// + "left join t_user user "
	// + "on funny.publisher=user.id " + "and funny.id<? order by funny.id desc
	// " + "limit ?",
	// new Object[] { lastID, pageSize }, new BeanPropertyRowMapper(Funny.class)
	// {
	// @Override
	// protected void initBeanWrapper(BeanWrapper bw) {
	// super.initBeanWrapper(bw);
	// bw.registerCustomEditor(User.class, new UserSupport());
	// }
	// });
	// return list;
	// }

}
