<div id="dictionary-edit">
	<div class="layui-card">
		<div class="layui-card-body">
			<form class="layui-form" action="">
				<input type="hidden" name="id" value="${resultInfo.data.id}">
				<!-- 编辑页面 -->
				<blockquote class="layui-elem-quote mb25">
					<a href="javascript:void(0);" class="layui-btn layui-btn-sm layui-btn-primary" name="back">
						<i class="layui-icon layui-icon-return"></i> 返回
					</a>
					<a href="javascript:void(0);" class="layui-btn layui-btn-sm" lay-submit lay-filter="dictionarySubmit">
						<i class="layui-icon layui-icon-ok"></i> 保存
					</a>
				</blockquote>
				<div class="layui-form-item">
					<label class="layui-form-label">编码</label>
					<div class="layui-input-block">
						<input name="code" lay-verify="required|charWord|lenRange" range="[1,20]" placeholder="请输入编码" class="layui-input"
						       autocomplete="off" value="${resultInfo.data.code}">
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">分组码名称</label>
					<div class="layui-input-block">
						<select name="groupCode" title="" lay-verify="required">
						</select>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">关联码</label>
					<div class="layui-input-block">
						<input name="joinCode" lay-verify="lenRange" range="[1,20]" placeholder="请选择输入关联码" class="layui-input"
						       autocomplete="off" value="${resultInfo.data.joinCode}">
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">名称</label>
					<div class="layui-input-block">
						<input name="name" lay-verify="required|lenRange" range="[1,20]" placeholder="请输入编码名称" class="layui-input"
						       autocomplete="off" value="${resultInfo.data.name}">
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
        let $dom = $("#dictionary-edit");
        let $groupCode = $dom.find("[name=groupCode]");
        let groupCode = '${resultInfo.data.groupCode}';
        // 初始化字典分组下拉框
        U.initSelect({
            $dom: $groupCode,
            url: 'sys/dictionaryGroup/listByEntity.json',
            idField: "code",
            callback: function () {
                if (!U.isEmpty(groupCode)) {
                    $groupCode.val(groupCode);
                }
                form.render('select');
            }
        });
        //监听表单提交
        form.on('submit(dictionarySubmit)', function (data) {
            U.loading();
            ajaxCall({
                url: 'sys/dictionary/saveWithSort.json',
                param: data.field,
                success: function () {
                    U.fireEvent("dictionary-list-back");
                }
            });
            return false;
        });
        // 返回上个页面的事件
        $dom.find("[name=back]").click(function () {
            U.fireEvent("dictionary-list-back");
        });
    }();
</script>