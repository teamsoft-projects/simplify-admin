/**
 * Pager页面组件封装
 * @version 1.0
 */
+function () {
    'use strict';

    /**
     * 分页构造函数
     * @param _opts 参数
     * @constructor
     */
    let Pager = window.Pager = function (_opts) {
        this.config = {id: _opts.id, param: {}}; // 页面配置
        this.cur = 1; // 当前页码
        this.init(_opts);
    }

    /**
     * 初始化设置
     */
    Pager.DEFAULTS = {
        url: undefined, // 数据远程地址
        type: 'POST', // 数据的获取方式  get or post
        $dataList: undefined, // 内容容器
        openWait: true, // 加载数据时是否显示等待框
        $dataListTemplate: undefined, //模板容器
        tempType: 0, // 模板类型, 0.模板DOM对象 1.模板字符串
        paged: true, // 是否开启分页
        $paged: undefined, // 分页DOM
        pageSize: 16, // 分页大小
        success: undefined, // 分页加载成功回调
        fail: undefined, // 分页加载失败回调
        total: 0, // 当前查询条件下的总数据数
        complete: undefined, // 分页加载完成回调
        isRowSelect: true, // 是否开启点击行自动选中事件
        isOperation: true, // 是否开启增删改按钮事件监听
        tabId: undefined, // 当前页所在标签编号
        tabClose: undefined, // 当前页所在标签关闭回调
        $query: undefined, // 查询按钮
        $queryArea: undefined, // 查询区域DOM
        querySelect: undefined, // 下拉框查询条件配置
        linkageSelect: undefined, // 联动下拉框配置
        queryWidth: undefined, // 查询区域宽度
        queryComplete: undefined, // 查询区域加载完成回调
        afterQuery: undefined, // 查询完成后回调
        isEditList: false, // 是否编辑页也是一个列表页
        $funcArea: undefined, // 权限点按钮区域DOM
        $addEditTemplate: undefined, // 新增编辑弹窗模板
        saveUrl: undefined, // 保存URL
        presetParam: undefined, // 预置参数
        events: undefined, // 组件事件缓存
        addDialogName: 'add-dialog',// 新增弹窗dom名
        $dialog: undefined, // 新增/编辑弹窗的layer jq dom对象
        dialogArea: undefined
    };

    /**
     * 版本号
     */
    Pager.prototype.v = '2.0';

    /**
     * 创建page对象入口
     * @param _opts
     * @returns {Window.Pager}
     */
    Pager.create = function (_opts) {
        return new Pager(_opts);
    };

    /**
     * 初始化
     */
    Pager.prototype.init = function (_opts) {
        let $dom = $('#' + this.config.id);
        if ($dom.length === 0) {
            throwError('页面ID配置错误');
        }
        // 合并配置参数
        this.config = $.extend({}, Pager.DEFAULTS, this.config, {
            $dom: $dom,
            url: _opts.url || $dom.data('url'),
            $dataList: $dom.find('[name=dataList]'),
            $dataListTemplate: $dom.find('[name=dataListTemplate]'),
            $paged: $dom.find('[name=paged]'),
            $mainPage: $dom.find('[name=mainPage]'),
            $subPage: $dom.find('[name=subPage]'),
            $detailPage: $dom.find('[name=detailPage]'),
            tabId: $dom.parent('div').attr('tab-detail-id'),
            $query: $dom.find('[name=query]'),
            $queryArea: $dom.find('.query-card'),
            querySelect: [],
            $funcArea: $dom.find('[name=func-area]'),
            $addEditTemplate: $dom.find('[name=addEditTemplate]'),
            saveUrl: $dom.data('save-url'),
            presetParam: {},
            events: [],
            dialogArea: _opts.dialogArea || ['680px']
        }, _opts);
        let _config = this.config;
        if (_config.url === undefined) {
            throwError('Paging Error:请配置远程URL!');
        }
        if (_config.$dataList === undefined || _config.$dataList.length === 0) {
            throwError('Paging Error: 数据列表DOM配置错误!');
        }
        if (_config.tempType === 0) {
            if (_config.$dataListTemplate === undefined || _config.$dataListTemplate.length === 0) {
                throwError('Paging Error: 模板容器DOM配置错误!');
            }
        }
        if (_config.paged && _config.$paged.length === 0) {
            throwError('Paging Error: 分页开启时, 必须配置分页DOM!');
        }
        if (_config.type.toUpperCase() !== 'GET' && _config.type.toUpperCase() !== 'POST') {
            throwError('Paging Error: type参数配置出错，只支持GET或都POST');
        }
        this.handler();
        this.refresh(true);

        return this;
    };

    /**
     * 配置页面上的按钮事件
     */
    Pager.prototype.handler = function () {
        // 绑定操作按钮事件
        let that = this;
        let _config = this.config;
        // 处理按钮权限点
        _config.$funcArea.find('a[layui-auth]').each(function (key, val) {
            let $val = $(val);
            let identify = $val.attr('layui-auth');
            let authority = window.Authorize[identify];
            if (U.isEmpty(authority)) {
                $val.remove();
            } else {
                $val.attr('data-url', authority['url']);
                $val.find('i').addClass(authority['icon']);
            }
        });

        if (_config.isOperation) {
            // 新增按钮
            let $add = _config.$dom.find('[name=add]');
            if ($add.length !== 0) {
                let addUrl = $add.attr("data-url");
                if (U.isEmpty(addUrl)) {
                    throwError('未配置按钮的链接地址');
                }
                $add.click(function () {
                    U.loading();
                    loadHtml({
                        url: addUrl,
                        param: $.extend({}, _config.presetParam.add),
                        success: function (html) {
                            _config.$subPage.html(html);
                            Pager.renderWidgets(_config.$subPage);
                            _config.$mainPage.hide();
                            _config.$subPage.show();
                            if (!_config.isEditList) {
                                U.closeLoading();
                            }
                        }
                    });
                });
            }
            // 修改按钮
            let $edit = _config.$dom.find('[name=edit]');
            if ($edit.length !== 0) {
                let editUrl = $edit.attr("data-url");
                if (U.isEmpty(editUrl)) {
                    throwError('未配置按钮的链接地址');
                }
                $edit.click(function () {
                    let $checked = _config.$dataList.find("input:checkbox:checked");
                    if ($checked.length === 0 || $checked.length > 1) {
                        U.warn("修改时必须选中一条记录");
                        return;
                    }
                    let id = $checked.eq(0).attr("identity");
                    loadHtml({
                        url: editUrl,
                        param: $.extend({id: id}, _config.presetParam.edit),
                        success: function (html) {
                            _config.$subPage.html(html);
                            Pager.renderWidgets(_config.$subPage);
                            _config.$mainPage.hide();
                            _config.$subPage.show();
                            U.closeLoading();
                        }
                    });
                });
            }
            // 删除按钮
            let $remove = _config.$dom.find('[name=remove]');
            if ($remove.length !== 0) {
                let removeUrl = $remove.attr("data-url");
                if (U.isEmpty(removeUrl)) {
                    throwError('未配置按钮的链接地址');
                }
                $remove.click(function () {
                    let checked = _config.$dataList.find("input:checkbox:checked").checkedList();
                    if (checked.length === 0) {
                        U.warn("删除时必须至少选中一条记录");
                        return;
                    }
                    U.confirm("确定删除 " + checked.length + " 条记录吗?", function () {
                        ajaxCall({
                            url: removeUrl,
                            param: checked,
                            success: function () {
                                that.refresh();
                            }
                        });
                    });
                });
            }
            // 详情按钮
            let $detail = _config.$dom.find('[name=detail]');
            if ($detail.length !== 0) {
                let detailUrl = $detail.attr("data-url");
                if (U.isEmpty(detailUrl)) {
                    throwError('未配置按钮的链接地址');
                }
                $detail.click(function () {
                    let $checked = _config.$dataList.find("input:checkbox:checked");
                    if ($checked.length === 0 || $checked.length > 1) {
                        U.warn("查看详情时必须选中一条记录");
                        return;
                    }
                    let id = $checked.eq(0).attr("identity");
                    loadHtml({
                        url: detailUrl,
                        param: $.extend({id: id}, _config.presetParam.edit),
                        success: function (html) {
                            _config.$detailPage.html(html);
                            Pager.renderWidgets(_config.$detailPage);
                            _config.$mainPage.hide();
                            _config.$detailPage.show();
                            U.closeLoading();
                        }
                    });
                });
            }
            let dialogIndex;
            // 新增弹窗按钮
            let $addDialog = _config.$dom.find('[name=' + _config.addDialogName + ']');
            if ($addDialog.length !== 0) {
                let addParam = $.extend({}, _config.presetParam.add);
                $addDialog.click(function () {
                    laytpl(_config.$addEditTemplate.html()).render(addParam, function (result) {
                        dialogIndex = layer.open({
                            type: 1,
                            area: _config.dialogArea,
                            shadeClose: false,
                            title: '新增',
                            content: result,
                            success: function ($layero) {
                                _config.$dialog = $layero;
                                that.renderDialog();
                                Pager.renderWidgets($layero);
                                that.trigger('add-dialog-shown');
                                form.render();
                            }
                        });
                    });
                });
            }
            // 修改弹窗按钮
            let $editDialog = _config.$dom.find('[name=edit-dialog]');
            if ($editDialog.length !== 0) {
                let editUrl = $editDialog.data('url');
                if (U.isEmpty(editUrl)) {
                    window.throwError('未配置按钮的链接地址');
                }
                $editDialog.click(function () {
                    let $checked = _config.$dataList.find("input:checkbox:checked");
                    if ($checked.length === 0 || $checked.length > 1) {
                        U.warn("编辑时必须选中一条记录");
                        return;
                    }
                    let id = $checked.eq(0).attr("identity");
                    ajaxCall({
                        url: editUrl,
                        param: {id: id},
                        success: function (_resp) {
                            laytpl(_config.$addEditTemplate.html()).render(_resp.data, function (result) {
                                dialogIndex = layer.open({
                                    type: 1,
                                    area: _config.dialogArea,
                                    shadeClose: false,
                                    title: '编辑',
                                    content: result,
                                    success: function ($layero) {
                                        _config.$dialog = $layero;
                                        that.renderDialog();
                                        Pager.renderWidgets($layero);
                                        form.render();
                                    }
                                });
                            });
                        }
                    });
                });
            }
            // 监听弹窗表单提交
            form.on('submit(' + _config.id + '-dialog-submit)', function (data) {
                U.loading();
                ajaxCall({
                    url: _config.saveUrl,
                    param: data.field,
                    success: function () {
                        that.refresh();
                        layer.close(dialogIndex);
                    }
                });
                return false;
            });
            // 查询区域加载完成回调
            if (_config.queryComplete) {
                _config.queryComplete(that);
            }
        }
        // 渲染查询区域
        // 初始化查询中的下拉框
        if (!U.isEmpty(_config.querySelect)) {
            let len = _config.querySelect.length;
            $.each(_config.querySelect, function (key, val) {
                let selectConfig = $.extend({
                    $dom: _config.$dom.find('[name=' + val['name'] + ']'),
                    callback: function () {
                        if (key === len - 1) {
                            form.render('select');
                        }
                    }
                }, val);
                U.initSelect(selectConfig);
            });
        }
        // 初始化查询中的联动下拉框
        if (window.Linkage && !U.isEmpty(_config.linkageSelect)) {
            let linkageSelect = _config.linkageSelect;
            if (linkageSelect.constructor !== Array) {
                linkageSelect = new Array(linkageSelect);
            }
            for (let i = 0; i < linkageSelect.length; i++) {
                let linkage = linkageSelect[i];
                let selects = linkage.selects;
                for (let j = 0; j < selects.length; j++) {
                    selects[j]['$dom'] = _config.$queryArea.find(selects[j]['el']);
                }
                Linkage.set(linkage);
            }
        }
        // 初始化查询中的多选下拉框
        if (!U.isEmpty(_config.muiltiSelect)) {
            if (_config.muiltiSelect.constructor !== Array) {
                _config.muiltiSelect = new Array(_config.muiltiSelect);
            }
            let muiltiSelect = _config.muiltiSelect;
            for (let i = 0; i < muiltiSelect.length; i++) {
                let mSelect = muiltiSelect[i];
                mSelect['select1'] = xmSelect.render({
                    el: '#' + _config.id + ' ' + mSelect.el,
                    prop: mSelect.prop || {},
                    tips: mSelect.tips || ''
                });
                ajaxCall({
                    url: mSelect.url,
                    param: mSelect.param || {},
                    success: function (_resp) {
                        mSelect['select1'].update({data: _resp['data']});
                    }
                })
            }
        }
        // 监听查询按钮点击
        form.on("submit(querySubmit-" + _config.id + ")", function (data) {
            // 填入多选下拉参数
            if (_config.muiltiSelect) {
                for (let i = 0; i < _config.muiltiSelect.length; i++) {
                    let mSelect = _config.muiltiSelect[i];
                    data.field[mSelect['name']] = mSelect['select1'].getValue('valueStr');
                }
            }
            _config.param = {};
            $.extend(_config.param, data.field);
            that.refresh();
            if (_config.afterQuery) {
                _config.afterQuery(_config.param);
            }
        });
        // 监听重置事件
        _config.$dom.on('click', 'button[name=reset]', function () {
            if (!U.isEmpty(_config.muiltiSelect)) {
                // 单独处理多选表格
                for (let i = 0; i < _config.muiltiSelect.length; i++) {
                    let mSelect = _config.muiltiSelect[i];
                    mSelect['select1'].setValue([]);
                }
            }
            setTimeout(function () {
                that.trigger('reset');
                _config.param = {};
                that.ensureParam();
                that.refresh(true);
            }, 100);
        });
        // 监听查询区域输入框内按回车事件
        _config.$dom.on('keypress', '.query-card :input', function (e) {
            if (e.which === 13) {
                e.preventDefault();
                _config.$query.click();
            }
        });
        // 监听编辑页返回事件
        U.addEvent(_config.id + "-back", function () {
            _config.$subPage.hide().empty();
            _config.$detailPage.hide().empty();
            _config.$mainPage.show();
            that.refresh();
        });
        // tab页关闭回调
        if (_config.tabClose) {
            U.addEvent("tabClose-" + _config.tabId, function () {
                _config.tabClose();
            });
        }

        this.ensureParam();
    };

    /**
     * 渲染新增/编辑弹窗中的select等
     */
    Pager.prototype.renderDialog = function () {
        let _config = this.config;
        if (!U.isEmpty(_config.dialogSelect)) {
            let len = _config.dialogSelect.length;
            $.each(_config.dialogSelect, function (key, val) {
                let selectConfig = $.extend({
                    $dom: _config.$dialog.find('[name=' + val['name'] + ']'),
                    callback: function () {
                        if (key === len - 1) {
                            form.render('select');
                        }
                    }
                }, val);
                U.initSelect(selectConfig);
            });
        }
    }

    /**
     * 重新获取一次查询参数包括隐藏参数
     * 并将结果放入config.param
     */
    Pager.prototype.ensureParam = function () {
        let params = this.config.$query.parents('form').serializeArray();
        let paramObj = {};
        params.forEach(function (item) {
            if (!U.isEmpty(item.value)) {
                paramObj[item.name] = item.value;
            }
        });
        $.extend(this.config.param, paramObj, this.config.presetParam.query);
        return this.config.param;
    }

    /**
     * Page组件自定义事件
     * @param key 事件名
     * @param callback 回调函数
     */
    Pager.prototype.on = function (key, callback) {
        let events = this.config.events;
        if (!events[key]) {
            events[key] = [];
        }

        events[key].push(callback);
    }

    /**
     * Pager组件自定义事件触发
     * @param key 事件名
     */
    Pager.prototype.trigger = function (key) {
        let events = this.config.events;
        if (!events[key]) {
            return this;
        }

        for (let i = 0; i < events[key].length; i++) {
            let callback = events[key][0];
            if (typeof callback === 'function') {
                callback.call(this, this);
            }
        }
    }

    /**
     * 渲染自定义组件
     * 开放方法
     */
    Pager.renderWidgets = function ($dom) {
        if (!$dom) {
            return;
        }
        $dom.find('[data-type=dic]').each(function (idx, elem) {
            let $elem = $(elem);
            $elem.attr('hidden', 'hidden');
            let widgetType = $elem.attr('widget-type');
            let dicGroup = $elem.attr('dic-group');
            let dataName = $elem.attr('data-name');
            let dataValue = $elem.attr('data-value');
            if (U.isEmpty(widgetType) || U.isEmpty(dicGroup) || U.isEmpty(dataName)) {
                throwError("字典组件配置错误，请检查配置。")
            }
            ajaxCall({
                url: 'sys/dictionary/getDicByGroupFromCache',
                param: {groupCode: dicGroup},
                success: function (_resp) {
                    let data = _resp['data'];
                    let html = '';
                    $.each(data, function (idx, val) {
                        let code = val['code'];
                        let name = val['name'];
                        if (code === dataValue) {
                            html += '<input type="radio" name="' + dataName + '" value="' + code + '" title="' + name + '" checked>';
                        } else {
                            html += '<input type="radio" name="' + dataName + '" value="' + code + '" title="' + name + '">';
                        }
                    })
                    $elem.before(html);
                    form.render('radio');
                }
            })
        })
    }

    /**
     * 刷新分页
     */
    Pager.prototype.refresh = function (isFirst) {
        if (isFirst === true) {
            this.cur = 1;
        }
        this.get();
    };

    /**
     * 获取数据
     */
    Pager.prototype.get = function () {
        let that = this;
        let _config = that.config;
        let loadIndex = undefined;
        if (_config.openWait) {
            loadIndex = U.loading();
        }
        // 查询参数
        let df = {
            pageStart: (this.cur - 1) * _config.pageSize,
            pageSize: _config.pageSize
        };
        $.extend(true, _config.param, df);
        ajaxCall({
            type: _config.type,
            url: _config.url,
            param: _config.param,
            isAutoCloseLoading: _config.isAutoCloseLoading,
            success: function (_resp) {
                let result = _resp.data;
                let total = _config.total = _resp.total;
                if (_config.paged && that.cur !== 1) {
                    let sub = parseInt(total / _config.pageSize + '');
                    let maxIndex = total % _config.pageSize === 0 ? sub : sub + 1;
                    if (that.cur > maxIndex) {
                        that.cur = maxIndex;
                        that.get();
                        return;
                    }
                }
                if (!U.isEmpty(result)) {
                    // 获取模板
                    let tpl = _config.tempType === 0 ? _config.$dataListTemplate.html() : _config.$dataListTemplate;
                    // 渲染数据
                    laytpl(tpl).render(result, function (html) {
                        _config.$dataList.html(html);
                    });
                    if (_config.paged) {
                        if (total === null) {
                            throwError('Paging Error:请返回数据总数！');
                            return;
                        }
                        laypage.render({
                            elem: _config.$paged[0],
                            count: total,
                            limit: _config.pageSize,
                            curr: that.cur,
                            layout: ['prev', 'page', 'next', 'count', 'skip'],
                            jump: function (obj, first) {
                                // 得到了当前页，用于向服务端请求对应数据
                                that.cur = obj.curr;
                                if (!first) {
                                    that.get({
                                        pageIndex: that.cur
                                    });
                                }
                            }
                        });
                    }
                    if (_config.success) {
                        _config.success(); //渲染成功
                    }
                } else {
                    _config.$dataList.html('<tr><td class="layui-nodata" colspan="99">无数据</td>\</tr>');
                    if (_config.paged) {
                        if (total === null) {
                            throwError('Paging Error:请返回数据总数！');
                            return;
                        }
                        laypage.render({
                            elem: _config.$paged[0],
                            count: total,
                            limit: _config.pageSize,
                            curr: that.cur,
                            layout: ['prev', 'page', 'next', 'count', 'skip'],
                            jump: function (obj, first) {
                                // 得到了当前页，用于向服务端请求对应数据
                                that.cur = obj.curr;
                                if (!first) {
                                    that.get({
                                        pageIndex: that.cur
                                    });
                                }
                            }
                        });
                    }
                }
                that.checkAllFilterState(_config.$dataList);
                // 渲染页面
                form.render('checkbox'); // 重新渲染复选框
                // 监听全选checkbox
                form.on('checkbox(allselector-' + _config.id + ')', function (data) {
                    let elem = data.elem;
                    _config.$dataList.children('tr').each(function () {
                        let $this = $(this);
                        // 全选或反选
                        let $checkbox = $this.children('td').eq(0).children('input[type=checkbox]');
                        if ($checkbox.length !== 0) {
                            $checkbox[0].checked = elem.checked;
                        }
                    });
                    form.render('checkbox');
                });
                // 监听数据行checkbox点击, 设置全选checkbox状态
                form.on('checkbox(selector-' + _config.id + ')', function () {
                    that.checkAllFilterState(_config.$dataList);
                    form.render('checkbox');
                });
                if (_config.isRowSelect) { // 开启行点击事件情况, 绑定事件
                    // 绑定行点击事件
                    _config.$dataList.find("tr").on("click", function (e) {
                        let $target = $(e.target);
                        // 按钮和行首checkbox点击不处理
                        if ($target.is('.layui-btn') || ($target.is('i') && $target.parent().is('div.layui-unselect'))) {
                            return;
                        }
                        let $this = $(this);
                        let checkbox = $this.children('td').eq(0).children('input[type=checkbox]')[0];
                        checkbox.checked = !checkbox.checked;
                        that.checkAllFilterState(_config.$dataList);
                        form.render('checkbox');
                    });
                }
                // 绑定
                if (_config.complete) { // 渲染完成回调
                    _config.complete(result);
                }
                if (loadIndex !== undefined) {
                    layer.close(loadIndex); //关闭等待层
                }
            },
            error: function () {
                if (loadIndex !== undefined) {
                    layer.close(loadIndex); //关闭等待层
                }
            }
        });
    };

    /**
     * 检查并设置全选的状态
     */
    Pager.prototype.checkAllFilterState = function (elem) {
        let isAllSelected = true; // 全部选中
        $(elem).children('tr').each(function () {
            let $this = $(this);
            let $checkbox = $this.children('td').eq(0).children('input[type=checkbox]');
            if ($checkbox.length === 0) {
                isAllSelected = false;
                return true;
            }
            let isSelected = $checkbox[0].checked;
            isAllSelected = isAllSelected && isSelected;
        });
        let allSelector = $(elem).parent().find("input[type=checkbox][lay-filter=allselector-" + this.config.id + "]")[0];
        if (!U.isEmpty(allSelector)) {
            allSelector.checked = isAllSelected;
        }
    };

    /**
     * 抛出一个异常错误信息
     * @param {String} msg
     */
    function throwError(msg) {
        console.error(msg);
    }
}();