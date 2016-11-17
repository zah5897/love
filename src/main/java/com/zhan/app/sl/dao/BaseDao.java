package com.zhan.app.sl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.zhan.app.sl.util.ObjectUtil;

public class BaseDao {

	protected long saveObj(JdbcTemplate jdbcTemplate, String tableName, Object obj) {
		try {
			String sql = " insert into " + tableName + " (";
			Map<String, String> map = ObjectUtil.getProperty(obj);

			String filedStr = "";
			Set<String> set = map.keySet();
			for (String key : set) {
				filedStr += (key + ",");
			}
			filedStr = filedStr.substring(0, filedStr.length() - 1);
			filedStr += " ) ";

			String values = " values ( ";
			for (String key : set) {
				values += ("'" + map.get(key) + "',");
			}
			values = values.substring(0, values.length() - 1);
			values += " ) ";

			sql += (filedStr + values);

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

	// protected int updateObj(JdbcTemplate jt, String tableName, Object obj) {
	// int re = 0;
	// try {
	//
	// //update t_person set name=? where id=?"
	//
	// String sql = " update " + tableName + " set ";
	// Map<String, String> map = ObjectUtil.getProperty(obj);
	// Set<String> set = map.keySet();
	// for (String key : set) {
	// sql += (key + ",");
	// }
	// sql += " tableName ) ";
	// sql += " values ( ";
	// for (String key : set) {
	// sql += ("'" + map.get(key) + "',");
	// }
	// sql += ("'" + tableName + "' ) ");
	// re = jt.update(sql);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return re;
	// }
}
