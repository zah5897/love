package com.zhan.app.sl.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.zhan.app.sl.exception.AppException;
import com.zhan.app.sl.exception.ERROR;

public class WriteJsonUtil {
	public static void write(HttpServletRequest request,HttpServletResponse response, Exception ex) {
		response.setContentType("text/json");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter writer = response.getWriter();
			ERROR err;
			if (ex instanceof AppException) {
				err = ((AppException) ex).getError();
			} else {
				err = ERROR.ERR_SYS;
			}
			
			JSONObject detail=new JSONObject();
			detail.put("url", request.getRequestURI());
			detail.put("cause", ex.getMessage());
			
			JSONObject json = new JSONObject();
			json.put("code", err.getValue());
			json.put("msg", err.getErrorMsg());
			json.put("detail", detail);
			writer.write(json.toString());
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void write(HttpServletResponse response, ERROR error) {
		response.setContentType("text/json");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter writer = response.getWriter();
			JSONObject json = new JSONObject();
			json.put("code", error.ordinal());
			json.put("msg", error.getErrorMsg());
			writer.write(json.toString());
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
