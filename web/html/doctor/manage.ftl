<#include "/html/template/home.ftl">
<@home>
    <#include "/html/template/header.ftl">
    <@header>
    <#--<li class="cursor-pointer"><a id="modify-password">修改密码</a></li>-->
    <li class="cursor-pointer"><a id="sign-out">退出登录</a></li>
    </@header>
<link rel="stylesheet" type="text/css" href="${contextPath}/style/doctor.css">
<script src="${contextPath}/script/doctor/manage.js"></script>
<div class="content">
    <div class="left">
        <div class="list-group">
            <div class="btn-group" role="group" aria-label="ds">
                <button type="button" class="btn btn-default" id="receive-pat">接诊病人</button>
                <button type="button" class="btn btn-default" id="finish-receive">完成接诊</button>
            </div><br>
            <button class="btn btn-default" id="show-case-list">查看历史病历</button><br>
            <button class="btn btn-default" type="button" data-toggle="modal" data-target="#register-pat" id="reg-this-pat">注册该病人</button><br>
            <button class="btn btn-default" id="show-ask-list">查看询问</button>
        </div>
    </div>
    <#--主页面-->
    <div class="right" <#--style="display: none"--> id="right-1">
        <div class="alert alert-info col-md-6 col-md-offset-1" role="alert">
            <input id="dr-id" value="${drId}" hidden="hidden" title="drId">
            <span><strong id="dr-name"></strong>医生，你好！</span>
            <span>当前队列人数：<strong class="head-text1" id="current-num"></strong></span>
        </div>
        <div class="col-md-9 col-md-offset-1 right-padding">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <td width="20%">姓名</td>
                    <td width="30%">
                        <input class="form-control" readonly="readonly" id="patient-name">
                    </td>
                    <td width="15%">性别</td>
                    <td width="15%">
                        <input class="form-control" readonly="readonly" id="patient-sex">
                    </td>
                    <td width="20%"></td>
                </tr>
                <tr>
                    <td>身份证号</td>
                    <td colspan="4">
                        <input class="form-control" readonly="readonly" id="patient-number">
                    </td>
                </tr>
                <tr>
                    <td>诊断</td>
                    <td colspan="4">
                        <textarea class="form-control" id="diagnosis" rows="7"></textarea>
                    </td>
                </tr>
                <tr>
                    <td>配药</td>
                    <td colspan="3" id="med-selected">

                    </td>
                    <td>
                        <button class="btn btn-info" type="button" data-toggle="modal" data-target="#add-med">添加</button>
                        <button class="btn btn-danger">移除</button>
                    </td>
                </tr>
                <tr>
                    <td>检验</td>
                    <td colspan="3" id="test-selected">

                    </td>
                    <td>
                        <button class="btn btn-info" type="button" data-toggle="modal" data-target="#add-test">添加</button>
                        <button class="btn btn-danger">移除</button>
                    </td>
                </tr>
                </thead>
            </table>
        </div>
    </div>

    <div class="right hidden" id="right-2">
        <div class="right-2-padding">
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
    </div>

    <div class="right hidden" id="right-3">
        <div class="right-2-padding">
            <table class="table left-table">
                <caption>询问列表</caption>
                <thead>
                <tr>
                    <th width="10%">病历id</th>
                    <th width="80%">询问内容</th>
                    <th width="10%">操作</th>
                </tr>
                </thead>
                <tbody class="table-striped" id="ask-list">
                <tr>
                    <td>2</td>
                    <td>dassssssssss</td>
                    <td>
                        <button class='btn btn-xs btn-primary' type='button'>回复</button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>


    <#--查看病例对话框-->
    <div class="right2" style="display: none">

    </div>
    <#--添加药品对话框-->
    <div class="modal" tabindex="-1" role="dialog" id="add-med">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">选择药物</h4>
                </div>
                <div class="modal-body">
                    <select class="dlg-med-select" id="med-cat-0" title="根类别"></select>
                    <select class="dlg-med-select" id="med-cat-1" title="子类别"></select>
                    <select class="dlg-med-select" id="med" title="药物"></select>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" id="btn-add-med">添加</button>
                </div>
            </div>
        </div>
    </div>
    <#--添加检验对话框-->
    <div class="modal" tabindex="-1" role="dialog" id="add-test">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">选择检验</h4>
                </div>
                <div class="modal-body">
                    <select class="dlg-test-select" id="test" title="检验类别"></select>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" id="btn-add-test">添加</button>
                </div>
            </div>
        </div>
    </div>
    <#--病人注册对话框-->
    <div class="modal" tabindex="-1" role="dialog" id="register-pat">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">病人注册</h4>
                </div>
                <div class="modal-body">
                    <form onsubmit="return false">
                        <div class="form-group">
                            <label for="reg-patient-name">病人姓名</label>
                            <input type="text" class="form-control" id="reg-patient-name" readonly="readonly">
                        </div>
                        <div class="form-group">
                            <label for="reg-patient-user">账号</label>
                            <input type="text" class="form-control" id="reg-patient-user" readonly="readonly">
                        </div>
                        <div class="form-group">
                            <label for="reg-patient-pass">密码</label>
                            <input type="password" class="form-control" id="reg-patient-pass" placeholder="密码">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" id="btn-reg">注册</button>
                </div>
            </div>
        </div>
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
                    <textarea class="form-control" id="diagnosis-2" rows="6" readonly></textarea>
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
                    <textarea class="form-control" id="test-2" rows="3" readonly></textarea><br>
                    <label for="medicine-status">检验结果：</label>
                    <textarea class="form-control" id="test-result" rows="3" readonly></textarea><br>
                </div>
            </div>
        </div>
    </div>

    <#--修改病历对话框-->
    <div class="modal" tabindex="-1" role="dialog" id="modify-dlg">
        <div class="modal-dialog modal-md">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">修改病历</h4>
                </div>
                <div class="modal-body">
                    <table class="table table-bordered">
                        <tr>
                            <td>诊断</td>
                            <td colspan="4">
                                <textarea class="form-control" id="modify-content" rows="7"></textarea>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" id="btn-case-modify">修改</button>
                </div>
            </div>
        </div>
    </div>

    <#--诊断对话框-->
    <div class="modal" tabindex="-1" role="dialog" id="ask-dlg">
        <div class="modal-dialog modal-md">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title">回复询问</h5>
                </div>
                <div class="modal-body">
                    <textarea class="form-control" id="reply" rows="6"></textarea>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" id="btn-reply">回复</button>
                </div>
            </div>
        </div>
    </div>
</div>
</@home>