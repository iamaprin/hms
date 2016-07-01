package hms.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * author: iamaprin
 * time: 2016/5/9 14:03
 */
public class TestModel extends Model<TestModel>{

    public static final TestModel dao = new TestModel();

    /**
     * 获取所有检验
     * @return .
     */
    public List<TestModel> _getTest() {
        String sql = "SELECT * FROM hms_test";
        return find(sql);
    }

    public List<String> getTest() {
        List<TestModel> models = _getTest();
        List<String> strings = new ArrayList<>();
        if (models == null) {
            strings.add("");
            return strings;
        }

        for (TestModel model : models) {
            strings.add(model.toJson());
        }
        return strings;
    }

    /**
     * 添加检验
     * @param testName 检验的名称
     * @return 添加成功返回true，否则返回false
     */
    public boolean addTest(String testName) {
        return new TestModel().set("test_name", testName).save();
    }

    /**
     * 根据id获取检验名称
     * @param id id
     * @return 检验名称
     */
    public String getTestName(int id) {
        TestModel model = findById(id);
        if (model == null) {
            return "";
        }
        return model.getStr("test_name");
    }

    public String getTestResult(int caseId) {
        String sql = "SELECT test_result FROM hms_test_result WHERE case_id = ?";
        Record record = Db.findFirst(sql, caseId);
        if (record == null) {
            return "";
        }
        return record.getStr("test_result");
    }

    /**
     * 添加检验结果
     * @param caseId 病历id
     * @param testResult 检验结果
     * @return .
     */
    public boolean addTestResult (int caseId, String testResult) {
        Record record = new Record().set("case_id", caseId).set("test_result", testResult);
        return Db.save("hms_test_result", record);
    }
}
