<div id="dictionaryGroup-edit">
	<form class="layui-form" action="">
		<input type="hidden" name="id" value="${resultInfo.data.id}">
		<!-- 编辑页面 -->
		<blockquote class="layui-elem-quote mb25">
			<a href="javascript:void(0);" class="layui-btn layui-btn-sm layui-btn-primary" name="back">
				<i class="layui-icon layui-icon-return"></i> 返回
			</a>
			<a href="javascript:void(0);" class="layui-btn layui-btn-sm" lay-submit lay-filter="dictionaryGroupSubmit">
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
			<label class="layui-form-label">名称</label>
			<div class="layui-input-block">
				<input name="name" lay-verify="required|lenRange" range="[1,20]" placeholder="请输入名称" class="layui-input"
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
<script>
	+function() {
		let $dom = $("#dictionaryGroup-edit");
		//监听表单提交
		form.on('submit(dictionaryGroupSubmit)', function(data) {
			U.loading();
			ajaxCall({
				url: 'sys/dictionaryGroup/saveWithSort.json',
				param: data.field,
				success: function() {
					U.fireEvent("dictionaryGroup-list-back");
				}
			});
			return false;
		});
		// 返回上个页面的事件
		$dom.find("[name=back]").click(function() {
			U.fireEvent("dictionaryGroup-list-back");
		});
	}();
</script>