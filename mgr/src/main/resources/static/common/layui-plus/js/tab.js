layui.define(['element', 'common'], function(exports) {
	"use strict";
	var mod_name = 'tab',
		element = layui.element,
		common = layui.common,
		globalTabIdIndex = 0,
		Tab = function() {
			this.config = {
				elem: undefined,
				closed: true, //是否包含删除按钮
				contextMenu: false,
				fixedCount: 0, // 首位固定显示标签数
				maxSetting: undefined,
				isShowing: false, // 是否正在展示TAB
				events: [] // 事件集合
			};
		};
	var ELEM = {};
	//版本号
	Tab.prototype.v = '0.1.5';
	/**
	 * 参数设置
	 * @param {Object} options
	 */
	Tab.prototype.set = function(options) {
		var that = this;
		$.extend(true, that.config, options);
		that.init();
		return that;
	};
	/**
	 * 初始化
	 */
	Tab.prototype.init = function() {
		var that = this;
		var _config = that.config;
		if(typeof(_config.elem) !== 'string' && typeof(_config.elem) !== 'object') {
			common.throwError('Tab error: elem参数未定义或设置出错，具体设置格式请参考文档API.');
		}
		var $container;
		if(typeof(_config.elem) === 'string') {
			$container = $('' + _config.elem + '');
		}
		if(typeof(_config.elem) === 'object') {
			$container = _config.elem;
		}
		if($container.length === 0) {
			common.throwError('Tab error:找不到elem参数配置的容器，请检查.');
		}
		var filter = $container.attr('lay-filter');
		if(filter === undefined || filter === '') {
			common.throwError('Tab error:请为elem容器设置一个lay-filter过滤器');
		}
		_config.elem = $container;
		ELEM.titleBox = $container.children('ul.layui-tab-title');
		ELEM.contentBox = $container.children('div.layui-tab-content');
		ELEM.tabFilter = filter;
		return that;
	};
	/**
	 * 获取tabid
	 * @param {String} tabId 标签编号
	 */
	Tab.prototype.exists = function(tabId) {
		if(U.isEmpty(tabId)) {
			return false;
		}
		var result = false;
		ELEM.titleBox.find('li').each(function(key, val) {
			var layId = $(this).attr("lay-id");
			if(tabId === layId) {
				result = true;
				return false;
			}
		});
		return result;
	};
	/**
	 * 添加选项卡，如果选项卡存在则获取焦点
	 * @param {Object} data
	 */
	Tab.prototype.tabAdd = function(data) {
		var that = this;
		var _config = that.config;
		_config.isShowing = true;
		var _field = data['field'];
		var menuTabId = data['menuTabId'];
		if(!that.exists(menuTabId)) { // 没有打开过的菜单
			//设置只能同时打开多少个tab选项卡
			if(_config.maxSetting !== 'undefined') {
				var currentTabCount = _config.elem.children('ul.layui-tab-title').children('li').length;
				if(typeof _config.maxSetting === 'number') {
					if(currentTabCount === _config.maxSetting) {
						U.warn('为了系统的流畅度，只能同时打开' + _config.maxSetting + '个选项卡。');
						_config.isShowing = false;
						U.closeLoading();
						return;
					}
				}
				if(typeof _config.maxSetting === 'object') {
					var max = _config.maxSetting.max || 8;
					var msg = _config.maxSetting.tipMsg || '为了系统的流畅度，只能同时打开' + max + '个选项卡。';
					if(currentTabCount === max) {
						U.warn(msg);
						_config.isShowing = false;
						U.closeLoading();
						return;
					}
				}
			}
			globalTabIdIndex++;
			// 将TabId写入菜单DOM, 建立菜单-TAB关联
			data['elem'].parent().attr("menu-tab-id", globalTabIdIndex);
			if(/http:\/\//.test(_field.href) || /https:\/\//.test(_field.href)) { // 如果TAB地址为网页, 使用iframe打开
				var content = '<iframe src="' + _field.href + '" tab-type="2" tab-detail-id="' + globalTabIdIndex + '"></iframe>';
				that.render(data, content);
			} else { // 内部页面, 使用div包裹后显示
				loadHtml({
					url: _field.href,
					success: function(htmlData) {
						if(U.isEmpty(htmlData)) {
							U.error("加载页面失败, URL: " + _field.href);
							U.closeLoading();
							return;
						}
						var content = '<div tab-type="1" tab-detail-id="' + globalTabIdIndex + '" data-href="' + _field.href + '">' + htmlData + '</div>';
						that.render(data, content);
					},
					error: function() {
						_config.isShowing = false;
					}
				});
			}
		} else {
			element.tabChange(ELEM.tabFilter, menuTabId);
			U.fireEvent("tabChanged", menuTabId);
			U.closeLoading();
			_config.isShowing = false;
		}
		if(_config.contextMenu) {
			element.on('tab(' + ELEM.tabFilter + ')', function() {
				$(document).find('div.admin-contextmenu').remove();
			});
			ELEM.titleBox.on('contextmenu', "li", function(e) {
				e.preventDefault();
				e.stopPropagation();

				var $target = e.target.nodeName === 'LI' ? e.target : e.target.parentElement;
				//判断，如果存在右键菜单的div，则移除，保存页面上只存在一个
				if($(document).find('div.admin-contextmenu').length > 0) {
					$(document).find('div.admin-contextmenu').remove();
				}
				//创建一个div
				var div = document.createElement('div');
				//设置一些属性
				div.className = 'admin-contextmenu';
				div.style.width = '130px';
				div.style.backgroundColor = 'white';

				var ul = '<ul>';
				ul += '<li data-target="refresh" title="刷新当前选项卡"><i class="fa fa-refresh" aria-hidden="true"></i> 刷新</li>';
				ul += '<li data-target="closeCurrent" title="关闭当前选项卡"><i class="fa fa-close" aria-hidden="true"></i> 关闭当前</li>';
				ul += '<li data-target="closeOther" title="关闭其他选项卡"><i class="fa fa-window-close-o" aria-hidden="true"></i> 关闭其他</li>';
				ul += '<li data-target="closeAll" title="关闭全部选项卡"><i class="fa fa-window-close-o" aria-hidden="true"></i> 全部关闭</li>';
				ul += '</ul>';
				div.innerHTML = ul;
				div.style.top = e.pageY + 'px';
				div.style.left = e.pageX + 'px';
				//将dom添加到body的末尾
				document.getElementsByTagName('body')[0].appendChild(div);

				//获取当前点击选项卡的id值
				var id = $($target).find('i.layui-tab-close').attr('tab-detail-id');
				//获取当前点击选项卡的索引值
				var clickIndex = $($target).attr('lay-id');
				var $context = $(document).find('div.admin-contextmenu');
				if($context.length > 0) {
					$context.eq(0).children('ul').children('li').each(function() {
						var $that = $(this);
						//绑定菜单的点击事件
						$that.on('click', function() {
							//获取点击的target值
							var target = $that.data('target');
							switch(target) {
								case 'refresh': //刷新当前
									var $tabDetail = ELEM.contentBox.find('[tab-detail-id=' + id + ']');
									if($tabDetail.length === 0) {
										return;
									}
									var tabType = $tabDetail.attr("tab-type");
									if(tabType === "1") { // 系统内部页面
										// 触发关闭事件
										that.trigger(id, 'close');
										var dataHref = $tabDetail.attr("data-href");
										if(U.isEmpty(dataHref)) {
											return;
										}
										loadHtml({
											url: dataHref,
											success: function(htmlData) {
												if(U.isEmpty(htmlData)) {
													U.error("加载页面失败, URL: " + _field.href);
													return;
												}
												$tabDetail.html(htmlData);
											}
										});
									} else { // 外部iframe页面
										var src = $tabDetail.attr("src");
										if(U.isEmpty(src)) {
											return;
										}
										$tabDetail.attr("src", src);
									}
									break;
								case 'closeCurrent': //关闭当前
									if(clickIndex !== 0) {
										element.tabDelete(ELEM.tabFilter, clickIndex);
										that.trigger(id, 'close');
									}
									break;
								case 'closeOther': //关闭其他
									ELEM.titleBox.children('li').each(function() {
										var $t = $(this);
										var id1 = $t.find('i.layui-tab-close').attr('tab-detail-id');
										if(id1 !== id && id1 !== undefined) {
											var closeId = $t.attr('lay-id');
											element.tabDelete(ELEM.tabFilter, closeId);
											// 触发关闭事件
											that.trigger(closeId, 'close');
										}
									});
									break;
								case 'closeAll': //全部关闭
									ELEM.titleBox.children('li').each(function() {
										var $t = $(this);
										if($t.index() > _config.fixedCount - 1) { // 除了固定显示的页签, 其他的都关闭
											var closeId = $t.attr('lay-id');
											element.tabDelete(ELEM.tabFilter, closeId);
											// 触发关闭事件
											that.trigger(closeId, 'close');
										}
									});
									break;
							}
							//处理完后移除右键菜单的dom
							$context.remove();
						});
					});

					$(document).on('click', function() {
						$context.remove();
					});
				}
				return false;
			});
		}
	};
	/**
	 * 监听和触发tab事件
	 * @param _opts.tabId 标签编号
	 * @param _opts.dom 标签下的DOM
	 * @param _opts.event 事件名
	 * @param _opts.callback 事件回调
	 */
	Tab.prototype.on = function(_opts) {
		var _config = this.config;
		var tabId = _opts.dom.parent().attr('tab-detail-id');
		var tabEvents = _config['events'][tabId];
		if(tabEvents === undefined) {
			_config['events'][tabId] = [];
		}
		var events = _config['events'][tabId][_opts.event];
		if(U.isEmpty(events)) {
			_config['events'][tabId][_opts.event] = [];
		}
		_config['events'][tabId][_opts.event].push(_opts.callback);
	};

	/**
	 * 触发指定tab事件
	 * @param tabId 标签编号
	 * @param event 事件名
	 */
	Tab.prototype.trigger = function(tabId, event) {
		var _config = this.config;
		var tabEvents = _config['events'][tabId];
		if(tabEvents === undefined) {
			return;
		}
		var events = tabEvents[event];
		if(U.isEmpty(events)) {
			return;
		}
		for(var i = 0; i < events.length; i++) {
			var callback = events[i];
			if(callback === undefined || callback === null || typeof callback !== 'function') {
				continue;
			}
			callback();
		}
		if(event === 'close') {
			// 清除事件缓存
			_config['events'][tabId] = undefined;
		}
	};

	/**
	 * 渲染TAB页
	 */
	Tab.prototype.render = function(data, content) {
		var that = this;
		var _config = that.config;
		var _field = data['field'];
		var title = '';
		if(!U.isEmpty(_field.icon)) {
			if(_field.icon.indexOf('fa-') !== -1) {
				title += '<i class="fa ' + _field.icon + '" aria-hidden="true"></i>';
			} else if(_field.icon.indexOf('layui-icon-') !== -1) {
				title += '<i class="layui-icon ' + _field.icon + '" aria-hidden="true"></i>';
			} else {
				title += '<i class="layui-icon">' + _field.icon + '</i>';
			}
		}
		title += '<cite>' + _field.title + '</cite>';
		if(_config.closed) {
			title += '<i class="layui-icon layui-unselect layui-tab-close" tab-detail-id="' + globalTabIdIndex + '">&#x1006;</i>';
		}
		// 添加tab
		element.tabAdd(ELEM.tabFilter, {
			title: title,
			content: content,
			id: globalTabIdIndex
		});
		// iframe 自适应
		ELEM.contentBox.find('iframe[tab-detail-id=' + globalTabIdIndex + ']').each(function() {
			$(this).height(ELEM.contentBox.height());
		});
		if(_config.closed) {
			// 监听关闭事件
			ELEM.titleBox.find('li').children('i.layui-tab-close[tab-detail-id=' + globalTabIdIndex + ']').on('click', function() {
				var tabId = $(this).parent('li').attr('lay-id');
				U.fireEvent("tabClose-" + tabId);
				element.tabDelete(ELEM.tabFilter, tabId).init();
				if(_config.contextMenu) {
					$(document).find('div.uiba-contextmenu').remove(); // 移除右键菜单dom
				}
				// 触发标签关闭事件
				that.trigger(tabId, 'close');
			});
		}
		// 切换到当前打开的选项卡
		element.tabChange(ELEM.tabFilter, globalTabIdIndex);
		U.fireEvent("tabChanged", globalTabIdIndex);
		_config.isShowing = false;
	};

	var tab = new Tab();
	exports(mod_name, function(options) {
		return tab.set(options);
	});
});