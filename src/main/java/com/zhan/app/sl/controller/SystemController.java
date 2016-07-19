package com.zhan.app.sl.controller;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhan.app.sl.bean.User;
import com.zhan.app.sl.exception.ERROR;
import com.zhan.app.sl.service.SystemService;
import com.zhan.app.sl.service.UserService;
import com.zhan.app.sl.util.ResultUtil;
import com.zhan.app.sl.util.TextUtils;

@RestController
@RequestMapping("/system")
public class SystemController {
	private static Logger log = Logger.getLogger(SystemController.class);
	@Resource
	private UserService userService;
	@Resource
	private SystemService systemService;

	@RequestMapping("report")
	public ModelMap report(long user_id, String token, long report_to_user_id, String report_tag_id, String content) {

		if (user_id < 1) {
			return ResultUtil.getResultMap(ERROR.ERR_PARAM.setNewText("用户ID异常"));
		}
		if (report_to_user_id < 1) {
			return ResultUtil.getResultMap(ERROR.ERR_PARAM.setNewText("被举报用户ID异常"));
		}

		if (user_id == report_to_user_id) {
			return ResultUtil.getResultMap(ERROR.ERR_PARAM.setNewText("不能自己举报自己!"));
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
		int count = systemService.report(user_id, report_to_user_id, report_tag_id, content);
		return ResultUtil.getResultOKMap();
	}

	@RequestMapping("prootl")
	public String prootl() {
		return "prootl";
	}

}
