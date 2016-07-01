/**
 * author: iamaprin
 * time: 2016/5/11 17:19
 */

(function ($, $w) {
    $(document).ready(function () {
        clickEvent();
    });

    var clickEvent = function () {
        $("#btn-login").click(doLogin);
    };
    
    /**
     * 登陆操作
     */
    var doLogin = function () {
        var username = $("#username").val();
        var password = $("#password").val();
        if (username == "" || password == "") {
            showTip("账号或密码不为空！");
            return;
        }

        var jsonData = {
            "username": username,
            "password": password
        };
        $.getJSON("doLogin", jsonData, function (data) {
            if (data == true) {
                toManage();
            } else {
                $("#password").val("");
                showTip("账号和密码不匹配！");
            }
        });
    };

    /**
     * 跳转管理界面
     */
    var toManage = function () {
        $w.location.href = "manage";
    };

    /**
     * 显示提示
     * @param msg       提示内容
     */
    var showTip = function (msg) {
        var selector = "#tip-login";
        $(selector).text(msg);
        $(selector).show();
        setTimeout(function () {
            $(selector).fadeOut();
        }, 1000);
    }
})(jQuery, window);
