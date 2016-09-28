package com.zhan.app.sl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.ui.ModelMap;

import com.zhan.app.sl.bean.Tag;
import com.zhan.app.sl.bean.User;
import com.zhan.app.sl.exception.ERROR;
import com.zhan.app.sl.service.UserInfoService;

public class UserDetailInfoUtil {
	public static ModelMap getDetailInfo(UserInfoService userInfoService, Long user_id, int count) {
		if (user_id == null || user_id < 1) {
			return ResultUtil.getResultMap(ERROR.ERR_PARAM, "用户ID异常");
		}
		User user = userInfoService.getUserInfo(user_id, count);
		if (user == null) {
			return ResultUtil.getResultMap(ERROR.ERR_USER_NOT_EXIST, "该用户不存在！");
		}
		Map<String, Object> secret_me = new HashMap<String, Object>();
		secret_me.put("interest", user.getInterest() != null ? user.getInterest() : new ArrayList<Tag>());
		secret_me.put("favourite_animal",
				user.getFavourite_animal() != null ? user.getFavourite_animal() : new ArrayList<Tag>());
		secret_me.put("favourite_music",
				user.getFavourite_music() != null ? user.getFavourite_music() : new ArrayList<Tag>());
		secret_me.put("weekday_todo", user.getWeekday_todo() != null ? user.getWeekday_todo() : new ArrayList<Tag>());
		secret_me.put("footsteps", user.getFootsteps() != null ? user.getFootsteps() : new ArrayList<Tag>());
		secret_me.put("want_to_where", user.getWant_to_where() != null ? user.getWant_to_where() : new String());

		Map<String, Object> userJson = new HashMap<String, Object>();
		userJson.put("about_me", user.getBasicUserInfoMap());
		userJson.put("secret_me", secret_me);
		userJson.put("my_tags", user.getMy_tags() != null ? user.getMy_tags() : new ArrayList<Tag>());
		ModelMap result = ResultUtil.getResultOKMap();
		result.put("user", userJson);
		return result;
	}
}
