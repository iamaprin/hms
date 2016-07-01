<#include "/html/template/home.ftl">
    <@home>
    <#include "/html/template/header.ftl">
        <@header></@header>
    <link rel="stylesheet" type="text/css" href="${contextPath}/style/register.css">
    <script src="${contextPath}/script/register/register.js"></script>
    <div class="content">
        <div class="left">
            <div class="register">
                <form class="form-horizontal" role="form" onsubmit="return false">
                    <div class="form-group">
                        <label for="patient-name" class="col-md-3 control-label">病人姓名</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" id="patient-name" placeholder="请输入病人姓名">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="patient-id-number" class="col-md-3 control-label">身份证号</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" id="patient-id-number" placeholder="请输入身份证号">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="patient-sex" class="col-md-3 control-label">性别</label>
                        <div class="col-md-7">
                            <select class="register-select select-sex" id="patient-sex">
                                <option value="0">男</option>
                                <option value="1">女</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="select-dept" class="col-md-3 control-label">挂号科室</label>
                        <div class="col-md-7">
                            <select class="register-select select-dept" id="select-dept"></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-offset-3 col-md-2">
                            <button type="submit" class="btn btn-default" id="btn-register">挂号</button>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-offset-3">
                            <span class="tip tip-login" id="tip-register"></span>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="right">
            <div class="right-header">
                <p>挂号信息</p>
            </div>
            <div class="right-list">
                <label class="col-md-4">病人姓名：</label>
                <span id="name"></span>
            </div>
            <div class="right-list">
                <label class="col-md-4">身份证号：</label>
                <span id="id-number"></span>
            </div>
            <div class="right-list">
                <label class="col-md-4">性别：</label>
                <span id="sex"></span>
            </div>
            <div class="right-list">
                <label class="col-md-4">挂号科室：</label>
                <span id="dept"></span>
            </div>
            <div class="right-list">
                <label class="col-md-4">分配号码：</label>
                <span id="queue-id"></span>
            </div>
            <div class="right-list">
                <label class="col-md-4">分配医生：</label>
                <span id="dr"></span>
            </div>
        </div>
    </div>
</@home>