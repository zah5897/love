package com.zhan.app.sl.util;

import java.util.List;

import com.zhan.app.sl.bean.Image;
import com.zhan.app.sl.bean.User;

public class ImagePathUtil {

	public static String HOST_PROFIX = "http://117.143.221.190/love";
	// public static String HOST_PROFIX = "http://139.196.111.132:8080/love";

	public static void completeAvatarPath(User user, boolean thumbAndOrigin) {
		String avatar = user.getAvatar();
		if (TextUtils.isEmpty(avatar)) {
			return;
		}
		if (avatar.startsWith("http://")) {
			if (thumbAndOrigin) {
				user.setOrigin_avatar(HOST_PROFIX + ImageSaveUtils.FILE_ROOT_AVATAR_ORIGIN
						+ avatar.substring(avatar.lastIndexOf("/") + 1, avatar.length()));
			}
			return;
		}
		user.setAvatar(HOST_PROFIX + ImageSaveUtils.FILE_ROOT_AVATAR_THUMB + avatar);
		if (thumbAndOrigin) {
			user.setOrigin_avatar(HOST_PROFIX + ImageSaveUtils.FILE_ROOT_AVATAR_ORIGIN + avatar);
		}
	}

	public static void completeImagePath(List<Image> images, boolean thumbAndOrigin) {

		if (images != null && images.size() > 0) {
			for (Image image : images) {
				String shortName = image.getName();
				if (!TextUtils.isEmpty(shortName)) {
					image.setThumb(HOST_PROFIX + ImageSaveUtils.FILE_ROOT_IMAGES_THUMB + shortName);
					if (thumbAndOrigin) {
						image.setOrigin(HOST_PROFIX + ImageSaveUtils.FILE_ROOT_IMAGES_ORIGIN + shortName);
					}
				}
			}
		}
	}

	// public static void main(String args[]){
	// String url="http://139.196.111.132:8080/love/images/1234.png";
	// String r=url.substring(url.lastIndexOf("/")+1, url.length());
	// System.out.println(r);
	// }
}
