/**
 * 医生登陆
 * author: iamaprin
 * time: 2016/5/6 9:29
 */

(function ($,$w) {
    $(document).ready(function () {
        clickEvent();
    });

    /**
     * 点击事件
     */
    var clickEvent = function () {
        $("#btn-login").click(doLogin);
        $("#btn-register").click(toRegister);
    };

    /**
     * 登陆操作
     */
    var doLogin = function () {
        var username = $("#username").val();
        var password = $("#password").val();
        if (username == "" || password == "") {
            showTip("#tip-login", "账号或密码不为空！");
            return;
        }
        
        var jsonData = {
            "username": username,
            "password": password
        };
        $.getJSON("doLogin", jsonData, function (data) {
            if (data == true) {
                $.getJSON("getDeptId", function (deptId) {
                    if (deptId == "34") {
                        toDispense();
                    } else if (deptId == "35") {
                        toTest();
                    } else {
                        toManage();
                    }
                });
            } else {
                $("#password").val("");
                showTip("#tip-login", "账号和密码不匹配！");
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
     * 跳转药房界面
     */
    var toDispense = function () {
        $w.location.href = "dispense";
    };

    /**
     * 跳转检验界面
     */
    var toTest = function () {
        $w.location.href = "test";
    };

    /**
     * 跳转注册界面
     */
    var toRegister = function () {
        $w.location.href = "register";
    };

    /**
     * 显示提示
     * @param selector  元素选择器
     * @param msg       提示内容
     */
    var showTip = function (selector, msg) {
        $(selector).text(msg);
        $(selector).show();
        setTimeout(function () {
            $(selector).fadeOut();
        }, 1000);
    }
})(jQuery, window);