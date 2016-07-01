package hms.model;

import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by iamaprin
 * on 2016/5/4.
 */
public class DeptModel extends Model<DeptModel>{
    public static final DeptModel dao = new DeptModel();

    /**
     * 获取所有科室
     * @return
     */
    public List<DeptModel> getAll() {
        String sql = "SELECT id, dept_name FROM hms_dept";
        return find(sql);
    }

    public String getDeptNameByid(int id) {
        DeptModel model = findById(id);
        if (model == null) {
            return "";
        }
        return model.getStr("dept_name");
    }
}
