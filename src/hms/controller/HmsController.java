package hms.controller;

import com.jfinal.core.Controller;
import hms.model.DutyModel;
import hms.model.HospModel;
import hms.model.MedModel;
import hms.model.TestModel;
import hms.service.DeptService;
import org.apache.log4j.Logger;

/**
 * Created by iamaprin
 * on 2016/4/28.
 */
public class HmsController extends Controller{

    private static final Logger log = Logger.getLogger(HmsController.class);

    public void index() {
        renderJson("success");
    }

    public void showDeptData() {
        renderJson(DeptService.getAll());
    }

    public void showMedCat0() {
        renderJson(MedModel.dao.getMedCat0());
    }

    public void showMedCat1() {
        int superId = getParaToInt("superId");
        renderJson(MedModel.dao.getMedCat1(superId));
    }

    public void addMed() {
        int superId = getParaToInt("superId", -1);
        String medName = getPara("medName", "");
        String medUse = getPara("medUse", "");
        renderJson(MedModel.dao.addMed(superId, medName, medUse));
    }

    public void addMedPage() {
        setAttr("userRole", "药品添加");
        render("/html/test/med.ftl");
    }

    public void addTest() {
        String testName = getPara("testName", "");
        if ("".equals(testName)) {
            renderJson(false);
            return;
        }

        renderJson(TestModel.dao.addTest(testName));
    }

    public void addTestPage() {
        setAttr("userRole", "药品添加");
        render("/html/test/test.ftl");
    }

    public void test() {
        renderJson(DutyModel.dao.getPharmacyDuty());
    }

    public void test2() {
        int drId = getParaToInt("drId", -1);
        String date = getPara("date", "");
        renderJson(DutyModel.dao.isExistForPha(drId, date));
    }
}
