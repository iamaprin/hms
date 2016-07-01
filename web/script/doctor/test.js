/**
 * author: iamaprin
 * time: 2016/5/15 21:29
 */

(function ($, $w) {
    $(document).ready(function () {
        init();
        clickEvent();
    });
    
    var init = function () {
        initPage();
    };
    
    var clickEvent = function () {
        $("#btn-query-case").click(loadCaseList);
        $("#do-receive").click(showRight1);
        $("#do-test").click(showRight2);
        $("#btn-add-result").click(addTestResult);
        $("#sign-out").click(signOut);
    };

    var initPage = function () {
        initDrName();
        isOnDuty();
    };

    var isOnDuty = function () {
        var drId = $("#dr-id").val();
        var date = new Date();
        var today =date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
        $.getJSON("isOnDutyForTest", {"drId": drId, "today": today}, function (date) {
            if (date) {
                enableAllBtn();
                $("#onDuty").removeClass("hidden");
            } else {
                alert("今天不是您值班！");
                $("#notOnDuty").removeClass("hidden");
            }
        });
    };

    var initDrName = function () {
        var drId = $("#dr-id").val();
        $.getJSON("getDrName", {"drId": drId}, function (data) {
            if (data != "") {
                $("#dr-name").text(data.drName);
            }
        });
    };

    var enableAllBtn = function () {
        $(".btn").removeAttr("disabled");
    };

    var showRight1 = function () {
        $(".right").children().addClass("hidden");
        $("#receive-patient").removeClass("hidden");
    };

    var showRight2 = function () {
        $(".right").children().addClass("hidden");
        $("#test-queue").removeClass("hidden");
        loadTestQueue(1, 8);
    };

    var loadCaseList = function () {
        $("#id-number-dlg").modal("hide");
        $("#case-list").empty();
        $w.localStorage.clear();
        var idNumber = $("#id-number").val();
        $.getJSON("loadCaseListForTest", {"idNumber": idNumber}, function (data) {
            if (data == false) {
                alert("无需要处理的检验信息！");
                return;
            }

            $w.localStorage.setItem("caseInfo", data);

            var len = data.length;
            var obj = null;
            for (var i = 0; i < len; i++) {
                obj = $.parseJSON(data[i]);
                $("#case-list").append(
                    "<tr>" +
                    "<td>" + obj.patientName + "</td>" +
                    "<td class='case-id'>" + obj.caseId + "</td>" +
                    "<td class='test'>" + obj.test + "</td>" +
                    "<td>" +
                    "<button class='btn btn-default add-to-queue' id='add-to-queue'>添加</button>" +
                    "</td>" +
                    "</tr>"
                );
            }
            $(".add-to-queue").click(addToQueue);
        });
    };

    var addToQueue = function () {
        var caseId = $(this).parent().parent().find(".case-id").text();
        var test = $(this).parent().parent().find(".test").text();
        var tmp = $(this);
        $.getJSON("addToQueueForTest", {"caseId": caseId, "test": test}, function (data) {
            if (data) {
                $.getJSON("updateTestStatus", {"caseId": caseId}, function (flag) {
                    if (flag) {
                        tmp.text("已添加");
                        tmp.attr("disabled", "disabled");
                        alert("添加成功！")
                    }
                });
            } else {
                alert("出现一些问题！")
            }
        });
    };

    var loadTestQueue = function (pageNumber, pageSize) {
        $("#test-list").empty();
        var pn = pageNumber == null ? 1 : pageNumber;
        var ps = pageSize == null ? 8 : pageSize;
        var jsonData = {
            "pageNumber": pn,
            "pageSize": ps
        };
        $.getJSON("loadQueueForTest", jsonData, function (data) {
            console.log(data);
            if (data) {
                var len = data.length;
                var obj = null;
                for (var i = 0; i < len; i++) {
                    obj = $.parseJSON(data[i]);
                    $("#test-list").append(
                        "<tr>" +
                            "<td class='case-id-2'>" + obj.case_id + "</td>" +
                            "<td class='hidden queue-id'>" + obj.id + "</td>" +
                            "<td>" + obj.test + "</td>" +
                            "<td>" +
                                "<button class='btn btn-default take-test'>呼叫病人</button>" +
                            "</td>" +
                            "<td>" +
                                "<button class='btn btn-default test-result'  data-toggle='modal' data-target='#test-result-dlg'>检验结果</button>" +
                            "</td>" +
                        "</tr>"
                    );
                }
            }
            $(".take-test").click(takeTest);
            $(".test-result").click(storeCurrentCaseId);
        });
    };

    var takeTest = function () {
        var id = $(this).parent().parent().find(".queue-id").text();
        var tmp = $(this);
        $.getJSON("updateStatusForTest", {"id": id}, function (data) {
            if (data) {
                tmp.text("已呼叫");
                tmp.attr("disabled", "disabled");
                //alert("更新信息成功！")
            }
        })
    };
    
    var storeCurrentCaseId = function () {
        var id = $(this).parent().parent().find(".queue-id").text();
        var caseId = $(this).parent().parent().find(".case-id-2").text();
        $w.localStorage.setItem("currentCaseId", caseId);
        $w.localStorage.setItem("currentId", id);
    };

    var addTestResult = function () {
        var id = $w.localStorage.getItem("currentId");
        var caseId = $w.localStorage.getItem("currentCaseId");
        var testResult = $("#test-result").val();
        $.getJSON("addTestResult", {"caseId": caseId, "testResult": testResult}, function (data) {
            if (data) {
                $w.localStorage.setItem("currentCaseId", "");
                $("#test-result-dlg").modal("hide");
                alert("添加成功");
                $.getJSON("updateStatusForTest2", {"id": id});
            } else {
                alert("添加失败");
            }
        });
    };
    
    var signOut = function () {
        $.getJSON("signOut", function (flag) {
            if (flag == true) {
                $w.location.href = "login";
            }
        });
    };
})(jQuery, window);