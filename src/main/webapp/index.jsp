<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>注册</title>
</head>
<body>
	<fieldset>
		<legend>注册</legend>

		<form action="user/regist" method="post" enctype="multipart/form-data">
			code: <input type="text" name="code"><br /> mobile: <input
				type="text" name="mobile" value="13243232322"><br />
			password: <input type="text" name="password"><br />
			userName: <input type="text" name="name"><br /> birthday:<input
				type="text" name="birthday" value="2016-03-08"><br /> sex:<input
				type="text" name="sex" value="0"><br /> avatar<input
				type="file" name="image" multiple="multiple"> <input
				type="hidden" name="_ua" value="123456789456"> <input
				type="submit" value="保存">
		</form>
	</fieldset>
	<fieldset>
		<legend>上传图片</legend>

		<form action="user/images" method="post" enctype="multipart/form-data">
			 
				  image1<input type="file" name="image1" multiple="multiple"><br/> 
				  image2<input type="file" name="image2" multiple="multiple"> 
				<input type="hidden" name="_ua" value="123456789456"> 
				<input type="text" name="user_id" value="13"> 
				<input type="hidden" name="token" value="11231"> 
				<input type="submit" value="保存">
		</form>
	</fieldset>
</body>
</html>
