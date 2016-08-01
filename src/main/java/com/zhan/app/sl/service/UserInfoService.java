package com.zhan.app.sl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easemob.server.example.Main;
import com.zhan.app.sl.bean.Image;
import com.zhan.app.sl.bean.Tag;
import com.zhan.app.sl.bean.User;
import com.zhan.app.sl.cache.InfoCacheService;
import com.zhan.app.sl.comm.Relationship;
import com.zhan.app.sl.controller.UserInfoController;
import com.zhan.app.sl.dao.TagDao;
import com.zhan.app.sl.dao.UserInfoDao;
import com.zhan.app.sl.util.ImagePathUtil;
import com.zhan.app.sl.util.RedisKeys;
import com.zhan.app.sl.util.TextUtils;

@Service
@Transactional("transactionManager")
public class UserInfoService {
	@Resource
	private UserInfoDao userInfoDao;
	@Resource
	private InfoCacheService infoCacheService;
	@Resource
	private TagDao tagDao;

	private static Logger log = Logger.getLogger(UserInfoController.class);

	public long saveUserImage(Image image) {
		return userInfoDao.saveImage(image);
	}

	public int modify_info(long user_id, String nick_name, String birthday, String job, String height, String weight,
			String signature, String my_tags, String interests, String animals, String musics, String weekday_todo,
			String footsteps, String want_to_where, boolean isNick_modify) {

		if (isNick_modify && !TextUtils.isEmpty(nick_name)) {
			try {
				Main.updateNickName(String.valueOf(user_id), nick_name);
			} catch (Exception e) {
				log.error(e);
			}
		}
		return userInfoDao.modify_info(user_id, nick_name, birthday, job, height, weight, signature, my_tags, interests,
				animals, musics, weekday_todo, footsteps, want_to_where);
	}

	public List<Tag> getTagsByType(int type) {
		return tagDao.getTagsByType(type);
	}

	public User getUserInfo(long user_id) {
		User user = userInfoDao.getUserInfo(user_id);
		if (user != null) {

			ImagePathUtil.completeAvatarPath(user, true); // 补全图片链接地址
			// 隐藏系统安全信息
			user.hideSysInfo();
			// 补全 tag 属性
			setTagByIds(user);
			// 补全images属性
			List<Image> userImages = userInfoDao.getUserImages(user_id);
			ImagePathUtil.completeImagePath(userImages, true); // 补全图片路径
			user.setImages(userImages);
		}
		return user;
	}

	public int deleteImage(long user_id, String image_id) {
		String[] ids = image_id.split(",");
		// 执行成功的个数
		int count = 0;
		for (String str_id : ids) {
			try {
				long id = Long.parseLong(str_id);
				count += userInfoDao.deleteImage(user_id, id);
			} catch (NumberFormatException e) {
				log.error(e.getMessage());
			}
		}
		return count;
	}

	public List<User> getRandUsers(long user_id, String lat, String lng, int count) {
		List<User> randomUsers = userInfoDao.getRandUsers(user_id, lat, lng, count);
		if (randomUsers != null) {
			for (User user : randomUsers) {
				ImagePathUtil.completeAvatarPath(user, true); // 补全图片链接地址
				List<Image> userImages = userInfoDao.getUserImages(user.getUser_id());
				ImagePathUtil.completeImagePath(userImages, true); // 补全图片路径
				user.setImages(userImages);
				setTagByIds(user);
			}
		}
		return randomUsers;
	}

