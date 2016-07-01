package hms.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * author: iamaprin
 * time: 2016/5/7 7:49
 */
public class RegModel extends Model<RegModel>{
    public static final RegModel dao = new RegModel();

    public int count(int drId) {
        String sql = "SELECT COUNT(patient_id) AS total FROM hms_reg WHERE dr_id = ? AND id_received = 0";
        RegModel model = findFirst(sql, drId);
        return model.getInt("total");
    }

    /**
     * 添加挂号数据
     * @param patientId 病人id
     * @param deptId    科室id
     * @param drId      动态分配的医生id
     * @return 添加成功返回true，失败返回false
     */
    public boolean add(int patientId, int deptId, int drId) {
        return new RegModel().set("patient_id", patientId).set("dept_id", deptId).set("dr_id", drId).save();
    }

    public int getDrId(int patientId, int deptId) {
        String sql = "SELECT dr_id FROM hms_reg WHERE patient_id = ? AND dept_id = ? ORDER BY reg_time DESC LIMIT 1";
        RegModel model = findFirst(sql, patientId, deptId);
        if (model == null) {
            return -1;
        }
        return model.getInt("dr_id");
    }

    /**
     * 验证病人在挂号表是否存在
     * @param patientId 病人id
     * @return 存在返回true，否则返回false
     */
    public boolean isExist(int patientId) {
        String sql = "SELECT * FROM hms_reg WHERE patient_id = ? AND is_received = 0";
        RegModel model = findFirst(sql, patientId);
        return model != null;
    }

    /**
     * 获取挂号分配的号码，即表中的id
     * @param patientId 病人id
     * @return .
     */
    public int getAssignedNumber(int patientId) {
        String sql = "SELECT id FROM hms_reg WHERE patient_id = ? ORDER BY reg_time DESC LIMIT 1";
        RegModel model = findFirst(sql, patientId);
        if (model == null) {
            return -1;
        }
        return model.getInt("id");
    }

    public RegModel getPatient(int drId) {
        String sql = "SELECT id, patient_id FROM hms_reg WHERE dr_id = ? AND is_received = 0 " +
                "ORDER BY id ASC LIMIT 1";      //"ORDER BY reg_time ASC LIMIT 1"
        return findFirst(sql, drId);
    }

    public boolean updateStatus(int regId) {
        return new RegModel().set("id", regId)
                .set("is_received", 1).
                update();
    }

    /**
     * 根据注册id获取医生id
     * @param regId 注册id
     * @return .
     */
    public int getDrId(int regId) {
        RegModel model = findById(regId);
        if (model == null) {
            return -1;
        }
        return model.getInt("dr_id");
    }
}
