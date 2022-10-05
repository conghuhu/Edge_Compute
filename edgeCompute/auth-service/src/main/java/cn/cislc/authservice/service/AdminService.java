package cn.cislc.authservice.service;

import cn.cislc.authservice.entity.Admin;
import cn.cislc.authservice.param.RegisterParam;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author conghuhu
 * @since 2022-03-10
 */
public interface AdminService extends IService<Admin> {

    Admin queryUser(String username, String password);

    String register(RegisterParam registerParam);
}
