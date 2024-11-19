<div id="menu-func-list" data-url="sys/menuFunc/listByEntity?menuId=${resultInfo.data}">
	<div name="mainPage" class="main-page">
		<div class="layui-card">
			<div class="layui-card-body auto-render-table pb25">
				<form class="layui-form" action="">
					<input type="hidden" name="menuId" value="${resultInfo.data}">
					<!-- 编辑页面 -->
					<blockquote class="layui-elem-quote mb25">
						<a href="javascript:void(0);" class="layui-btn layui-btn-sm layui-btn-primary" name="back">
							<i class="layui-icon layui-icon-return"></i> 返回
						</a>
						<a href="javascript:void(0);" class="layui-btn layui-btn-sm" name="save">
							<i class="layui-icon layui-icon-ok"></i> 保存
						</a>
						<a href="javascript:void(0);" class="layui-btn layui-btn-sm layui-btn-prefer" name="addRow">
							<i class="layui-icon layui-icon-add-1"></i> 添加行
						</a>
						<a href="javascript:void(0);" class="layui-btn layui-btn-sm layui-btn-danger" name="removeRow">
							<i class="layui-icon layui-icon-delete"></i> 删除行
						</a>
						<a href="javascript:void(0);" class="layui-btn layui-btn-sm layui-btn-normal" name="moveUp">
							<i class="layui-icon layui-icon-up"></i> 上移
						</a>
						<a href="javascript:void(0);" class="layui-btn layui-btn-sm layui-btn-normal" name="moveDown">
							<i class="layui-icon layui-icon-down"></i> 下移
						</a>
					</blockquote>
					<table class="layui-hide" name="dataTable" lay-filter="menuFuncTable"></table>
				</form>
			</div>
		</div>
	</div>
