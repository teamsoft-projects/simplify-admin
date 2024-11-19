<div id="user-edit">
	<div class="layui-card">
		<div class="layui-card-body">
			<form class="layui-form" action="">
				<input type="hidden" name="id" value="${resultInfo.data.id}">
				<!-- 编辑页面 -->
				<blockquote class="layui-elem-quote mb25">
					<a href="javascript:void(0);" class="layui-btn layui-btn-sm layui-btn-primary" name="back">
						<i class="layui-icon layui-icon-return"></i> 返回
					</a>
					<a href="javascript:void(0);" class="layui-btn layui-btn-sm" lay-submit lay-filter="userSubmit">
						<i class="layui-icon layui-icon-ok"></i> 保存
					</a>
				</blockquote>
				<div class="layui-form-item">
					<label class="layui-form-label">登录账号</label>
					<div class="layui-input-block">
						<input name="loginName" lay-verify="required|charWord|lenRange" range="[2,20]" placeholder="请输入登录账号" class="layui-input"
						       autocomplete="off" value="${resultInfo.data.loginName}">
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">用户名</label>
					<div class="layui-input-block">
						<input name="name" lay-verify="required|lenRange" range="[2,20]" placeholder="请输入用户名" class="layui-input"
						       autocomplete="off" value="${resultInfo.data.name}">
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">角色</label>
					<div class="layui-input-block">
						<select name="roleId" title="" lay-verify="required">
						</select>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">手机号</label>
					<div class="layui-input-block">
						<input name="mobile" lay-verify="phone" placeholder="请输入手机号" class="layui-input"
						       autocomplete="off" value="${resultInfo.data.mobile}">
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">描述</label>
					<div class="layui-input-block">
						<input name="description" lay-verify="lenRange" range="[1,50]" placeholder="请输入描述信息" class="layui-input"
						       autocomplete="off" value="${resultInfo.data.description}">
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">排序码</label>
					<div class="layui-input-block">
						<input name="sortOrder" lay-verify="number|lenRange" range="[1,10]" placeholder="请输入排序码" value="${resultInfo.data.sortOrder}"
						       autocomplete="off" class="layui-input">
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
<script>
    +function () {
        let $dom = $("#user-edit");
        let $roleId = $dom.find("[name=roleId]");
        let roleId = '${resultInfo.data.roleId}';
        // 初始化角色下拉框
        U.initSelect({
            $dom: $roleId,
            url: 'sys/role/list.json',
            nameField: "name",
            callback: function () {
                if (!U.isEmpty(roleId)) {
                    $roleId.val(roleId);
                }
                form.render('select');
            }
        });
        //监听表单提交
        form.on('submit(userSubmit)', function (data) {
            U.loading();
            ajaxCall({
                url: 'sys/user/saveWithSort.json',
                param: data.field,
                success: function () {
                    U.fireEvent("user-list-back");
                }
            });
            return false;
        });
        // 返回上个页面的事件
        $dom.find("[name=back]").click(function () {
            U.fireEvent("user-list-back");
        });
    }();
</script>