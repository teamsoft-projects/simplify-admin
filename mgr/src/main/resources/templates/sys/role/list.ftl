<div class="admin-main" id="role-list" data-url="sys/role/list.json">
	<!-- 列表页面 -->
	<div name="mainPage" class="main-page">
		<div class="layui-card">
			<div class="layui-card-body query-card">
				<form class="layui-form" action="">
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">角色名称</label>
							<div class="layui-input-inline query-inline">
								<input type="text" name="name" placeholder="" class="layui-input">
							</div>
						</div>
						<div class="layui-inline">
							<button type="button" class="layui-btn layui-btn-sm" name="query" lay-submit lay-filter="querySubmit-role-list">
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
						<!-- 菜单功能按钮 -->
						<a href="javascript:void(0);" class="layui-btn layui-btn-sm layui-btn-prefer" name="add" layui-auth="role-add">
							<i class="layui-icon"></i> 新增
						</a>
						<a href="javascript:void(0);" class="layui-btn layui-btn-sm" name="edit" layui-auth="role-edit">
							<i class="layui-icon"></i> 修改
						</a>
						<a href="javascript:void(0);" class="layui-btn layui-btn-sm layui-btn-danger" name="remove" layui-auth="role-remove">
							<i class="layui-icon"></i> 删除
						</a>
						<a href="javascript:void(0);" class="layui-btn layui-btn-sm layui-btn-normal" name="auth" layui-auth="role-auth">
							<i class="layui-icon"></i> 角色授权
						</a>
					</div>
					<table class="layui-table admin-table" lay-even>
						<thead>
						<tr>
							<th><input type="checkbox" lay-filter="allselector-role-list" lay-skin="primary" title=""></th>
							<th>角色名</th>
							<th>描述</th>
							<th>排序码</th>
						</tr>
						</thead>
						<tbody name="dataList">
						</tbody>
					</table>
				</div>
				<div class="admin-table-page">
					<div name="paged" class="page">
					</div>
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
			<td><input type="checkbox" lay-skin="primary" title="" lay-filter="selector-role-list" identity="{{item.id}}"></td>
			<td>{{=item.name}}</td>
			<td>{{=item.description }}</td>
			<td>{{=item.sortOrder}}</td>
		</tr>
		{{# }); }}
	</script>
</div>
<script>
    +function () {
        // 创建页面对象
        let pager = Pager.create({
            id: 'role-list' // 页面编号
        });
        let $dom = $('#role-list');
        // 角色授权按钮事件
        let $auth = $dom.find('[name=auth]');
        let authUrl = $auth.attr("data-url");
        $auth.click(function () {
            if (U.isEmpty(authUrl)) {
                U.error('未配置按钮的链接地址');
                return;
            }
            let $checked = pager.config.$dataList.find("input:checkbox:checked");
            if ($checked.length === 0 || $checked.length > 1) {
                U.warn("修改时必须选中一条记录");
                return;
            }
            U.loading();
            let id = $checked.eq(0).attr("identity");
            loadHtml({
                url: authUrl,
                param: {roleId: id},
                success: function (html) {
                    pager.config.$subPage.html(html);
                    pager.config.$mainPage.hide();
                    pager.config.$subPage.show();
                }
            });
        });
    }();
</script>