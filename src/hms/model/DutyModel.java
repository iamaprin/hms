package hms.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 值班管理数据操作类
 * author: iamaprin
 * time: 2016/5/13 19:43
 */
public class DutyModel extends Model<DutyModel>{

    public static final DutyModel dao = new DutyModel();

    /**
     *
     * @return
     */
    public List<Record> _getPharmacyDuty() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now = sdf.format(new Date());
        //System.out.println(now);
        String sql = "SELECT id, dr_id, DATE(duty_time) AS duty_time FROM hms_pharmacy_duty WHERE DATE(duty_time) >= ?";
        return Db.find(sql, now);
    }

    /**
     *
     * @return
     */
    public List<String> getPharmacyDuty() {
        List<Record> records = _getPharmacyDuty();
        List<String> strings = new ArrayList<>();
        if (records == null) {
            strings.add("");
            return strings;
        }

        for (Record record : records) {
            strings.add(record.toJson());
        }
        return strings;
    }

    public boolean isDateExistForPha(String date) {
        String sql = "SELECT id FROM hms_pharmacy_duty WHERE DATE(duty_time) = ?";
        Record record = Db.findFirst(sql, date);
        if (record != null) {
            return true;
        }
        return false;
    }

    public boolean uploadPharmacyDuty(int dutyId, int drId, String date) {
        Record record = new Record().set("dr_id", drId).set("id", dutyId).set("duty_time", date);
        System.out.println(record.toString());
        return  Db.update("hms_pharmacy_duty", record);
    }

    public boolean addPharmacyDuty(int drId, String date) {
        Record record = new Record().set("duty_time", date).set("dr_id", drId);
        return Db.save("hms_pharmacy_duty", record);
    }

/*------------------------------------------------------------------------------------*/
    /**
     *
     * @return
     */
    public List<Record> _getTestDuty() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now = sdf.format(new Date());
        //System.out.println(now);
        String sql = "SELECT id, dr_id, DATE(duty_time) AS duty_time FROM hms_test_duty WHERE DATE(duty_time) >= ?";
        return Db.find(sql, now);
    }

    public List<String> getTestDuty() {
        List<Record> records = _getTestDuty();
        List<String> strings = new ArrayList<>();
        if (records == null) {
            strings.add("");
            return strings;
        }

        for (Record record : records) {
            strings.add(record.toJson());
        }
        return strings;
    }

    public boolean isDateExistForTest(String date) {
        String sql = "SELECT id FROM hms_test_duty WHERE DATE(duty_time) = ?";
        Record record = Db.findFirst(sql, date);
        if (record != null) {
            return true;
        }
        return false;
    }

    public boolean updateTestDuty(int dutyId, int drId, String date) {
        Record record = new Record().set("dr_id", drId).set("id", dutyId).set("duty_time", date);
        System.out.println(record.toString());
        return  Db.update("hms_test_duty", record);
    }

    public boolean addTestDuty(int drId, String date) {
        Record record = new Record().set("duty_time", date).set("dr_id", drId);
        return Db.save("hms_test_duty", record);
    }


    /*------------------------------------------------------------------------------------*/
    public List<Record> _getRegDuty() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now = sdf.format(new Date());
        //System.out.println(now);
        String sql = "SELECT id, dr_id, DATE(duty_time) AS duty_time FROM hms_reg_duty WHERE DATE(duty_time) >= ?";
        return Db.find(sql, now);
    }

    public List<String> getRegDuty() {
        List<Record> records = _getRegDuty();
        List<String> strings = new ArrayList<>();
        if (records == null) {
            strings.add("");
            return strings;
        }

        for (Record record : records) {
            strings.add(record.toJson());
        }
        return strings;
    }

    public boolean isDateExistForReg(String date) {
        String sql = "SELECT id FROM hms_reg_duty WHERE DATE(duty_time) = ?";
        Record record = Db.findFirst(sql, date);
        if (record != null) {
            return true;
        }
        return false;
    }

    public boolean updateRegDuty(int dutyId, int drId, String date) {
        Record record = new Record().set("dr_id", drId).set("id", dutyId).set("duty_time", date);
        System.out.println(record.toString());
        return  Db.update("hms_reg_duty", record);
    }

    public boolean addRegDuty(int drId, String date) {
        Record record = new Record().set("duty_time", date).set("dr_id", drId);
        return Db.save("hms_reg_duty", record);
    }

    /*-----------------------------------------医生：病房-------------------------------------*/
    public boolean isExistForPha(int drId, String date) {
        String sql = "SELECT * FROM hms_pharmacy_duty WHERE dr_id = ? AND DATE(duty_time) = ?";
        Record record = Db.findFirst(sql, drId, date);
        return record != null;
    }

    /*-----------------------------------------医生：病房-------------------------------------*/
    public boolean isExistForTest(int drId, String date) {
        String sql = "SELECT * FROM hms_test_duty WHERE dr_id = ? AND DATE(duty_time) = ?";
        Record record = Db.findFirst(sql, drId, date);
        return record != null;
    }

}
