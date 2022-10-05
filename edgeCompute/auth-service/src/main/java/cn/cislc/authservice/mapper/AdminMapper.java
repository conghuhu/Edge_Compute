package cn.cislc.authservice.mapper;

import cn.cislc.authservice.entity.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author conghuhu
 * @since 2022-03-10
 */
@Repository
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

}
