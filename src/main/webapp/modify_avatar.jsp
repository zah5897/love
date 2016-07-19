<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>注册</title>
</head>
<body>
	<form action="user/modify_avatar" method="post"
		enctype="multipart/form-data">
		<input type="hidden" name="_ua" value="1234567895"><br />
		token<input type="text" name="token"><br /> user_id:<input
			type="text" name="user_id" value="13"><br /> avatar<input
			type="file" name="image" multiple="multiple"> <input
			type="submit" value="修改">
	</form>
</body>
</html>
