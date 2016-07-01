<#include "/html/template/home.ftl">
<@home>
    <#include "/html/template/header.ftl"/>
    <@header>
        <#--<li class="cursor-pointer"><a id="modify-password">修改密码</a></li>-->
        <li class="cursor-pointer"><a id="sign-out">退出登录</a></li>
    </@header>
    <link rel="stylesheet" href="${contextPath}/style/hospital.css">
    <script src="${contextPath}/script/hospital/manage.js"></script>

    <div class="content">
        <div class="left">
            <div>
                <button class="btn btn-default" id="btn-doctor">医生科室管理</button>
                <button class="btn btn-default" id="btn-register">门诊值班管理</button>
                <button class="btn btn-default" id="btn-test">检验值班管理</button>
                <button class="btn btn-default" id="btn-pharmacy">药房值班管理</button>
                <button class="btn btn-default" id="btn-ask-status">病人询问状态</button>
            </div>
        </div>
        <div class="right">
            <#--医生科室管理-->
            <div class="right-1 hidden" id="doctor-manage">
                <div class="right-table col-sm-8">
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <th width="30%">#</th>
                            <th width="30%">医生</th>
                            <th width="20%">性别</th>
                            <th width="20%">科室</th>
                        </tr>
                        </thead>
                        <tbody id="dr-list"></tbody>
                    </table>
                </div>
                <div class="btn-group btn-group-sm right-1-btn" role="group">
                    <button class="btn btn-default" id="btn-pre-page">上一页</button>
                    <button class="btn btn-default" id="btn-next-page">下一页</button>
                </div>
            </div>

            <#--门诊值班管理-->
            <div class="right-2 hidden" id="reg-manage">
                <div class="right-table col-sm-8">
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <th width="30%">日期</th>
                            <th width="30%">星期</th>
                            <th width="40%">值班人员</th>
                        </tr>
                        </thead>
                        <tbody id="reg-list"></tbody>
                    </table>
                </div>
            </div>

            <#--检验值班管理-->
            <div class="right-3 hidden" id="test-manage">
                <div class="right-table col-sm-8">
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <th width="30%">日期</th>
                            <th width="30%">星期</th>
                            <th width="40%">值班人员</th>
                        </tr>
                        </thead>
                        <tbody id="test-list"></tbody>
                    </table>
                </div>
            </div>

            <#--药房值班管理-->
            <div class="right-4 hidden" id="pharmacy-manage">
                <div class="right-table col-sm-8">
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <th width="30%">日期</th>
                            <th width="30%">星期</th>
                            <th width="40%">值班人员</th>
                        </tr>
                        </thead>
                        <tbody id="pharmacy-list"></tbody>
                    </table>
                </div>
            </div>

                <div class="right-5 hidden" id="ask-status">
                    <div class="right-table col-sm-8">
                        <table class="table table-hover">
                            <thead>
                            <tr>
                                <th width="30%">时间</th>
                                <th width="10%">病例id</th>
                                <th width="20%">病人id</th>
                                <th width="20%">医生id</th>
                                <th width="20%">状态</th>
                            </tr>
                            </thead>
                            <tbody id="ask-list"></tbody>
                        </table>
                    </div>
                </div>
        </div>
    </div>
</@home>