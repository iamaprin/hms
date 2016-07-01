package hms.config;

import com.jfinal.config.*;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.ViewType;
import hms.controller.*;
import hms.model.*;

/**
 * 医院管理系统配置
 * Created by iamaprin
 * on 2016/4/28.
 */
public class HmsConfig extends JFinalConfig {
    @Override
    public void configConstant(Constants me) {
        PropKit.use("config.properties");
        me.setDevMode(PropKit.getBoolean("devMode"));
        me.setViewType(ViewType.FREE_MARKER);
    }

    @Override
    public void configRoute(Routes me) {
        me.add("/", HmsController.class);
        me.add("/admin", AdminController.class);
        me.add("/hospital", HospController.class);
        me.add("/doctor", DrController.class);
        me.add("/register", RegController.class);
        me.add("/patient", PatController.class);
    }

    @Override
    public void configPlugin(Plugins me) {
        String jdbcUrl = PropKit.get("hms.jdbcUrl").trim();
        String username = PropKit.get("hms.username").trim();
        String password = PropKit.get("hms.password").trim();

        C3p0Plugin c3p0Plugin = new C3p0Plugin(jdbcUrl, username, password);
        me.add(c3p0Plugin);

        ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
        me.add(arp);

        arp.addMapping("hms_hosp", HospModel.class);
        arp.addMapping("hms_admin", AdminModel.class);
        arp.addMapping("hms_dept", DeptModel.class);
        arp.addMapping("hms_dr", DrModel.class);
        arp.addMapping("hms_patient", PatModel.class);
        arp.addMapping("hms_queue", QueueModel.class);
        arp.addMapping("hms_reg", RegModel.class);
        arp.addMapping("hms_med", MedModel.class);
        arp.addMapping("hms_test", TestModel.class);
        arp.addMapping("hms_case_history", CaseModel.class);
        arp.addMapping("hms_ask", AskModel.class);
    }

    @Override
    public void configInterceptor(Interceptors me) {

    }

    @Override
    public void configHandler(Handlers me) {
        me.add(new ContextPathHandler("contextPath"));
    }
}
