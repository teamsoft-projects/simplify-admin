layui.ready(function () {
    window.tab = layTab({
        elem: '.admin-nav-card', //设置选项卡容器
        maxSetting: 20,
        contextMenu: true,
        fixedCount: 0
    });
    // iframe自适应
    $(window).on('resize', function () {
        let $content = $('.admin-nav-card');
        $content.height($(this).height() - 121);
        $content.find('iframe').each(function () {
            $(this).height($content.height());
        });
    }).resize();

    // 设置navbar
    navbar.set({
        spreadOne: true,
        elem: '#admin-navbar-side',
        url: 'sys/menu/listByRole.json',
        complete: function () {
            // 关闭首页载入动画
            U.closeIndexLoading();
        }
    });
    // 渲染navbar
    navbar.render(function () {
        // 监听点击事件
        navbar.on('click(side)', function (data) {
            if (U.isEmpty(data['field']['href'])) {
                return;
            }
            if (tab.config.isShowing) {
                return;
            }
            U.loading();
            let menuTabId = data['elem'].parent().attr("menu-tab-id");
            $.extend(data, {
                menuTabId: menuTabId
            });
            tab.tabAdd(data);
        });
    });
});

+function () {
    // 点击左上角折叠/展开菜单事件
    let $adminSide = $('#admin-side');
    $('.admin-side-toggle').on('click', function () {
        let sideWidth = $adminSide.width();
        if (sideWidth === 200) {
            $('#admin-body').animate({
                left: '0'
            });
            $adminSide.animate({
                width: '0'
            });
            $(".admin-table-page").animate({
                "left": "2px"
            });
        } else {
            $('#admin-body').animate({
                left: '200px'
            });
            $adminSide.animate({
                width: '200px'
            });
            $(".admin-table-page").animate({
                left: "202px"
            });
        }
    });

    // 右上角按钮事件
    $(".layui-nav a").click(function (e) {
        // 阻止默认时间, 解决layui框架自动渲染导致的点击之后下方的动态条一直存在的问题
        e.stopPropagation();
        e.preventDefault();
        let $this = $(this);
        let name = $this.attr("name");
        switch (name) {
            case "lock": {
                U.lock();
                break;
            }
            case "logout": {
                window.location = app_path + "/sys/user/logout";
                break;
            }
            case "updatePasswd": { // 点击修改密码
                U.updatePassword();
                break;
            }
        }
    });

    // 点击全屏事件
    $('.admin-side-full').on('click', function () {
        let docElm = document.documentElement;
        if (docElm['requestFullscreen']) { //W3C
            docElm['requestFullscreen']();
        } else if (docElm['mozRequestFullScreen']) { //FireFox
            docElm['mozRequestFullScreen']();
        } else if (docElm['webkitRequestFullScreen']) { //Chrome等
            docElm['webkitRequestFullScreen']();
        } else if (element['msRequestFullscreen']) { //IE11
            element['msRequestFullscreen']();
        }
        layer.msg('按Esc即可退出全屏');
    });

    // 获取所有权限点
    window.Authorize = {};
    ajaxCall({
        url: 'sys/menuFunc/getMenuFuncForLoginUser.json',
        success: function (_resp) {
            let data = _resp['data'];
            if (!U.isEmpty(data)) {
                window.Authorize = data;
            }
        }
    })
}();