package hms.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import hms.model.*;
import hms.util.SecurityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 病人登陆、操作
 * author: iamaprin
 * time: 2016/5/11 17:10
 */
public class PatController extends Controller{

    public void index() {
        renderJson("success");
    }

    /**
     * 渲染登陆界面
     */
    public void login() {
        setAttr("userRole", "病人");
        render("/html/patient/login.ftl");
    }

    /**
     * 渲染管理界面
     */
    public void manage() {
        String cookieF = getCookie("patIsLogined", "false");
        String cookieU = getCookie("patientId", "");
        if ("false".equals(cookieF)) {
            renderNull();
            return;
        }

        setAttr("patientId", cookieU);
        setAttr("userRole", "病人");
        render("/html/patient/manage.ftl");
    }

    /**
     * 执行登陆操作
     */
    public void doLogin() {
        String patientUn = getPara("username", "");
        String patientPwd = getPara("password", "");

        PatModel model = PatModel.dao.getPatByIdNum(patientUn);
        if ("".equals(patientUn) || "".equals(patientPwd) || model == null) {
            renderJson(false);
            return;
        }

        String encodePwd = model.getStr("patient_pwd");
        String salt = model.getStr("patient_salt");
        int patientId = model.getInt("id");
        boolean flag = false;
        if (SecurityUtil.SHA256Encode(patientPwd, salt).equals(encodePwd)) {
            flag = true;
            setCookie("patIsLogined", "true", 60 * 60);
            setCookie("patientId", String.valueOf(patientId), 60 * 60);

        }
        renderJson(flag);
    }

    public void loadCaseList() {
        int patientId = getParaToInt("patientId", -1);
        if (patientId == -1) {
            renderJson("");
            return;
        }
        renderJson(CaseModel.dao.getCase(patientId));
    }

    public void getDiagnosis() {
        int caseId = getParaToInt("caseId", -1);
        if (caseId == -1) {
            renderJson("");
            return;
        }
        String diagnosis = CaseModel.dao.getDiagnosis(caseId);
        renderJson(new Record().set("diagnosis", diagnosis).toJson());
    }

    public void getMed() {
        int caseId = getParaToInt("caseId", -1);
        if (caseId == -1) {
            renderJson("");
            return;
        }

        CaseModel model = CaseModel.dao._getMedInfo(caseId);
        String _medIds = model.getStr("medicine");
        if ("".equals(_medIds)) {
            renderJson(new Record().set("med", "无").set("medStatus", "false").toJson());
            return;
        }

        boolean medStatus = model.getBoolean("medicine_status");
        System.out.println(medStatus);
        String[] medIds = _medIds.split("-");
        String medName = "";
        for (String medId : medIds) {
            medName += MedModel.dao.getMedName(Integer.valueOf(medId)) + ".";
        }
        renderJson(new Record().set("med", medName).set("medStatus", String.valueOf(medStatus)).toJson());
    }

    public void getTest() {
        int caseId = getParaToInt("caseId", -1);
        if (caseId == -1) {
            renderJson("");
            return;
        }

        CaseModel model = CaseModel.dao._getTestInfo(caseId);
        String _testIds = model.get("test");
        if ("".equals(_testIds)) {
            renderJson(new Record().set("test", "无检验").set("testResult", "无结果").toJson());
            return;
        }

        String[] testIds = _testIds.split("-");
        String testName = "";
        for (String testId : testIds) {
            testName += TestModel.dao.getTestName(Integer.valueOf(testId)) + ".";
        }

        boolean testStatus = model.getBoolean("test_status");
        String testResult = null;
        if (testStatus) {
            testResult = TestModel.dao.getTestResult(caseId);
        } else {
            testResult = "检验结果未出";
        }
        renderJson(new Record().set("test", testName).set("testResult", testResult).toJson());
    }

    public void getDrId() {
        int caseId = getParaToInt("caseId", -1);
        if (caseId == -1) {
            renderJson(-1);
            return;
        }
        int regId = CaseModel.dao.getRegId(caseId);
        if (regId == -1) {
            renderJson(-1);
            return;
        }

        int drId = RegModel.dao.getDrId(regId);
        renderJson(new Record().set("drId", drId).toJson());
    }

    /*
    var jsonData = {
            "caseId": caseId,
            "drId": drId,
            "patientId": patientId,
            "askContent": askContent
        };
     */
    public void doAsk() {
        int caseId = getParaToInt("caseId", -1);
        int drId = getParaToInt("drId", -1);
        int patientId = getParaToInt("patientId", -1);
        String msg = getPara("askContent", "");

        if (caseId == -1 || drId == -1 || patientId == -1 || "".equals(msg)) {
            renderJson(false);
            return;
        }
        boolean flag = AskModel.dao.addAsk(caseId, patientId, 0, drId, 1, msg);
        renderJson(flag);
    }

    public void signOut() {
        setCookie("patientId", "", 0);
        setCookie("patIsLogined", "false", 0);
        renderJson(true);
    }

    public void loadReplyList() {
        //int patientId = getParaToInt("patientId", -1);
        int patientId = getCookieToInt("patientId");
        List<AskModel> models = AskModel.dao.getMsgForPat(patientId);
        if (models == null || models.size() == 0) {
            renderJson(false);
            return;
        }
        List<String> strings = new ArrayList<>();
        for (AskModel model : models) {
            strings.add(model.toJson());
        }
        renderJson(strings);
    }
}
