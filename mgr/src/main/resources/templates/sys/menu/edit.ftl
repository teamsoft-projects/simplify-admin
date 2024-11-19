<div id="menu-edit">
	<div class="layui-card">
		<div class="layui-card-body">
			<form class="layui-form" action="">
				<input type="hidden" name="id" value="${resultInfo.data.id}">
				<!-- 编辑页面 -->
				<blockquote class="layui-elem-quote mb25">
					<a href="javascript:void(0);" class="layui-btn layui-btn-sm layui-btn-primary" name="back">
						<i class="layui-icon layui-icon-return"></i> 返回
					</a>
					<a href="javascript:void(0);" class="layui-btn layui-btn-sm" lay-submit lay-filter="menuSubmit">
						<i class="layui-icon layui-icon-ok"></i> 保存
					</a>
				</blockquote>
				<div class="layui-form-item">
					<label class="layui-form-label">菜单名称</label>
					<div class="layui-input-block">
						<input name="name" lay-verify="required|lenRange" range="[2,10]" placeholder="请输入菜单名称" class="layui-input" autocomplete="off"
						       value="${resultInfo.data.name!""}">
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">上级菜单</label>
					<div class="layui-input-block">
						<select name="parentId" title="">
						</select>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">菜单图标</label>
					<div class="layui-input-block">
						<i class="layui-icon"></i>
						<input name="icon" placeholder="请选择菜单图标" class="layui-input" lay-verify="required" autocomplete="off"
						       value="${resultInfo.data.icon!""}" readonly>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">菜单链接</label>
					<div class="layui-input-block">
						<input name="url" lay-verify="lenRange" range="[2,50]" placeholder="请输入菜单链接" class="layui-input" autocomplete="off"
						       value="${resultInfo.data.url!""}">
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">排序码</label>
					<div class="layui-input-block">
						<input name="sortOrder" placeholder="请输入排序码" value="${resultInfo.data.sortOrder!""}" class="layui-input" autocomplete="off">
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
<script>
    +function () {
        let $dom = $("#menu-edit");
        let $icon = $dom.find("[name=icon]");
        let $iconI = $icon.prev();
        let $parentId = $dom.find("[name=parentId]");
        let parentId = '${resultInfo.data.parentId}';
        // 初始化上级菜单下拉框
        U.initSelect({
            $dom: $parentId,
            url: 'sys/menu/listByEntity.json',
            nameField: "title",
            param: {parentId: 'top'},
            callback: function () {
                if (!U.isEmpty(parentId) && parentId !== 'top') {
                    $parentId.val(parentId);
                }
                form.render('select');
            }
        });
        let icon = '${resultInfo.data.icon}';
        if (!U.isEmpty(icon)) {
            changeIcon(icon);
        }
        let isPopup = false; // 是否已弹出, 解决快速点击时会弹出两次的问题
        // 添加选择图标绑定事件
        $dom.find("[name=icon]").click(function () {
            if (isPopup) {
                return;
            }
            isPopup = true;
            U.loading();
            let iconIndex;
            // 绑定图标选中事件处理
            U.addEvent("icon-seleted", function ($dom) {
                if (iconIndex === undefined || U.isEmpty($dom)) {
                    return;
                }
                let className = $dom.attr("class").split(/\s+/)[1];
                if (className !== undefined) {
                    changeIcon(className);
                }
                layer.close(iconIndex);
            });
            // 打开选择图标页面
            loadHtml({
                url: 'icon',
                param: {type: 2},
                success: function (html) {
                    iconIndex = layer.open({
                        type: 1,
                        title: '选择图标',
                        area: ['1165px', '740px'],
                        content: html,
                        success: function () {
                            U.closeLoading();
                            isPopup = false;
                        }
                    });
                }
            });
        });
        //监听表单提交
        form.on('submit(menuSubmit)', function (data) {
            U.loading();
            ajaxCall({
                url: 'sys/menu/save.json',
                param: data.field,
                success: function () {
                    U.fireEvent("menu-list-back", data.field);
                }
            });
            return false;
        });
        // 返回上个页面的事件
        $dom.find("[name=back]").click(function () {
            U.fireEvent("menu-list-back");
        });

        // 改变图标
        function changeIcon(className) {
            $icon.parent().addClass("layui-icon-input-self");
            $icon.val(className);
            $iconI.removeClass().addClass('layui-icon').addClass(className);
        }
    }();
</script>