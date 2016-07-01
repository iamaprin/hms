/**
 * author: iamaprin
 * time: 2016/5/13 16:05
 */

(function ($, $w) {
    $(document).ready(function () {
        initPage();
        clickEvent();
    });

    var initPage = function () {
        initDrName();
        isOnDuty();
    };

    var clickEvent = function () {
        $("#btn-query-case").click(loadCaseList);
        $("#do-register").click(showRight1);
        $("#do-dispense").click(showRight2);
        $("#sign-out").click(signOut);
    };

    var isOnDuty = function () {
        var drId = $("#dr-id").val();
        var date = new Date();
        var today =date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
        $.getJSON("isOnDuty", {"drId": drId, "today": today}, function (date) {
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

    var loadCaseList = function () {
        $("#id-number-dlg").modal("hide");
        $("#case-list").empty();
        $w.localStorage.clear();
        var idNumber = $("#id-number").val();
        $.getJSON("loadCaseListForPha", {"idNumber": idNumber}, function (data) {
            if (data == false) {
                alert("无需要处理的配药信息！");
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
                        "<td class='medicine'>" + obj.medicine + "</td>" +
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
        var medicine = $(this).parent().parent().find(".medicine").text();
        var tmp = $(this);
        $.getJSON("addToQueueForPha", {"caseId": caseId, "medicine": medicine}, function (data) {
            if (data) {
                $.getJSON("updateMedStatus", {"caseId": caseId}, function (flag) {
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
    
    var showRight1 = function () {
        $(".right").children().addClass("hidden");
        $("#register-patient").removeClass("hidden");
    };
    
    var showRight2 = function () {
        $(".right").children().addClass("hidden");
        $("#med-queue").removeClass("hidden");
        loadPhaQueue(1, 8);
    };

    var loadPhaQueue = function (pageNumber, pageSize) {
        $("#pha-queue").empty();
        var pn = pageNumber == null ? 1 : pageNumber;
        var ps = pageSize == null ? 8 : pageSize;
        var jsonData = {
            "pageNumber": pn,
            "pageSize": ps
        };
        $.getJSON("loadQueueForPha", jsonData, function (data) {
            console.log(data);
            if (data) {
                var len = data.length;
                var obj = null;
                for (var i = 0; i < len; i++) {
                    obj = $.parseJSON(data[i]);
                    $("#pha-queue").append(
                        "<tr>" +
                            "<td class='case-id-2'>" + obj.case_id + "</td>" +
                            "<td class='hidden queue-id'>" + obj.id + "</td>" +
                            "<td>" + obj.medicine + "</td>" +
                            "<td>" +
                                "<button class='btn btn-default take-medicine' id='take-medicine'>取药</button>" +
                            "</td>" +
                        "</tr>"
                    );
                }
            }
            $(".take-medicine").click(takeMedicine);
        });
    };
    
    var takeMedicine = function () {
        var id = $(this).parent().parent().find(".queue-id").text();
        var tmp = $(this);
        $.getJSON("updatePhaStatus", {"id": id}, function (data) {
            if (data) {
                tmp.text("已取药");
                tmp.attr("disabled", "disabled");
                alert("更新信息成功！")
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
