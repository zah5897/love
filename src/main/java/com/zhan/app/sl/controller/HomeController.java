package com.zhan.app.sl.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhan.app.sl.bean.Tag;
import com.zhan.app.sl.bean.User;
import com.zhan.app.sl.comm.Relationship;
import com.zhan.app.sl.exception.ERROR;
import com.zhan.app.sl.service.UserInfoService;
import com.zhan.app.sl.service.UserService;
import com.zhan.app.sl.util.ResultUtil;
import com.zhan.app.sl.util.TextUtils;

@RestController
@RequestMapping("/main")
public class HomeController {
	private static Logger log = Logger.getLogger(HomeController.class);
	@Resource
	private UserService userService;
	@Resource
	private UserInfoService userInfoService;

	@RequestMapping("found")
	public ModelMap found(long user_id, String lat, String lng, Integer count) {
		int realCount;
		if (count == null || count <= 0) {
			realCount = 5;
		} else {
			realCount = count;
		}
		List<User> users = userInfoService.getRandUsers(user_id, lat, lng, realCount);
		List<Map> resultList = new ArrayList<Map>();
		if (users != null) {
			for (User user : users) {
				Map<String, Object> userObj = new HashMap<String, Object>();
				userObj.put("user_id", user.getUser_id());
				userObj.put("nick_name", user.getNick_name());
				userObj.put("avatar", user.getAvatar());
				userObj.put("origin_avatar", user.getOrigin_avatar());
				userObj.put("disc", user.getDisc());
				userObj.put("jobs", user.getJobs());
				userObj.put("images", user.getImages());
				userInfoService.setTagByIds(user);
				userObj.put("interest", user.getInterest() != null ? user.getInterest() : new ArrayList<Tag>());
				resultList.add(userObj);
			}
		}
		ModelMap result = ResultUtil.getResultOKMap();
		result.put("users", resultList);
		return result;
	}

	@RequestMapping("fate_place")
	public ModelMap fate_place(long user_id, String token, String last_user_id, int page_size) {
		if (user_id < 0) {
			return ResultUtil.getResultMap(ERROR.ERR_USER_NOT_EXIST);
		}
		if (TextUtils.isEmpty(token)) {
			return ResultUtil.getResultMap(ERROR.ERR_NO_LOGIN);
		}

		if (page_size > 100 || page_size < 1) {
			return ResultUtil.getResultMap(ERROR.ERR_PARAM.setNewText("每页数量值为[1-100]"));
		}
		long last_user;
		if (TextUtils.isEmpty(last_user_id)) {
			last_user = 0;
		} else {
			try {
				last_user = Long.parseLong(last_user_id);
			} catch (NumberFormatException e) {
				return ResultUtil.getResultMap(ERROR.ERR_PARAM.setNewText("最后一个用户ID参数异常"));
			}
		}

		List<User> users = userInfoService.getFatePlaceUsers(user_id, last_user, page_size);
		ModelMap result = ResultUtil.getResultOKMap();
		result.put("users", users);
		return result;

	}

	@RequestMapping("like")
	public ModelMap like(long user_id, String token, String with_user_id) {

		if (user_id < 0) {
			return ResultUtil.getResultMap(ERROR.ERR_USER_NOT_EXIST);
		}
		User user=userService.getUser(user_id);
		
		if(user==null){
			return ResultUtil.getResultMap(ERROR.ERR_USER_NOT_EXIST);
		}
		
		if (TextUtils.isEmpty(token)) {
			return ResultUtil.getResultMap(ERROR.ERR_NO_LOGIN);
		}

		if (TextUtils.isEmpty(with_user_id)) {
			return ResultUtil.getResultMap(ERROR.ERR_PARAM);
		}

		String[] with_ids = with_user_id.split(",");
		for (String id : with_ids) {
			try {
				long with_user = Long.parseLong(id);
				if (user_id == with_user) {
					continue;
				}
				userInfoService.updateRelationship(user, with_user, Relationship.LIKE.ordinal());
			} catch (NumberFormatException e) {
			}
		}
		return ResultUtil.getResultOKMap();
	}

	@RequestMapping("like_each")
	public ModelMap like_each(long user_id, String token, long last_user_id, int page_size) {

		
		if(page_size>50){
			return ResultUtil.getResultMap(ERROR.ERR_PARAM.setNewText("每页数量超出限制"));
		}
		
		if (user_id < 0) {
			return ResultUtil.getResultMap(ERROR.ERR_USER_NOT_EXIST);
		}
		if (TextUtils.isEmpty(token)) {
			return ResultUtil.getResultMap(ERROR.ERR_NO_LOGIN);
		}
		List<User> users = userInfoService.getLikeEachUsers(user_id, last_user_id, page_size);
		ModelMap result = ResultUtil.getResultOKMap();
		result.put("users", users);
		return result;
	}

}