	public void setTagByIds(User user) {

		String ids[];
		List<Tag> tags = infoCacheService.getTagsByKey(RedisKeys.KEY_TAG);
		if (tags == null) {
			tags = tagDao.getTags();
			infoCacheService.setTagsByKey(RedisKeys.KEY_TAG, tags);
		}
		if (tags == null || tags.size() == 0) {
			return;
		}

		// 补全 职属性
		if (!TextUtils.isEmpty(user.getJob_ids())) {
			ids = user.getJob_ids().split(",");
			List<Tag> jobs = new ArrayList<Tag>();
			for (String id : ids) {
				try {
					int tag_id = Integer.parseInt(id);
					for (Tag tag : tags) {
						if (tag.getType() == Tag.TYPE_JOB && tag.getId() == tag_id) {
							jobs.add(tag);
						}
					}
				} catch (NumberFormatException e) {
					continue;
				}
			}
			user.setJobs(jobs);
		}
		// 补全我的标签
		if (!TextUtils.isEmpty(user.getMy_tag_ids())) {
			ids = user.getMy_tag_ids().split(",");
			List<Tag> myTags = new ArrayList<Tag>();
			for (String id : ids) {
				try {
					int tag_id = Integer.parseInt(id);
					for (Tag tag : tags) {
						if (tag.getType() == Tag.TYPE_TAG && tag.getId() == tag_id) {
							myTags.add(tag);
						}
					}
				} catch (NumberFormatException e) {
					continue;
				}
			}
			user.setMy_tags(myTags);
		}
		// 补全我的兴趣爱好
		if (!TextUtils.isEmpty(user.getInterest_ids())) {
			ids = user.getInterest_ids().split(",");
			List<Tag> myInterest = new ArrayList<Tag>();
			for (String id : ids) {
				try {
					int tag_id = Integer.parseInt(id);
					for (Tag tag : tags) {
						if (tag.getType() == Tag.TYPE_INTEREST && tag.getId() == tag_id) {
							myInterest.add(tag);
						}
					}
				} catch (NumberFormatException e) {
					continue;
				}
			}
			user.setInterest(myInterest);
		}
		// 补全我喜欢的动物
		if (!TextUtils.isEmpty(user.getAnimal_ids())) {
			ids = user.getAnimal_ids().split(",");
			List<Tag> myAnimal = new ArrayList<Tag>();
			for (String id : ids) {
				try {
					int tag_id = Integer.parseInt(id);
					for (Tag tag : tags) {
						if (tag.getType() == Tag.TYPE_LIKE_ANIMAL && tag.getId() == tag_id) {
							myAnimal.add(tag);
						}
					}
				} catch (NumberFormatException e) {
					continue;
				}
			}
			user.setFavourite_animal(myAnimal);
		}
		// 补全我喜欢的音乐
		if (!TextUtils.isEmpty(user.getMusic_ids())) {
			ids = user.getMusic_ids().split(",");
			List<Tag> musics = new ArrayList<Tag>();
			for (String id : ids) {
				try {
					int tag_id = Integer.parseInt(id);
					for (Tag tag : tags) {
						if (tag.getType() == Tag.TYPE_LIKE_MUSIC && tag.getId() == tag_id) {
							musics.add(tag);
						}
					}
				} catch (NumberFormatException e) {
					continue;
				}
			}
			user.setFavourite_music(musics);
		}
		// 补全周末想去干嘛
		if (!TextUtils.isEmpty(user.getWeekday_todo_ids())) {
			ids = user.getWeekday_todo_ids().split(",");
			List<Tag> weekday = new ArrayList<Tag>();
			for (String id : ids) {
				try {
					int tag_id = Integer.parseInt(id);
					for (Tag tag : tags) {
						if (tag.getType() == Tag.TYPE_WEEKDAY && tag.getId() == tag_id) {
							weekday.add(tag);
						}
					}
				} catch (NumberFormatException e) {
					continue;
				}
			}
			user.setWeekday_todo(weekday);
		}
		// 补全足迹
		if (!TextUtils.isEmpty(user.getFootstep_ids()))

		{
			ids = user.getFootstep_ids().split(",");
			List<Tag> footstep = new ArrayList<Tag>();
			for (String id : ids) {
				try {
					int tag_id = Integer.parseInt(id);
					for (Tag tag : tags) {
						if (tag.getType() == Tag.TYPE_FOOTSTEPS && tag.getId() == tag_id) {
							footstep.add(tag);
						}
					}
				} catch (NumberFormatException e) {
					continue;
				}
				user.setFootsteps(footstep);
			}
		}
	}

	public void updateRelationship(User user, long with_user_id, int relationship) {
		userInfoDao.updateRelationship(user.getUser_id(), with_user_id, relationship);

		//判断对方是否也已经喜欢我了
		if (relationship == Relationship.LIKE.ordinal()) {
			int count = userInfoDao.isLikeMe(user.getUser_id(), with_user_id);
			if (count >0) {
				ImagePathUtil.completeAvatarPath(user, true);
				
				
				Map<String, String> ext=new HashMap<String, String>();
				ext.put("nickname", user.getNick_name());
				ext.put("avatar", user.getAvatar());
				ext.put("origin_avatar", user.getOrigin_avatar());
                Object result= Main.sendTxtMessage(String.valueOf(user.getUser_id()), new String[] { String.valueOf(with_user_id) }, "很高兴认识你!",ext);
				if (result != null) {
					System.out.println(result);
				}
			}
		}
	}
	public void updateRelationshipNOHX(User user, long with_user_id, int relationship) {
		userInfoDao.updateRelationship(user.getUser_id(), with_user_id, relationship);
	}
	
	

	public List<User> getLikeMeUsers(long user_id, long last_user_id, int page_size) {
		List<User> users = userInfoDao.getLikeMeUsers(user_id, last_user_id, page_size);
		if (users != null) {
			for (User user : users) {
				ImagePathUtil.completeAvatarPath(user, true); // 补全图片链接地址
			}
		}
		return users;
	}
	public List<User> getFatePlaceUsers(long user_id, long last_user_id, int page_size) {
		List<User> users = userInfoDao.getOnlyLikeMeUsers(user_id, last_user_id, page_size);
		if (users != null) {
			for (User user : users) {
				ImagePathUtil.completeAvatarPath(user, true); // 补全图片链接地址
			}
		}
		return users;
	}

	public List<User> getLikeEachUsers(long user_id, long last_user_id, int page_size) {
		List<User> users = userInfoDao.getLikeEachUsers(user_id, last_user_id, page_size);
		if (users != null) {
			for (User user : users) {
				ImagePathUtil.completeAvatarPath(user, true); // 补全图片链接地址
			}
		}
		return users;
	}
}
