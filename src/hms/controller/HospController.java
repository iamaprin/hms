package hms.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import hms.model.*;
import hms.service.HospService;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iamaprin
 * on 2016/5/2.
 */
public class HospController extends Controller {

    private static final Logger log = Logger.getLogger(HospController.class);

    /**
     * 首页
     */
    public void index() {
        setAttr("userRole", "医院");
        render("/html/hospital/login.ftl");
    }

    /**
     * 登陆
     */
    public void login() {
        String username = getPara("username", "");
        String password = getPara("password", "");

        if ("".equals(username) || "".equals(password)) {
            renderJson(false);
            return;
        }

        boolean flag = HospService.validate(username, password);
        if (flag) {
            setCookie("hospIsLogin", "true", 60 * 60);
        }
        renderJson(flag);
    }

    /**
     * 管理界面
     */
    public void manage() {

        String hospIsLogin = getCookie("hospIsLogin", "false");
        if ("false".equals(hospIsLogin)) {
            log.info("Unauthorized user");
            renderText("未登录");
            return;
        }

        setAttr("userRole", "医院");
        render("/html/hospital/manage.ftl");
    }

    public void loadPharmacyList() {
        renderJson(DutyModel.dao.getPharmacyDuty());
    }

    public void loadPharmacyDr() {
        renderJson(DrModel.dao.getPharmacyDr());
    }

    public void isDateExistForPha() {
        String date = getPara("date", "");
        if ("".equals(date)) {
            renderJson(false);
            return;
        }
        renderJson(DutyModel.dao.isDateExistForPha(date));
    }

    public void updatePharmacyDuty() {
        String date = getPara("date", "");
        int dutyId = getParaToInt("dutyId", -1);
        int drId = getParaToInt("drId", -1);
        if (dutyId == -1 || drId == -1) {
            renderJson(false);
            return;
        }
        boolean flag = DutyModel.dao.uploadPharmacyDuty(dutyId, drId, date);
        renderJson(flag);
    }

    public void addPharmacyDuty() {
        String date = getPara("date", "");
        int drId = getParaToInt("drId", -1);
        if ("".equals(date) || drId == -1) {
            renderJson(false);
            return;
        }
        boolean flag = DutyModel.dao.addPharmacyDuty(drId, date);
        renderJson(flag);
    }

    /*------------------------------------------------------------------*/
    public void loadTestList() {
        renderJson(DutyModel.dao.getTestDuty());
    }

    public void loadTestDr() {
        renderJson(DrModel.dao.getTestDr());
    }

    public void isDateExistForTest() {
        String date = getPara("date", "");
        if ("".equals(date)) {
            renderJson(false);
            return;
        }
        renderJson(DutyModel.dao.isDateExistForTest(date));
    }

    public void updateTestDuty() {
        String date = getPara("date", "");
        int dutyId = getParaToInt("dutyId", -1);
        int drId = getParaToInt("drId", -1);
        if (dutyId == -1 || drId == -1) {
            renderJson(false);
            return;
        }
        boolean flag = DutyModel.dao.updateTestDuty(dutyId, drId, date);
        renderJson(flag);
    }

    public void addTestDuty() {
        String date = getPara("date", "");
        int drId = getParaToInt("drId", -1);
        if ("".equals(date) || drId == -1) {
            renderJson(false);
            return;
        }
        boolean flag = DutyModel.dao.addTestDuty(drId, date);
        renderJson(flag);
    }

    /*------------------------------------------------------------------*/
    public void loadRegList() {
        renderJson(DutyModel.dao.getRegDuty());
    }

    public void loadRegDr() {
        renderJson(DrModel.dao.getRegDr());
    }

    public void isDateExistForReg() {
        String date = getPara("date", "");
        if ("".equals(date)) {
            renderJson(false);
            return;
        }
        renderJson(DutyModel.dao.isDateExistForReg(date));
    }

    public void updateRegDuty() {
        String date = getPara("date", "");
        int dutyId = getParaToInt("dutyId", -1);
        int drId = getParaToInt("drId", -1);
        if (dutyId == -1 || drId == -1) {
            renderJson(false);
            return;
        }
        boolean flag = DutyModel.dao.updateRegDuty(dutyId, drId, date);
        renderJson(flag);
    }

    public void addRegDuty() {
        String date = getPara("date", "");
        int drId = getParaToInt("drId", -1);
        if ("".equals(date) || drId == -1) {
            renderJson(false);
            return;
        }
        boolean flag = DutyModel.dao.addRegDuty(drId, date);
        renderJson(flag);
    }

    /*---------------------------------------------------------------------------------*/

    public void loadDrList() {
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 8);
        renderJson(DrModel.dao.getDr(pageNumber, pageSize));
    }


    public void loadDeptList() {
        List<DeptModel> models = DeptModel.dao.getAll();
        List<String> strings = new ArrayList<>();
        if (models == null) {
            strings.add("");
        } else {
            for (DeptModel model : models) {
                strings.add(model.toJson());
            }
        }
        renderJson(strings);
    }

    public void updateDept() {
        int drId = getParaToInt("drId", -1);
        int deptId = getParaToInt("deptId", -1);
        if (drId == -1 || deptId == -1) {
            renderJson(false);
            return;
        }

        boolean flag = DrModel.dao.updateDrDept(drId, deptId);
        renderJson(flag);
    }

    /*---------------------------------------------------------------------------------*/

    public void queryNameByDrId() {
        int drId = getParaToInt("drId", -1);
        if (drId == -1) {
            renderJson("");
            return;
        }

        String drName = DrModel.dao.getNameById(drId);
        renderJson(new Record().set("drName", drName).toJson());
    }

    public void signOut() {
        setCookie("hospIsLogin", "false", 0);
        renderJson(true);
    }

    public void loadReplyList() {
        List<AskModel> models = AskModel.dao.getAll();
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
