package cn.cislc.authservice.service.impl;

import cn.cislc.authservice.entity.Admin;
import cn.cislc.authservice.mapper.AdminMapper;
import cn.cislc.authservice.param.RegisterParam;
import cn.cislc.authservice.service.AdminService;
import cn.cislc.authservice.service.AuthService;
import cn.cislc.authservice.utils.JwtTokenUtil;
import cn.cislc.authservice.utils.MD5UUIDSaltUtil;
import cn.cislc.authservice.utils.RedisUtil;
import cn.itcast.feign.pojo.AdminInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author conghuhu
 * @since 2022-03-10
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    private final AdminMapper adminMapper;

    private final JwtTokenUtil jwtTokenUtil;

    private final RedisUtil redisUtil;

    public AdminServiceImpl(AdminMapper adminMapper, JwtTokenUtil jwtTokenUtil, RedisUtil redisUtil) {
        this.adminMapper = adminMapper;
        this.jwtTokenUtil = jwtTokenUtil;
        this.redisUtil = redisUtil;
    }

    @Override
    public Admin queryUser(String username, String password) {
        Admin admin = adminMapper.selectOne(new LambdaQueryWrapper<Admin>().eq(Admin::getUsername, username));
        if (admin == null) {
            return null;
        }
        String md5Code = MD5UUIDSaltUtil.createMd5Code(password + admin.getSalt());
        if (admin.getPassword().equals(md5Code)) {
            return admin;
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String register(RegisterParam registerParam) {
        String username = registerParam.getUsername();
        String password = registerParam.getPassword();
        String email = registerParam.getEmail();
        Admin admin = new Admin();
        try {
            Integer selectCount = adminMapper.selectCount(new LambdaQueryWrapper<Admin>().eq(Admin::getUsername, username));
            if (selectCount > 0) {
                return null;
            }
            String salt = MD5UUIDSaltUtil.getSalt();
            String newPassword = MD5UUIDSaltUtil.createMd5Code(password + salt);
            admin.setUsername(username);
            admin.setSalt(salt);
            admin.setPassword(newPassword);
            admin.setAdmin(false);
            admin.setStatus(true);
            admin.setCreated(LocalDateTime.now());
            admin.setLastLogin(LocalDateTime.now());
            admin.setEmail(email);
            adminMapper.insert(admin);
            return AuthService.getToken(username, password, admin, jwtTokenUtil, redisUtil);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
