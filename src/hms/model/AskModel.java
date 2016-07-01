package hms.model;

import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * author: iamaprin
 * time: 2016/5/12 20:48
 */
public class AskModel extends Model<AskModel>{

    public static final AskModel dao = new AskModel();

    public boolean addAsk(int caseId, int pid, int prole, int did, int drole, String msg) {
        return new AskModel().set("case_id", caseId)
                .set("patient_id", pid)
                .set("patient_role", prole)
                .set("doctor_id", did)
                .set("doctor_role", drole)
                .set("msg", msg)
                .save();
    }

    public List<AskModel> getMsgForDr(int drId) {
        String sql = "SELECT id, case_id, msg FROM hms_ask WHERE doctor_id = ? AND doctor_role = 1 AND is_read = 0";
        return find(sql, drId);
    }

    public List<AskModel> getMsgForPat(int patId) {
        String sql = "SELECT case_id, msg, reply FROM hms_ask WHERE patient_id = ? AND is_read = 1";
        return find(sql, patId);
    }

    public boolean addReply(int id, String reply) {
        return new AskModel().set("id", id).set("reply", reply).update();
    }

    public boolean updateStatus(int id) {
        return new AskModel().set("id", id).set("is_read", 1).update();
    }

    public List<AskModel> getAll() {
        String sql = "SELECT case_id, patient_id, doctor_id, is_read, time FROM hms_ask";
        return find(sql);
    }
}
