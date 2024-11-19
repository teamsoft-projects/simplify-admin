let U = {};
// 获取项目名
let app_path = generateAppPath();
// 首页路径
let index_path = app_path + "/index";
// 登录用户信息
let loginUser = {
    loginName: $("#loginName").val(),
    name: $("#username").val()
};

/**
 * 获取app_path
 */
function generateAppPath() {
    let scripts = document.getElementsByTagName('script');
    let cur = scripts[scripts.length - 1];
    let src = cur.src;
    let idx = src.lastIndexOf('?');
    if (idx > 0) {
        src = src.substr(idx + 1);
        let paramArr = src.split('&');
        for (let i = 0; i < paramArr.length; i++) {
            let keyArr = paramArr[i].split('=');
            if (keyArr[0] === 'url') {
                return keyArr[1];
            }
        }
    }
    return "";
}

// 一键复制
let $copyText = $('#one-key-copy');
$(document.body).on('click', '.one-key-copy', function (e) {
    let val = $(this).text();
    if (!U.isEmpty(val)) {
        $copyText.val(val);
        $copyText[0].select();
        document.execCommand("Copy");
        U.success("复制成功", {time: 400});
    }
    e.preventDefault();
    e.stopPropagation();
    return false;
});

/**
 * Ajax请求封装
 * @param _opts.url {string} 请求URL
 * @param _opts.param {object} 请求参数
 * @param _opts.type {string} 请求Method
 * @param _opts.dataType {string} 数据类型
 * @param _opts.isAutoLoading {boolean} 是否显示Loading
 * @param _opts.isAutoCloseLoading {boolean} 是否自动关闭Loading
 * @param _opts.async {boolean} 是否异步请求
 * @param _opts.contentType {string} 请求类型
 * @param _opts.processData {boolean} 是否转换请求参数
 * @param _opts.success {function} 请求成功回调
 * @param _opts.isShowNotice {boolean} 是否自动显示提示
 * @param _opts.isShowErrorNotice {boolean} 是否自动显示错误提示
 * @param _opts.notice {string} 默认提示信息
 * @param _opts.error {function} 出错回调
 * @param _opts.complete {function} 完成回调
 */
function ajaxCall(_opts) {
    _opts = _opts || {};
    if (U.isEmpty(_opts.url)) {
        U.error("Ajax请求必须传入URL!");
        return;
    }
    if (_opts.isAutoLoading !== false) {
        U.loading();
    }
    $.ajax({
        async: _opts.async !== false,
        url: app_path + '/' + _opts.url,
        data: _opts.param || {},
        type: _opts.type || 'POST',
        dataType: _opts.dataType || 'json',
        contentType: _opts.contentType || 'application/x-www-form-urlencoded',
        processData: _opts.processData !== false,
        success: function (_resp) {
            if (U.isEmpty(_resp)) {
                U.showExceptionDialog();
                return;
            }
            if (U.checkExceptionAndPermission(_resp, _opts.isShowNotice)) {
                if (_opts.success) {
                    _opts.success(_resp);
                }
            }
            if (_resp.flag === 100303 && _opts.notice) {
                _opts.notice(_resp.data);
            }
            if (_opts.isAutoCloseLoading !== false) {
                U.closeLoading();
            }
        },
        error: function (_xhr) {
            if (_opts.error) {
                _opts.error(_xhr);
            }
            if (_opts.isAutoCloseLoading !== false) {
                U.closeLoading();
            }
            if (_opts.isShowErrorNotice !== false) {
                if (_xhr.status === 0 || _xhr.status === 503) {
                    U.error("网络异常, 请检查网络是否通畅!");
                } else {
                    U.showExceptionDialog();
                }
            }
        },
        complete: function () {
            U.closeLoading();
            if (_opts.complete) {
                _opts.complete();
            }
        }
    });
}

