package me.mason.springbatch.dao.local;

import me.mason.springbatch.entity.DataFlow;
import org.beetl.sql.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: WYS
 * @time: 2022/3/5 14:40
 */

@Repository
public interface DataFlowRepository extends BaseMapper<DataFlow> {
    @Override
    List<DataFlow> all();
}
