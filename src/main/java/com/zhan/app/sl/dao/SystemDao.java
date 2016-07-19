package com.zhan.app.sl.dao;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("systemDao")
public class SystemDao extends BaseDao {
	@Resource
	private JdbcTemplate jdbcTemplate;

	public int report(long user_id, long report_to_user_id, String report_tag_id, String content) {

		int count=jdbcTemplate.update("insert into t_report_record (user_id,report_to_user_id,report_tag_id,content) values(?,?,?,?)",
				new Object[] { user_id, report_to_user_id, report_tag_id, content }, new int[] { java.sql.Types.INTEGER,
						java.sql.Types.INTEGER, java.sql.Types.VARCHAR, java.sql.Types.VARCHAR });
		return count;
	}

}
