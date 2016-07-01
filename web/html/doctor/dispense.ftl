<#include "/html/template/home.ftl">
<@home>
    <#include "/html/template/header.ftl">
    <@header>
    <#--<li class="cursor-pointer"><a id="modify-password">修改密码</a></li>-->
    <li class="cursor-pointer"><a id="sign-out">退出登录</a></li>
    </@header>
<link rel="stylesheet" type="text/css" href="${contextPath}/style/doctor.css">
<script src="${contextPath}/script/doctor/dispense.js"></script>
<div class="content">
    <div class="left">
        <input type="hidden" value="${drId}" id="dr-id">
        <div class="left-div1">
            <p class="div1-span div1-padding"><strong id="dr-name"></strong>医生</p>
            <p class="div1-span green hidden" id="onDuty">当班</p>
            <p class="div1-span red hidden" id="notOnDuty">非当班</p>
        </div>
        <div class="left-div2">
            <button class="btn btn-default" disabled="disabled" id="do-register" data-toggle="modal" data-target="#id-number-dlg">病人登记</button>
            <button class="btn btn-default" disabled="disabled" id="do-dispense">配药</button>
            <button class="btn btn-default" disabled="disabled">病人取药</button>
        </div>
    </div>
    <div class="right">
        <#--登记病人-->
        <div class="right-1" id="register-patient">
            <div class="right-table col-sm-8">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th width="15%">病人姓名</th>
                        <th width="10%">病历id</th>
                        <th width="60%">所需配药</th>
                        <th width="15%">操作</th>
                    </tr>
                    </thead>
                    <tbody id="case-list"></tbody>
                </table>
            </div>
        </div>

        <#--配药队列-->
        <div class="right-2 hidden" id="med-queue">
            <div class="right-table col-sm-8">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th width="15%">病历id</th>
                        <th width="60%">所需配药</th>
                        <th width="25%">操作</th>
                    </tr>
                    </thead>
                    <tbody id="pha-queue"></tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="modal" tabindex="-1" role="dialog" id="id-number-dlg">
        <div class="modal-dialog">
            <div class="modal-content">
<#--                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">Modal title</h4>
                </div>-->
                <div class="modal-body" style="height: 100px">
                    <div class="form-group">
                        <label for="id-number">身份证号</label>
                        <input type="text" class="form-control" id="id-number" placeholder="请输入身份证号">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" id="btn-query-case">查询</button>
                </div>
            </div>
        </div>
    </div>
</div>
</@home>