package com.zhan.app.sl.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.zhan.app.sl.bean.Image;
import com.zhan.app.sl.bean.Tag;
import com.zhan.app.sl.bean.User;
import com.zhan.app.sl.cache.UserCacheService;
import com.zhan.app.sl.exception.ERROR;
import com.zhan.app.sl.service.UserInfoService;
import com.zhan.app.sl.service.UserService;
import com.zhan.app.sl.util.ImagePathUtil;
import com.zhan.app.sl.util.ImageSaveUtils;
import com.zhan.app.sl.util.ResultUtil;
import com.zhan.app.sl.util.TextUtils;

@RestController
@RequestMapping("/user")
public class UserInfoController {
	private static Logger log = Logger.getLogger(UserInfoController.class);
	@Resource
	private UserService userService;
	@Resource
	private UserInfoService userInfoService;

	@Resource
	private UserCacheService userCacheService;

	@RequestMapping("images")
	public ModelMap images(DefaultMultipartHttpServletRequest multipartRequest, long user_id, String token) {

		if (user_id < 1) {
			return ResultUtil.getResultMap(ERROR.ERR_PARAM.setNewText("用户ID异常"));
		}
		//
		if (TextUtils.isEmpty(token)) {
			return ResultUtil.getResultMap(ERROR.ERR_NO_LOGIN);
		}
		User user = userService.getUser(user_id);
		//
		if (user == null) {
			return ResultUtil.getResultMap(ERROR.ERR_USER_NOT_EXIST, "该用户不存在！");
		}
		// else if (!token.equals(user.getToken())) {
		// return ResultUtil.getResultMap(ERROR.ERR_NO_LOGIN);
		// }

		List<Image> images = null;
		if (multipartRequest != null) {
			Iterator<String> iterator = multipartRequest.getFileNames();
			images = new ArrayList<Image>();
			while (iterator.hasNext()) {
				MultipartFile file = multipartRequest.getFile((String) iterator.next());
				if (!file.isEmpty()) {
					try {

						String imageShortName = ImageSaveUtils.saveUserImages(file,
								multipartRequest.getServletContext());
						if (imageShortName != null) {
							Image image = new Image();
							image.setUser_id(user_id);
							image.setName(imageShortName);
							long id = userInfoService.saveUserImage(image);
							if (id != -1) {
								image.setId(id);
							}
							images.add(image);
						}
					} catch (Exception e) {
						e.printStackTrace();
						log.error(e.getMessage());
					}
				}
			}
		}

		if (images == null || images.size() == 0) {
			return ResultUtil.getResultMap(ERROR.ERR_FAILED);
		}
		ModelMap result = ResultUtil.getResultOKMap();

		// 补全图片url
		ImagePathUtil.completeImagePath(images, true);
		result.put("images", images);
		return result;
	}

	@RequestMapping("modify_info")
	public ModelMap modify_info(long user_id, String token, String nick_name, String age, String jobs,
			String height, String weight, String signature, String my_tags, String interest, String favourite_animal,
			String favourite_music, String weekday_todo, String footsteps, String want_to_where) {
		if (user_id < 1) {
			return ResultUtil.getResultMap(ERROR.ERR_PARAM.setNewText("用户ID异常"));
		}
		//
		if (TextUtils.isEmpty(token)) {
			return ResultUtil.getResultMap(ERROR.ERR_NO_LOGIN);
		}
		User user = userService.getUser(user_id);
		//
		if (user == null) {
			return ResultUtil.getResultMap(ERROR.ERR_USER_NOT_EXIST, "该用户不存在！");
		} else if (!token.equals(user.getToken())) {
			return ResultUtil.getResultMap(ERROR.ERR_NO_LOGIN);
		}
		boolean isNick_modify=false;
		if(user.getNick_name()!=null){
			if(!user.getNick_name().equals(nick_name)){
				isNick_modify=true;
			}
		}else if(!TextUtils.isEmpty(nick_name)){
			isNick_modify=true;
		}
		
		userInfoService.modify_info(user_id, nick_name, age, jobs, height, weight, signature, my_tags, interest,
				favourite_animal, favourite_music, weekday_todo, footsteps, want_to_where,isNick_modify);
		return detial_info(user_id,0,0);
	}

