layui.config({
	base: app_path + '/static/common/layui-plus/js/'
}).use(['layer', 'form', 'toast'], function () {
	window.layer = layui.layer;
	window.toast = layui.toast;
	let tipsIndex; // 提示索引
	let $loginSubmit = $("#login-submit");
	let $loginName = $("#loginName");
	let $password = $("#password");
	let isLoading = false;
	let g_loginName, g_oldPasswd;

	// 修改密码模板
	let updatePasswdTemplate = $("#update-password-template").html();

	// 修改密码弹窗
	function updatePasswd() {
		layer.open({
			type: 1,
			title: "修改密码",
			area: "360px",
			content: updatePasswdTemplate,
			success: function (layero) {
				let $this = $(layero);
				let $passwd = $this.find("[name=passwd]");
				let $passwd2 = $this.find("[name=passwd2]");
				$passwd.focus();
				$this.find("[name=updatePasswd]").click(function () {
					let passwd = $passwd.val();
					let passwd2 = $passwd2.val();
					if (U.isEmpty(passwd)) {
						showTips($passwd, "请输入密码");
						return false;
					}
					if (!/[a-zA-Z0-9_]{6,20}/.test(passwd)) {
						showTips($passwd, "密码必须是6-20位英文、数字、下划线");
						return false;
					}
					if (U.isEmpty(passwd2)) {
						showTips($passwd2, "请输入确认密码");
						return false;
					}
					if (!/[a-zA-Z0-9_]{6,20}/.test(passwd2)) {
						showTips($passwd2, "密码必须是6-20位英文、数字、下划线");
						return false;
					}
					if (passwd !== passwd2) {
						showTips($passwd2, "两次输入不一致");
						return false;
					}
					let passwordEncry = hex_md5(g_loginName + U.encry.salt + passwd);
					// 发送请求, 修改密码
					ajaxCall({
						url: "sys/user/updateDefaultPasswd.json",
						param: {loginName: g_loginName, password: passwordEncry},
						success: function () {
							U.success('修改成功', function () {
								window.location = index_path;
							});
						}
					});
				});
			},
			cancel: function () {
				window.location = index_path;
			}
		});
	}

	// 回车手动触发登陆
	$loginName.add($password).keypress(function (e) {
		if (e.keyCode === 13) {
			$loginSubmit.click();
		}
	});

	// 登陆提交按钮
	$loginSubmit.click(function () {
		if (isLoading) {
			return;
		}
		isLoading = true;
		let loginName = $loginName.val();
		let password = $password.val();
		let passwordEncry;
		if (!checkLogin(loginName, password)) {
			isLoading = false;
			return;
		}
		passwordEncry = hex_md5(loginName + U.encry.salt + password);
		// 显示loading动画
		$loginSubmit.html('<i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</i>');
		$password.val(passwordEncry);
		ajaxCall({
			url: "sys/user/doLogin.json",
			param: {loginName: loginName, password: passwordEncry},
			success: function (_resp) {
				if (_resp.flag === 100306) { // 后台提示
					showTips($loginName, _resp.message);
					$loginSubmit.html('<span>登录</span>');
					$password.val('');
					isLoading = false;
					return;
				}
				let isDefaultPasswd = _resp['data']['isDefaultPasswd'];
				if (isDefaultPasswd === 1) {
					// 使用默认密码, 弹出修改提示
					g_loginName = loginName;
					g_oldPasswd = password;
					layer.open({
						title: "温馨提示",
						content: "当前密码为默认密码, 存在安全隐患, 是否需要修改密码?",
						btn: ["修改密码"],
						shade: 0,
						shadeClose: false,
						time: -1,
						yes: function (index) {
							layer.close(index);
							updatePasswd();
						},
						cancel: function () {
							window.location = index_path;
						}
					});
				} else {
					window.location = index_path;
				}
			}
		});
	});

	/**
	 * 校验登录信息
	 * @returns {boolean}
	 */
	function checkLogin(loginName, password) {
		if (U.isEmpty(loginName)) {
			showTips($loginName, "请输入用户名");
			return false;
		}
		if (!/[a-zA-Z0-9_]{2,20}/.test(loginName)) {
			showTips($loginName, "用户名必须是2-20位英文、数字、下划线");
			return false;
		}
		if (U.isEmpty(password)) {
			showTips($password, "请输入密码");
			return false;
		}
		if (!/[a-zA-Z0-9_]{6,20}/.test(password)) {
			showTips($password, "密码必须是6-20位英文、数字、下划线");
			return false;
		}
		return true;
	}

	/**
	 * 显示校验结果提示
	 * @param $item
	 * @param tips
	 */
	function showTips($item, tips) {
		$item.addClass("layui-form-danger");
		setTimeout(function () {
			tipsIndex = layer.tips(tips, $item[0], {
				tips: [1, '#FF5722'],
				time: 2000
			});
			$item.focus();
			$item.one("click", function () {
				layer.close(tipsIndex);
				$item.removeClass("layui-form-danger");
			});
		}, 30);
	}
});