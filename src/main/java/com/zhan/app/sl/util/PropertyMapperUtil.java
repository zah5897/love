package com.zhan.app.sl.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zhan.app.sl.annotation.ColumnType;
import com.zhan.app.sl.annotation.Type;

public class PropertyMapperUtil {

	public static <T> Object prase(Class<T> clazz, ResultSet rs) throws Exception {
		// 获得对象属性
		Field field[] = clazz.getDeclaredFields();
		Object bean = clazz.newInstance();
		for (Field f : field) {
			f.setAccessible(true);
			ColumnType ignore = f.getAnnotation(ColumnType.class);
			if (ignore != null && ignore.value() == Type.QUERY_OBJECT) {
			} else {
				Object result = invokeMethod(bean, f, rs);
			}
		}
		return bean;
	}

	private static Object invokeMethod(Object object, Field field, ResultSet rs) {
		Class<? extends Object> objClass = object.getClass();

		String fieldName = field.getName();

		String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		Method method = null;
		Object result = null;
		try {
			method = objClass.getMethod("set" + methodName, new Class<?>[] { getFieldType(field) });
			Object argValue = valueToType(field, rs);
			result = method.invoke(object, argValue);
		} catch (SecurityException e) {
			return " can't find 'get" + methodName + "' method";
		} catch (NoSuchMethodException e) {
			return " can't find 'get" + methodName + "' method";
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private static Class<?> getFieldType(Field field) {
		return field.getType();
	}

	private static Object valueToType(Field field, ResultSet rs) throws SQLException {
		Class<?> t = getFieldType(field);
		String clazzType = t.toString();
		String name = field.getName();
		if ("long".equals(clazzType)) {
			return rs.getLong(name);
		} else if (String.class.toString().equals(clazzType)) {
			return rs.getString(name);
		} else if ("int".equals(clazzType)) {
			return rs.getInt(name);
		} else if ("short".equals(clazzType)) {
			return rs.getShort(name);
		}
		return rs.getObject(name);
	}

}
