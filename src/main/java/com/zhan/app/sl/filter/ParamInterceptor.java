package com.zhan.app.sl.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.zhan.app.sl.exception.ERROR;
import com.zhan.app.sl.util.TextUtils;
import com.zhan.app.sl.util.WriteJsonUtil;

public class ParamInterceptor implements HandlerInterceptor {

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String _ua = request.getParameter("_ua");
		if (TextUtils.isEmpty(_ua) || _ua.length() < 10) {
			WriteJsonUtil.write(response, ERROR.ERR_NO_AGREE);
			return false;
		}
		return true;
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

}
