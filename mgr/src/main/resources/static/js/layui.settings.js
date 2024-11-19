// layui中自定义内容封装
// 定义layui变量, layui加载状态通知
layui.config({
	base: app_path + 'static/common/layui-plus/js/'
}).use(['element', 'navbar', 'tab', 'form', 'laytpl', 'laypage', 'table', 'laydate', 'upload', 'layer', 'toast', 'treetable', 'util', 'dropdown'], function() {
	window.$ = layui.$;
	window.element = layui.element;
	window.layTab = layui.tab;
	window.navbar = layui.navbar();
	window.form = layui.form;
	window.laytpl = layui.laytpl;
	window.laypage = layui.laypage;
	window.table = layui.table;
	window.hint = layui.hint();
	window.laydate = layui.laydate;
	window.upload = layui.upload;
	window.layer = layui.layer;
	window.toast = layui.toast;
	window.treetable = layui.treetable;
	window.util = layui.util;
	window.dropdown = layui.dropdown;
	layui.ready();
});
layui.ready(function() {
	// 自定义校验规则
	form.verify({
		/* 长度范围(range=[a,b]) */
		lenRange: function(value, item) {
			let len = value.length;
			if(len === 0) {
				return;
			}
			let range = $(item).attr("range");
			if(range === undefined) {
				return '请配置长度范围';
			}
			let rangeArr = JSON.parse(range);
			if(len < rangeArr[0] || len > rangeArr[1]) {
				return '请输入' + rangeArr[0] + '-' + rangeArr[1] + '位的内容';
			}
		},
		/* 数字验证 */
		number: function(value) {
			let len = value.length;
			if(len === 0) {
				return;
			}
			if(!/^\d+$/.test(value)) {
				return '请输入数字';
			}
		},
		/* 小数验证 */
		double: function(value) {
			let len = value.length;
			if(len === 0) {
				return;
			}
			if(!/^\d+(\.\d+)?$/.test(value)) {
				return '请输入浮点型数字';
			}
		},
		/* 英文、数字、下划线验证 */
		charWord: function(value) {
			let len = value.length;
			if(len === 0) {
				return;
			}
			if(!/[a-zA-Z0-9_]/.test(value)) {
				return "只能输入英文、数字、下划线";
			}
		},
		/* 指定长度 */
		len: function(value, item) {
			let length = $(item).attr("len");
			if(U.isEmpty(length)) {
				throw new Error("长度校验中未配置长度值");
			}
			let lengthInt = parseInt(length);
			if(lengthInt !== value.length) {
				return "请输入 " + lengthInt + "位的长度值";
			}
		},
		/* yyyy-MM-dd */
		date: function(value, item) {
			if(!/\d{4}-\d{2}-\d{2}/.test(value)) {
				return "输入不合法";
			}
		},
		/* yyyy-MM-dd - yyyy-MM-dd */
		dateRange: function(value, item) {
			if(!/\d{4}-\d{2}-\d{2} - \d{4}-\d{2}-\d{2}/.test(value)) {
				return "输入不合法";
			}
		},
		/* yyyy-MM */
		month: function(value, item) {
			if(!/\d{4}-\d{2}/.test(value)) {
				return "输入不合法";
			}
		},
		/* 手机号 */
		mobile: function(value, item) {
			if(!/(\+\d{1,4})?\d{6,15}/.test(value)) {
				return "请输入有效的手机号";
			}
		}
	});
	// 定义验证不通过时的提醒方式
	/*form.invalidTips(function(item, tips) {
		let dom = item; // 对select单独处理
		if(item.tagName === 'SELECT') { // 对select单独处理
			dom = $(item).next().find("input")[0];
		}
		let $item = $(dom);
		$item.addClass('layui-form-danger');
		let tipsIndex = layer.tips(tips, dom, {
			tips: [1, '#FF5722'],
			time: 2000
		});
		$item.focus();
		$item.one('click', function() {
			$item.removeClass('layui-form-danger');
			layer.close(tipsIndex);
		});
	});*/
	// 定义表格渲染远程调用接口方式
	/*table.getRemoteData(function(options, curr, loadIndex, that) {
		ajaxCall({
			type: options.method || 'get',
			url: options.url,
			param: $.extend({
				page: curr,
				limit: options.limit
			}, options.where),
			success: function(res) {
				that.renderData(res, curr, res.total);
				loadIndex && layer.close(loadIndex);
				typeof options.done === 'function' && options.done(res, curr, res.count);
			},
			error: function(e, m) {
				U.error('数据请求异常');
				hint.error('初始table时的接口' + options.url + '异常：' + m);
				loadIndex && layer.close(loadIndex);
			}
		});
	});*/
});