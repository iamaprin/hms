package hms.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import hms.model.*;
import hms.service.DeptService;
import hms.service.DrService;
import hms.util.SecurityUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iamaprin
 * on 2016/5/4.
 */
public class DrController extends Controller{

    private static final Logger log = Logger.getLogger(DrController.class);

    public void index() {

    }

    /**
     * 渲染登陆界面
     */
    public void login() {
        String ip = getRequest().getRemoteAddr();
        log.info("msg | ip:" + ip + " access /doctor/login");
        setAttr("userRole", "医生");
        render("/html/doctor/login.ftl");
    }

    /**
     * 渲染注册界面
     */
    public void register() {
        String ip = getRequest().getRemoteAddr();
        log.info("msg | ip:" + ip + " access /doctor/register");
        setAttr("userRole", "医生");
        render("/html/doctor/register.ftl");
    }

    /**
     * 渲染管理界面
     */
    public void manage() {
        String ip = getRequest().getRemoteAddr();
        log.info("msg | ip:" + ip + " access /doctor/manage");
        String cookieF = getCookie("drIsLogined", "false");
        String cookieU = getCookie("drId", "");
        if ("false".equals(cookieF)) {
            renderNull();
            return;
        }

        setAttr("userRole", "医生");
        setAttr("drId", cookieU);
        render("/html/doctor/manage.ftl");
    }

    /**
     * 渲染药房界面
     */
    public void dispense() {
        String cookieF = getCookie("drIsLogined", "false");
        String cookieU = getCookie("drId", "");
        if ("false".equals(cookieF)) {
            renderNull();
            return;
        }

        setAttr("userRole", "药房");
        setAttr("drId", cookieU);
        render("/html/doctor/dispense.ftl");
    }

    /**
     * 渲染检验界面
     */
    public void test() {
        String cookieF = getCookie("drIsLogined", "false");
        String cookieU = getCookie("drId", "");
        if ("false".equals(cookieF)) {
            renderNull();
            return;
        }

        setAttr("userRole", "检验");
        setAttr("drId", cookieU);
        render("/html/doctor/test.ftl");
    }


    /**
     * 执行登陆操作
     */
    public void doLogin() {
        String username = getPara("username", "");
        String password = getPara("password", "");

        if ("".equals(username) || "".equals(password)) {
            renderJson(false);
            return;
        }

        boolean flag = DrService.verifyPassword(username, password);
        if (flag) {
            setCookie("drId", String.valueOf(DrModel.dao.getIdByUsername(username)), 60 * 60);
            setCookie("drIsLogined", String.valueOf(true), 60 * 60);
        }
        renderJson(flag);
    }

    /**
     * 执行注册操作
     */
    public void doRegister() {
        String username = getPara("username", "");
        String password = getPara("password", "");
        String drName = getPara("drName", "");
        String sex = getPara("sex", "");
        int deptId = getParaToInt("deptId", -1);

        if ("".equals(username) || "".equals(password) || "".equals(drName) || "".equals(sex) || deptId == -1) {
            renderJson(false);
            return;
        }

        boolean flag =  DrService.addData(drName, sex, deptId, username, password);
        if (!flag) {
            renderJson(flag);
            return;
        }

        int drId = DrModel.dao.getIdByUsername(username);
        renderJson(QueueModel.dao.add(deptId, drId));
    }

    public void getDeptId() {
        String drId = getCookie("drId", "");
        if ("".equals(drId)) {
            renderJson("-1");
            return;
        }
        renderJson(DrModel.dao.getDeptId(Integer.valueOf(drId)));
    }

    /**
     * 获取科室数据<br>
     * 用于加载注册界面科室下拉框数据
     */
    public void getDeptData() {
        List<String> strings = DeptService.getAll();
        log.debug("Dept data: " + strings);
        renderJson(strings);
    }

    public void showMedCat0() {
        renderJson(MedModel.dao.getMedCat0());
    }

    public void showMedCat1() {
        int cat0Id = getParaToInt("cat0Id");
        renderJson(MedModel.dao.getMedCat1(cat0Id));
    }

    public void showTest() {
        renderJson(TestModel.dao.getTest());
    }

    public void showMed() {
        int cat1Id = getParaToInt("cat1Id");
        renderJson(MedModel.dao.getMed(cat1Id));
    }

    public void getPageInfo() {
        int drId = getParaToInt("drId", -1);
        if (drId == -1) {
            renderNull();
            return;
        }
        String drName = DrModel.dao.getNameById(drId);
        int queue = QueueModel.dao.getQueueByDrId(drId);
        renderJson(new Record().set("drName", drName).set("queue", queue).toJson());
    }

