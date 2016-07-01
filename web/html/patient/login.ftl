<#include "/html/template/home.ftl">
<@home>
    <#include "/html/template/header.ftl"/>
    <@header></@header>
<link rel="stylesheet" type="text/css" href="${contextPath}/style/patient.css">
<script src="${contextPath}/script/patient/login.js"></script>
<div class="content">
    <div class="login">
        <form class="form-horizontal" role="form" onsubmit="return false">
            <div class="form-group">
                <label for="username" class="col-md-3 control-label">账号</label>
                <div class="col-md-7">
                    <input type="text" class="form-control" id="username" placeholder="请输入账号,即身份证号">
                </div>
            </div>
            <div class="form-group">
                <label for="password" class="col-md-3 control-label">密码</label>
                <div class="col-md-7">
                    <input type="password" class="form-control" id="password" placeholder="请输入密码">
                </div>
            </div>
            <div class="form-group">
                <div class="col-md-offset-3 col-md-2">
                    <button type="submit" class="btn btn-default" id="btn-login">登录</button>
                </div>
            </div>
            <div class="form-group">
                <div class="col-md-offset-3">
                    <span class="tip tip-login" id="tip-login"></span>
                </div>
            </div>
        </form>
    </div>
</div>
</@home>