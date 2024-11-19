<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>500</title>
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="shortcut icon" type="image/x-icon" href="/static/images/favicon.png"/>
    <link rel="bookmark" href="/static/images/favicon.png">
    <style>
        div {
            -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
        }

        html {
            height: 100%;
        }

        body {
            height: 100%;
            margin: 0;
            padding: 0;
            font-family: Adobe Heiti Std, serif;
        }

        /*  头部  start  */
        .download-title {
            width: 1300px;
            height: 100px;
            /*border: 1px solid red;*/
            margin: 0 auto;
            display: flex;
        }

        .top-box-img {
            /*border: 1px solid red;*/
            width: 150px;
            height: 50px;
            background-image: url("/static/images/logo.png");
            background-size: 100% 100%;
            margin-top: 25px;
        }

        .top-box-center {
            width: 330px;
            height: 30px;
            position: relative;
            color: black;
            font-size: 15px;
            top: 35%;
            left: 55%;
        }

        .arrow {
            position: absolute;
            top: 8px;
            left: 40px;
        }

        .top-box-center a {
            list-style: none;
            text-decoration: none;
            color: black;
        }

        .top-box-center span {
            cursor: pointer;
            margin-left: 46px;
        }

        .top-box-lang {
            height: 30px;
            position: relative;
            left: 60%;
            top: 35%;
            cursor: pointer;
        }

        .top-box-lang > img:nth-child(2) {
            width: 15px;
            height: 8px;
        }

        .mobile-top {
            display: none;
        }

        @media screen and (max-width: 480px) {
            .download-title {
                display: none;
            }

            .mobile-top {
                display: flex;
                width: 100%;
            }

            .mobile-logo {
                width: 80px;
                height: 22px;
                background-image: url("/static/images/logo.png");
                background-size: 100% 100%;
                margin-left: 20px;
            }
        }

        /*   移动端右上角start  */
        .RightLogo-box {
            width: 120px;
            height: 150px;
            position: absolute;
            padding-top: 20px;
            color: white;
            top: 50px;
            right: 14px;
            font-size: 14px;
            text-align: center;
            background-size: 100% 100%;
            background-image: url("/static/images/RightLogo-box.png");
            z-index: 2000;
        }

        .RightLogo-box div {
            color: black;
            margin-bottom: 16px;
        }

        .RightLogo-box a {
            color: white;
        }

        .RightLogo-box div:nth-child(1) {
            width: 60px;
            height: 20px;
            line-height: 20px;
            margin: 0 auto 16px auto;
            border-radius: 10px;
            background: white;
            border: 1px solid rgba(75, 129, 255, 1);
            color: rgba(95, 179, 255, 1);
        }

        .top-lang-center {
            background-color: rgba(0, 0, 0, 0.5);
            border-radius: 5px;
            width: 80px;
            height: 70px;
            position: relative;
            cursor: pointer;
            padding-top: 10px;
            margin-left: -22px;
        }

        .top-lang-center img {
            width: 34px;
            height: 22px;
            margin: 5px auto;
            display: block;
        }

        @media screen and (max-width: 480px) {
            .lang-background-box, .lang-background-EN {
                display: inline-block;
                width: 30px;
                height: 20px;
                font-size: 15px;
                line-height: 20px;
                position: relative;
                left: 8px;
            }

            .lang-background-box {
                left: -8px;
                top: 1px;
            }

            #RightLogo {
                display: block;
                position: absolute;
                right: 32px;
                top: 10px;
                width: 8px;
                height: 30px;
                z-index: 900;
            }

            .lang-background-center {
                display: inline-block;
                position: absolute;
                right: 32px;
                width: 20px;
                height: 20px;
                background-size: 100% 100%;
                background-image: url("/static/images/lang background center.png");
            }
        }

        /*   移动端右上角end  */

        /*  内容 start */
        .content-box {
            width: 100%;
            min-width: 1300px;
            height: 90%;
            background-image: url("/static/images/500.png");
            background-size: 100% 100%;
        }

        .content-box-text {
            width: 600px;
            height: 500px;
            /*border: 1px solid #ccc;*/
            margin-left: 5%;
            padding-top: 15%;
        }

        .content-box-text > div:nth-child(2) {
            height: 184px;
            text-align: center;
            font-size: 268px;
            font-family: FZKangTi-S07S, serif;
            font-weight: 400;
            color: rgba(253, 239, 151, 1);
            line-height: 184px;
        }

        .content-box-text > div:nth-child(3) {
            text-align: center;
            margin-top: 50px;
            font-size: 28px;
            font-family: Source Han Sans CN, serif;
            font-weight: 300;
            color: rgba(97, 149, 255, 1);
            line-height: 47px;
        }

        .content-box-text > div:nth-child(4) {
            display: flex;
            justify-content: space-between;
            margin-top: 50px;
        }

        .return {
            width: 250px;
            height: 70px;
            background: rgba(97, 149, 255, 1);
            border-radius: 35px;
            color: white;
            text-align: center;
            line-height: 70px;
            font-size: 26px;
            cursor: pointer;
        }

        .refresh {
            width: 250px;
            height: 70px;
            border: 2px solid rgba(97, 149, 255, 1);
            border-radius: 35px;
            color: rgba(97, 149, 255, 1);
            text-align: center;
            line-height: 70px;
            font-size: 26px;
            cursor: pointer;
        }

        .content-box-text-img {
            display: none;
        }

        @media screen and (max-width: 480px) {
            .content-box {
                min-width: 300px;
                background: white;
            }

            .content-box-text {
                width: 100%;
                margin-left: 0;
                margin-top: 0;
            }

            .content-box-text > div:nth-child(2) {
                font-size: 120px;
                height: 130px;
                line-height: 130px;
            }

            .content-box-text > div:nth-child(3) {
                font-size: 15px;
                margin-top: 0;
                margin-bottom: 50px;
            }

            .content-box-text > div:nth-child(4) {
                width: 80%;
                margin: 0 auto;
            }

            .return {
                width: 100px;
                height: 30px;
                font-size: 15px;
                line-height: 30px;
            }

            .refresh {
                font-size: 15px;
                width: 100px;
                height: 30px;
                line-height: 30px;
            }

            .content-box-text-img {
                display: block;
                width: 180px;
                height: 130px;
                background-image: url("/static/images/mobile-500.png");
                background-size: 100% 100%;
                margin: 30px auto 0 auto;
            }
        }
    </style>