	@RequestMapping("tags")
	public ModelMap getTags(int type) {
		ModelMap result = ResultUtil.getResultOKMap();
		List<Tag> tags = userInfoService.getTagsByType(type);
		if (tags != null) {
			result.put("tags", tags);
		}
		return result;
	}

	@RequestMapping("detial_info")
	public ModelMap detial_info(long user_id_for,long last_image_id,int count) {

		if (user_id_for < 1) {
			return ResultUtil.getResultMap(ERROR.ERR_PARAM.setNewText("用户ID异常"));
		}
		User user = userInfoService.getUserInfo(user_id_for,last_image_id,count);
		//
		if (user == null) {
			return ResultUtil.getResultMap(ERROR.ERR_USER_NOT_EXIST, "该用户不存在！");
		}
		ModelMap result = ResultUtil.getResultOKMap();

		Map<String, Object> secret_me = new HashMap<String, Object>();
		secret_me.put("interest", user.getInterest()!=null?user.getInterest():new ArrayList<Tag>());
		secret_me.put("favourite_animal", user.getFavourite_animal()!=null?user.getFavourite_animal():new ArrayList<Tag>());
		secret_me.put("favourite_music", user.getFavourite_music()!=null?user.getFavourite_music():new ArrayList<Tag>());
		secret_me.put("weekday_todo", user.getWeekday_todo()!=null?user.getWeekday_todo():new ArrayList<Tag>());
		secret_me.put("footsteps", user.getFootsteps()!=null?user.getFootsteps():new ArrayList<Tag>());
		secret_me.put("want_to_where", user.getWant_to_where()!=null?user.getWant_to_where():new String());

		Map<String, Object> userJson = new HashMap<String, Object>();
		userJson.put("about_me", user.getBasicUserInfoMap());
		userJson.put("secret_me", secret_me);
		userJson.put("my_tags", user.getMy_tags() != null ? user.getMy_tags() : new ArrayList<Tag>());

		result.put("user", userJson);
		return result;
	}

	@RequestMapping("del_image")
	public ModelMap del_image(long user_id, String token, String image_id) {

		if (user_id < 1) {
			return ResultUtil.getResultMap(ERROR.ERR_PARAM.setNewText("用户ID异常"));
		}

		if (TextUtils.isEmpty(token)) {
			return ResultUtil.getResultMap(ERROR.ERR_NO_LOGIN);
		}

		if (TextUtils.isEmpty(image_id)) {
			return ResultUtil.getResultMap(ERROR.ERR_PARAM.setNewText("图片id不存在"));
		}

		User user = userService.getUser(user_id);
		//
		if (user == null) {
			return ResultUtil.getResultMap(ERROR.ERR_USER_NOT_EXIST, "该用户不存在！");
		}

		int count = userInfoService.deleteImage(user_id, image_id);

		ModelMap result = ResultUtil.getResultOKMap();
		result.put("count", count);
		return result;
	}

	@RequestMapping("update_location")
	public ModelMap update_location(long user_id, String token, String lat, String lng) {

		if (user_id < 1) {
			return ResultUtil.getResultMap(ERROR.ERR_PARAM.setNewText("用户ID异常"));
		}

		if (TextUtils.isEmpty(token)) {
			return ResultUtil.getResultMap(ERROR.ERR_NO_LOGIN);
		}

		if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lng)) {
			return ResultUtil.getResultMap(ERROR.ERR_PARAM.setNewText("位置信息异常"));
		}

		User user = userService.getUser(user_id);
		if (user == null) {
			return ResultUtil.getResultMap(ERROR.ERR_USER_NOT_EXIST, "该用户不存在！");
		}
		int count = userService.updateLocation(user_id, lat, lng);

		ModelMap result = ResultUtil.getResultOKMap();
		return result;
	}
}
