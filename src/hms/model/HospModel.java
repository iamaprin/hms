package hms.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * 医院管理系统：医院表Model
 * Created by iamaprin
 * on 2016/4/28.
 */
public class HospModel extends Model<HospModel> {
    public static final HospModel dao = new HospModel();

    /**
     * 增加医院管理账号信息
     * @param username  医院管理账号
     * @param password  医院管理密码
     * @param salt      医院密码加密的盐
     * @return 添加成功返回true，斗则返回false
     */
    public boolean addHosp(String username, String password, String salt) {
        return new HospModel()
                .put("hosp_username", username)
                .put("hosp_password", password)
                .put("hosp_salt", salt)
                .save();
    }

    public Page<HospModel> queryAll(int pageNumber, int pageSize) {
        String select = "SELECT id, hosp_username";
        String sqlExceptSelect = "FROM hms_hosp";

        return paginate(pageNumber, pageSize, select, sqlExceptSelect);
    }

    public HospModel queryHosp(String username) {
        String sql = "SELECT * FROM hms_hosp WHERE hosp_username = ?";
        return findFirst(sql, username);
    }
}
