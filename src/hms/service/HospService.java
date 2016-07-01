package hms.service;

import hms.model.HospModel;
import hms.util.SecurityUtil;
import org.apache.log4j.Logger;

/**
 * Created by iamaprin
 * on 2016/5/2.
 */
public class HospService {

    private static final Logger log = Logger.getLogger(HospService.class);

    public static boolean validate(String username, String password) {
        HospModel model = HospModel.dao.queryHosp(username);
        if (model == null) {
            log.info("user: " + username + " | msg: model is null");
            return false;
        }
        String pass = model.get("hosp_password", "");
        String salt = model.get("hosp_salt", "");

        if ("".equals(password) || "".equals(salt)) {
            log.info("user: " + username + " | msg: password or salt is null");
            return false;
        }

        String encodePassword = SecurityUtil.SHA256Encode(password, salt);
        if (encodePassword.equals(pass)) {
            log.info("user: " + username + " | msg： password correct");
            return true;
        }

        log.info("user: " + username + " | msg: password incorrect");
        return false;
    }
}
