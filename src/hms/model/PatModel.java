package hms.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * author: iamaprin
 * time: 2016/5/7 7:09
 */
public class PatModel extends Model<PatModel>{
    public static final PatModel dao = new PatModel();

    /**
     * 添加病人数据
     * @param name      病人姓名
     * @param idNumber  病人身份证号
     * @param sex       病人性别
     * @return 添加成功返回true，失败返回false
     */
    public boolean add(String name, String idNumber, String sex) {
        return new PatModel().set("patient_name", name)
                .set("patient_id_number", idNumber).set("patient_sex", sex).save();
    }

    /**
     * 检查病人数据是否已存在，身份证号唯一
     * @param idNumber  病人身份证号
     * @return 存在返回true，否则返回false
     */
    public boolean isPatientExist(String idNumber) {
        String sql = "SELECT id FROM hms_patient WHERE patient_id_number = ?";
        PatModel model = findFirst(sql, idNumber);
        return model != null;
    }

    /**
     * 根据身份证号获取病人id
     * @param idNumber  身份证号
     * @return 病人id
     */
    public int getIdByIdNumber(String idNumber) {
        String sql = "SELECT id FROM hms_patient WHERE patient_id_number = ?";
        PatModel model = findFirst(sql, idNumber);
        if (model != null) {
            return model.getInt("id");
        }
        return -1;
    }

    public PatModel getPatient(int patientId) {
        String sql = "SELECT * FROM hms_patient WHERE id = ?";
        return findFirst(sql, patientId);
    }

    public boolean addPwd(int patientId, String patientPwd, String salt) {
        return new PatModel().set("id", patientId)
                .set("patient_pwd", patientPwd)
                .set("patient_salt", salt)
                .update();
    }

    public PatModel getPatByIdNum(String patientIdNum) {
        String sql = "SELECT id, patient_pwd, patient_salt FROM hms_patient WHERE patient_id_number = ?";
        return findFirst(sql, patientIdNum);
    }

    public String getPatName(int patientId) {
        PatModel model = findById(patientId);
        if (model == null) {
            return "";
        }
        return model.getStr("patient_name");
    }
}
