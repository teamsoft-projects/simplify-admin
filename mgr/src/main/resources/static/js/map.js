/**
 * 百度地图Map对象封装
 * @author zhangcc
 * @version 20170919
 */
+function() {
	/**
	 * 构造函数
	 * @constructor
	 */
	window.Map = function(_opts) {
		this.config = $.extend(false, _opts, Map.DEFAULTS);
		this.init();
	};

	/**
	 * 主入口
	 * @param _opts 参数
	 * @returns {Window.Map}
	 */
	Map.create = function(_opts) {
		return new Map(_opts);
	};

	/**
	 * 默认参数
	 * @type {{}}
	 */
	Map.DEFAULTS = {
		bMap: undefined, // 百度地图对象实例
		id: "layui-bmap", // 地图唯一标识符
		_Point: {
			_lng: 103.919406,
			_lat: 34.184486
		}, // 默认位置
		_lng: 103.919406, // 默认经度
		_lat: 34.184486, // 默认纬度
		_zoom: 3, // 默认放大系数
		$dom: undefined, // 地图依附的DOM对象
		url: undefined, // 后台加载地图Marker数据的URL
		param: undefined, // 后台加载地图Marker数据的请求参数
		control: undefined, // 地图缩放控件
		click: undefined, // 地图点击事件
		ready: undefined, // 地图加载成功后回调
		readyState: false, // 地图是否已加载、渲染完成
		readyStack: [], // 地图加载完成回调函数栈
		search: undefined, // 本地搜索配置
		searchInstance: undefined // 本地搜索BMap对象
	};

	/**
	 * 初始化地图
	 */
	Map.prototype.init = function() {
		var that = this;
		var _config = this.config;
		if(!_config.bMap) { // 初始化Map对象
			var interval = setInterval(function() {
				if(!_config.$dom.is(":hidden")) {
					clearInterval(interval);
					_config.bMap = new BMap.Map(_config.$dom[0]);
					that.initOptions();
					that.handler();
					that.ready();
				}
			}, 50);
		} else {
			that.initOptions();
			that.handler();
			that.ready();
		}
	};

	/**
	 * 初始化地图配置
	 */
	Map.prototype.initOptions = function() {
		var that = this;
		var _config = that.config;
		// 添加地图缩放控制控件
		if(_config.control) {
			_config.bMap.addControl(_config.control);
		}
		// 添加自动滚轮缩放功能
		if(_config.enableScrollWheelZoom) {
			_config.bMap.enableScrollWheelZoom();
		}
		var point = new BMap.Point(_config._lng, _config._lat);
		_config.bMap.centerAndZoom(point, _config._zoom); // 初始化地图，设置中心点坐标和地图级别
		// 添加地图加载完成后回调
		if(_config.ready) {
			_config.ready(this);
		}
		// 配置本地搜索
		if(_config.search) {
			_config.searchInstance = new BMap.LocalSearch(_config.bMap, _config.search);
		}
	};

	/**
	 * 刷新后台Marker数据
	 */
	Map.prototype.refresh = function() {
		var that = this;
		var _config = that.config;
		// 加载地图Marker点数据
		if(_config.url) {
			ajaxCall({
				url: _config.url,
				param: _config.param,
				success: function(_resp) {
					that.addMarkers(_resp.data);
				}
			});
		}
	};

	/**
	 * 添加事件监听
	 */
	Map.prototype.handler = function() {
		var that = this;
		var _config = that.config;
		if(_config.click) {
			_config.bMap.addEventListener("click", function(e) {
				_config.click(e, that);
			});
		}
	};

	/**
	 * 在地图上添加标注
	 * @param points 待添加标注的点坐标
	 * @param notViewPort 是否不以新增加的标记点为中心
	 */
	Map.prototype.addMarkers = function(points, notViewPort) {
		var that = this;
		if(that.config.readyState) {
			_addMarkers();
		} else {
			that.ready(function() {
				_addMarkers();
			});
		}

		function _addMarkers() {
			var _config = that.config;
			var ovys = _config.bMap.getOverlays();
			$.each(ovys, function(key, val) {
				_config.bMap.removeOverlay(val);
			});
			if(!U.isEmpty(points)) {
				var pots = [];
				$.each(points, function(key, val) {
					if(val['lng'] === 0 || isNaN(val['lng']) || val['lat'] === 0 || isNaN(val['lat'])) {
						return true;
					}
					var point = new BMap.Point(val['lng'], val['lat']);
					pots.push(point);
					_config.bMap.addOverlay(new BMap.Marker(point));
				});
				if(notViewPort !== true) {
					// 解决快速切换地图时, 视野指定错误问题
					setTimeout(function() {
						_config.bMap.setViewport(pots);
					}, 500);
				}
			}
		}
	};

	/**
	 * 地图加载完成后调用方法
	 */
	Map.prototype.ready = function(func) {
		var _config = this.config;
		if(func === undefined) {
			for(var i = 0; i < _config.readyStack.length; i++) {
				_config.readyStack[i]();
			}
			_config.readyStack = [];
			_config.readyState = true;
			return;
		}
		if(typeof func === 'function') {
			if(_config.readyState) {
				func();
				return;
			}
			_config.readyStack.push(func);
		}
	};
}();