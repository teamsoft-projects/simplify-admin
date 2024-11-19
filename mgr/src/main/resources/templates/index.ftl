<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>${resultInfo.data.SYSTEM_NAME} - 首页</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <link rel="shortcut icon" type="image/x-icon" href="/static/images/favicon.png"/>
    <link rel="bookmark" href="/static/images/favicon.png">
    <link rel="stylesheet" href="/static/common/font-awesome/css/font-awesome.min.css?v=231201">
    <link rel="stylesheet" href="/static/common/layui/css/layui.css?v=231201"/>
    <link rel="stylesheet" href="/static/common/layui-plus/css/global.css?v=231201"/>
    <link rel="stylesheet" href="/static/common/layui-plus/css/toast.css?v=231201"/>
    <link rel="stylesheet" href="/static/css/main.css?v=231201"/>
</head>
<body>
<div class="layui-layout layui-layout-admin" style="border-bottom: solid 5px #1aa094;">
    <div class="layui-header header header-demo">
        <div class="layui-main mr0">
            <div class="admin-login-box">
                <div class="layui-logo">${resultInfo.data.SYSTEM_NAME}</div>
                <div class="admin-side-toggle">
                    <i class="fa fa-bars"></i>
                </div>
                <div class="admin-side-full">
                    <i class="fa fa-life-bouy"></i>
                </div>
            </div>
            <ul class="layui-nav">
                <li class="layui-nav-item">
                    <a href="javascript:void(0);"><i class="fa fa-user"></i>${LOGIN_USER.name}</a>
                </li>
                <li class="layui-nav-item">
                    <a href="javascript:void(0);" name="logout"><i class="fa fa-sign-out"></i> 注销</a>
                </li>
            </ul>
        </div>
    </div>
    <div class="layui-side layui-bg-black" id="admin-side">
        <div class="layui-side-scroll" id="admin-navbar-side" lay-filter="side"></div>
    </div>
    <div class="layui-body" style="bottom: 0;border-left: solid 2px #1AA094;" id="admin-body">
        <div class="layui-tab admin-nav-card layui-tab-brief" lay-filter="admin-tab">
            <ul class="layui-tab-title">
            </ul>
            <div class="layui-tab-content" style="min-height: 150px; padding: 5px 0 0 0;position: relative;">
            </div>
        </div>
    </div>
</div>
<input class="unvisible-copyinput" type="text" id="one-key-copy">
<script src="/static/common/jquery/jquery.min.js?v=231201"></script>
<script src="/static/common/jquery/jquery.cookie.js?v=231201"></script>
<script src="/static/common/layui/layui.js?v=231201"></script>
<script src="/static/js/common.js?v=231201&url="></script>
<script src="/static/js/core.js?v=231201"></script>
<script src="/static/common/encrypt/md5.js?v=231201"></script>
<script src="/static/common/encrypt/base64.min.js?v=231201"></script>
<script src="/static/js/layui.settings.js?v=231201"></script>
<script src="/static/js/pager.js?v=231201"></script>
<script src="/static/common/layui-plus/js/linkage.js?v=231201"></script>
<script src="/static/common/layui-plus/js/xm-select.js?v=231201"></script>
<script src="/static/js/index.js?v=231201"></script>
</body>
</html>