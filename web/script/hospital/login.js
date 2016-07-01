;
(function ($) {
    $(document).ready(function () {
        clickEvent();
    });
    
    function clickEvent() {
        $("#login-btn").click(doLogin);
    }
    
    function doLogin() {
        var username = $("#username").val();
        var password = $("#password").val();
        
        if (username == "" || password == "") {
            showTip("#login-tip", "账号或密码不为空！");
            return;
        }

        var jsonData = {
                "username": username,
                "password": password
        };
        $.getJSON("hospital/login", jsonData, function (data) {
            if (data == false) {
                console.log("login failure");
                showTip("#login-tip", "账号与密码不匹配！");
                $("#password").val("");
            } else {
                console.log("login success");
                window.location.href = "hospital/manage";
            }
        });

    }

    function showTip(selector, msg) {
        $(selector).text(msg);
        $(selector).show();
        setTimeout(function () {
            $(selector).fadeOut();
        }, 1000);
    }
})(jQuery);