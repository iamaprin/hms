package hms.controller;

import com.jfinal.core.Controller;
import hms.service.AdminService;
import org.apache.log4j.Logger;

/**
 * Created by iamaprin
 * on 2016/4/29.
 */
public class AdminController extends Controller{

    private static final Logger log = Logger.getLogger(AdminController.class);

    public void index() {
        render("/html/admin/login.html");
    }

    public void login() {
        String adminName = getPara("adminName", "");
        String adminPass = getPara("adminPass", "");

        if ("".equals(adminName) || "".equals(adminPass)) {
            renderJson(false);
            return;
        }

        boolean flag = AdminService.isExist(adminName, adminPass);

        if (flag) {
            setCookie("adminIsLogin", "true", 60 * 60);
            setCookie("username", adminName, 60 * 60);
        }

        renderJson(flag);
    }

    public void manage() {
        String adminIsLogin = getCookie("adminIsLogin", "false");
        log.info("adminIsLogin = " + adminIsLogin);

        if ("false".equals(adminIsLogin)) {
            log.info("Unauthorized user");
            renderNull();
            return;
        }

        render("/html/admin/manage.html");
    }

    public void listHosp() {
        int pageNumber = getParaToInt("pageNumber", 0);
        int pageSize = getParaToInt("pageSize", 0);

        if (pageNumber < 1 || pageSize < 1) {
            renderJson("");
            return;
        }

        renderJson(AdminService.listHosp(pageNumber, pageSize));
    }

    public void loginout() {
        String username = getCookie("username", "");

        setCookie("adminIsLogin", "false", 0);
        log.info(username + " has logined out");
        renderJson(true);
    }

    public void changePass() {
        String oldPass = getPara("oldPass", "");
        String newPass = getPara("newPass", "");
        String username = getCookie("username", "");
        log.info("Change pass user: " + username + " | oldPass: " + oldPass + " | newPass: " + newPass);

        if ("".equals(oldPass) || "".equals(newPass) || "".equals(username)) {
            log.info("username or oldPass or newPass is null");
            renderJson(false);
            return;
        }

        if (AdminService.isExist(username, oldPass)) {
            log.info("user: " + username + " | msg: oldPass is correct");
            boolean flag = AdminService.changePass(username, newPass);
            renderJson(flag);
            return;
        }

        renderJson(false);
    }

    public void addHosp() {
        String username = getPara("username", "");
        String password = getPara("password", "");

        log.info("msg: new user: " + username + "  pass:" + password);

        boolean flag = AdminService.addHosp(username, password);
        renderJson(flag);
    }
}
