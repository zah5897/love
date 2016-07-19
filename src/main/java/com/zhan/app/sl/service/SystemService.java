package com.zhan.app.sl.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhan.app.sl.dao.SystemDao;

@Service
@Transactional("transactionManager")
public class SystemService {
	@Resource
	private SystemDao systemDao;

	public int report(long user_id, long report_to_user_id, String report_id, String content) {
		return systemDao.report(user_id, report_to_user_id, report_id, content);
	}
}
