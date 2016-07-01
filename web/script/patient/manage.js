/**
 * author: iamaprin
 * time: 2016/5/11 17:19
 */

(function ($, $w) {
    $(document).ready(function () {
        init();
        clickEvent();
    });

    var init = function () {
        loadCaseList();
    };

    var clickEvent = function () {
        $("#btn-ask").click(doAsk);
        $("#sign-out").click(signOut);
        $("#show-reply").click(clickReply);
        $("#btn-return").click(backMain);
    };

    var backMain = function () {
        $(".right").addClass("hidden");
        $(".main").removeClass("hidden");
    };

    var clickReply = function () {
        $(".right").addClass("hidden");
        $(".reply-list").removeClass("hidden");
        showReplyList();
    };

    var showReplyList = function () {
        $("#reply-list").empty();
        $.getJSON("loadReplyList", function (data) {
            if (!data) {
                alert("无数据！");
                return;
            }
            var obj = undefined;
            var len = data.length;
            for (var i = 0; i < len; i++) {
                obj = $.parseJSON(data[i]);
                $("#reply-list").append(
                    "<tr>" +
                        "<td>" + obj.case_id + "</td>" +
                        "<td>" + obj.msg + "</td>" +
                        "<td>" + obj.reply + "</td>"+
                    "</tr>"
                );
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

    var setDrId = function () {
        var caseId = $(this).parent().parent().attr("id");
        $.getJSON("getDrId", {"caseId": caseId}, function (data) {
            $w.localStorage.setItem("p_m_dr_id", data.drId);
        });
        $w.localStorage.setItem("p_m_case_id", caseId);
    };
    
    var doAsk = function () {
        var caseId = $w.localStorage.getItem("p_m_case_id");
        var drId = $w.localStorage.getItem("p_m_dr_id");
        var patientId = $("#patient-id").val();
        var askContent = $("#ask-content").val();
        
        if (askContent == "") {
            alert("请输入内容");
            return;
        }
        
        var jsonData = {
            "caseId": caseId,
            "drId": drId,
            "patientId": patientId,
            "askContent": askContent
        };
        $.getJSON("doAsk", jsonData, function (data) {
            if (data) {
                alert("发送成功");
            } else {
                alert("发送失败");
            }
            $("#ask-dlg").modal("hide");
        });
        $w.localStorage.clear();
    };

    var showTest = function () {
        var caseId = $(this).parent().parent().attr("id");
        $.getJSON("getTest", {"caseId": caseId}, function (data) {
            $("#test").val(data.test);
            $("#test-result").val(data.testResult);
        });
    };

    var shwoDiag = function () {
        var caseId = $(this).parent().parent().attr("id");
        //console.log(caseId);
        $.getJSON("getDiagnosis", {"caseId": caseId}, function (data) {
            $("#diagnosis").val(data.diagnosis);
        })
    };

    var showMed = function () {
        var caseId = $(this).parent().parent().attr("id");
        $.getJSON("getMed", {"caseId": caseId}, function (data) {
            $("#medicine").val(data.med);
            var medStatus = data.medStatus;
            if (medStatus == "true") {
                $("#medicine-status").text("已配药");
            } else if (medStatus == "false") {
                $("#medicine-status").text("未配药");
            }
        })
    };

    var loadCaseList = function () {
        var patientId = $("#patient-id").val();
        $.getJSON("loadCaseList", {"patientId": patientId}, function (data) {
            if (data == "") {
                alert("没有数据！");
                return;
            }
            var len = data.length;
            var obj = null;
            for (var i = 0; i < len; i++) {
                obj = $.parseJSON(data[i]);
                var caseId = obj.id;
                var diagnosis = obj.diagnosis;
                var medStatus = obj.medicine_status;
                var testStatus = obj.test_status;


                var lDiagnosis = diagnosis.length > 5
                    ? diagnosis.substring(0, 5) + "...<button class='btn btn-default btn-xs show-diag' id='show-diag' " +
                        "type='button' data-toggle='modal' data-target='#diag-dlg'>更多</button>"
                    : diagnosis;
                var lMedStatus = medStatus == 0 ? "N" : "Y";
                var lTestStatus = testStatus == 0 ? "N" : "Y";
                $("#case-list").append("" +
                    "<tr id='" + caseId + "'>" +
                        "<td>" + obj.time + "</td>" +
                        "<td>" + lDiagnosis + "</td>" +
                        "<td>" +
                            "<button class='btn btn-default btn-xs show-med' id='show-med' data-toggle='modal' data-target='#med-dlg'>查看" + lMedStatus + "</button>" +
                        "</td>" +
                        "<td>" +
                            "<button class='btn btn-default btn-xs show-test' id='show-test' data-toggle='modal' data-target='#test-dlg'>查看" + lTestStatus + "</button>" +
                        "</td>" +
                        "<td>" +
                            "<button class='btn btn-default btn-xs pat-ask' data-toggle='modal' data-target='#ask-dlg'>问询</button>" +
                        "</td>" +
                    "</tr>"
                );
            }
            $(".show-diag").click(shwoDiag);
            $(".show-med").click(showMed);
            $(".show-test").click(showTest);
            $(".pat-ask").click(setDrId);
        });

    };




})(jQuery, window);