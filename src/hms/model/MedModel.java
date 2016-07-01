package hms.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * author: iamaprin
 * time: 2016/5/8 21:58
 */
public class MedModel extends Model<MedModel> {

    public static final MedModel dao = new MedModel();


    /**
     * 获取药物主类别
     * @return Record
     */
    private List<Record> _getMedCat0() {
        String sql = "SELECT id, cat_name FROM hms_med_cat_0";
        return Db.find(sql);
    }

    /**
     * 获取药物主类别
     * @return String
     */
    public List<String> getMedCat0() {
        List<Record> records = _getMedCat0();
        List<String> strings = new ArrayList<>();

        if (records == null) {
            strings.add("[empty]");
            return strings;
        }

        for (Record record : records) {
            strings.add(record.toJson());
        }
        return strings;
    }

    /**
     * 获取药物次类别
     * @param superId 父类别id
     * @return Record
     */
    public List<Record> _getMedCat1(int superId) {
        String sql = "SELECT id, cat_name FROM hms_med_cat_1 WHERE super_cat_id = ?";
        return Db.find(sql, superId);
    }

    /**
     * 获取药物次类别
     * @param superId 父类别id
     * @return Record
     */
    public List<String> getMedCat1(int superId) {
        List<Record> records = _getMedCat1(superId);
        List<String> strings = new ArrayList<>();

        if (records == null) {
            strings.add("[empty]");
            return strings;
        }

        for (Record record : records) {
            strings.add(record.toJson());
        }
        return strings;
    }


    public List<Record> _getMed(int cat1Id) {
        String sql = "SELECT id, med_name FROM hms_med WHERE super_cat_id = ?";
        return Db.find(sql, cat1Id);
    }

    public List<String> getMed(int cat1Id) {
        List<Record> records = _getMed(cat1Id);
        List<String> strings = new ArrayList<>();

        if (records == null) {
            strings.add("[empty]");
            return strings;
        }

        for (Record record : records) {
            strings.add(record.toJson());
        }
        return strings;
    }

    public boolean addMed(int superId, String medName, String medUse) {
        return new MedModel().set("med_name", medName).set("super_cat_id", superId)
                .set("med_use", medUse).save();
    }


    /**
     * 根据药品id获取药物名称
     * @param medId 药物id
     * @return .
     */
    public String getMedName(int medId) {
        MedModel model = findById(medId);
        if (model == null) {
            return "";
        }
        return model.get("med_name");
    }

}
