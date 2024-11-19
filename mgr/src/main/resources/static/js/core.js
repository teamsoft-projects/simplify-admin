/**
 * 定义命名空间
 * @param path 命名空间
 * @auther zhangcc
 * @version 2015-5-26 上午11:26:03
 */
function namespace(path) {
    let arr = path.split(".");
    let ns = "";
    for (let i = 0; i < arr.length; i++) {
        if (i > 0)
            ns += ".";
        ns += arr[i];
        eval("if(typeof(" + ns + ") == 'undefined') " + ns + " = new Object();");
    }
}

/**
 * 扩展JS数组功能, 是否包含
 * @auther zhangcc
 * @version 2015-5-26 上午11:26:03
 */
Array.prototype.contains = function (item) {
    for (let i = 0; i < this.length; i++) {
        if (this[i] === item) {
            return true;
        }
    }
    return false;
};

/**
 * 扩展JS数组功能, 是否包含, 并跳过flag为true的元素
 * @auther zhangcc
 * @version 2015-5-26 上午11:26:03
 */
Array.prototype.containsWithoutFlag = function (item) {
    for (let i = 0; i < this.length; i++) {
        if (this[i] === item && this[i]['flag'] !== true) {
            return true;
        }
    }
    return false;
};

/**
 * 扩展JS字符串功能, 字符串是否为空
 * @auther zhangcc
 * @version 2015-5-26 上午11:26:03
 */
String.prototype.isEmpty = function () {
    return U.isEmpty(this);
};

/**
 * 扩展JS字符串功能, 去除空格
 * @returns
 * @auther zhangcc
 * @version 2015-6-27 下午4:35:22
 */
String.prototype.trim = function () {
    return this.replace(/(^\s*)|(\s*$)/g, "");
};

/**
 * 扩展JS字符串功能, 去除左边空格
 * @returns
 * @auther zhangcc
 * @version 2015-6-27 下午4:35:38
 */
String.prototype.ltrim = function () {
    return this.replace(/(^\s*)/g, "");
};

/**
 * 扩展JS字符串功能, 去除右边空格
 * @returns
 * @auther zhangcc
 * @version 2015-6-27 下午4:35:41
 */
String.prototype.rtrim = function () {
    return this.replace(/(\s*$)/g, "");
};

/**
 * 日期时间格式化
 * @param fmt
 * @returns {*}
 * @constructor
 */
