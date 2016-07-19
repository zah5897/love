package com.zhan.app.sl.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhan.app.sl.bean.Image;
import com.zhan.app.sl.bean.User;
import com.zhan.app.sl.dao.UserInfoDao;

@Service
@Transactional("transactionManager")
public class TagService {
	@Resource
	private UserInfoDao userInfoDao;

	public long saveUserImage(Image image) {
		return userInfoDao.saveImage(image);
	}

	public User getUserInfo(long user_id) {
		User user = userInfoDao.getUserInfo(user_id);
		if (user != null) {

		}
		return user;
	}
}