    public void getPatient() {
        int drId = getParaToInt("drId", -1);
        if (drId == -1) {
            renderNull();
            return;
        }
        RegModel regModel = RegModel.dao.getPatient(drId);
        if (regModel == null) {
            renderJson(false);
            return;
        }

        int regId = regModel.getInt("id");
        int patientId = regModel.getInt("patient_id");
        PatModel patModel = PatModel.dao.getPatient(patientId);
        String patientName = patModel.getStr("patient_name");
        String idNUmber = patModel.getStr("patient_id_number");
        String patientSex = patModel.getStr("patient_sex");
        Record record = new Record().set("regId", regId).set("patientId", patientId)
                .set("patientName", patientName).set("idNumber", idNUmber)
                .set("patientId", patientId).set("patientSex", patientSex);
        System.out.println(record.toJson());
        renderJson(record.toJson());
    }

    /**
     * var jsonData = {
     "patientId": patientId,
     "regId": regId,
     "diagnosis": diagnosis,
     "medicine": medId,
     "test": testId
     };
     */

    public void addCase() {
        int patientId = getParaToInt("patientId", -1);
        int regId = getParaToInt("regId", -1);
        String diagnosis = getPara("diagnosis", "");
        String medicine = getPara("medicine", "");
        String test = getPara("test", "");

        if (patientId == -1 || regId == -1) {
            renderJson(false);
            return;
        }

        boolean flag = CaseModel.dao.addCase(patientId, regId, diagnosis, medicine, test);
        renderJson(flag);
    }

    public void updateStatus() {
        int regId = getParaToInt("regId", -1);
        if (regId == -1) {
            renderJson(false);
            return;
        }

        boolean flag = RegModel.dao.updateStatus(regId);
        renderJson(flag);
    }

    public void minusOne() {
        int drId = getParaToInt("drId", -1);
        if (drId == -1) {
            renderJson(false);
            return;
        }
        renderJson(QueueModel.dao.minusOne(drId));
    }

    public void doRegisterPat() {
        int patientId = getParaToInt("patientId", -1);
        String patientPwd = getPara("patientPwd", "");
        if ("".equals(patientPwd) || patientId == -1) {
            renderJson(false);
            return;
        }

        String salt = SecurityUtil.generateSalt(32);
        String encodePwd = SecurityUtil.SHA256Encode(patientPwd, salt);
        boolean flag = PatModel.dao.addPwd(patientId, encodePwd, salt);
        renderJson(flag);
    }

    public void isOnDuty() {
        int drId = getParaToInt("drId", -1);
        String date = getPara("today", "");
        if ("".equals(date) || drId == -1) {
            renderJson(false);
            return;
        }
        renderJson(DutyModel.dao.isExistForPha(drId, date));
    }

    public void isOnDutyForTest() {
        int drId = getParaToInt("drId", -1);
        String date = getPara("today", "");
        if ("".equals(date) || drId == -1) {
            renderJson(false);
            return;
        }
        renderJson(DutyModel.dao.isExistForTest(drId, date));
    }

    public void getDrName() {
        int drId = getParaToInt("drId", -1);
        if (drId == -1) {
            renderJson("");
            return;
        }
        String drName = DrModel.dao.getNameById(drId);
        renderJson(new Record().set("drName", drName).toJson());
    }

    public void loadCaseListForPha() {
        String idNumber = getPara("idNumber", "");
        if ("".equals(idNumber)) {
            renderJson(false);
            return;
        }

        int patientId = PatModel.dao.getIdByIdNumber(idNumber);
        if (patientId == -1) {
            renderJson(false);
            return;
        }
        String patientName = PatModel.dao.getPatName(patientId);
        if ("".equals(patientName)) {
            renderJson(false);
            return;
        }

        List<CaseModel> models = CaseModel.dao._getCaseForPha(patientId);
        List<String> result = new ArrayList<>();
        if (models == null) {
            renderJson(false);
            return;
        }
        for (CaseModel model : models) {
            int caseId = model.getInt("id");
            String _medicine = model.getStr("medicine");
            if ("".equals(_medicine)) {
                continue;
            }
            String[] medicineIds = _medicine.split("-");
            String tmp = "";
            for (String medicineId : medicineIds) {
                tmp += MedModel.dao.getMedName(Integer.valueOf(medicineId)) + "  ";
            }
            result.add(new Record().set("patientName", patientName).set("caseId", caseId).set("medicine", tmp).toJson());
        }
        renderJson(result);
    }

    public void loadCaseListForTest() {
        String idNumber = getPara("idNumber", "");
        if ("".equals(idNumber)) {
            renderJson(false);
            return;
        }

        int patientId = PatModel.dao.getIdByIdNumber(idNumber);
        if (patientId == -1) {
            renderJson(false);
            return;
        }
        String patientName = PatModel.dao.getPatName(patientId);
        if ("".equals(patientName)) {
            renderJson(false);
            return;
        }

        List<CaseModel> models = CaseModel.dao._getCaseForTest(patientId);
        List<String> result = new ArrayList<>();
        if (models == null) {
            renderJson(false);
            return;
        }
        for (CaseModel model : models) {
            int caseId = model.getInt("id");
            String _test = model.getStr("test");
            if ("".equals(_test)) {
                continue;
            }
            String[] testIds = _test.split("-");
            String tmp = "";
            for (String testId : testIds) {
                tmp += TestModel.dao.getTestName(Integer.valueOf(testId)) + "  ";
            }
            result.add(new Record().set("patientName", patientName).set("caseId", caseId).set("test", tmp).toJson());
        }
        renderJson(result);
    }

