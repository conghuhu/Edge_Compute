package me.mason.springbatch.dao.target;

import me.mason.springbatch.entity.Prescription;
import me.mason.springbatch.entity.WMedicalS;
import org.beetl.sql.core.annotatoin.Sql;
import org.beetl.sql.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: WYS
 * @time: 2022/3/6 18:25
 */
@Repository
public interface TargetPrescriptionRepository extends BaseMapper<Prescription> {
    /**
     * 查询数据最新时间
     *
     * @return
     */
    @Sql(value = "select max(sys_update_time) from prescription")
    Date selectMaxUpdateTime();


    List<Prescription> getTargetIncrePrescription(Map<String, Object> params);

}