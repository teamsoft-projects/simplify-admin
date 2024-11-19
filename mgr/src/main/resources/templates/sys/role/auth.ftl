<div id="auth-list" data-url="sys/menu/listWithFunc.json?roleId=${resultInfo.holder}">
	<div class="layui-card">
		<div class="layui-card-body">
			<!-- 列表页面 -->
			<div name="mainPage" class="main-page">
				<blockquote class="layui-elem-quote layui-function-area">
					<a href="javascript:void(0);" class="layui-btn layui-btn-sm layui-btn-primary" name="back">
						<i class="layui-icon layui-icon-return"></i> 返回
					</a>
					<a href="javascript:void(0);" class="layui-btn layui-btn-sm" name="save" data-url="sys/role/saveAuth">
						<i class="layui-icon layui-icon-ok"></i> 保存
					</a>
				</blockquote>
				<div class="layui-form pt1">
					<table class="layui-table admin-table" lay-filter="menuTable" lay-even>
						<thead>
						<tr>
							<th style="width:auto">菜单名称</th>
							<th width="10%">全选</th>
							<th width="10%">访问权</th>
							<th>操作权</th>
							<th>菜单链接</th>
						</tr>
						</thead>
						<tbody name="dataList">
						</tbody>
					</table>
				</div>
			</div>
			<!-- 详情页面 -->
			<div name="subPage" class="hide"></div>
			<!-- 列表模板 -->
			<script type="text/template" name="dataListTemplate">
				{{# layui.each(d, function(index, item) { }}
				<tr>
					<td><input type="hidden" name="id" value="{{item.id}}"><i class="mr5 layui-icon {{=item.icon }}"></i>{{=item.title }}</td>
					<td><input type="checkbox" lay-skin="primary" title="" data-type="selectAll" lay-filter="selectAllSel"></td>
					<td>
						{{# if(item.hasAuth === 1) { }}
						<input type="checkbox" lay-skin="primary" title="" checked lay-filter="notAllSelector" data-type="accessible">
						{{# } }}
						{{# if(item.hasAuth !== 1) { }}
						<input type="checkbox" lay-skin="primary" title="" lay-filter="notAllSelector" data-type="accessible">
						{{# } }}
					</td>
					<td>&nbsp;</td>
					<td>{{=item.href }}</td>
				</tr>
				{{#     if(!U.isEmpty(item.children)) { }}
				{{#         layui.each(item.children, function(index, child) { }}
				<tr>
					<td>
						<input type="hidden" name="id" value="{{child.id}}">
						<span class="pr24">&nbsp;</span><i class="mr5 layui-icon {{=child.icon }}"></i>{{=child.title}}
					</td>
					<td><input type="checkbox" lay-skin="primary" title="" lay-filter="selectAllSel" data-type="selectAll"></td>
					<td>
						{{# if(child.hasAuth === 1) { }}
						<input type="checkbox" lay-skin="primary" title="" checked lay-filter="notAllSelector" data-type="accessible">
						{{# } }}
						{{# if(child.hasAuth !== 1) { }}
						<input type="checkbox" lay-skin="primary" title="" lay-filter="notAllSelector" data-type="accessible">
						{{# } }}
					</td>
					<td>
						{{# if(!U.isEmpty(child.funcs)) { }}
						{{# layui.each(child.funcs, function(index, func) { }}
						{{# if(!U.isEmpty(func.id)) { }}
						{{# if(func.hasAuth === 1) { }}
						<input type="checkbox" lay-skin="primary" value="{{func.id}}" title="{{func.description}}" checked
						       lay-filter="notAllSelector" data-type="func">
						{{# } }}
						{{# if(func.hasAuth !== 1) { }}
						<input type="checkbox" lay-skin="primary" value="{{func.id}}" title="{{func.description}}"
						       lay-filter="notAllSelector" data-type="func">
						{{# } }}
						{{# } }}
						{{# }); }}
						{{# } }}
						{{# if(U.isEmpty(child.func)) { }}
						&nbsp;
						{{# } }}
					</td>
					<td>{{=child.href}}</td>
				</tr>
				{{#         }); }}
				{{#     } }}
				{{# }); }}
				{{# if(d.length === 0){ }}
				无数据
				{{# } }}
			</script>
		</div>
	</div>
</div>
<script>
    +function () {
        let pager = new Pager({
            id: "auth-list",
            paged: false,
            isOperation: false,
            isRowSelect: false,
            complete: function () {
                // 监听全选checkbox
                form.on("checkbox(selectAllSel)", function (data) {
                    $.each($(data.elem).parent().parent().find("input:checkbox:not([data-type=selectAll])"), function (key, val) {
                        val.checked = data.elem.checked;
                    });
                    form.render('checkbox');
                });
                // 监听非全选checkbox, 动态设置全选按钮
                form.on("checkbox(notAllSelector)", function (data) {
                    let $parent = $(data.elem).parent().parent();
                    let checkAll = $parent.find("input:checkbox[data-type=selectAll]")[0];
                    checkAll.checked = $parent.find("input:checkbox:not([data-type=selectAll]):not(:checked)").length === 0;
                    form.render('checkbox');
                });
                // 加载完成后, 检查全选状态
                pager.config.$dataList.find("tr").each(function (key, val) {
                    let $parent = $(val);
                    let checkAll = $parent.find("input:checkbox[data-type=selectAll]")[0];
                    checkAll.checked = $parent.find("input:checkbox:not([data-type=selectAll]):not(:checked)").length === 0;
                    form.render('checkbox');
                });
            }
        });
        let $dom = $("#auth-list");
        let roleId = '${resultInfo.holder}';
        let $save = $dom.find("[name=save]");
        let saveUrl = $save.attr("data-url");
        // 保存
        $save.click(function () {
            let menuAccess = []; // 存放菜单权限
            let menuFuncs = []; // 存放菜单-功能
            pager.config.$dataList.children("tr").each(function (key, val) {
                let $row = $(val);
                let menuId = $row.find("input:hidden").val();
                $row.find("input:checkbox:checked").each(function (key1, val1) {
                    let $checkbox = $(val1);
                    let dataType = $checkbox.attr("data-type");
                    if (dataType === "selectAll") {
                        return true;
                    } else if (dataType === "accessible") {
                        menuAccess.push({
                            roleId: roleId,
                            menuId: menuId
                        });
                    } else if (dataType === 'func') {
                        menuFuncs.push({
                            roleId: roleId,
                            menuId: menuId,
                            funcId: $checkbox.val()
                        });
                    }
                });
            });
            let param = $.extend({roleId: roleId}, U.convertArrayToObj("roleMenus", menuAccess), U.convertArrayToObj("menuFuncs", menuFuncs));
            ajaxCall({ // 发送请求, 保存授权信息
                url: saveUrl,
                param: param,
                success: function () {
                    U.success("保存成功!", function () {
                        pager.refresh();
                    });
                }
            });
        });
        // 返回上个页面的事件
        $dom.find("[name=back]").click(function () {
            U.fireEvent("role-list-back");
        });
    }();
</script>