package hms.service;

import hms.model.DrModel;
import hms.util.SecurityUtil;
import org.apache.log4j.Logger;

/**
 * author: iamaprin
 * time: 2016/5/6 8:30
 */
public class DrService {

    private static final Logger log = Logger.getLogger(DrService.class);


    /**
     * 添加医生数据
     * @param drName    医生姓名
     * @param drSex     医生性别
     * @param deptId    所属科室id
     * @param username  用户名
     * @param password  密码
     * @return
     */
    public static boolean addData(String drName, String drSex, int deptId, String username, String password) {

        if (DrModel.dao.isUserExist(username)) {
            log.info("user: " + username + " | msg: username already exists");
            return false;
        }

        String salt = SecurityUtil.generateSalt(32);
        log.info("user: " + username + " | msg: salt = " + salt);
        String encodePass = SecurityUtil.SHA256Encode(password, salt);
        log.info("user: " + username + " | msg: encodePass = " + encodePass);

        return DrModel.dao.add(drName, drSex, deptId, username, encodePass, salt);
    }

    /**
     * 验证密码
     * @param username  用户名
     * @param password  密码
     * @return  验证成功返回true，否则返回false
     */
    public static boolean verifyPassword(String username, String password) {
        DrModel model = DrModel.dao.get(username);
        if (model == null) {
            log.info("msg: username don't exists");
            return false;
        }

        String encodePass = model.getStr("dr_password");
        String salt = model.getStr("dr_salt");
        if (SecurityUtil.SHA256Encode(password, salt).equals(encodePass)) {
            log.info("user: " + username + " | msg： password correct");
            return true;
        }

        log.info("user: " + username + " | msg： password incorrect");
        return false;
    }
}
