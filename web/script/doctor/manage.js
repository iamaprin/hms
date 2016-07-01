/**
 * author: iamaprin
 * time: 2016/5/6 10:17
 */
;
(function ($, $w) {
    $(document).ready(function () {
        emptyPatientInfo();
        initPage();
        loadMedCat0();
        loadMedCat1(1);
        loadTest();
        clickEvent();
    });

    var clickEvent = function () {
        $("#med-cat-0").change(cat0Click);
        $("#med-cat-1").change(cat1Click);

        $("#btn-add-med").click(addMed);
        $("#btn-add-test").click(addTest);

        $("#receive-pat").click(showRight1);
        $("#finish-receive").click(finishReceival);

        $("#reg-this-pat").click(registerPat);

        $("#btn-reg").click(doRegisterPat);
        $("#btn-reply").click(doReply);

        $("#show-case-list").click(showRight2);
        $("#show-ask-list").click(showRight3);
        $("#sign-out").click(signOut);
        $("#btn-case-modify").click(updateCase2);
    };

    var updateCase2 = function () {
        var content = $("#modify-content").val();
        var id = $w.localStorage.getItem("case_id_2");
        $.getJSON("updateContent", {"id": id, "content": content}, function (data) {
            if (data ) {
                alert("修改成功！");
                $("#modify-dlg").modal("hide");
                $w.localStorage.setItem("case_id_2", -1);
            } else {
                alert("修改失败！")
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

    var showRight1 = function () {
        $(".right").addClass("hidden");
        $("#right-1").removeClass("hidden");
        receivePat();
    };

    var showRight2 = function () {
        var patientId = $w.localStorage.getItem("patientId");
        if (patientId == null || patientId == "") {
            alert("目前无接诊病人");
            return;
        }
        $(".right").addClass("hidden");
        $("#right-2").removeClass("hidden");
        loadCaseList();
    };

    var showRight3 = function () {
        $(".right").addClass("hidden");
        $("#right-3").removeClass("hidden");
        loadAskList();
    };
    
    var loadAskList = function () {
        $("#ask-list").empty();
        var drId = $("#dr-id").val();
        $.getJSON("loadAskList", {"drId": drId}, function (data) {
            if (!data) {
                alert("没有数据！");
                return;
            }
            var obj = undefined;
            var len = data.length;
            for (var i = 0; i < len; i++) {
                obj = $.parseJSON(data[i]);
                $("#ask-list").append(
                    "<tr>" +
                        "<td class='hidden'>" + obj.id + "</td>" +
                        "<td>" + obj.case_id + "</td>" +
                        "<td>" + obj.msg + "</td>" +
                        "<td>" +
                            "<button class='btn btn-xs btn-primary btn-ask' value='" + obj.id + "' type='button' data-toggle='modal' data-target='#ask-dlg'>回复</button>" +
                        "</td>" +
                    "</tr>"
                );
            }
            $(".btn-ask").click(clickBtnForAsk);
        });
    };

    var clickBtnForAsk = function () {
        var id = $(this).val();
        $w.localStorage.setItem("ask_id", id);
    };

    var doReply = function () {
        var id = $w.localStorage.getItem("ask_id");
        var replyMsg = $("#reply").val();
        if (replyMsg == "") {
            alert("回复不为空！");
            return;
        }
        $.getJSON("addReply", {"id": id, "replyMsg": replyMsg}, function (flag) {
            if (flag) {
                $.getJSON("updateAskStatus", {"id": id}, function (flag2) {
                    if (flag2) {
                        $("#ask-dlg").modal("hide");
                        alert("添加回复成功！");
                        loadAskList();
                    }
                });
            } else {
                alert("添加回复失败！")
            }

        });
    };

    var doRegisterPat = function () {
        var patientId = $w.localStorage.getItem("patientId");
        var patientPwd = $("#reg-patient-pass").val();
        if (patientPwd == "") {
            alert("请输入密码！");
            return;
        }

        var jsonData = {
            "patientId": patientId,
            "patientPwd": patientPwd
        };
        $.getJSON("doRegisterPat", jsonData, function (data) {
            if (data) {
                alert("注册成功！");
            } else {
                alert("注册失败！");
            }
            $("#register-pat").modal("hide");
        });
    };

    var registerPat = function () {
        var patientId = $w.localStorage.getItem("patientId");
        if (patientId == null || patientId == "") {
            alert("目前无接诊病人");
            return;
        }
        var patientName = $("#patient-name").val();
        var patientIdNumber = $("#patient-number").val();
        $("#reg-patient-name").val(patientName);
        $("#reg-patient-user").val(patientIdNumber);
    };

    var finishReceival = function () {
        changeRegStatus();
        addCaseHistory();
        minusOne();
        emptyPatientInfo();
    };

    var minusOne = function () {
        var drId = $("#dr-id").val();
        $.getJSON("minusOne", {"drId": drId}, function (data) {
            if (data) {
                console.log("minusOne successful");
            } else {
                console.log("minusOne fail");
            }
        });
    };


    var addMed = function () {
        var medId = $("#med").find('option:selected').val();
        var medName = $("#med").find('option:selected').text();
        if (medName == "") {
            $("#add-med").modal("hide");
            return;
        }

        $("#med-selected").append("" +
            "<span>" + medName + "、" + "</span>"
        );
        var medIdLS = $w.localStorage.getItem("medId");
        if (medIdLS == null || medIdLS == "") {
            $w.localStorage.setItem("medId", medId);
        } else {
            $w.localStorage.setItem("medId", medIdLS + "-" + medId);
        }

        $("#add-med").modal("hide");
    };

    var changeRegStatus = function () {
        var regId = $w.localStorage.getItem("regId");
        $.getJSON("updateStatus", {"regId": regId}, function (data) {
            if (data) {
                console.log("update successfully");
                return true;
            } else {
                alert("更新状态失败！");
                return false;
            }
        });
    };

    /**
     * 添加病历
     */
    var addCaseHistory = function () {
        var medId = $w.localStorage.getItem("medId");
        var testId = $w.localStorage.getItem("testId");
        var regId = $w.localStorage.getItem("regId");
        var patientId = $w.localStorage.getItem("patientId");
        var diagnosis = $("#diagnosis").val();
        
        var jsonData = {
            "patientId": patientId,
            "regId": regId,
            "diagnosis": diagnosis,
            "medicine": medId,
            "test": testId
        };
        
        $.getJSON("addCase", jsonData, function (data) {
            if (data) {
                console.log("add successfully");
                return true;
            } else {
                alert("添加失败");
                return false;
            }
        });
    };

    var addTest = function () {
        var testId = $("#test").find('option:selected').val();
        var testName = $("#test").find('option:selected').text();
        if (testName == "") {
            $("#add-test").modal("hide");
            return;
        }

        $("#test-selected").append("" +
            "<span>" + testName + "、" + "</span>"
        );
        var testIdLS = $w.localStorage.getItem("testId");
        if (testIdLS == null || testIdLS == "") {
            $w.localStorage.setItem("testId", testId);
        } else {
            $w.localStorage.setItem("testId", testIdLS + "-" + testId);
        }

        $("#add-test").modal("hide");
    };
    
    var initPage = function () {
        var drId = $("#dr-id").val();
        $.getJSON("getPageInfo", {"drId": drId}, function (data) {
            $("#dr-name").text(data.drName);
            $("#current-num").text(data.queue);
        })
    };

    var emptyPatientInfo = function () {
        $("#patient-name").val("");
        $("#patient-sex").val("");
        $("#patient-number").val("");
        $("#diagnosis").val("");
        $("#med-selected").empty();
        $("#test-selected").empty();
        $w.localStorage.clear();
    };
    var receivePat = function () {
        emptyPatientInfo();

        $w.localStorage.setItem("medId", "");
        $w.localStorage.setItem("testId", "");

        var drId = $("#dr-id").val();
        $.getJSON("getPatient", {"drId": drId}, function (data) {
            $w.localStorage.setItem("patientId", data.patientId);
            $w.localStorage.setItem("regId", data.regId);
            $("#patient-name").val(data.patientName);
            $("#patient-number").val(data.idNumber);
            $("#patient-sex").val(data.patientSex);
        });
    };



    var cat0Click = function () {
        var cat0Id = $(this).find('option:selected').val();
        loadMedCat1(cat0Id);
    };

    var cat1Click = function () {
        var cat1Id = $(this).find('option:selected').val();
        loadMed(cat1Id);
    };
    
    var loadTest = function () {
        $("#test").empty();
        $.getJSON("showTest", function (data) {
            var len = data.length;
            var obj = null;
            for (var i = 0; i < len; i++) {
                obj = $.parseJSON(data[i]);
                $("#test").append("" +
                    "<option value='" + obj.id + "'>" + obj.test_name + "</option>"
                );
            }
        });
    };

    var loadMedCat0 = function () {
        $("#med-cat-0").empty();
        $.getJSON("showMedCat0", function (data) {
            var len = data.length;
            var obj = null;
            for (var i = 0; i < len; i++) {
                obj = $.parseJSON(data[i]);
                $("#med-cat-0").append("" +
                    "<option value='" + obj.id + "'>" + obj.cat_name + "</option>"
                );
            }
        });
    };

    var loadMedCat1 = function (cat0Id) {
        $("#med-cat-1").empty();
        $.getJSON("showMedCat1", {"cat0Id": cat0Id}, function (data) {
            var len = data.length;
            var obj = null;
            $("#med-cat-1").append("<option value='0'>选择子类别</option>");
            for (var i = 0; i < len; i++) {
                obj = $.parseJSON(data[i]);
                $("#med-cat-1").append("" +
                    "<option value='" + obj.id + "'>" + obj.cat_name + "</option>"
                );
            }
        });
    };

    var loadMed = function (cat1Id) {
        $("#med").empty();
        $.getJSON("showMed", {"cat1Id": cat1Id}, function (data) {
            var len = data.length;
            var obj = null;
            for (var i = 0; i < len; i++) {
                obj = $.parseJSON(data[i]);
                $("#med").append("" +
                    "<option value='" + obj.id + "'>" + obj.med_name + "</option>"
                );
            }
        });
    };

    var loadCaseList = function () {
        $("#case-list").empty();
        var patientId = $w.localStorage.getItem("patientId");

        $.getJSON("../patient/loadCaseList", {"patientId": patientId}, function (data) {
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
                            "<button class='btn btn-default btn-xs modify-case' data-toggle='modal' data-target='#modify-dlg'>修改</button>" +
                        "</td>" +
                    "</tr>"
                );
            }
            $(".show-diag").click(shwoDiag);
            $(".show-med").click(showMed);
            $(".show-test").click(showTest);
            $(".modify-case").click(loadCaseInfo);
        });
    };


    var loadCaseInfo = function () {
        var caseId = $(this).parent().parent().attr("id");
        console.log(caseId)
        $w.localStorage.setItem("case_id_2", caseId);
    };

    var showTest = function () {
        var caseId = $(this).parent().parent().attr("id");
        $.getJSON("../patient/getTest", {"caseId": caseId}, function (data) {
            $("#test-2").val(data.test);
            $("#test-result").val(data.testResult);
        });
    };

    var shwoDiag = function () {
        var caseId = $(this).parent().parent().attr("id");
        //console.log(caseId);
        $.getJSON("../patient/getDiagnosis", {"caseId": caseId}, function (data) {
            $("#diagnosis-2").val(data.diagnosis);
        })
    };

    var showMed = function () {
        var caseId = $(this).parent().parent().attr("id");
        $.getJSON("../patient/getMed", {"caseId": caseId}, function (data) {
            $("#medicine").val(data.med);
            var medStatus = data.medStatus;
            if (medStatus == "true") {
                $("#medicine-status").text("已配药");
            } else if (medStatus == "false") {
                $("#medicine-status").text("未配药");
            }
        })
    };
    

})(jQuery, window);