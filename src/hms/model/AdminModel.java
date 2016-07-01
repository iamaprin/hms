package hms.model;

import com.jfinal.plugin.activerecord.Model;
import org.apache.log4j.Logger;

/**
 * Created by iamaprin
 * on 2016/4/29.
 */
public class AdminModel extends Model<AdminModel>{

    private static final Logger log = Logger.getLogger(AdminModel.class);
    public static AdminModel dao = new AdminModel();

    public AdminModel queryAdmin(String adminName) {
        String sql = "SELECT * FROM hms_admin WHERE admin_username = ?";
        return findFirst(sql, adminName);
    }

    public int queryIdbyName(String username) {
        String sql = "SELECT id FROM hms_admin WHERE admin_username = ?";
        AdminModel model = findFirst(sql, username);
        return model.getInt("id");
    }

    public boolean updateAdmin(String username, String newPass, String salt) {
        int id = queryIdbyName(username);
        boolean flag = new AdminModel().set("id", id).set("admin_password", newPass).set("admin_salt", salt).update();

        if (flag) {
            log.info("user: " + username + " | msg: update success");
        } else {
            log.info("user: " + username + " | msg: update failure");
        }

        return flag;
    }
}
