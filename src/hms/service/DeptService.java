package hms.service;

import hms.model.DeptModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iamaprin
 * on 2016/5/4.
 */
public class DeptService {

    public static List<String> getAll() {
        List<DeptModel> models = DeptModel.dao.getAll();
        if (models == null) {
            return null;
        }

        List<String> strings = new ArrayList<>();
        for (DeptModel model : models) {
            strings.add(model.toJson());
        }
        return strings;
    }
}
