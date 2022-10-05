package cn.cislc.authservice.controller;


import cn.cislc.authservice.param.RegisterParam;
import cn.cislc.authservice.service.AdminService;
import cn.cislc.cloudcommon.result.JsonResult;
import cn.cislc.cloudcommon.result.ResultTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author conghuhu
 * @since 2022-03-10
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/register")
    public JsonResult register(@RequestBody RegisterParam registerParam) {
        String token = adminService.register(registerParam);
        if (StringUtils.hasLength(token)) {
            return ResultTool.success(token);
        } else {
            return ResultTool.fail("注册失败，请检查账号，可能已存在");
        }
    }
}
