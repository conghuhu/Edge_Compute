package cn.cislc.authservice.service;

import cn.cislc.authservice.entity.Admin;
import cn.cislc.authservice.param.LoginParam;
import cn.cislc.authservice.param.TokenParam;
import cn.cislc.authservice.utils.JwtTokenUtil;
import cn.cislc.authservice.utils.RedisUtil;
import cn.itcast.feign.pojo.AdminInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author conghuhu
 * @create 2022-03-10 13:20
 */
@Slf4j
@Service
public class AuthService {

    private final AdminService adminService;

    private final JwtTokenUtil jwtTokenUtil;

    private final RedisUtil redisUtil;

    public AuthService(AdminService adminService, JwtTokenUtil jwtTokenUtil, RedisUtil redisUtil) {
        this.adminService = adminService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.redisUtil = redisUtil;
    }

    public String authentication(LoginParam loginParam) {
        String username = loginParam.getUsername();
        String password = loginParam.getPassword();
        try {
            Admin admin = adminService.queryUser(username, password);
            if (admin == null) {
                return null;
            }
            return getToken(username, password, admin, jwtTokenUtil, redisUtil);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getToken(String username, String password, Admin admin, JwtTokenUtil jwtTokenUtil, RedisUtil redisUtil) {
        String token = jwtTokenUtil.createToken(username, password);
        AdminInfo adminInfo = new AdminInfo();
        BeanUtils.copyProperties(admin, adminInfo);
        redisUtil.set(token, adminInfo);
        redisUtil.setExpire(token, 60 * 60 * 24);
        return token;
    }

    public Boolean authService(TokenParam tokenParam) {
        AdminInfo adminInfo = (AdminInfo) redisUtil.get(tokenParam.getToken());
        if (adminInfo == null) {
            return false;
        }
        return true;
    }
}
