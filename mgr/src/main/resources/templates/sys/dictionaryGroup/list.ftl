<div class="admin-main" id="dictionaryGroup-list" data-url="sys/dictionaryGroup/listByEntity.json">
	<!-- 列表页面 -->
	<div name="mainPage" class="main-page">
		<div class="layui-card no-shadow">
			<div class="layui-card-body query-card">
				<form class="layui-form" action="">
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">分组编码</label>
							<div class="layui-input-inline query-inline">
								<input type="text" name="code" placeholder="" class="layui-input">
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">分组名称</label>
							<div class="layui-input-inline query-inline">
								<input type="text" name="name" placeholder="" class="layui-input">
							</div>
						</div>
						<div class="layui-inline">
							<button type="button" class="layui-btn layui-btn-sm" name="query" lay-submit lay-filter="querySubmit-dictionaryGroup-list">
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
		<div class="layui-card no-shadow">
			<div class="layui-card-body">
				<div class="layui-form pt1">
					<div class="func-area" name="func-area">
						<a href="javascript:void(0);" class="layui-btn layui-btn-sm layui-btn-prefer" name="add" layui-auth="dictionary-group-add">
							<i class="layui-icon"></i> 新增
						</a>
						<a href="javascript:void(0);" class="layui-btn layui-btn-sm" name="edit" layui-auth="dictionary-group-edit">
							<i class="layui-icon"></i> 修改
						</a>
						<a href="javascript:void(0);" class="layui-btn layui-btn-sm layui-btn-danger" name="remove" layui-auth="dictionary-group-remove">
							<i class="layui-icon"></i> 删除
						</a>
					</div>
					<table class="layui-table admin-table" lay-even>
						<thead>
						<tr>
							<th>
								<input type="checkbox" lay-filter="allselector-dictionaryGroup-list" lay-skin="primary" title="">
							</th>
							<th>编码</th>
							<th>名称</th>
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
			<td><input type="checkbox" lay-skin="primary" title="" lay-filter="selector-dictionaryGroup-list" identity="{{item.id}}"></td>
			<td>{{=item.code}}</td>
			<td>{{=item.name}}</td>
			<td>{{=item.sortOrder}}</td>
		</tr>
		{{# }); }}
	</script>
</div>
<script>
    +function () {
        // 创建页面对象
        Pager.create({
            id: 'dictionaryGroup-list'
        });
    }();
</script>