    public void addToQueueForPha() {
        int caseId = getParaToInt("caseId", -1);
        String medicine = getPara("medicine", "");
        if (caseId == -1 || "".equals(medicine)) {
            renderJson(false);
            return;
        }
        boolean flag = QueueModel.dao.addCaseForPha(caseId, medicine);
        renderJson(flag);
    }

    public void addToQueueForTest() {
        int caseId = getParaToInt("caseId", -1);
        String test = getPara("test", "");
        if (caseId == -1 || "".equals(test)) {
            renderJson(false);
            return;
        }
        boolean flag = QueueModel.dao.addCaseForTest(caseId, test);
        renderJson(flag);
    }

    public void loadQueueForPha() {
        int pageNumber = getParaToInt("pageNumber", -1);
        int pageSize = getParaToInt("pageSize", -1);
        if (pageNumber == -1 || pageSize == -1) {
            renderJson(false);
            return;
        }

        renderJson(QueueModel.dao.getCaseForPha(pageNumber, pageSize));
    }

    public void loadQueueForTest() {
        int pageNumber = getParaToInt("pageNumber", -1);
        int pageSize = getParaToInt("pageSize", -1);
        if (pageNumber == -1 || pageSize == -1) {
            renderJson(false);
            return;
        }

        renderJson(QueueModel.dao.getCaseForTest(pageNumber, pageSize));
    }

    public void updateStatusForPha() {
        int id = getParaToInt("id", -1);
        if (id == -1) {
            renderJson(false);
            return;
        }
        renderJson(QueueModel.dao.updateStatusForPha(id));
    }

    public void updateStatusForTest() {
        int id = getParaToInt("id", -1);
        if (id == -1) {
            renderJson(false);
            return;
        }
        renderJson(QueueModel.dao.updateStatusForTest(id));
    }

    public void updateStatusForTest2() {
        int id = getParaToInt("id", -1);
        if (id == -1) {
            renderJson(false);
            return;
        }
        renderJson(QueueModel.dao.updateStatusForTest2(id));
    }

    public void updateMedStatus() {
        int caseId = getParaToInt("caseId", -1);
        if (caseId == -1) {
            renderJson(false);
            return;
        }
        boolean flag = CaseModel.dao.updateMedStatus(caseId);
        renderJson(flag);
    }

    public void updateTestStatus() {
        int caseId = getParaToInt("caseId", -1);
        if (caseId == -1) {
            renderJson(false);
            return;
        }
        boolean flag = CaseModel.dao.updateTestStatus(caseId);
        renderJson(flag);
    }

    public void addTestResult() {
        int caseId = getParaToInt("caseId", -1);
        String testResult = getPara("testResult", "");
        if (caseId == -1 || "".equals(testResult)) {
            renderJson(false);
            return;
        }
        boolean flag = TestModel.dao.addTestResult(caseId, testResult);
        renderJson(flag);
    }

    public void loadCaseInfo() {
        int caseId = getParaToInt("caseId", -1);
        if (caseId == -1) {
            renderJson(false);
            return;
        }

        CaseModel model = CaseModel.dao.getCaseInfo(caseId);
        if (model == null) {
            renderJson(false);
            return;
        }
    }

    public void signOut() {
        setCookie("drId", "", 0);
        setCookie("drIsLogined", "false", 0);
        renderJson(true);
    }

    public void loadAskList() {
        int drId = getParaToInt("drId", -1);
        if (drId == -1) {
            renderJson(false);
            return;
        }

        List<AskModel> models = AskModel.dao.getMsgForDr(drId);
        List<String> strings = new ArrayList<>();
        if (models == null || models.size() == 0) {
            renderJson(false);
            return;
        }

        for (AskModel model : models) {
            strings.add(model.toJson());
        }
        renderJson(strings);
    }

    public void addReply() {
        int id = getParaToInt("id", -1);
        String replyMsg = getPara("replyMsg", "");
        if (id == -1 || "".equals(replyMsg)) {
            renderJson(false);
            return;
        }
        boolean flag = AskModel.dao.addReply(id, replyMsg);
        renderJson(flag);
    }

    public void updateAskStatus() {
        int id = getParaToInt("id", -1);
        renderJson(AskModel.dao.updateStatus(id));
    }

    public void updatePhaStatus() {
        int id = getParaToInt("id", -1);
        renderJson(QueueModel.dao.updateStatusForPha(id));
    }

    public void updateContent() {
        int id = getParaToInt("id", -1);
        String content = getPara("content", "");
        renderJson(CaseModel.dao.updateCaseContent(id, content));
    }


}