/**
 * 根据请求地址获取HTML信息
 * @param _opts.async 是否异步
 * @param _opts.url 请求地址
 * @param _opts.param 请求参数
 * @param _opts.type 请求类型(GET|POST)
 * @param _opts.success 成功回调
 * @param _opts.error 出错回调
 * @param _opts.isShowNotice 是否直接显示业务提示
 * @param _opts.notice 有业务提示时回调
 */
function loadHtml(_opts) {
    U.loading();
    $.ajax({
        async: _opts.async !== false,
        url: app_path + '/' + _opts.url,
        data: _opts.param || {},
        type: _opts.type || "POST",
        dataType: "text",
        success: function (data, flag, xhr) {
            if (/^{/.test(data)) {
                let dataTemp;
                try {
                    dataTemp = JSON.parse(data);
                } catch (e) {
                    dataTemp = {flag: 100102, message: '处理失败'};
                }
                U.checkExceptionAndPermission(dataTemp, _opts.isShowNotice);
                if (dataTemp.flag === 100303 && _opts.notice) {
                    _opts.notice(dataTemp);
                }
                U.closeLoading();
                return;
            }
            if (xhr.responseText.indexOf('<html aria-label="login"') !== -1) {
                window.location = app_path + '/sys/user/login';
                return;
            }
            if (_opts.success) {
                _opts.success(data);
            }
        },
        error: function (xhr) {
            if (_opts.error) {
                _opts.error();
            }
            U.closeLoading();
            if (xhr.status === 0 || xhr.status === 503) {
                U.error("网络异常, 请检查网络是否通畅!");
            } else {
                if (xhr.status === 404) {
                    U.error("加载失败, 未找到该地址: " + _opts.url);
                    return;
                }
                U.showExceptionDialog();
            }
        }
    });
}

/**
 * 检查请求是否出现异常 或权限问题
 * @param _resp 返回数据
 * @param _resp.flag 返回的状态码
 * @param _resp.message 返回的消息
 * @param _resp.data 返回的数据
 * @param isShowNotice 是否直接弹出提示(设置为false时, 不弹出提示)
 * @auther zhangcc
 * @version 2015-6-25 下午11:22:45
 */
U.checkExceptionAndPermission = function (_resp, isShowNotice) {
    let flag = _resp ? _resp['flag'] : null;
    if (!_resp || !flag || flag === 100102) { // 请求发生异常
        U.showExceptionDialog(_resp);
        return false;
    } else if (flag === 100103) { // 无登陆权限
        U.confirm("您尚未登陆, 点击确认进入登陆验证", function () {
            window.location = app_path + '/sys/user/login';
        });
        return false;
    } else if (flag === 100303) {
        if (isShowNotice !== false) { // 提示
            U.msg(_resp.message);
            return false;
        }
    } else if (flag === 100305) { // 提示后重定向
        U.msg(_resp.message, function () {
            window.location = app_path + _resp.data;
        });
        return false;
    } else if (flag === 100405) { // 重定向标识
        window.location = app_path + _resp.data;
        return false;
    } else if (flag === 100406) { // index主页内重定向
        let url = _resp.data;
        if (!U.isEmpty(url) && url !== '/') {
            window.location = app_path + "/index#url=" + _resp.data;
        } else {
            window.location = app_path + "/index";
        }
        return false;
    } else if (flag === 100701) { // 参数错误
        U.msg(_resp.message);
        return false;
    } else if (flag === 100900) { // 恶意请求
        window.location = app_path + "/banned";
        return false;
    }
    return true;
};

/**
 * 显示出错提示
 * @auther zhangcc
 * @version 2015-6-26 上午8:42:19
 */
U.showExceptionDialog = function (_resp) {
    let errMsg = _resp.message || "系统异常，请稍后再试.";
    U.error(errMsg);
};

/**
 * 弹出消息
 * @param message 需提示的文本
 * @param callback 回调函数
 */
U.msg = function (message, callback) {
    toast.info({
        title: '温馨提示',
        message: message,
        position: 'topRight',
        onClosed: function () {
            if (callback) {
                callback();
            }
        }
    });
};

/**
 * 弹出成功消息
 * @param message 需提示的文本
 * @param callback 回调函数
 */
U.success = function (message, callback) {
    toast.success({
        title: '温馨提示',
        message: message,
        position: 'topRight',
        onClosed: function () {
            if (callback) {
                callback();
            }
        }
    });
};

/**
 * 弹出错误消息
 * @param message 需提示的文本
 * @param callback 回调函数
 */
U.error = function (message, callback) {
    toast.error({
        title: '错误',
        message: message,
        position: 'topRight',
        onClosed: function () {
            if (callback) {
                callback();
            }
        }
    });
};

/**
 * 弹出警告消息
 * @param message 需提示的文本
 * @param callback 回调函数
 */
U.warn = function (message, callback) {
    toast.warning({
        title: '注意',
        message: message,
        position: 'topRight',
        onClosed: function () {
            if (callback) {
                callback();
            }
        }
    });
};

/**
 * 确认框
 * @param message 需提示的文本
 * @param btn 确定按钮文本
 * @param callback 确定回调函数
 */
U.confirm = function (message, callback, btn) {
    if (!btn) {
        btn = "确定";
    }
    return layer.msg(message, {
        btn: [btn, "取消"],
        shadeClose: false,
        yes: function (index) {
            layer.close(index);
            if (callback) {
                callback();
            }
        },
        no: function (index) {
            layer.close(index);
        },
        time: -1
    });
};

/**
 * 自定义按钮的确认
 * @param message
 * @param btns
 * @param yes
 * @param no
 */
U.confirmBtn = function (message, btns, yes, no) {
    if (!btns) {
        btns = ['确定', '取消']
    }
    return layer.open({
        title: "温馨提示",
        content: message,
        btn: btns,
        shade: 0,
        shadeClose: false,
        time: -1,
        yes: function (index) {
            layer.close(index);
            if (yes) {
                yes();
            }
        },
        btn2: function (index) {
            layer.close(index);
            if (no) {
                no();
            }
        }
    });
};

/**
 * 显示loading窗口
 */
U.loading = function () {
    U.loadingIndex = layer.msg('加载中', {
        icon: 16,
        shade: 0.01,
        time: 9999999
    });
    return U.loadingIndex;
};

/**
 * 关闭loading窗口
 */
U.closeLoading = function () {
    layer.close(U.loadingIndex);
};

/**
 * 覆盖整个页面并显示加载提示
 */
U.pageLoading = function () {
    let $body = $("body");
    if ($body.children(".pageMask").length === 0) {
        $body.append('<div class="pageMask"></div>');
    }
    U.loading();
};

/**
 * 整个页面显示, 并移除加载提示
 */
U.pageLoadingClose = function () {
    U.closeLoading();
    let $body = $("body");
    $body.children(".pageMask").remove();
};

let tipsIndex;
/**
 * 显示提示框
 * @param message 提示信息
 * @param selector 提示信息位置
 * @param time 延迟关闭时间
 */
U.tips = function (message, selector, time) {
    time = time || 2000;
    tipsIndex = layer.tips(message, selector, {
        tips: [1, '#DB3652'],
        time: time
    });
};

// 自定义事件抽象
U._listener = {};
U.addEvent = function (key, func) {
    U._listener[key] = func;
};
U.removeEvent = function (key) {
    U._listener[key] = undefined;
};
U.fireEvent = function (key, param1, param2, param3) {
    let func = U._listener[key];
    if (func) {
        func(param1, param2, param3);
    }
};

/**
 * 先解除再重新绑定事件
 * @param eventName
 * @param func
 */
$.fn.rebind = function (eventName, func) {
    let $this = $(this);
    $this.unbind(eventName);
    $this.bind(func);
};

/**
 * 数值前填零
 * @param num 待填零的数值
 * @param width 填零后的数值位数
 * @returns {*}
 */
U.preFill = function (num, width) {
    if (num === null || num === undefined) {
        return 0;
    }
    num = num + "";
    let len = num.length;
    width = width || 1;
    if (len === 0) {
        return 0;
    }
    if (len >= width) {
        return num;
    }
    for (let i = 0; i < width - len; i++) {
        num = "0" + num;
    }
    return num;
};

/**
 * 是否为空
 * @auther zhangcc
 * @version 2015-5-26 上午11:26:03
 */
U.isEmpty = function (obj) {
    return obj === undefined || obj === null || obj === '' || obj.length === 0;
};

/**
 * 在指定的DOM上显示tips提示
 * @param $item
 * @param tips
 */
U.showTips = function ($item, tips) {
    $item.addClass("layui-form-danger");
    setTimeout(function () {
        tipsIndex = layer.tips(tips, $item[0], {
            tips: [1, '#FF5722'],
            time: 2000
        });
        $item.focus();
        $item.one("click", function () {
            layer.close(tipsIndex);
            $item.removeClass("layui-form-danger");
        });
    }, 30);
};

// md5签名盐值
U.encry = {
    salt: "layui"
};
// layui是否已加载完成
let layuiReadyState = false;
// layui加载完执行的方法集合
let layuiReadyMethods = [];
/**
 * layui加载完成事件
 * @param func 加载完成后调用的函数
 */
if (window.layui) {
    layui.ready = function (func) {
        if (func === undefined) {
            for (let i = 0; i < layuiReadyMethods.length; i++) {
                layuiReadyMethods[i]();
            }
            layuiReadyMethods = [];
            layuiReadyState = true;
            return;
        }
        if (typeof func === 'function') {
            if (layuiReadyState) {
                func();
                return;
            }
            layuiReadyMethods.push(func);
        }
    };
}

// 是否已经锁定
let isShowLock = false;

let lockTemplate = $('#lock-template').html();
let psswordReg = /[a-zA-Z0-9_]{6,20}/;

/**
 * 锁屏
 */
U.lock = function () {
    if (isShowLock) {
        return;
    }
    //自定页
    layer.open({
        title: false,
        type: 1,
        closeBtn: 0,
        anim: -1,
        content: lockTemplate,
        shade: [0.8, '#393D49'],
        success: function (layero, lockIndex) {
            isShowLock = true;
            // 清除后台的登录信息
            ajaxCall({
                url: "sys/user/logout.json"
            });
            let $loginName = layero.find('#lockUserName');
            let $passwd = layero.find('#lockPwd');
            let $loginError = layero.find(".login-error");
            // 给显示用户名赋值
            $loginName.text(loginUser.name);
            let $unlock = layero.find('#unlock');
            $passwd.focus();
            $passwd.on("keydown", function (e) {
                if (e.keyCode === 13) {
                    $unlock.click();
                }
            });
            // 绑定解锁按钮的点击事件
            $unlock.on('click', function () {
                let pwd = $passwd.val();
                if (U.isEmpty(pwd)) {
                    $loginError.html("请输入密码");
                    return;
                }
                if (!psswordReg.test(pwd)) {
                    $loginError.html("密码必须是6-20位英文、数字、下划线");
                    return;
                }
                let loginName = loginUser.loginName;
                let password = hex_md5(loginName + U.encry.salt + pwd);
                ajaxCall({
                    url: "sys/user/doLogin",
                    param: {loginName: loginName, password: password},
                    success: function (_resp) {
                        if (_resp.flag !== 100101) {
                            $loginError.html("密码错误");
                            return;
                        }
                        isShowLock = false;
                        //关闭锁屏层
                        layer.close(lockIndex);
                    }
                });
            });
        }
    });
};

// 修改密码模板
let updatePasswodTemplete = $("#update-password-template").html();
/**
 * 修改密码
 */
U.updatePassword = function () {
    //自定页
    layer.open({
        title: false,
        type: 1,
        anim: -1,
        content: updatePasswodTemplete,
        shade: [0.8, '#393D49'],
        success: function (layero, layerIndex) {
            let $dom = $(layero);
            let $oldPwd = $dom.find("[name=oldPwd]");
            let $pwd = $dom.find("[name=pwd]");
            let $confirmPwd = $dom.find("[name=confirmPwd]");
            let $error = $dom.find(".login-error");
            $dom.find("[name=submit]").click(function () {
                let oldPwd = $oldPwd.val();
                if (U.isEmpty(oldPwd)) {
                    $error.html("请输入旧密码");
                    return;
                }
                if (!psswordReg.test(oldPwd)) {
                    $error.html("密码必须是6-20位英文、数字、下划线");
                    return;
                }
                let pwd = $pwd.val();
                if (U.isEmpty(pwd)) {
                    $error.html("请输入新密码");
                    return;
                }
                if (!psswordReg.test(pwd)) {
                    $error.html("密码必须是6-20位英文、数字、下划线");
                    return;
                }
                let confirmPwd = $confirmPwd.val();
                if (U.isEmpty(confirmPwd)) {
                    $error.html("请输入确认密码");
                    return;
                }
                if (!psswordReg.test(confirmPwd)) {
                    $error.html("密码必须是6-20位英文、数字、下划线");
                    return;
                }
                if (pwd !== confirmPwd) {
                    $error.html("新密码与确认密码不一致");
                    return;
                }
                let loginName = loginUser.loginName;
                oldPwd = hex_md5(loginName + U.encry.salt + oldPwd);
                pwd = hex_md5(loginName + U.encry.salt + pwd);
                ajaxCall({
                    url: "sys/user/updatePasswd.json",
                    param: {loginName: loginName, oldPasswd: oldPwd, password: pwd},
                    success: function () {
                        layer.close(layerIndex);
                        U.success("密码修改成功");
                    }
                });
            });
        }
    });
};

let layer = window.layer || {
    closeAll: function () {
    }
};
// 点击任意位置, 关闭所有tips
$(document).click(function (e) {
    if (!$(e.target).is(".btn") && $(e.target).parents(".layui-layer-tips").length === 0) {
        layer.closeAll('tips');
    }
});

// 一键复制
U.copy = function (_opts) {
    let dom = _opts.$dom[0];
    if (dom.select) {
        dom.select();
    } else {
        U.error("复制失败");
        return;
    }
    try {
        let flag = document.execCommand('copy');
        if (flag) {
            if (_opts.success) {
                _opts.success();
            }
        } else {
            U.error("一键复制失败，请手动复制")
        }
    } catch (err) {
        U.error("复制失败")
    }
};

// 首页加载动画索引
let indexLoadingIndex;
// 首页加载动画等待完成次数
let indexLoadingWaitCount = 3;
/**
 * 首页动画
 */
U.indexLoading = function () {
    indexLoadingIndex = layer.open({
        type: 3,
        icon: 1,
        shade: false,
        success: function () {
            U.closeIndexLoading();
        }
    });
};

/**
 * 关闭或计数首页加载动画
 */
let currentIndexLoadingCount = 0;
U.closeIndexLoading = function () {
    if (++currentIndexLoadingCount >= indexLoadingWaitCount) {
        let iterval = setInterval(function () {
            if (indexLoadingIndex) {
                layer.close(indexLoadingIndex);
                clearInterval(iterval);
            }
        }, 100);
    }
};

// 进度条设置缓存对象
U.progress = {};

/**
 * 显示进度条
 */
U.showProgess = function (title, taskKey) {
    loadHtml({
        url: 'progress',
        param: {title: title, taskKey: taskKey},
        success: function (html) {
            U.progress[taskKey] = layer.open({
                type: 1,
                area: ['680px'],
                shadeClose: false,
                closeBtn: 0,
                title: null,
                content: html
            });
        }
    });
};

/**
 * 隐藏进度条
 */
U.closeProgress = function (taskKey) {
    layer.close(U.progress[taskKey]);
};