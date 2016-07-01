package hms.service;

import com.jfinal.plugin.activerecord.Page;
import hms.model.AdminModel;
import hms.model.HospModel;
import hms.util.SecurityUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iamaprin
 * on 2016/4/29.
 */
public class AdminService {
    private static final Logger log = Logger.getLogger(AdminService.class);

    public static boolean isExist(String adminName, String adminPass) {
        AdminModel model = AdminModel.dao.queryAdmin(adminName);

        if (model == null) {
            log.info("user: " + adminName + " | msg: model is null");
            return false;
        }
        String password = model.get("admin_password", "");
        String salt = model.get("admin_salt", "");

        if ("".equals(password) || "".equals(salt)) {
            log.info("user: " + adminName + " | msg: password or salt is null");
            return false;
        }

        String encodePassword = SecurityUtil.SHA256Encode(adminPass, salt);
        if (encodePassword.equals(password)) {
            log.info("user: " + adminName + " | msg： password correct");
            return true;
        }

        log.info("user: " + adminName + " | msg: password incorrect");
        return false;
    }

    public static List<String> listHosp(int pageNumber, int pageSize) {
        Page<HospModel> model = HospModel.dao.queryAll(pageNumber, pageSize);
        int totalPage = model.getTotalPage();

        List<HospModel> hospModels = model.getList();
        List<String> result = new ArrayList<>();
        result.add(String.valueOf(totalPage));

        for (HospModel hospModel : hospModels) {
            result.add(hospModel.toJson());
        }

        log.info("msg: result = " + result);
        return result;
    }

    public static boolean changePass(String username, String newPass) {
        String salt = SecurityUtil.generateSalt(32);
        log.info("user: " + username + " | msg: salt = " + salt);
        String encodePass = SecurityUtil.SHA256Encode(newPass, salt);
        log.info("user: " + username + " | msg: encodePass = " + encodePass);

        return AdminModel.dao.updateAdmin(username, encodePass, salt);
    }

    public static boolean addHosp(String username, String password) {

        if ("".equals(username) || "".equals(password)) {
            log.info("user: " + username + " | msg: username or password is null");
            return false;
        }

        String salt = SecurityUtil.generateSalt(32);
        log.info("user: " + username + " | msg: salt = " + salt);
        String encodePass = SecurityUtil.SHA256Encode(password, salt);
        log.info("user: " + username + " | msg: encodePass = " + encodePass);

        boolean flag = HospModel.dao.addHosp(username, encodePass, salt);
        if (flag) {
            log.info("user: " + username + " | msg: User successfully added");
            return true;
        }

        return false;
    }
}
