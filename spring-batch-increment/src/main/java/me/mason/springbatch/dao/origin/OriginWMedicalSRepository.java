package me.mason.springbatch.dao.origin;

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
 * @time: 2022/3/6 13:47
 */
@Repository
public interface OriginWMedicalSRepository extends BaseMapper<WMedicalS> {
    List<WMedicalS> getOriginWMedicalS(Map<String, Object> params);

    List<WMedicalS> getOriginIncreWMedicalS(Map<String, Object> params);

    /**
     * 查询数据最新时间
     *
     * @return
     */
    @Sql(value = "select max(sys_update_time) from west_medical_storehouse")
    Date selectMaxUpdateTime();
}
