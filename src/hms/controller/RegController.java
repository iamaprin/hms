package hms.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import hms.model.*;
import hms.service.DeptService;
import hms.service.RegService;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 挂号页面逻辑处理
 * author: iamaprin
 * time: 2016/5/6 19:10
 */
public class RegController extends Controller{

    private static final Logger log = Logger.getLogger(RegController.class);

    public void index() {
        setAttr("userRole", "挂号");
        render("/html/register/register.ftl");
    }

    /**
     * 挂号操作
     */
    public void doRegister() {
        String name = getPara("name", "");
        String idNumber = getPara("idNumber", "");
        String sex = getPara("sex", "");
        int deptId = getParaToInt("deptId", -1);
        int patientId = -1;

        if ("".equals(name) || "".equals(idNumber) || "".equals(sex) || deptId == -1) {
            renderJson(false);
            return;
        }

        // 验证病人信息是否已存在
        if (PatModel.dao.isPatientExist(idNumber)) {
            log.info("patient: " + idNumber + " | msg: patient exists");
            patientId = PatModel.dao.getIdByIdNumber(idNumber);
        } else {
            log.info("patient: " + idNumber + " | msg: patient not exists");
            if (PatModel.dao.add(name, idNumber, sex)) {
                log.info("patient: " + idNumber + " | msg: add patient info successful");
                patientId = PatModel.dao.getIdByIdNumber(idNumber);
            } else {
                log.info("patient: " + idNumber + " | msg: fail to add patient info");
                renderJson(false);
                return;
            }
        }

        // 验证病人是否已经在挂号队列中，即id_received标记为0，表示病人已在队列中，无需重复添加；
        // id_received标记为1，表示病人曾经挂号过，但是已经接诊过；
        if (RegModel.dao.isExist(patientId)) {
            log.info("patient: " + idNumber + " | msg: is in queue");
            renderJson(false);
            return;
        }

        // 分配医生：如果在科室下已有挂号信息，则分配原医生。否则根据该科室下医生接诊队列大小动态分配。
        int drId;
        if (RegModel.dao.getDrId(patientId, deptId) != -1) {
            drId = RegModel.dao.getDrId(patientId, deptId);
        } else {
            drId = RegService.assignDoctor(deptId);
        }

        // 向挂号表hms_reg中添加信息
        if (!RegModel.dao.add(patientId, deptId, drId)) {
            log.info("patient: " + idNumber + " | msg: fail to add patient to hms_reg");
            renderJson(false);
        }

        // 分配的医生的接诊队列长度加一
        boolean flag = QueueModel.dao.plusOne(drId);
        //renderJson(flag);
        if (flag) {
            String drName = DrModel.dao.getNameById(drId);
            String deptName = DeptModel.dao.getDeptNameByid(deptId);
            int assignedNumber = RegModel.dao.getAssignedNumber(patientId);

            String data = new Record().set("pname", name).set("idNumber", idNumber)
                    .set("sex", sex).set("deptName", deptName).set("deptId", deptId)
                    .set("drId", drId).set("drName", drName).set("assignedNumber", assignedNumber)
                    .toJson();
            renderJson(data);
            return;
        }

        renderJson(false);
    }
    /*var jsonData = {
            "name": name,
            "idNumber": idNumber,
            "sex": sex,
            "deptId": deptId
        };*/

    /**
     * 获取科室数据<br>
     * 用于加载挂号界面科室下拉框数据
     */
    public void getDeptData() {
        List<String> strings = DeptService.getAll();
        log.debug("Dept data: " + strings);
        renderJson(strings);
    }
}
