/**
 * 下拉框联动插件
 */
layui.use(['form'], function () {
    "use strict";
    let form = layui.form;

    /**
     * 主入口
     * @param _opts 配置
     * @constructor
     */
    let Linkage = window.Linkage = function (_opts) {
        this.config = $.extend({}, Linkage.DEFAULTS, _opts, {
            filter: _opts.id + '-linkage',
            total: _opts.selects.length
        });
        this.handler();
        this.init();
    }

    /**
     * 默认参数
     * @type {{version: string}}
     */
    Linkage.DEFAULTS = {
        version: '1.0'
    }

    /**
     * 初始化数据
     */
    Linkage.prototype.init = function () {
        let _config = this.config;
        for (let i = 0; i < _config.selects.length; i++) {
            let $dom = _config.selects[i].$dom;
            $dom.attr('data-index', i);
            $dom.attr('lay-filter', _config.filter);
            $dom.html('<option value="">' + _config.selects[i].defaultText + '</option>');
        }
        this.initSelect(0);
    };

    /**
     * 添加事件绑定
     */
    Linkage.prototype.handler = function () {
        let that = this;
        let _config = that.config;
        form.on('select(' + _config.filter + ')', function (obj) {
            let $elem = $(obj.elem);
            let index = parseInt($elem.data('index'));
            if (index + 1 < _config.total) { // 如果不是最后一个元素
                that.initSelect(index + 1);
            }
        });
    };

    /**
     * 初始化指定级别的下拉框
     * @param index select顺序下标
     */
    Linkage.prototype.initSelect = function (index) {
        let _config = this.config;
        let _selects = _config['selects'];
        let preSelect, preVal;
        if (index !== 0) {
            preSelect = _selects[index - 1];
            preVal = preSelect.$dom.val();
        }
        let thisSelect = _selects[index];
        if (index !== 0 && U.isEmpty(preVal)) {
            thisSelect['$dom'].html('<option value="">' + thisSelect.defaultText + '</option>');
            thisSelect['$dom'].next().find('[lay-value=""]').click();
            form.render('select');
            return;
        }
        // 设置参数
        let params = thisSelect['param'] || {};
        if (index !== 0 && thisSelect['parentKey']) {
            params[thisSelect['parentKey']] = preVal;
        }
        U.initSelect({
            $dom: thisSelect.$dom,
            url: thisSelect.url,
            param: params,
            idField: thisSelect['idField'],
            nameField: thisSelect['nameField'],
            defaultText: thisSelect.defaultText,
            callback: function () {
                form.render('select');
                if (!isEmpty(thisSelect.val)) {
                    thisSelect['$dom'].next().find('[lay-value=' + thisSelect.val + ']').click();
                    thisSelect.val = undefined;
                } else {
                    _selects[index]['$dom'].next().find('[lay-value=""]').click();
                }
            }
        });
    };

    /**
     * 调用入口，设置初始化数据
     * @param _opts.id 唯一标识符, 用于lay-filter事件侦听筛选
     * @param _opts.selects 下拉框列表, 按照顺序级联, 可支持2-N级
     * @param _opts.selects.$dom 下拉框的jq对象
     * @param _opts.selects.url 下拉框的请求URL
     * @param _opts.selects.val 下拉框的默认值
     * @param _opts.selects.parentKey 后台参数中与上级菜单关联字段名
     * @returns {Window.Linkage}
     */
    Linkage.set = function (_opts) {
        if (isEmpty(_opts) || isEmpty(_opts['selects'])) {
            throwError('参数错误');
        }
        return new Linkage(_opts);
    };

    /**
     * 抛出异常
     * @param msg
     */
    function throwError(msg) {
        throw new Error(msg);
    }

    /**
     * 是否为空或length值为0
     * @param obj
     * @returns {boolean}
     */
    function isEmpty(obj) {
        return obj === undefined || obj === null || obj === '' || obj.length === 0;
    }
});