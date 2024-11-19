<div class="admin-main" id="paramConfig-list">
	<div name="mainPage" class="main-page">
		<div class="layui-card">
			<div class="layui-card-body sys-config">
				<form class="layui-form sys-configs" action="" lay-filter="param-config-form">
					<blockquote class="layui-elem-quote mb25">
						<a href="javascript:void(0);" class="layui-btn layui-btn-sm" lay-submit lay-filter="paramConfigSubmit">
							<i class="layui-icon layui-icon-ok"></i> 保存
						</a>
					</blockquote>
                    <#list resultInfo.data as item>
						<div class="layui-form-item">
                            <#if item.foo1 == 'SPLIT_SYMBOL'>
								<div class="site-title">
									<fieldset>
										<legend><a name="card">${item.paramName}</a></legend>
									</fieldset>
								</div>
								<input type="hidden" name="paramConfigs[${item_index}].foo1" value="${item.foo1}">
                            <#elseif item.foo1 == 'RADIO_SYMBOL'>
								<input type="hidden" name="paramConfigs[${item_index}].paramKey" value="${item.paramKey}">
								<input type="hidden" name="paramConfigs[${item_index}].foo1" value="${item.foo1}">
								<input type="hidden" name="paramConfigs[${item_index}].foo2" value="${item.foo2}">
								<label class="layui-form-label" style="margin-right: 11px;">${item.paramName}</label>
                                <#list item.foo2?split("|") as radioData>
                                    <#list radioData?split(",") as option>
                                        <#if option_index == 0>
                                            <#assign optionValue = option>
                                        <#else>
                                            <#assign optionName = option>
                                        </#if>
                                    </#list>
                                    <#if item.paramValue == optionValue>
										<input type="radio" name="paramConfigs[${item_index}].paramValue" value="${optionValue}" title="${optionName}" checked>
                                    <#else>
										<input type="radio" name="paramConfigs[${item_index}].paramValue" value="${optionValue}" title="${optionName}">
                                    </#if>
                                </#list>
                            <#elseif item.foo1 == 'CHECKBOX_SYMBOL'>
								<input type="hidden" name="paramConfigs[${item_index}].paramKey" value="${item.paramKey}">
								<input type="hidden" name="paramConfigs[${item_index}].foo1" value="${item.foo1}">
								<input type="hidden" name="paramConfigs[${item_index}].foo2" value="${item.foo2}">
								<label class="layui-form-label" style="margin-right: 11px;">${item.paramName}</label>
                                <#list item.foo2?split("|") as checkData>
                                    <#list checkData?split(",") as option>
                                        <#if option_index == 0>
                                            <#assign optionValue = option>
                                        <#else>
                                            <#assign optionName = option>
                                        </#if>
                                    </#list>
                                    <#list item.paramValue?split(",") as val>
                                        <#if val == optionValue>
											<input type="checkbox" name="paramConfigs[${item_index}].paramValue" value="${optionValue}" title="${optionName}" checked>
                                            <#break>
                                        </#if>
                                        <#if !val_has_next>
											<input type="checkbox" name="paramConfigs[${item_index}].paramValue" value="${optionValue}" title="${optionName}">
                                        </#if>
                                    </#list>
                                </#list>
                            <#else>
								<label class="layui-form-label">${item.paramName}</label>
								<div class="layui-input-block">
									<input type="hidden" name="paramConfigs[${item_index}].paramName" value="${item.paramName}">
									<input type="hidden" name="paramConfigs[${item_index}].paramKey" value="${item.paramKey}">
									<input type="hidden" name="paramConfigs[${item_index}].foo1" value="${item.foo1}">
									<input name="paramConfigs[${item_index}].paramValue" lay-verify="required" placeholder="请输入${item.paramName}" class="layui-input"
									       autocomplete="off" value="${item.paramValue}">
								</div>
                                <#if item.tips != null>
									<div class="layui-input-block">
										<div class="layui-form-mid layui-word-aux">${item.tips}</div>
									</div>
                                </#if>
                            </#if>
						</div>
                    </#list>
				</form>
			</div>
		</div>
	</div>
</div>
<script>
    +function () {
        let $dom = $('#paramConfig-list');
        form.render();
        form.on("submit(paramConfigSubmit)", function (data) {
            U.loading();
            // 获取checkbox的数据，并将不同的value合并到字符串，以逗号分隔
            let checkboxObj = {};
            $dom.find('input:checkbox:checked').each(function (key, val) {
                let $val = $(val);
                let name = $val.attr('name');
                let value = $val.val();
                let curVal = checkboxObj[name];
                if (U.isEmpty(curVal)) {
                    checkboxObj[name] = value;
                } else {
                    checkboxObj[name] = curVal + ',' + value;
                }
            });
            let param = $.extend(data.field, checkboxObj);
            U.closeLoading();
            ajaxCall({
                url: 'sys/paramConfig/update.json',
                param: param,
                success: function () {
                    U.success("保存配置成功");
                }
            });
            return false;
        });
        U.closeLoading();
    }();
</script>