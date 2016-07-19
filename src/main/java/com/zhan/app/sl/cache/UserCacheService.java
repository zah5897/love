package com.zhan.app.sl.cache;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.zhan.app.sl.bean.User;
import com.zhan.app.sl.util.RedisKeys;

/**
 * 用户信息缓存
 * 
 * @author youxifuhuaqi
 *
 */
@Service
public class UserCacheService {

	@Resource
	protected RedisTemplate<String, Serializable> redisTemplate;

	public void cacheLoginToken(User user) {
		String id = String.valueOf(user.getUser_id());
		// String json = JSON.toJSONString(user);
		redisTemplate.opsForHash().put(RedisKeys.KEY_LOGIN_TOKEN, id, user.getToken());
	}

	public void cacheValidateCode(String mobile, String code) {
		redisTemplate.opsForHash().put(RedisKeys.KEY_CODE, mobile, code);
		redisTemplate.opsForHash().put(RedisKeys.KEY_CODE_TIME, mobile,
				String.valueOf(System.currentTimeMillis() / 1000));// 精确秒级别
	}

	public long getLastCodeTime(String mobile) {
		Object time = redisTemplate.opsForHash().get(RedisKeys.KEY_CODE_TIME, mobile);
		if (time == null) {
			return 0;
		}
		return Long.parseLong(time.toString());
	}

	public boolean valideCode(String mobile, String code) {
		if (!redisTemplate.opsForHash().hasKey(RedisKeys.KEY_CODE, mobile)) {
			return false; // 不存在该手机号码发送的code
		}
		Object codeObj = redisTemplate.opsForHash().get(RedisKeys.KEY_CODE, mobile);
		if (codeObj == null) {
			return false;
		}

		return codeObj.toString().equals(code);
	}

	public void clearCode(String mobile) {
		redisTemplate.opsForHash().delete(RedisKeys.KEY_CODE, mobile);
		redisTemplate.opsForHash().delete(RedisKeys.KEY_CODE_TIME, mobile);// 精确秒级别
	}

	public String getCacheToken(long user_id) {
		Object tokenObj = redisTemplate.opsForHash().get(RedisKeys.KEY_LOGIN_TOKEN, String.valueOf(user_id));
		if (tokenObj != null) {
			return tokenObj.toString();
		}
		return null;
	}

	public void clearLoginUser(String token, long user_id) {
		String sid = String.valueOf(user_id);
		redisTemplate.opsForHash().delete(RedisKeys.KEY_LOGIN_TOKEN, sid);
		redisTemplate.opsForHash().delete(RedisKeys.KEY_LOGIN_TOKEN, sid);
	}
}
