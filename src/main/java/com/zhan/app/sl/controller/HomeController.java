package com.zhan.app.sl.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhan.app.sl.bean.Tag;
import com.zhan.app.sl.bean.User;
import com.zhan.app.sl.comm.Relationship;
import com.zhan.app.sl.comm.UserType;
import com.zhan.app.sl.exception.ERROR;
import com.zhan.app.sl.service.UserInfoService;
import com.zhan.app.sl.service.UserService;
import com.zhan.app.sl.util.ResultUtil;
import com.zhan.app.sl.util.TextUtils;

@RestController
@RequestMapping("/main")
public class HomeController {
	@Resource
	private UserService userService;
	@Resource
	private UserInfoService userInfoService;

	/**
	 * 发现
	 * 
	 * @param user_id
	 * @param lat
	 * @param lng
	 * @param count
	 * @return
	 */
	@RequestMapping("found")
	public ModelMap found(long user_id, String lat, String lng, Integer count) {
		int realCount;
		if (count == null || count <= 0) {
			realCount = 5;
		} else {
			realCount = count;
		}
		List<User> users = userInfoService.getRandUsers(user_id, lat, lng, realCount);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
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
				userObj.put("birthday", user.getBirthday());
				userInfoService.setTagByIds(user);
				userObj.put("interest", user.getInterest() != null ? user.getInterest() : new ArrayList<Tag>());
				resultList.add(userObj);
			}
		}
		ModelMap result = ResultUtil.getResultOKMap();
		result.put("users", resultList);
		return result;
	}

	/**
	 * 缘分场
	 * 
	 * @param user_id
	 * @param token
	 * @param last_user_id
	 * @param page_size
	 * @return
	 */
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

	/**
	 * 喜欢某人
	 * 
	 * @param user_id
	 * @param token
	 * @param with_user_id
	 *            被喜欢的某人
	 * @return
	 */
	@RequestMapping("like")
	public ModelMap like(long user_id, String token, String with_user_id) {

		if (user_id < 0) {
			return ResultUtil.getResultMap(ERROR.ERR_USER_NOT_EXIST);
		}
		User user = userService.getUser(user_id);

		if (user == null) {
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
				User withUser = userService.getUser(with_user);
				if (user_id == with_user || withUser == null) {
					continue;
				}
				userInfoService.updateRelationship(user, withUser, Relationship.LIKE.ordinal());
			} catch (NumberFormatException e) {
			}
		}
		return ResultUtil.getResultOKMap();
	}

	/**
	 * 点击 X 忽略
	 * 
	 * @param user_id
	 * @param token
	 * @param with_user_id
	 *            被忽略的用户id
	 * @return
	 */
	@RequestMapping("ignore")
	public ModelMap ignore(long user_id, String token, String with_user_id) {

		if (user_id < 0) {
			return ResultUtil.getResultMap(ERROR.ERR_USER_NOT_EXIST);
		}
		User user = userService.getUser(user_id);

		if (user == null) {
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
				userInfoService.updateRelationshipNOHX(user, with_user, Relationship.IGNORE.ordinal());
			} catch (NumberFormatException e) {
			}
		}
		return ResultUtil.getResultOKMap();
	}

	/**
	 * 获取互相喜欢的用户
	 * 
	 * @param user_id
	 *            当前登陆用户
	 * @param token
	 *            登陆token
	 * @param last_user_id
	 *            分页 最后一条用户id
	 * @param page_size
	 *            分页参数
	 * @return
	 */
	@RequestMapping("like_each")
	public ModelMap get_like_each(long user_id, String token, long last_user_id, int page_size) {

		if (page_size > 50) {
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

	/**
	 * 随便看看（ 游客接口）,具有注册功能
	 * @param deviceId
	 * @param deviceToken
	 * @param zh_cn
	 * @param lat
	 * @param lng
	 * @param count
	 * @return
	 */
	@RequestMapping("look_around")
	public ModelMap look_around(String deviceId, String deviceToken, String zh_cn, String lat, String lng,
			Integer count) {

		if (!TextUtils.isEmpty(zh_cn)) {
			if (zh_cn.length() > 2) {
				return ResultUtil.getResultMap(ERROR.ERR_PARAM.setNewText("zh-cn has too long,max &lt 2"));
			}
		}

		if (TextUtils.isEmpty(deviceId)) {
			return ResultUtil.getResultMap(ERROR.ERR_PARAM.setNewText("deviceId is empty"));
		}

		User user = userService.findUserByMobile(deviceId);
		if (user == null) {

			user = new User();
			user.setMobile(deviceId);
			user.setDevice_token(deviceToken);
			user.setLat(lat);
			user.setLng(lng);
			user.setZh_cn(zh_cn);
			user.setType((short) UserType.VISITOR.ordinal());
			userService.insertUser(user);
		} else {
			userService.updateVisitor(user.getUser_id(), deviceToken, lat, lng, zh_cn);
		}

		int realCount;
		if (count == null || count <= 0) {
			realCount = 5;
		} else {
			realCount = count;
		}
		List<User> users = userInfoService.getRandUsers(0, lat, lng, realCount);
		ModelMap result = ResultUtil.getResultOKMap();
		result.put("users", users);
		result.put("user", user);
		return result;
	}
}
