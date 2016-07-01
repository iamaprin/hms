<#include "/html/template/home.ftl">
<@home>
    <#include "/html/template/header.ftl">
    <@header>
    <#--<li class="cursor-pointer"><a id="modify-password">修改密码</a></li>-->
    <li class="cursor-pointer"><a id="sign-out">退出登录</a></li>
    </@header>
<link rel="stylesheet" type="text/css" href="${contextPath}/style/patient.css">
<script src="${contextPath}/script/patient/manage.js"></script>
<div class="content">
    <input id="patient-id" value="${patientId}" hidden="hidden">
    <div class="left">

    </div>
    <div class="right main">
        <span class="left-span"></span>
        <button class="btn btn-default" id="show-reply">查看回复</button>
        <table class="table left-table">
            <caption>病历列表</caption>
            <thead>
            <tr>
                <th>时间</th>
                <th>诊断</th>
                <th>配药(Y/N)</th>
                <th>检验(Y/N)</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody class="table-striped" id="case-list"></tbody>
        </table>
    </div>

    <div class="right reply-list hidden">
        <button class="btn btn-default" id="btn-return">返回</button>
        <table class="table left-table">
            <caption>回复列表</caption>
            <thead>
            <tr>
                <th width="10%">病历id</th>
                <th width="45%">询问</th>
                <th width="45%">回复</th>
            </tr>
            </thead>
            <tbody class="table-striped" id="reply-list"></tbody>
        </table>
    </div>

    <#--诊断对话框-->
    <div class="modal" tabindex="-1" role="dialog" id="diag-dlg">

        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">检验</h4>
                </div>
                <div class="modal-body">
                    <textarea class="form-control" id="diagnosis" rows="6" readonly></textarea>
                </div>
            </div>
        </div>
    </div>

    <#--配药对话框-->
    <div class="modal" tabindex="-1" role="dialog" id="med-dlg">
        <div class="modal-dialog modal-md">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">配药</h4>
                </div>
                <div class="modal-body">
                    <label for="medicine">配药：</label>
                    <textarea class="form-control" id="medicine" rows="3" readonly></textarea><br>
                    <label for="medicine-status">状态：</label>
                    <span id="medicine-status"></span>
                </div>
            </div>
        </div>
    </div>

    <#--检验对话框-->
    <div class="modal" tabindex="-1" role="dialog" id="test-dlg">
        <div class="modal-dialog modal-md">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">检验</h4>
                </div>
                <div class="modal-body">
                    <label for="medicine">检验：</label>
                    <textarea class="form-control" id="test" rows="3" readonly></textarea><br>
                    <label for="medicine-status">检验结果：</label>
                    <textarea class="form-control" id="test-result" rows="3" readonly></textarea><br>
                </div>
            </div>
        </div>
    </div>

    <#--询问对话框-->
    <div class="modal" tabindex="-1" role="dialog" id="ask-dlg">
        <div class="modal-dialog modal-md">
            <div class="modal-content">
                <#--
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">询问</h4>
                </div>
                -->
                <div class="modal-body">
                    <label for="ask-content">询问内容：</label>
                    <textarea class="form-control" id="ask-content" rows="6"></textarea>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" id="btn-ask">提交</button>
                </div>
            </div>
        </div>
    </div>
</div>
</@home>