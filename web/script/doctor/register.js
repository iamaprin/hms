/**
 * author: iamaprin
 * time: 2016/5/5 23:12
 */
;
(function ($, $w) {
    $(document).ready(function () {
        loadDeptOption();
        clickEvent();
    });

    /**
     * 点击事件
     */
    var clickEvent = function () {
        $("#btn-register").click(doRegister);
        $("#btn-login").click(toLogin);
    };

    /**
     * 注册操作
     */
    var doRegister = function () {
        var username = $("#username").val();
        var password = $("#password").val();
        var drName = $("#dr-name").val();
        var sexId = $("#select-sex").find("option:selected").val();
        var deptId = $("#select-dept").find("option:selected").val();

        if (username == "" || password == "" || drName == "" || sexId == "" || deptId == "") {
            showTip("#tip-register", "输入不为空！")
            return;
        }

        var sex = parseInt(sexId) == 0 ? "男" : "女";
        var jsonData = {
            "username": username,
            "password": password,
            "drName": drName,
            "sex": sex,
            "deptId": deptId
        };
        $.getJSON("doRegister", jsonData, function (data) {
            if (data == true) {
                alert("添加成功，即将返回登陆界面");
                window.location.href = "login";
            } else {
                alert("添加失败！")
            }
        });
    };

    /**
     * 返回登陆界面
     */
    var toLogin = function () {
        window.location.href = "login";
    };

    /**
     *  医生注册：加载科室下拉框数据
     */
    var loadDeptOption = function () {
        $.getJSON("getDeptData", function (data) {
            var len = data.length;
            var obj = null;
            for (var i = 0; i < len; i++) {
                obj = $.parseJSON(data[i]);
                console.log(obj);
                $("#select-dept").append("" +
                    "<option value='" + obj.id + "'>" + obj.dept_name + "</option>"
                );
            }
        });
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