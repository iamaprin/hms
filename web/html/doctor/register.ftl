<#include "/html/template/home.ftl">
<@home>
    <#include "/html/template/header.ftl">
    <@header></@header>
    <link rel="stylesheet" type="text/css" href="${contextPath}/style/doctor.css">
    <script src="${contextPath}/script/doctor/register.js"></script>
    <div class="content">
        <div class="register">
            <form class="form-horizontal" role="form" onsubmit="return false">
                <div class="form-group">
                    <label for="username" class="col-md-3 control-label">账号</label>
                    <div class="col-md-7">
                        <input type="text" class="form-control" id="username" placeholder="请输入账号">
                    </div>
                </div>
                <div class="form-group">
                    <label for="password" class="col-md-3 control-label">密码</label>
                    <div class="col-md-7">
                        <input type="password" class="form-control" id="password" placeholder="请输入密码">
                    </div>
                </div>
                <div class="form-group">
                    <label for="dr-name" class="col-md-3 control-label">姓名</label>
                    <div class="col-md-7">
                        <input type="text" class="form-control" id="dr-name" placeholder="请输入姓名">
                    </div>
                </div>
                <div class="form-group">
                    <label for="select-sex" class="col-md-3 control-label">性别</label>
                    <div class="col-md-7">
                        <select class="login-select select-sex" id="select-sex">
                            <option value="0">男</option>
                            <option value="1">女</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label for="select-dept" class="col-md-3 control-label">所属科室</label>
                    <div class="col-md-7">
                        <select class="login-select select-dept" id="select-dept"></select>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-md-offset-3 col-md-2">
                        <button type="submit" class="btn btn-default" id="btn-register">注册</button>
                    </div>
                    <div class="col-md-2">
                        <button type="submit" class="btn btn-default" id="btn-login">返回登陆</button>
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
</@home>