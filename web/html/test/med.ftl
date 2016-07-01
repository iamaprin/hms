<#include "/html/template/home.ftl">
<@home>
    <#include "/html/template/header.ftl">
    <@header></@header>
    <script>
        $(document).ready(function () {
            loadMedCat0();
            loadMedCat1(1);
            $("#med-cat-0").change(cat0Click);
            $("#btn-add").click(addMedInfo);
        });

        var addMedInfo = function () {
            var superId = $("#med-cat-1").children('option:selected').val();
            var medName = $("#med-name").val();
            var medUse = $("#med-use").val();
            var jsonData = {
                "superId": superId,
                "medName": medName,
                "medUse": medUse
            };
            $.getJSON("addMed", jsonData, function (data) {
                if (data) {
                    alert("success");
                    $("#med-name").val("");
                    $("#med-use").val("");
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

        var cat0Click = function () {
            var superId = $(this).children('option:selected').val();
            loadMedCat1(superId)
        };

        var loadMedCat1 = function (superId) {
            $("#med-cat-1").empty();
            //noinspection JSDuplicatedDeclaration
            $.getJSON("showMedCat1", {"superId": superId}, function (data) {
                var len = data.length;
                var obj = null;
                for (var i = 0; i < len; i++) {
                    obj = $.parseJSON(data[i]);
                    $("#med-cat-1").append("" +
                            "<option value='" + obj.id + "'>" + obj.cat_name + "</option>"
                    );
                }
            });
        }
    </script>
    <style>
        .med-select-2 {
            width: 200px;
        }
    </style>
    <div class="content">
        <select title="cat-0" class="med-select-1" id="med-cat-0">
            <option></option>
        </select>
        <select title="cat-1" class="med-select-2" id="med-cat-1"></select><br> 
        <input title="name" type="text" id="med-name"><br>
        <input title="name" type="text" id="med-use"><br>
        <button id="btn-add">add</button>
    </div>
</@home>