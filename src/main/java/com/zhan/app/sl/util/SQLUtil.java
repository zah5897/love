package com.zhan.app.sl.util;

import java.util.List;

public class SQLUtil {
	public static void appendSql(StringBuilder sql_str, String param, String sql_field_name, List<Object> values) {
		if (param != null) {
			if (values.size() > 0) {
				sql_str.append("," + sql_field_name + "=?");
			} else {
				sql_str.append(sql_field_name + "=?");
			}
			values.add(param);
		}
	}
}
