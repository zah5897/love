package com.zhan.app.sl.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.zhan.app.sl.annotation.ColumnType;
import com.zhan.app.sl.annotation.Type;

/**
 * @说明 对象操纵高级方法
 * @author cuisuqiang
 * @version 1.0
 * @since
 */
public class ObjectUtil {
	/**
	 * 返回一个对象的属性和属性值
	 */
	public static Map<String, String> getProperty(Object entityName) {
		Map<String, String> map = new HashMap<String, String>();
		Class<? extends Object> c = entityName.getClass();
		// 获得对象属性
		Field field[] = c.getDeclaredFields();
		for (Field f : field) {
			f.setAccessible(true);
			ColumnType ignore = f.getAnnotation(ColumnType.class);
			try {
				Object result;
				if (ignore != null) {
					if (ignore.value() == Type.IGNORE_PERSISTENCE) {
						continue;
					} else if (ignore.value() == Type.QUERY_OBJECT) {
						result = invokeMethod(entityName, f.getName(), null);
						if (result != null) {
							result = getGenerValue(result);
							if (result != null) {
								map.put(f.getName(), result.toString());
							}
						}
						continue;
					}
				} else {
					result = invokeMethod(entityName, f.getName(), null);
					if (result != null) {
						if(result instanceof Date){
							map.put(f.getName(), DateTimeUtil.parse((Date)result));
						}else{
							map.put(f.getName(), result.toString());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				map.put(f.getName(), "");
			}
		}

		return map;
	}

	private static Object getGenerValue(Object entityName) throws Exception {
		Class<? extends Object> c = entityName.getClass();
		// 获得对象属性
		Field field[] = c.getDeclaredFields();
		for (Field f : field) {
			f.setAccessible(true);
			ColumnType id = f.getAnnotation(ColumnType.class);
			if (id != null && id.value() == Type.IGNORE_PERSISTENCE) {
				Object result = invokeMethod(entityName, f.getName(), null);
				return result;
			}
		}
		return 0;
	}

	/**
	 * 获得对象属性的值
	 */
	private static Object invokeMethod(Object owner, String methodName, Object[] args) throws Exception {
		Class<? extends Object> ownerClass = owner.getClass();
		methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
		Method method = null;
		try {
			method = ownerClass.getMethod("get" + methodName);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
			return " can't find 'get" + methodName + "' method";
		}
		return method.invoke(owner);
	}
}
