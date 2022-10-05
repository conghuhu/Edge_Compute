package cn.cislc.dockerservice.mapper;

import cn.cislc.dockerservice.entity.Node;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author conghuhu
 * @since 2022-03-07
 */
@Repository
@Mapper
public interface NodeMapper extends BaseMapper<Node> {

}
