<#include "/html/template/home.ftl">
<@home>
    <#include "/html/template/header.ftl">
    <@header></@header>
    <script>
        $(document).ready(function () {
            $("#btn-add").click(addTest);
        });

        var addTest = function () {
            console.log(123);
            var testName = $("#test-name").val();
            if (testName == "") {
                alert("添加失败！");
                return;
            }
            $.getJSON("addTest", {"testName": testName}, function (data) {
                if (data == true) {
                    alert("添加成功！");
                    $("#test-name").val("");
                }
            });
        };
    </script>
    <style>
        .content {
            text-align: center;
        }

        .add-test {
            position: relative;
            top: 200px;
        }

        .add-test > input {
            height: 30px;
        }

    </style>
    <div class="content">
        <div class="add-test">
            <input title="name" type="text" id="test-name">
            <button id="btn-add">add</button>
        </div>
    </div>
</@home>