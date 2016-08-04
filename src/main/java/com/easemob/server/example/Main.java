package com.easemob.server.example;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.easemob.server.example.api.IMUserAPI;
import com.easemob.server.example.api.SendMessageAPI;
import com.easemob.server.example.comm.ClientContext;
import com.easemob.server.example.comm.EasemobRestAPIFactory;
import com.easemob.server.example.comm.body.IMUserBody;
import com.easemob.server.example.comm.body.TextMessageBody;
import com.easemob.server.example.comm.wrapper.BodyWrapper;

public class Main {
	private static EasemobRestAPIFactory factory;

	private static void initFactory() {
		if (factory == null) {
			factory = ClientContext.getInstance().init(ClientContext.INIT_FROM_PROPERTIES).getAPIFactory();
		}
	}

	public static void main(String[] args) throws Exception {
		//initFactory();
		//IMUserAPI user = (IMUserAPI) factory.newInstance(EasemobRestAPIFactory.USER_CLASS);
		// ChatMessageAPI chat =
		// (ChatMessageAPI)factory.newInstance(EasemobRestAPIFactory.MESSAGE_CLASS);
		// FileAPI file =
		// (FileAPI)factory.newInstance(EasemobRestAPIFactory.FILE_CLASS);
		// SendMessageAPI message =
		// (SendMessageAPI)factory.newInstance(EasemobRestAPIFactory.SEND_MESSAGE_CLASS);
		// ChatGroupAPI chatgroup =
		// (ChatGroupAPI)factory.newInstance(EasemobRestAPIFactory.CHATGROUP_CLASS);
		// ChatRoomAPI chatroom =
		// (ChatRoomAPI)factory.newInstance(EasemobRestAPIFactory.CHATROOM_CLASS);
		//
		// ResponseWrapper fileResponse = (ResponseWrapper) file.uploadFile(new
		// File("d:/logo.png"));
		// String uuid = ((ObjectNode)
		// fileResponse.getResponseBody()).get("entities").get(0).get("uuid").asText();
		// String shareSecret = ((ObjectNode)
		// fileResponse.getResponseBody()).get("entities").get(0).get("share-secret").asText();
		// InputStream in = (InputStream) ((ResponseWrapper)
		// file.downloadFile(uuid, shareSecret, false)).getResponseBody();
		// FileOutputStream fos = new FileOutputStream("d:/logo1.png");
		// byte[] buffer = new byte[1024];
		// int len1 = 0;
		// while ((len1 = in.read(buffer)) != -1) {
		// fos.write(buffer, 0, len1);
		// }
		// fos.close();
		//BodyWrapper userBody = new IMUserBody("User101", "123456", "HelloWorld");
		//user.createNewIMUserSingle(userBody);
		/*
		 * // Create a IM user // BodyWrapper userBody = new
		 * IMUserBody("User101", "123456", "HelloWorld"); //
		 * user.createNewIMUserSingle(userBody);
		 * 
		 * // Create some IM users List<IMUserBody> users = new
		 * ArrayList<IMUserBody>(); users.add(new IMUserBody("User002",
		 * "123456", null)); users.add(new IMUserBody("User003", "123456",
		 * null)); BodyWrapper usersBody = new IMUsersBody(users);
		 * user.createNewIMUserBatch(usersBody);
		 * 
		 * // Get a IM user user.getIMUsersByUserName("User001");
		 * 
		 * // Get a fake user user.getIMUsersByUserName("FakeUser001");
		 * 
		 * // Get 12 users user.getIMUsersBatch(null, null);
		 */
		
		
		Map<String, String> ext=new HashMap<String, String>();
		ext.put("nickname", "测试昵称25");
		ext.put("avatar", "");
		ext.put("origin_avatar", "");
		
		System.out.println(sendTxtMessage("15",new String[]{"26"},"hello",ext));
//		System.out.println(sendTxtMessage("25",new String[]{"15"},"hello",ext));
	}

	public static Object registUser(String userName, String password, String nickname) {
		initFactory();
		IMUserAPI user = (IMUserAPI) factory.newInstance(EasemobRestAPIFactory.USER_CLASS);
		BodyWrapper userBody = new IMUserBody(userName, password, nickname);
		return user.createNewIMUserSingle(userBody);
	}

	public static Object updateNickName(String userName, String nickname) {
		initFactory();

		IMUserAPI user = (IMUserAPI) factory.newInstance(EasemobRestAPIFactory.USER_CLASS);
		JSONObject payload = new JSONObject();
		payload.put("nickname", nickname);
		return user.modifyIMUserNickNameWithAdminToken(userName, payload);
	}

	public static Object sendTxtMessage(String from, String[] users, String msgTxt,Map<String, String> ext) {
		initFactory();
		// {
		// "target_type" : "users", // users 给用户发消息。chatgroups: 给群发消息，chatrooms:
		// 给聊天室发消息
		// "target" : ["u1", "u2", "u3"], // 注意这里需要用数组，数组长度建议不大于20，即使只有一个用户，
		// // 也要用数组 ['u1']，给用户发送时数组元素是用户名，给群组发送时
		// // 数组元素是groupid
		// "msg" : {
		// "type" : "txt",
		// "msg" : "hello from rest"
		// //消息内容，参考[[start:100serverintegration:30chatlog|聊天记录]]里的bodies内容
		// },
		// "from" : "jma2"
		// //表示消息发送者。无此字段Server会默认设置为"from":"admin"，有from字段但值为空串("")时请求失败
		// }
		
		
		BodyWrapper payload = new TextMessageBody("users", users, from, ext,msgTxt);
		SendMessageAPI message = (SendMessageAPI) factory.newInstance(EasemobRestAPIFactory.SEND_MESSAGE_CLASS);
		return message.sendMessage(payload);
	}
}
