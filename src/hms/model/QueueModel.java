package hms.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * author: iamaprin
 * time: 2016/5/7 8:41
 */
public class QueueModel extends Model<QueueModel>{
    private static final Logger log = Logger.getLogger(QueueModel.class);
    public static final QueueModel dao = new QueueModel();

    /**
     * 添加队列数据
     * @param deptId    医生所属科室id
     * @param drId      医生id
     * @return 添加成功返回true，失败返回false
     */
    public boolean add(int deptId, int drId) {
        log.info("DB: hms_queue | [msg] " + "insert: dept_id = " + deptId + " dr_id = " + drId);
        return new QueueModel().set("dept_id", deptId).set("dr_id", drId).save();
    }

    /**
     * 查询给定科室下queue值最小的医生
     * @param deptId 科室id
     * @return 医生id
     */
    public int getDrId(int deptId) {
        String sql = "SELECT dr_id FROM hms_queue WHERE dept_id = ? ORDER BY queue ASC LIMIT 1";
        QueueModel model = findFirst(sql, deptId);
        if (model == null) {
            return -1;
        }
        return model.getInt("dr_id");
    }

    /**
     * 根据drId查询id
     * drId与id一一对应
     * @param drId 医生id
     * @return .
     */
    public int getId(int drId) {
        String sql = "SELECT id FROM hms_queue WHERE dr_id = ?";

        QueueModel model = findFirst(sql, drId);
        if (model == null) {
            return -1;
        }

        return model.getInt("id");
    }

    /**
     * 指定医生的队列数加一
     * 一般挂号操作时调用
     * @param drId  医生id
     * @return 更新成功返回true，否则返回false
     */
    public boolean plusOne(int drId) {
        int id = getId(drId);
        if (id == -1) {
            log.error("[drId: " + drId + "] not exists");
            return false;
        }
        QueueModel model = findById(id);
        if (model == null) {
            return false;
        }
        int queue = model.getInt("queue");
        return new QueueModel().set("id", id).set("queue", ++queue).update();
    }

    /**
     * 指定医生的队列数减一
     * 一般医生接诊完调用
     * @param drId  医生id
     * @return 更新成功返回true，否则返回false
     */
    public boolean minusOne(int drId) {
        int id = getId(drId);
        if (id == -1) {
            log.error("[drId: " + drId + "] not exists");
            return false;
        }
        QueueModel model = findById(id);
        if (model == null) {
            return false;
        }
        int queue = model.getInt("queue");
        if (queue == 0) {
            log.error("queue is 0");
            return false;
        }
        return new QueueModel().set("id", id).set("queue", --queue).update();
    }



    public int getQueueByDrId(int drId) {
        String sql = "SELECT queue FROM hms_queue WHERE dr_id = ?";
        return findFirst(sql, drId).getInt("queue");
    }


    /*------------------------病房排队队列------------------------*/
    public boolean addCaseForPha(int caseId, String medicine) {
        Record record = new Record().set("case_id", caseId).set("medicine", medicine);
        return Db.save("hms_pharmacy_queue", record);
    }

    public Page<Record> _getCaseForPha(int pageNumber, int pageSize) {
        String select = "SELECT id, case_id, medicine";
        String sqlExceptSelect = "FROM hms_pharmacy_queue WHERE status = 0";
        return Db.paginate(pageNumber, pageSize, select, sqlExceptSelect);
    }

    public List<String> getCaseForPha(int pageNumber, int pageSize) {
        Page<Record> page = _getCaseForPha(pageNumber, pageSize);
        List<String> strings = new ArrayList<>();
        if (page == null) {
            strings.add("");
            return strings;
        }

        List<Record> records = page.getList();
        for (Record record : records) {
            strings.add(record.toJson());
        }
        return strings;
    }

    public boolean updateStatusForPha(int id) {
        Record record = new Record().set("id", id).set("status", 1);
        return Db.update("hms_pharmacy_queue", record);
    }

    /*------------------------检验排队队列------------------------*/
    public boolean addCaseForTest(int caseId, String test) {
        Record record = new Record().set("case_id", caseId).set("test", test);
        return Db.save("hms_test_queue", record);
    }

    public Page<Record> _getCaseForTest(int pageNumber, int pageSize) {
        String select = "SELECT id, case_id, test";
        String sqlExceptSelect = "FROM hms_test_queue WHERE status = 0 OR has_result = 0";
        return Db.paginate(pageNumber, pageSize, select, sqlExceptSelect);
    }

    public List<String> getCaseForTest(int pageNumber, int pageSize) {
        Page<Record> page = _getCaseForTest(pageNumber, pageSize);
        List<String> strings = new ArrayList<>();
        if (page == null) {
            strings.add("");
            return strings;
        }

        List<Record> records = page.getList();
        for (Record record : records) {
            strings.add(record.toJson());
        }
        return strings;
    }

    public boolean updateStatusForTest(int id) {
        Record record = new Record().set("id", id).set("status", 1);
        return Db.update("hms_test_queue", record);
    }

    public boolean updateStatusForTest2(int id) {
        Record record = new Record().set("id", id).set("has_result", 1);
        return Db.update("hms_test_queue", record);
    }
}