</div>
<script>
    +function () {
        let $dom = $("#menu-func-list");
        let isPopup = false; // 是否已弹出选择图标页面, 解决快速点击时会弹出两次的问题
        // 选择图标单元格模板
        let iconTemplete = ['<div>',
            '<i class="layui-icon {{d.icon}}"></i>',
            '</div>'].join('');
        // 渲染表格
        let funcTable = table.render({
            id: 'menuFuncTable',
            method: 'POST',
            elem: $dom.find("[name=dataTable]")[0],
            url: 'sys/menuFunc/listByEntity.json',
            where: {menuId: '${resultInfo.data}'},
            parseData: function (res) {
                return {
                    code: res.flag === 100101 ? 0 : 1,
                    msg: res.message,
                    count: res.total,
                    data: res.data
                }
            },
            cols: [[
                {checkbox: true, width: '3%'},
                {
                    field: 'icon',
                    title: '图标',
                    width: '12%',
                    templet: iconTemplete,
                    event: 'setIcon',
                    style: 'cursor: pointer;'
                },
                {field: 'identify', title: '唯一标识', edit: 'text', width: '20%'},
                {field: 'name', title: '名称', edit: 'text', width: '15%'},
                {field: 'url', title: '链接地址', edit: 'text', width: '30%'},
                {field: 'description', title: '描述', edit: 'text'}
            ]],
            done: function () { // 表格渲染完成回调
                // 监听选择图标点击事件
                table.on('tool(menuFuncTable)', function (obj) {
                    if (obj.event !== 'setIcon') {
                        return;
                    }
                    if (isPopup) {
                        return;
                    }
                    isPopup = true;
                    U.loading();
                    let $target = obj.tr.find('td').eq(1);
                    let iconIndex;
                    // 绑定图标选中事件处理
                    U.addEvent("icon-seleted", function ($dom) {
                        if (iconIndex === undefined || U.isEmpty($dom)) {
                            return;
                        }
                        let className = $dom.attr("class").split(/\s+/)[1];
                        if (className !== undefined) {
                            $target.find('i').addClass(className);
                            $target.find('span').html(className);
                            // 更新表格和缓存对应的值
                            obj.update({
                                icon: className
                            });
                        }
                        layer.close(iconIndex);
                    });
                    // 打开选择图标页面
                    loadHtml({
                        url: 'icon',
                        param: {type: 2}, // type为2表示layui图标
                        success: function (html) {
                            iconIndex = layer.open({
                                type: 1,
                                title: '选择图标',
                                area: ['1165px', '740px'],
                                content: html,
                                success: function () {
                                    U.closeLoading();
                                    isPopup = false;
                                    obj.tr.removeClass("layui-table-click");
                                }
                            });
                        }
                    });
                });
            }
        });
        // 添加行
        $dom.find("[name=addRow]").click(function () {
            table.cache['menuFuncTable'].push({icon: '', LAY_CHECKED: false});
            //funcTable.instance.renderData({data: table.cache['menuFuncTable']});
	        table.renderData('menuFuncTable');
        });
        // 删除行
        $dom.find("[name=removeRow]").click(function () {
            let selectedLength = table.checkStatus('menuFuncTable').data.length;
            if (selectedLength < 1) {
                U.warn("请选择要删除的记录");
                return;
            }
            U.confirm("确定删除 " + selectedLength + "行记录吗?", function () {
                let deleteRows = []; // 待删除的行
                $dom.find("tbody tr").each(function (index, val) {
                    let $checkbox = $(val).find("td:eq(0) input:checkbox");
                    if ($checkbox.length !== 0 && $checkbox[0].checked) {
                        deleteRows.push(index);
                    }
                });
                if (deleteRows.length === 0) {
                    return;
                }
                // 反向删除数组, 解决正向删除时, 因删除导致的数组元素移位, 进而导致个别数据删除失败
                $.each(deleteRows.reverse(), function (key, val) {
                    table.cache['menuFuncTable'].splice(val, 1);
                });
                $dom.find('input[lay-filter=layTableAllChoose]:checkbox')[0].checked = false;
                form.render('checkbox');
                //funcTable.instance.renderData({data: table.cache['menuFuncTable']});
                table.renderData('menuFuncTable');
            });
        });
        // 上下移动, 支持批量移动
        $dom.find("[name=moveUp],[name=moveDown]").click(function () {
            let selectedLength = table.checkStatus('menuFuncTable').data.length;
            if (selectedLength < 1) {
                U.warn("请选择要移动的记录");
                return;
            }
            let direction = $(this).attr("name") === 'moveUp' ? 1 : 2; // 移动方向
            let removeRows = []; // 待移动的记录
            let data = table.cache['menuFuncTable'];
            let dataLen = data.length;
            $dom.find("tbody tr").each(function (index, val) {
                let $checkbox = $(val).find("td:eq(0) input:checkbox");
                if ($checkbox.length !== 0 && $checkbox[0].checked) {
                    removeRows.push(index);
                }
            });
            // 向上移动, 需要正向移动
            if (direction === 1) { // 向上移动
                $.each(removeRows, function (key, val) {
                    if (val === 0 || removeRows.contains(val - 1)) {
                        return true;
                    }
                    let temp = data[val - 1];
                    data[val - 1] = data[val];
                    data[val] = temp;
                    removeRows[key] = -1;
                });
            } else { // 向下移动, 需要反向移动
                $.each(removeRows.reverse(), function (key, val) {
                    if (val === dataLen - 1 || removeRows.contains(val + 1)) {
                        return true;
                    }
                    let temp = data[val + 1];
                    data[val + 1] = data[val];
                    data[val] = temp;
                    removeRows[key] = -1;
                });
            }
            funcTable.instance.renderData({data: table.cache['menuFuncTable']});
        });
        // 保存按钮
        $dom.find('[name=save]').click(function () {
            ajaxCall({
                url: 'sys/menuFunc/saveMenuFunc.json?menuId=${resultInfo.data}',
                param: U.convertArrayToObj('datas', table.cache['menuFuncTable']),
                success: function () {
                    U.success("保存成功!", function () {
                        funcTable.reload();
                    });
                }
            });
        });
        // 返回按钮
        $dom.find("[name=back]").click(function () {
            U.fireEvent("menu-list-back");
        });
    }();
</script>