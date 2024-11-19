<div class="admin-main" id="menu-list" data-url="sys/menu/list.json">
	<!-- 列表页面 -->
	<div name="mainPage" class="main-page">
		<div class="layui-card">
			<div class="layui-card-body">
				<div class="layui-form pt1">
					<div class="func-area" name="func-area">
						<a href="javascript:void(0);" class="layui-btn layui-btn-sm layui-btn-prefer" name="add" layui-auth="menu-add">
							<i class="layui-icon"></i> 新增
						</a>
						<a href="javascript:void(0);" class="layui-btn layui-btn-sm" name="edit" layui-auth="menu-edit">
							<i class="layui-icon"></i> 修改
						</a>
						<a href="javascript:void(0);" class="layui-btn layui-btn-sm layui-btn-danger" name="remove" layui-auth="menu-remove">
							<i class="layui-icon"></i> 删除
						</a>
						<a href="javascript:void(0);" class="layui-btn layui-btn-sm layui-btn-normal" name="func" layui-auth="menu-func">
							<i class="layui-icon"></i> 功能配置
						</a>
					</div>
					<table class="layui-table admin-table" lay-filter="menuTable" lay-even>
						<thead>
						<tr>
							<th><input type="checkbox" lay-filter="allselector-menu-list" lay-skin="primary" title=""></th>
							<th>菜单名称</th>
							<th>菜单图标</th>
							<th>菜单链接</th>
							<th>排序码</th>
						</tr>
						</thead>
						<tbody name="dataList">
						</tbody>
					</table>
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
			<td><input type="checkbox" lay-skin="primary" title="" lay-filter="selector-menu-list" identity="{{item.id}}" data-type="top"></td>
			<td>{{=item.title }}</td>
			<td><i class="layui-icon {{=item.icon }}"></i></td>
			<td>{{=item.href }}</td>
			<td>{{=item.sortOrder}}</td>
		</tr>
		{{#     if(!U.isEmpty(item.children)) { }}
		{{#         layui.each(item.children, function(index, child) { }}
		<tr>
			<td><input type="checkbox" lay-skin="primary" title="" lay-filter="selector-menu-list" identity="{{child.id}}"></td>
			<td><span class="pr24">&nbsp;</span>{{=child.title}}
			</td>
			<td><i class="layui-icon {{=child.icon}}"></i></td>
			<td>{{=child.href}}</td>
			<td>{{=child.sortOrder}}</td>
		</tr>
		{{#         }); }}
		{{#     } }}
		{{# }); }}
	</script>
</div>
<script>
    +function () {
        let pager = Pager.create({
            id: 'menu-list',
            paged: false
        });
        let $dom = $("#menu-list");
        // 功能配置按钮事件
        $dom.find("[name=func]").click(function () {
            let url = $(this).attr("data-url");
            if (U.isEmpty(url)) {
                U.error("功能配置按钮的URL为空");
                return;
            }
            let $checked = pager.config.$dataList.find("input:checkbox:checked");
            if ($checked.length === 0 || $checked.length > 1) {
                U.warn("配置菜单功能时必须只选中一条记录");
                return;
            }
            if ($checked.attr("data-type") === 'top') {
                U.warn("顶层菜单无需功能配置");
                return;
            }
            U.loading();
            let id = $checked.eq(0).attr("identity");
            loadHtml({
                url: url,
                param: {menuId: id},
                success: function (html) {
                    pager.config.$subPage.html(html);
                    pager.config.$mainPage.hide();
                    pager.config.$subPage.show();
                    U.closeLoading();
                }
            });
        });
    }();
</script>