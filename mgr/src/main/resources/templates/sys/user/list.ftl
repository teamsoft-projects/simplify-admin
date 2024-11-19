<div class="admin-main" id="user-list" data-url="sys/user/listByEntity.json">
	<!-- 列表页面 -->
	<div name="mainPage" class="main-page">
		<div class="layui-card">
			<div class="layui-card-body query-card">
				<form class="layui-form" action="">
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">登录账号</label>
							<div class="layui-input-inline query-inline">
								<input type="text" name="loginName" placeholder="" class="layui-input">
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">用户名</label>
							<div class="layui-input-inline query-inline">
								<input type="text" name="name" placeholder="" class="layui-input">
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">角色</label>
							<div class="layui-input-inline query-inline">
								<select name="roleId" title="" lay-filter="role-select"></select>
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">手机号</label>
							<div class="layui-input-inline query-inline">
								<input type="text" name="mobile" placeholder="" class="layui-input">
							</div>
						</div>
						<div class="layui-inline">
							<button type="button" class="layui-btn layui-btn-sm" name="query" lay-submit lay-filter="querySubmit-user-list">
								<i class="layui-icon layui-icon-search"></i>查询
							</button>
							<button type="reset" class="layui-btn layui-btn-sm layui-btn-primary" name="reset">
								<i class="layui-icon layui-icon-refresh"></i>重置
							</button>
						</div>
					</div>
				</form>
			</div>
		</div>
		<div class="layui-card">
			<div class="layui-card-body">
				<div class="layui-form pt1">
					<div class="func-area" name="func-area">
						<a href="javascript:void(0);" class="layui-btn layui-btn-sm layui-btn-prefer" name="add" layui-auth="user-add">
							<i class="layui-icon"></i> 新增
						</a>
						<a href="javascript:void(0);" class="layui-btn layui-btn-sm" name="edit" layui-auth="user-edit">
							<i class="layui-icon"></i> 修改
						</a>
						<a href="javascript:void(0);" class="layui-btn layui-btn-sm layui-btn-danger" name="remove" layui-auth="user-remove">
							<i class="layui-icon"></i> 删除
						</a>
						<a href="javascript:void(0);" class="layui-btn layui-btn-sm layui-btn-danger" name="resetPasswd" layui-auth="user-resetPasswd"
						   data-url="sys/user/resetPasswd">
							<i class="layui-icon"></i> 重置密码
						</a>
					</div>
					<table class="layui-table admin-table" lay-even>
						<thead>
						<tr>
							<th>
								<input type="checkbox" lay-filter="allselector-user-list" lay-skin="primary" title="">
							</th>
							<th>登录账号</th>
							<th>用户名</th>
							<th>角色</th>
							<th>手机号</th>
							<th>描述</th>
							<th>排序码</th>
						</tr>
						</thead>
						<tbody name="dataList">
						</tbody>
					</table>
				</div>
				<div class="admin-table-page">
					<div name="paged" class="page"></div>
				</div>
			</div>
		</div>
	</div>
	<!-- 详情页面 -->
	<div name="subPage" class="hide"></div>
	<!-- 列表模板 -->
	<script type="text/template" name="dataListTemplate">
		{{# layui.each(d, function(index, item) { }}
		<tr>
			<td><input type="checkbox" lay-skin="primary" title="" lay-filter="selector-user-list" identity="{{item.id}}"></td>
			<td>{{=item.loginName}}</td>
			<td>{{=item.name}}</td>
			<td>{{=item.roleName}}</td>
			<td>{{=item.mobile}}</td>
			<td>{{=item.description}}</td>
			<td>{{=item.sortOrder}}</td>
		</tr>
		{{# }); }}
	</script>
</div>
<script>
    +function () {
        let $dom = $("#user-list");
        // 创建页面对象
        let pager = Pager.create({
            id: 'user-list',
            querySelect: [{
                name: "roleId",
                url: "sys/role/list",
                param: {name: "超级管理员"}
            }]
        });
        let $resetPasswd = $dom.find("[name=resetPasswd]");
        let resetUrl = $resetPasswd.attr("data-url");
        $resetPasswd.click(function () {
            let $checked = pager.config.$dataList.find("input:checkbox:checked");
            if ($checked.length === 0 || $checked.length > 1) {
                U.warn("修改时必须选中一条记录");
                return;
            }
            let id = $checked.eq(0).attr("identity");
            let loginName = $checked.parent().next().html();
            U.confirm("确定重置登录名为 " + loginName + " 的用户密码吗?", function () {
                ajaxCall({
                    url: resetUrl,
                    param: {userId: id},
                    success: function () {
                        U.success("重置成功");
                    }
                });
            });
        });
    }();
</script>