</head>
<body>
<!--     移动端头部      -->
<div class="mobile-top">
    <p class="mobile-logo"></p>
    <img src="/static/images/right-logo-black.png" alt="" id="RightLogo">
    <div class="RightLogo-box" style="display: none">
        <div id="langBox">
            <span class="lang-background-box">國語</span>
            <span class="lang-background-center"></span>
            <span class="lang-background-EN">E N</span>
        </div>
       <#-- <div><span class="service-public">DAPP</span></div>
        <div><span class="Notice-public">公告</span></div>
        <div><span class="me-public">帮助</span></div>-->
    </div>
</div>
<!--   内容start  -->
<div class="content-box">
    <div class="content-box-text">
        <div class="content-box-text-img"></div>
        <div>500</div>
        <div class="non-existent">服务器发生内部错误，请稍后再试</div>
        <div>
            <div class="return">返回</div>
            <div class="refresh">刷新</div>
        </div>
    </div>
</div>
<!--   内容end  -->
</body>
<script src="/static/common/jquery/jquery.min.js"></script>
<script>
	let returnDom = $('.return');
	let refreshDom = $('.refresh');
	let topLangImg = $('.top-lang-img');

	//语言版本
	let Language = {
		'.Notice-public': {zh: '公告', en: 'Notice'},
		'.me-public': {zh: '帮助', en: 'Help'},
		'.non-existent': {zh: '服务器发生内部错误，请稍后再试', en: 'The server report a error, please try again later'},
		'.return': {zh: '返回', en: 'Return'},
		'.refresh': {zh: '刷新', en: 'Refresh'},
	};

	//返回点击事件
	returnDom.click(function () {
		window.history.go(-1);
	});

	//刷新点击事件
	refreshDom.click(function () {
		window.history.go(0);
	});

	//切换语言点击事件
	$('.top-box-lang').click(function () {
		$(".top-lang-center").slideToggle();
	});

	//切换中文的方法
	let langZh = function () {
		localStorage.setItem("lang", "Zh");
		topLangImg.attr('src', '/static/images/Hong Kong.png');
		for (let index in Language) {
			$(index).html(Language[index].zh);
		}
	};

	//切换英文的方法
	let langEn = function () {
		localStorage.setItem("lang", "En");
		topLangImg.attr('src', '/static/images/mw.png');
		for (let index in Language) {
			$(index).html(Language[index].en);
		}
	};

	//当前语言
	let language = function () {
		return localStorage.getItem("lang") === null || localStorage.getItem("lang") === 'En';
	};

	//加载页面的时候判断上次的语言
	if (language()) {
		langEn();
		$('.lang-background-box').hide();
		$(".lang-background-center").css('right', '68px');
	} else {
		langZh();
		$('.lang-background-EN').hide();
		$(".lang-background-center").css('right', '32px');
	}

	//pc端切换中文
	$('.top-lang-zh').click(function () {
		langZh();
	});
	//pc端切换英文
	$('.top-lang-en').click(function () {
		langEn();
	});

	//移动端右侧小图标点击事件
	$('#RightLogo').click(function () {
		$('.RightLogo-box').slideToggle();
	});

	//移动端中英文切换
	$('#langBox').click(function () {
		//判断当前是否是英文
		if (language()) {
			langZh();
			$('.lang-background-EN').hide();
			$(".lang-background-center").animate({right: '32px'}, 500, function () {
				$('.lang-background-box').show();
			});
		} else {
			langEn();
			$('.lang-background-box').hide();
			$(".lang-background-center").animate({right: '68px'}, 500, function () {
				$('.lang-background-EN').show();
			});
		}
	});
</script>
</html>