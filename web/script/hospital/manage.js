(function ($, $w) {
    $(document).ready(function () {
        clickEvent();
    });

    function clickEvent() {

        $("#btn-doctor").click(showDoctor);
        $("#btn-register").click(showRegister);
        $("#btn-pharmacy").click(showPharmacy);
        $("#btn-ask-status").click(showAsk);
        $("#btn-test").click(showTest);
        $("#btn-pre-page").click(turnPage.prePage);
        $("#btn-next-page").click(turnPage.nextPage);
        $("#sign-out").click(signOut);
    }

    /*---------------------------------------------------------------------------*/

    var showAsk = function () {
        $(".right").children().addClass("hidden");
        $("#ask-status").removeClass("hidden");
        showAskStatus();
    };

    var showAskStatus = function () {
        $("#ask-list").empty();
        $.getJSON("loadReplyList", function (data) {
            if (!data) {
                alert("无数据！");
                return;
            }
            var obj = undefined;
            var len = data.length;
            for (var i = 0; i < len; i++) {
                obj = $.parseJSON(data[i]);
                $("#ask-list").append(
                    "<tr>" +
                        "<td>" + obj.time + "</td>" +
                        "<td>" + obj.case_id + "</td>" +
                        "<td>" + obj.patient_id + "</td>" +
                        "<td>" + obj.doctor_id + "</td>" +
                        "<td>" + obj.is_read + "</td>" +
                    "</tr>"
                );
            }
        });
    };

    var signOut = function () {
        $.getJSON("signOut", function (flag) {
            if (flag == true) {
                $w.location.href = "./";
            }
        });
    };
    

    var showDoctor = function () {
        loadDrList();
        $(".right").children().addClass("hidden");
        $("#doctor-manage").removeClass("hidden");
        console.log(turnPage.totalPage);
    };

    var showRegister = function () {
        initRegister();
        $(".right").children().addClass("hidden");
        $("#reg-manage").removeClass("hidden");
    };

    var showPharmacy = function () {
        initPharmacy();
        $(".right").children().addClass("hidden");
        $("#pharmacy-manage").removeClass("hidden");
    };

    var showTest = function () {
        initTest();
        $(".right").children().addClass("hidden");
        $("#test-manage").removeClass("hidden");
    };

    /*---------------------------------------------------------------------------*/
    
    var initPharmacy = function () {
        $("#pharmacy-list").empty();
        $.getJSON("loadPharmacyList", function (data) {
            console.log(data);
            var len = data.length;
            var obj = null;

            for (var i = 0; i < len; i++) {
                obj = $.parseJSON(data[i]);

                var date = changeDate(i);
                var md = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                var w = getWeek(date.getDay());
                $("#pharmacy-list").append("" +
                    "<tr>" +
                    "<td class='pharmacy-date'>" + (i == 0 ? md + '<span class="disabled">[今天]</span>' : md) + "</td>" +
                    "<td>" + w + "</td>" +
                    "<td>" + (i == 0 ? "<input class='form-control' value='" + getDrName(obj.dr_id) + "' readonly>"
                        : "<select class='pharmacy-select' id='pha-" + obj.id + "'></select>") + "</td>" +
                    "</tr>"
                );
            }
            for (var k = len; k < 7; k++) {
                date = changeDate(k);
                md = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                w = getWeek(date.getDay());
                $("#pharmacy-list").append("" +
                    "<tr>" +
                    "<td class='pharmacy-date'>" + (i == 0 ? md + '<span class="disabled">[今天]</span>' : md) + "</td>" +
                    "<td>" + w + "</td>" +
                    "<td>" + "<select class='pharmacy-select'></select>" + "</td>" +
                    "</tr>"
                );
            }
            $(".pharmacy-select").change(pharmacySelectChange);
            loadPharmacyDrSelect();
            for (var j = 1; j < len; j++) {
                obj = $.parseJSON(data[j]);
                //console.log(obj.dr_id);
                $("#pha-" + obj.id + " option[value='" + obj.dr_id + "']").prop("selected", true);
            }
        });

    };

    var getDrName = function (drId) {
        var drName= null;
        $.ajax({
            async: false,
            url: "queryNameByDrId",
            data: {"drId": drId},
            success: function (data) {
                drName =  data.drName;
            },
            error: function () {
                drName =  "{error}";
            }
        });
        return drName;
    };

    var pharmacySelectChange = function () {
        var date = $(this).parent().parent().find(".pharmacy-date").text();
        var drId = $(this).find("option:selected").val();
        var dutyId = null;
        var tmp = $(this);

        $.getJSON("isDateExistForPha", {"date": date}, function (data) {
            if (data) {
                dutyId = tmp.attr("id").split("-")[1];
                //console.log(dutyId);
                $.getJSON("updatePharmacyDuty", {"dutyId": dutyId, "drId": drId, "date": date}, function (flag) {

                })
            } else {
                $.getJSON("addPharmacyDuty", {"drId": drId, "date": date}, function (flag) {
                    if (flag) {
                        initPharmacy();
                    }
                });
            }
        });
    };

    var loadPharmacyDrSelect = function () {
        $.ajax({
            async: false,
            url: "loadPharmacyDr",
            success: function (data) {
                var len = data.length;
                var obj = null;
                $(".pharmacy-select").append("<option value='0'>请选择值班医生</option>");
                for (var i = 0; i < len; i++) {
                    obj = $.parseJSON(data[i]);
                    $(".pharmacy-select").append("<option value='" + obj.id + "'> " + obj.dr_name + " </option>");
                }
            },
            fail: function () {
                console.log("fail");
            }
        });
    };

    /*---------------------------------------------------------------------------*/

    var initTest = function () {
        $("#test-list").empty();
        $.getJSON("loadTestList", function (data) {
            console.log(data);
            var len = data.length;
            var obj = null;

            for (var i = 0; i < len; i++) {
                obj = $.parseJSON(data[i]);

                var date = changeDate(i);
                var md = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                var w = getWeek(date.getDay());
                $("#test-list").append("" +
                    "<tr>" +
                    "<td class='test-date'>" + (i == 0 ? md + '<span class="disabled">[今天]</span>' : md) + "</td>" +
                    "<td>" + w + "</td>" +
                    "<td>" + (i == 0 ? "<input class='form-control' value='" + getDrName(obj.dr_id) + "' readonly>"
                        : "<select class='test-select' id='test-" + obj.id + "'></select>") + "</td>" +
                    "</tr>"
                );
            }
            for (var k = len; k < 7; k++) {
                date = changeDate(k);
                md = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                w = getWeek(date.getDay());
                $("#test-list").append("" +
                    "<tr>" +
                    "<td class='test-date'>" + (i == 0 ? md + '<span class="disabled">[今天]</span>' : md) + "</td>" +
                    "<td>" + w + "</td>" +
                    "<td>" + "<select class='test-select'></select>" + "</td>" +
                    "</tr>"
                );
            }
            $(".test-select").change(testSelectChange);
            loadTestDrSelect();
            for (var j = 1; j < len; j++) {
                obj = $.parseJSON(data[j]);
                //console.log(obj.dr_id);
                $("#test-" + obj.id + " option[value='" + obj.dr_id + "']").prop("selected", true);
            }
        });

    };

    var testSelectChange = function () {
        var date = $(this).parent().parent().find(".test-date").text();
        var drId = $(this).find("option:selected").val();
        var dutyId = null;
        var tmp = $(this);

        $.getJSON("isDateExistForTest", {"date": date}, function (data) {
            if (data) {
                dutyId = tmp.attr("id").split("-")[1];
                //console.log(dutyId);
                $.getJSON("updateTestDuty", {"dutyId": dutyId, "drId": drId, "date": date}, function (flag) {

                })
            } else {
                $.getJSON("addTestDuty", {"drId": drId, "date": date}, function (flag) {
                    if (flag) {
                        initTest();
                    }
                });
            }
        });
    };

    var loadTestDrSelect = function () {
        $.ajax({
            async: false,
            url: "loadTestDr",
            success: function (data) {
                var len = data.length;
                var obj = null;
                $(".test-select").append("<option value='0'>请选择值班医生</option>");
                for (var i = 0; i < len; i++) {
                    obj = $.parseJSON(data[i]);
                    $(".test-select").append("<option value='" + obj.id + "'> " + obj.dr_name + " </option>");
                }
            },
            fail: function () {
                console.log("fail");
            }
        });
    };

    /*---------------------------------------------------------------------------*/

    var initRegister = function () {
        $("#reg-list").empty();
        $.getJSON("loadRegList", function (data) {
            console.log(data);
            var len = data.length;
            var obj = null;

            for (var i = 0; i < len; i++) {
                obj = $.parseJSON(data[i]);

                var date = changeDate(i);
                var md = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                var w = getWeek(date.getDay());
                $("#reg-list").append("" +
                    "<tr>" +
                    "<td class='reg-date'>" + (i == 0 ? md + '<span class="disabled">[今天]</span>' : md) + "</td>" +
                    "<td>" + w + "</td>" +
                    "<td>" + (i == 0 ? "<input class='form-control' value='" + getDrName(obj.dr_id) + "' readonly>"
                        : "<select class='reg-select' id='reg-" + obj.id + "'></select>") + "</td>" +
                    "</tr>"
                );
            }
            for (var k = len; k < 7; k++) {
                date = changeDate(k);
                md = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                w = getWeek(date.getDay());
                $("#reg-list").append("" +
                    "<tr>" +
                    "<td class='reg-date'>" + (i == 0 ? md + '<span class="disabled">[今天]</span>' : md) + "</td>" +
                    "<td>" + w + "</td>" +
                    "<td>" + "<select class='reg-select'></select>" + "</td>" +
                    "</tr>"
                );
            }
            $(".reg-select").change(regSelectChange);
            loadRegDrSelect();
            for (var j = 1; j < len; j++) {
                obj = $.parseJSON(data[j]);
                //console.log(obj.dr_id);
                $("#reg-" + obj.id + " option[value='" + obj.dr_id + "']").prop("selected", true);
            }
        });

    };

    var regSelectChange = function () {
        var date = $(this).parent().parent().find(".reg-date").text();
        var drId = $(this).find("option:selected").val();
        var dutyId = null;
        var tmp = $(this);

        $.getJSON("isDateExistForReg", {"date": date}, function (data) {
            if (data) {
                dutyId = tmp.attr("id").split("-")[1];
                $.getJSON("updateRegDuty", {"dutyId": dutyId, "drId": drId, "date": date}, function (flag) {

                })
            } else {
                $.getJSON("addRegDuty", {"drId": drId, "date": date}, function (flag) {
                    if (flag) {
                        initTest();
                    }
                });
            }
        });
    };

    var loadRegDrSelect = function () {
        $.ajax({
            async: false,
            url: "loadRegDr",
            success: function (data) {
                var len = data.length;
                var obj = null;
                $(".reg-select").append("<option value='0'>请选择值班医生</option>");
                for (var i = 0; i < len; i++) {
                    obj = $.parseJSON(data[i]);
                    $(".reg-select").append("<option value='" + obj.id + "'> " + obj.dr_name + " </option>");
                }
            },
            fail: function () {
                console.log("fail");
            }
        });
    };

    /*---------------------------------------------------------------------------*/
    var loadDrList = function (pageNumber, pageSize) {
        var pn = pageNumber == null ? 1 : pageNumber;
        var ps = pageSize == null ? 8 : pageSize;

        $("#dr-list").empty();
        $.getJSON("loadDrList", {"pageNumber": pn, "pageSize": ps}, function (data) {
            var len = data.length;
            var obj = null;
            totalPage = data[0];
            for (var i = 1; i < len; i++) {
                obj = $.parseJSON(data[i]);
                $("#dr-list").append(
                    "<tr>" +
                        "<td>" + obj.id + "</td>" +
                        "<td>" + obj.dr_name + "</td>" +
                        "<td>" + obj.dr_sex + "</td>" +
                        "<td>" +
                            "<select class='dr-select' id='dr-" + obj.id + "'></select>" +
                        "</td>" +
                    "</tr>"
                );
            }
            $(".dr-select").change(drSelectChange);
            loadDeptList();
            for (var j = 1; j < len; j++) {
                obj = $.parseJSON(data[j]);
                console.log(obj.id + ": " + obj.dept_id);
                console.log("#dr-" + obj.id + " option[value='" + obj.dept_id + "']");
                $("#dr-" + obj.id + " option[value='" + obj.dept_id + "']").prop("selected", true);
            }

        });
    };
    
    var loadDeptList = function () {
        $(".dr-select").empty();
        $.ajax({
            async: false,
            url: "loadDeptList",
            success: function (data) {
                var len = data.length;
                var obj = null;
                for (var i = 0; i < len; i++) {
                    obj = $.parseJSON(data[i]);
                    $(".dr-select").append(
                        "<option value='" + obj.id + "'>" + obj.dept_name + "</option>"
                    );
                }
            },
            fail: function () {
                console.log("fail");
            }
        });
    };

    var drSelectChange = function () {
        var drId = $(this).attr("id").split("-")[1];
        var deptId = $(this).find("option:selected").val();
        $.getJSON("updateDept", {"drId": drId, "deptId": deptId}, function (date) {
            if (date) {
                console.log("seccess");
            } else {
                console.log("fail");
            }
        })
    };

    var turnPage = {
        prePage: function () {
            loadDrList(thisPage == 1 ? 1: --thisPage);
        },
        nextPage: function () {
            loadDrList(thisPage == totalPage ? totalPage : ++thisPage);
        }
    };

    /*---------------------------------------------------------------------------*/

    var getWeek = function (num) {
        var result = "";
        switch (num) {
            case 0:
                result = "星期日";
                break;
            case 1:
                result = "星期一";
                break;
            case 2:
                result = "星期二";
                break;
            case 3:
                result = "星期三";
                break;
            case 4:
                result = "星期四";
                break;
            case 5:
                result = "星期五";
                break;
            case 6:
                result = "星期六";
                break;
            default:
                result = "wrong";
        }
        return result;
    };

    var changeDate = function (num) {
        var now = new Date();
        return new Date(now.valueOf() + num * 24 * 60 * 60 * 1000);
    };

    var totalPage;
    var thisPage = 1;


})(jQuery, window);