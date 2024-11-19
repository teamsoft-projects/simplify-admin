<!doctype html>
<html aria-label="login" lang="en">
<head>
	<meta charset="UTF-8">
	<title>${resultInfo.data.SYSTEM_NAME} - 登录</title>
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="format-detection" content="telephone=no">
	<link rel="shortcut icon" type="image/x-icon" href="/static/images/favicon.png"/>
	<link rel="bookmark" href="/static/images/favicon.png">
	<link rel="stylesheet" href="/static/common/layui/css/layui.css?v=231201">
	<link rel="stylesheet" href="/static/common/layui-plus/css/toast.css?v=231201">
	<link rel="stylesheet" href="/static/css/login.css?v=231201">
</head>
<body>
<div class="login-panel">
	<div class="logo"></div>
	<div class="system-title">${resultInfo.data.SYSTEM_NAME}</div>
	<div class="form-item">
		<input type="text" id="loginName" class="username" autocomplete="off" placeholder="登陆账号">
	</div>
	<div class="form-item">
		<input type="password" id="password" class="password" autocomplete="off" placeholder="登录密码">
	</div>
	<div class="form-item">
		<button id="login-submit">登 录</button>
	</div>
</div>
<!--修改密码模板 start-->
<script type="text/template" id="update-password-template">
	<div class="login-update-password layui-form">
		<div class="layui-form-item">
			<label class="layui-form-label">新密码</label>
			<div class="layui-input-block">
				<input name="passwd" type="password" lay-verify="required|lenRange" range="[1,50]" placeholder="请输入新密码" class="layui-input" autocomplete="off">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">确认密码</label>
			<div class="layui-input-block">
				<input name="passwd2" type="password" lay-verify="required|lenRange|number" range="[1,13]" placeholder="请输入确认密码" class="layui-input" autocomplete="off">
			</div>
		</div>
		<div class="layui-form-item center">
			<a name="updatePasswd" class="layui-btn layui-btn-normal btn-smaller" href="javascript:void(0);">修改密码</a>
		</div>
	</div>
</script>
<!--修改密码模板 end-->
<script src="/static/common/jquery/jquery.min.js?v=231201"></script>
<script src="/static/common/layui/layui.js?v=231201"></script>
<script src="/static/common/layui-plus/js/toast.js?v=231201"></script>
<script src="/static/common/encrypt/md5.js?v=231201"></script>
<script src="/static/js/common.js?v=231201&url="></script>
<script src="/static/js/core.js?v=231201"></script>
<script src="/static/js/login.js?v=231201"></script>
</body>
</html>