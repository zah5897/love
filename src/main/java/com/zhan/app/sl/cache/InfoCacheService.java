package com.zhan.app.sl.cache;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zhan.app.sl.bean.Tag;

/**
 * 用户信息缓存
 * 
 * @author youxifuhuaqi
 *
 */
@Service
public class InfoCacheService {
	@Resource
	protected RedisTemplate<String, Serializable> redisTemplate;

	public List<Tag> getTagsByKey(String key) {
		Object obj = redisTemplate.opsForValue().get(key);
		if (obj != null) {
			List<Tag> tags = JSON.parseArray(obj.toString(), Tag.class);
			return tags;
		}
		return null;
	}

	public void setTagsByKey(String key, List<Tag> tags) {
		if (tags != null) {
			redisTemplate.opsForValue().set(key, JSON.toJSONString(tags));
		}
	}
}
