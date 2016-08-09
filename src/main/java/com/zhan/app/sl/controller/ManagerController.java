package com.zhan.app.sl.controller;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhan.app.sl.service.UserInfoService;
import com.zhan.app.sl.service.UserService;

@Controller
@RequestMapping("/manager")
public class ManagerController {
	private static Logger log = Logger.getLogger(ManagerController.class);
	@Resource
	private UserService userService;
	@Resource
	private UserInfoService userInfoService;

	@RequestMapping("fwd_login")
	public String fwd_login(Long user_id, String lat, String lng, Integer count) {
		return "login";
	}

}
