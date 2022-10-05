package cn.cislc.authservice.controller;

import cn.cislc.authservice.param.LoginParam;

import cn.cislc.authservice.param.TokenParam;
import cn.cislc.authservice.service.AuthService;
import cn.cislc.cloudcommon.result.JsonResult;
import cn.cislc.cloudcommon.result.ResultCode;
import cn.cislc.cloudcommon.result.ResultTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author conghuhu
 * @create 2022-03-10 13:13
 */
@Slf4j
@RestController
@RequestMapping("/authentication")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public JsonResult login(@RequestBody LoginParam loginParam) {
        String token = authService.authentication(loginParam);
        if (StringUtils.isEmpty(token)) {
            return ResultTool.fail(ResultCode.USER_CREDENTIALS_ERROR);
        }
        return ResultTool.success(token);
    }

    @PostMapping("/checkToken")
    public Boolean authService(@RequestBody TokenParam tokenParam){
        return authService.authService(tokenParam);
    }
}
