package hms.service;

import hms.model.QueueModel;

/**
 * author: iamaprin
 * time: 2016/5/7 7:00
 */
public class RegService {

    /**
     * 分配医生
     * 规则：该科室下接诊队列人数最少的医生
     * @param deptId 科室id
     * @return 分配的医生id
     */
    public static int assignDoctor(int deptId) {
        return QueueModel.dao.getDrId(deptId);
    }
}