Date.prototype.format = function (fmt) { //author: meizz
    let o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (let k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

/**
 * 扩展Jquery, 返回选中元素们的指定属性数组
 */
$.fn.checkedList = function (attribName) {
    attribName = attribName || "identity";
    let result = {};
    let len = this.length;
    result.length = len;
    if (len === 0) {
        return result;
    }
    for (let i = 0; i < this.length; i++) {
        result['ids[' + i + ']'] = this.eq(i).attr(attribName);
    }
    return result;
};

/**
 * 模板实现
 * @param html 模板字符串 data 嵌入模板的数据 blankParam 须留空的参数
 * @param data 附加到模板上的数据
 * @param blankParam 不需要填入模板的数据
 * @auther zhangcc
 * @version 2015-5-26 上午11:26:03
 */
U.template = function (html, data, blankParam) {
    let templateReg = /{{(.*?)}}/g;
    while (templateReg.test(html)) {
        let keyWord = RegExp.$1;
        let sourceReg = new RegExp("\\{\\{" + keyWord + "\\}\\}", "g");
        let item;
        if (blankParam === keyWord) {
            item = '';
        } else {
            try {
                item = eval("data." + keyWord);
                item = U.isEmpty(item) ? '' : item;
            } catch (e) {
                item = '';
            }
        }
        html = html.replace(sourceReg, item);
    }
    return html;
};

/**
 * <p>正则替换关键字</p>
 * <p>如${name} ${mobile} 可以传入{name: 'xxx', mobile: 'xxx'}进行对象值替换</p>
 * @param str {string} 待替换字符串
 * @param data {object} 用来替换的数据对象
 * @param _opts {{reg: string, isReplaceBlank: boolean}|{}} 配置项
 * @param _opts.reg {string} 替换正则
 * @param _opts.isReplaceBlank {boolean} 是否替换空白数据
 */
U.replace = function (str, data, _opts) {
    if (!str || !data) {
        return '';
    }
    _opts = _opts || {};
    let reg = _opts.reg || /\${(.*?)}/g;
    let isReplaceBlank = _opts.isReplaceBlank === true;
    let matched;
    while (matched = reg.exec(str)) {
        let objKey = matched[1];
        let item;
        try {
            item = eval("data." + objKey);
            item = U.isEmpty(item) ? '' : item;
        } catch (e) {
            item = '';
        }
        if (!isReplaceBlank && U.isEmpty(item)) {
            continue;
        }
        let sourceReg = new RegExp('\\$\\{' + objKey + '\\}', 'g');
        str = str.replace(sourceReg, item);
    }
    return str;
}

/**
 * 通过正则表达式提取参数
 * @param content 待提取内容
 * @param reg 正则表达式（可选）
 */
U.getParams = function (content, reg) {
    reg = reg || /\${(.*?)}/g;
    let ret = [];
    let matched;
    while (matched = reg.exec(content)) {
        ret.push(matched[1]);
    }
    return ret;
}

/**
 * 裁剪字符串
 * @param str 待裁剪字符串
 * @param width 裁剪后的长度, 一个中文占两位
 * @return object
 */
U.cutStr = function (str, width) {
    if (!str) {
        return null;
    }
    if (!width || width === 0) {
        return "";
    }
    let realLen = 0;
    let result = "";
    for (let i = 0; i < width; i++) {
        if (str.charCodeAt(i) > 127) {
            realLen += 2;
        } else {
            realLen++;
        }
        if (realLen > width || i >= str.length) {
            break;
        }
        result += str.charAt(i);
    }
    return result;
};

/**
 * 字符串超出部分用..代替
 * @param str 待填充字符串
 * @param width 填充后的长度, 一个中文占两位
 * @return object 裁剪后的字符串
 */
U.fillStr = function (str, width) {
    if (!str) {
        return null;
    }
    if (!width || width === 0) {
        return "";
    }
    if (width < 2) {
        return "..";
    }

    let result = U.cutStr(str, width);
    if (result.length < str.length) {
        result = U.cutStr(result, width - 2) + "..";
    }
    return result;
};

/**
 * 将字符串转换成整型数值
 * @param str 待转换的字符串
 * @returns {number} 转换后的数值, 如果发生任何问题, 返回0
 */
U.strToInt = function (str) {
    let result = 0;
    if (!/-?\d+/.test(str)) {
        return result;
    } else {
        try {
            result = parseInt(str);
        } catch (e) {
            console.info("最大购买数量有误");
            result = 0;
        }
    }
    return result;
};

/**
 * 创建并初始化下拉框
 * @param opts.url 获取下拉列表的远程数据URL
 * @param opts.param 远程请求参数
 * @param opts.$dom 下拉框的DOM对象
 * @param opts.idField 填充到option的value值对应的数据键
 * @param opts.nameField 填充到option内的对应数据键
 * @param opts.autoAppend 是否由本函数自动填充option，默认为自动填充
 * @param opts.callback 填充完成后的回调函数
 * @param opts.defaultText 默认显示的下拉框名
 * @param opts.isSelectFist 默认选中第一个(如果有的话)
 * @param opts.holder 额外从返回值中取数据并存放到相应的attr
 */
U.initSelect = function (opts) {
    opts.idField = opts.idField || "id";
    opts.nameField = opts.nameField || "name";
    opts.autoAppend = opts.autoAppend !== false;
    opts.param = opts.param || {};
    opts.defaultText = opts.defaultText || "请选择";
    ajaxCall({
        url: opts.url,
        param: opts.param,
        success: function (_resp) {
            let data = _resp.data;
            let html = '<option value="">' + opts.defaultText + '</option>';
            if (opts.autoAppend) {
                let index = 0;
                opts.holder = (opts.holder && opts.holder.constructor !== Array) ? new Array(opts.holder) : opts.holder;
                $.each(data, function (key, val) {
                    let id = val[opts.idField];
                    let name = val[opts.nameField];
                    let holder = '';
                    if (opts.holder) {
                        for (let i = 0; i < opts.holder.length; i++) {
                            holder += ' ' + opts.holder[i]['attr'] + '="' + val[opts.holder[i]['key']] + '"';
                        }
                    }
                    if (opts.isSelectFist && (index++) === 0) {
                        html += '<option value="' + id + '" ' + holder + ' selected>' + name + '</option>';
                    } else {
                        html += '<option value="' + id + '" ' + holder + '>' + name + '</option>';
                    }
                });
                opts.$dom.html(html);
            }
            if (opts.callback) {
                opts.callback(data);
            } else {
                form.render('select');
            }
        }
    });
};

/**
 * 将数组转换成key[0].m=x key[1].n=y形式
 */
U.convertArrayToObj = function (key, arr) {
    if (U.isEmpty(arr)) {
        return [];
    }
    let result = {length: arr.length};
    for (let i = 0; i < arr.length; i++) {
        let keyVal = key + '[' + i + ']';
        result[keyVal] = {};
        for (let temp in arr[i]) {
            result[keyVal + '.' + temp] = arr[i][temp];
        }
    }
    return result;
};

/**
 * 将数组转换成key[0]=x key[1]=y形式
 */
U.convertArray = function (key, arr) {
    if (U.isEmpty(arr)) {
        return [];
    }
    let result = {};
    for (let i = 0; i < arr.length; i++) {
        let keyVal = key + '[' + i + ']';
        result[keyVal] = arr[i];
    }
    return result;
};

/**
 * <p>对象前加上前缀</p>
 * <p>{a: 1: b: 2}, prefix=n => {n.a: 1, n.b:2}</p>
 * @param obj {object} 待处理对象
 * @param prefix {string} 前缀
 * @return {object}
 */
U.prefixObject = function (obj, prefix) {
    if (U.isEmpty(obj) || U.isEmpty(prefix)) {
        return obj;
    }
    let ret = {};
    if (typeof obj === Array) {
        ret[prefix] = obj;
        return ret;
    }
    for (const key in obj) {
        ret[prefix + '.' + key] = obj[key];
    }
    return ret;
}

/**
 * laydate日期对象转字符串类型
 */
U.dateToStr = function (dateObj, withTime) {
    let result = dateObj['year'];
    result += '-' + U.preFill(dateObj['month'], 2);
    result += '-' + U.preFill(dateObj['date'], 2);

    if (withTime === true) {
        result += ' ' + U.preFill(dateObj['hour'], 2);
        result += ':' + U.preFill(dateObj['minutes'], 2);
        result += ':' + U.preFill(dateObj['seconds'], 2);
    }
    return result;
}

/**
 * 计算年龄
 * @param birthDate 出生日期，Date类型
 */
U.computeAge = function (birthDate) {
    if (U.isEmpty(birthDate) || !(birthDate instanceof Date)) {
        return -1;
    }

    let now = new Date();
    if (now.getTime() < birthDate.getTime()) {
        return -1;
    }
    let nowYear = now.getFullYear();
    let nowMonth = now.getMonth();
    let nowDay = now.getDate();

    let birthYear = birthDate.getFullYear();
    let birthMonth = birthDate.getMonth();
    let birthDay = birthDate.getDate();

    if (nowYear === birthYear) {
        return 1;
    }

    let subYear = nowYear - birthYear;
    if (nowMonth < birthMonth || (nowMonth === birthMonth && nowDay < birthDay)) {
        subYear--;
    }

    return subYear === 0 ? 1 : subYear;
}

/**
 * 异步延迟执行队列，支持多个事件完成标识
 * @type {*[]}
 */
U.waiting = [];

/**
 * <p>U.done(['done1', 'done2'], function(data) {})</p>
 * <p>U.done('done1', {data: 123})</p>
 * <p>U.done('done2')</p>
 * @param waiting
 * @param callback
 */
U.done = function (waiting, callback) {
    if (waiting.constructor !== Array) {
        waiting = new Array(waiting);
    }
    if (callback && typeof callback === "function") {
        U.waiting.push({key: waiting, callback: callback});
    } else {
        for (let i = 0; i < U.waiting.length; i++) {
            let wait = U.waiting[i];
            if (wait === undefined) {
                continue;
            }
            let keyArr = wait['key'];
            for (let j = 0; j < keyArr.length; j++) {
                if (keyArr[j] !== undefined && waiting.contains(keyArr[j])) {
                    keyArr[j] = undefined;
                }
            }
            if (typeof callback === 'object') {
                wait['param'] = $.extend({}, wait['param'], callback);
            }
            if (keyArr.every(k => k === undefined)) {
                wait['callback'].call(this, wait['param']);
                U.waiting[i] = undefined;
            }
        }
    }
}

/**
 * 填充短信模板
 */
U.fillSmsTemplate = function ($dom, $content, $paramList, $templateId,) {
    let value = $templateId.val();
    if (U.isEmpty(value)) {
        return;
    }
    let content = $dom.find('[value=' + value + ']').attr('lay-content');
    let param = {};
    $paramList.find('.input-label-long').each(function (k, v) {
        let $val = $(v);
        let key = $val.html();
        param[key] = $val.parents('.param-item').find('.param-value-input').val();
    });
    $content.val(U.replace(content, param));
}