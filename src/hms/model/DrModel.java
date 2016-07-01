package hms.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * author: iamaprin
 * time: 2016/5/6 8:31
 */
public class DrModel extends Model<DrModel>{

    public static final DrModel dao = new DrModel();

    /**
     * 添加医生数据
     * @param drName    医生姓名
     * @param drSex     医生性别
     * @param deptId    所属科室id
     * @param username  用户名
     * @param password  加密后的密码
     * @param salt      加密的盐
     * @return 添加成功返回true，否则返回false
     */
    public boolean add(String drName, String drSex, int deptId, String username, String password, String salt) {
        return new DrModel().set("dr_name", drName)
                .set("dr_sex", drSex)
                .set("dept_id", deptId)
                .set("dr_username", username)
                .set("dr_password", password)
                .set("dr_salt", salt)
                .save();
    }

    /**
     * 判断用户名是否存在
     * @param username  用户名
     * @return  已存在返回true，否则返回false
     */
    public boolean isUserExist(String username) {
        String sql = "SELECT id FROM hms_dr WHERE dr_username = ?";
        if (findFirst(sql, username) != null) {
            return true;
        }
        return false;
    }

    /**
     * 根据用户名获取加密密码和加密的盐
     * @param username  用户名
     * @return .
     */
    public DrModel get(String username) {
        String sql = "SELECT dr_password, dr_salt FROM hms_dr WHERE dr_username = ?";
        return findFirst(sql, username);
    }

    /**
     * 根据科室查找其下的医生
     * @param deptId    科室id
     * @return .
     */
    public List<Integer> getIdByDeptId(int deptId) {
        String sql = "SELECT id FROM hms_dr WHERE dept_id = ?";
        List<DrModel> models = find(sql, deptId);
        List<Integer> result = new ArrayList<>();
        for (DrModel model : models) {
            result.add(model.getInt("id"));
        }
        return result;
    }

    /**
     * 根据用户名查询医生id
     * @param username 用户名
     * @return .
     */
    public int getIdByUsername(String username) {
        String sql = "SELECT id FROM hms_dr WHERE dr_username = ?";
        DrModel model = findFirst(sql, username);
        if (model == null) {
            return -1;
        }
        return model.getInt("id");
    }

    /**
     * 根据医生id获取医生姓名
     * @param id  医生id
     * @return .
     */
    public String getNameById(int id) {
        DrModel model = findById(id);
        if (model == null) {
            return "";
        }
        return model.getStr("dr_name");
    }

    public int getDeptId(int id) {
        DrModel model = findById(id);
        if (model == null) {
            return -1;
        }
        return model.getInt("dept_id");
    }

    public List<DrModel> _getPharmacyDr() {
        String sql = "SELECT id, dr_name FROM hms_dr WHERE dept_id = 34";
        return find(sql);
    }

    public List<String> getPharmacyDr() {
        List<DrModel> models = _getPharmacyDr();
        List<String> strings = new ArrayList<>();
        if (models == null) {
            strings.add("");
            return strings;
        }
        for (DrModel model : models) {
            strings.add(model.toJson());
        }
        return strings;
    }

    public List<String> getTestDr() {
        String sql = "SELECT id, dr_name FROM hms_dr WHERE dept_id = 35";
        List<DrModel> models = find(sql);
        List<String> strings = new ArrayList<>();
        if (models == null) {
            strings.add("");
            return strings;
        }

        for (DrModel model : models) {
            strings.add(model.toJson());
        }
        return strings;
    }

    public List<String> getRegDr() {
        String sql = "SELECT id, dr_name FROM hms_dr WHERE dept_id = 36";
        List<DrModel> models = find(sql);
        List<String> strings = new ArrayList<>();
        if (models == null) {
            strings.add("");
            return strings;
        }

        for (DrModel model : models) {
            strings.add(model.toJson());
        }
        return strings;
    }

    public Page<DrModel> _getDr(int pageNumber, int pageSize) {
        String select = "SELECT id, dr_name, dr_sex, dept_id";
        String sqlExceptSelect = "FROM hms_dr";
        return paginate(pageNumber, pageSize, select, sqlExceptSelect);
    }

    public List<String> getDr(int pageNumber, int pageSize) {
        Page<DrModel> page = _getDr(pageNumber, pageSize);
        int totalPage = page.getTotalPage();

        List<DrModel> models = page.getList();
        List<String> strings = new ArrayList<>();
        strings.add(String.valueOf(totalPage));
        for (DrModel model : models) {
            strings.add(model.toJson());
        }
        return strings;
    }

    /**
     * 更新医生科室
     * @param drId      医生id
     * @param deptId    新的科室id
     * @return .
     */
    public boolean updateDrDept(int drId, int deptId) {
        return new DrModel().set("id", drId).set("dept_id", deptId).update();
    }
}
