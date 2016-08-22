package com.zhan.app.sl.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import com.zhan.app.sl.util.UserDetailInfoUtil;

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

	/**
	 * 上传图片
	 * 
	 * @param multipartRequest
	 * @param user_id
	 * @param token
	 * @return
	 */
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

	/**
	 * 修改信息
	 * 
	 * @param user_id
	 * @param token
	 * @param nick_name
	 * @param age
	 * @param jobs
	 * @param height
	 * @param weight
	 * @param signature
	 * @param my_tags
	 * @param interest
	 * @param favourite_animal
	 * @param favourite_music
	 * @param weekday_todo
	 * @param footsteps
	 * @param want_to_where
	 * @return
	 */
	@RequestMapping("modify_info")
	public ModelMap modify_info(long user_id, String token, String nick_name, String age, String jobs, String height,
			String weight, String signature, String my_tags, String interest, String favourite_animal,
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
		boolean isNick_modify = false;
		if (user.getNick_name() != null) {
			if (!user.getNick_name().equals(nick_name)) {
				isNick_modify = true;
			}
		} else if (!TextUtils.isEmpty(nick_name)) {
			isNick_modify = true;
		}

		userInfoService.modify_info(user_id, nick_name, age, jobs, height, weight, signature, my_tags, interest,
				favourite_animal, favourite_music, weekday_todo, footsteps, want_to_where, isNick_modify);
		return detial_info(user_id, null, null);
	}

	/**
	 * 获取系统标签
	 * 
	 * @param type
	 * @return
	 */
	@RequestMapping("tags")
	public ModelMap getTags(int type) {
		ModelMap result = ResultUtil.getResultOKMap();
		List<Tag> tags = userInfoService.getTagsByType(type);
		if (tags != null) {
			result.put("tags", tags);
		}
		return result;
	}

	/**
	 * 获取自己的详情或者别人的详细信息
	 * 
	 * @param user_id
	 * @param user_id_for
	 * @param count
	 * @return
	 */
	@RequestMapping("detial_info")
	public ModelMap detial_info(Long user_id, Long user_id_for, Integer count) {
		if (user_id_for != null && user_id_for > 0) {
			user_id = user_id_for;
		}
		if (count == null || count <= 0) {
			count = 4;
		}
		return UserDetailInfoUtil.getDetailInfo(userInfoService, user_id, count);
	}

	/**
	 * 获取用户图片列表
	 * 
	 * @param user_id
	 * @param last_image_id
	 * @param count
	 * @return
	 */
	@RequestMapping("list_image")
	public ModelMap list_image(Long user_id, Long last_image_id, Integer count) {
		if (user_id == null || user_id < 1) {
			return ResultUtil.getResultMap(ERROR.ERR_PARAM.setNewText("用户ID异常"));
		}

		if (last_image_id == null || last_image_id < 0) {
			last_image_id = 0l;
		}
		if (count == null || count <= 0) {
			count = 5;
		}
		List<Image> images = userInfoService.getUserImages(user_id, last_image_id, count);
		ModelMap result = ResultUtil.getResultOKMap();
		result.put("images", images);
		return result;
	}

	/**
	 * 删除图片
	 * 
	 * @param user_id
	 * @param token
	 * @param image_id
	 * @return
	 */
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

	/**
	 * 更新位置信息
	 */
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
