package hms.model;

import com.jfinal.plugin.activerecord.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * 病历操作类
 * author: iamaprin
 * time: 2016/5/10 22:41
 */
public class CaseModel extends Model<CaseModel>{

    public static final CaseModel dao = new CaseModel();

    public boolean addCase(int patientId, int regId, String diagnosis, String medicine, String test) {
        return new CaseModel().set("patient_id", patientId)
                .set("reg_id", regId)
                .set("diagnosis", diagnosis)
                .set("medicine", medicine)
                .set("test", test)
                .save();
    }


    public List<CaseModel> _getCase(int patientId) {
        String sql = "SELECT * FROM hms_case_history WHERE patient_id = ?";
        return find(sql, patientId);
    }

    public List<String> getCase(int patientId) {
        List<CaseModel> models = _getCase(patientId);
        List<String> strings = new ArrayList<>();
        if (models == null) {
            strings.add("");
        } else {
            for (CaseModel model : models) {
                strings.add(model.toJson());
            }
        }
        return strings;
    }

    /**
     * 根据病历id获取诊断结果
     * @param caseId 病历id
     * @return .
     */
    public String getDiagnosis(int caseId) {
        CaseModel model = findById(caseId);
        if (model == null) {
            return "";
        }
        return model.getStr("diagnosis");
    }

    public CaseModel _getMedInfo(int caseId) {
        String sql = "SELECT medicine, medicine_status FROM hms_case_history WHERE id = ?";
        return findFirst(sql, caseId);
    }

    public String getMedInfo(int caseId) {
        CaseModel model = _getMedInfo(caseId);
        if (model == null) {
            return "";
        }
        return model.toJson();
    }

    public CaseModel _getTestInfo(int caseId) {
        String sql = "SELECT test, test_status FROM hms_case_history WHERE id = ?";
        return findFirst(sql, caseId);
    }

    public String getTestInfo(int caseId) {
        CaseModel model = _getTestInfo(caseId);
        if (model == null) {
            return "";
        }
        return model.toJson();
    }

    /**
     * 根据病例id获取挂号id
     * @param caseId 病历id
     * @return .
     */
    public int getRegId(int caseId) {
        String sql = "SELECT reg_id FROM hms_case_history WHERE id = ?";
        CaseModel model = findFirst(sql, caseId);
        if (model == null) {
            return -1;
        }
        return model.getInt("reg_id");
    }


    /**
     * 获取该病人所有未配药的病历
     * @param patientId 病人id
     * @return .
     */
    public List<CaseModel> _getCaseForPha(int patientId) {
        String sql = "SELECT id, medicine FROM hms_case_history WHERE patient_id = ? AND medicine_status = 0";
        return find(sql, patientId);
    }

    /**
     * 获取该病人所有未配药的病历
     * @param patientId 病人id
     * @return .
     */
    public List<String> getCaseForPha(int patientId) {
        List<CaseModel> models = _getCaseForPha(patientId);
        List<String> strings = new ArrayList<>();
        if (models==null) {
            strings.add("");
            return strings;
        }

        for (CaseModel model : models) {
            strings.add(model.toJson());
        }
        return strings;
    }

    /**
     * 获取该病人所有未作检验的病历
     * @param patientId 病人id
     * @return .
     */
    public List<CaseModel> _getCaseForTest(int patientId) {
        String sql = "SELECT id, test FROM hms_case_history WHERE patient_id = ? AND test_status = 0";
        return find(sql, patientId);
    }

    /**
     * 获取该病人所有未作检验的病历
     * @param patientId 病人id
     * @return .
     */
    public List<String> getCaseForTest(int patientId) {
        List<CaseModel> models = _getCaseForTest(patientId);
        List<String> strings = new ArrayList<>();
        if (models==null) {
            strings.add("");
            return strings;
        }

        for (CaseModel model : models) {
            strings.add(model.toJson());
        }
        return strings;
    }


    /**
     * 更新病历中的配药状态为已配药
     * @param caseId    病历id
     * @return .
     */
    public boolean updateMedStatus(int caseId) {
        return new CaseModel().set("id", caseId).set("medicine_status", 1).update();
    }

    /**
     * 更新病历中的检验状态为已检验
     * @param caseId    病历id
     * @return .
     */
    public boolean updateTestStatus(int caseId) {
        return new CaseModel().set("id", caseId).set("test_status", 1).update();
    }


    public CaseModel getCaseInfo(int caseId) {
        return findById(caseId);
    }

    public boolean updateCaseContent(int caseId, String content) {
        return new CaseModel().set("id", caseId).set("diagnosis", content).update();
    }
}
