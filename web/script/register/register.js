/**
 * 挂号
 * author: iamaprin
 * time: 2016/5/6 19:08
 */
(function ($, $w) {
    $(document).ready(function () {
        loadDeptOption();
        clickEvent();
    });

    var clickEvent = function () {
        $("#btn-register").click(doRegister);
    };

    var doRegister = function () {
        var name = $("#patient-name").val();
        var idNumber = $("#patient-id-number").val();
        var sexId = $("#patient-sex").val();
        var deptId = $("#select-dept").find("option:selected").val();

        if (name == "" || idNumber == "") {
            showTip("姓名或身份证号不为空！");
            return
        }

        var sex = parseInt(sexId) == 0 ? "男" : "女";
        var jsonData = {
            "name": name,
            "idNumber": idNumber,
            "sex": sex,
            "deptId": deptId
        };
        $.getJSON("register/doRegister", jsonData, function (data) {
            if (data == false) {
                showTip("挂号失败！")
            } else {
                console.log(data);
                loadRightInfo(data);
            }
        });
    };

    var loadRightInfo = function (data) {
        $("#name").text(data.pname);
        $("#id-number").text(data.idNumber);
        $("#sex").text(data.sex);
        $("#dept").text(data.deptName + "(" + data.deptId + ")");
        $("#queue-id").text(data.assignedNumber);
        $("#dr").text(data.drName + "(" + data.drId + ")");
    }


    /**
     *  挂号：加载科室下拉框数据
     */
    var loadDeptOption = function () {
        $.getJSON("register/getDeptData", function (data) {
            var len = data.length;
            var obj = null;
            for (var i = 0; i < len; i++) {
                obj = $.parseJSON(data[i]);
                //console.log(obj);
                $("#select-dept").append("" +
                    "<option value='" + obj.id + "'>" + obj.dept_name + "</option>"
                );
            }
        });
    };

    /**
     * 显示提示
     * @param msg       提示内容
     */
    var showTip = function (msg) {
        var selector = "#tip-register";
        $(selector).text(msg);
        $(selector).show();
        setTimeout(function () {
            $(selector).fadeOut();
        }, 1000);
    }
    
})(jQuery, window);
