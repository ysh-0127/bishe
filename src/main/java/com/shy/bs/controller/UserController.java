package com.shy.bs.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shy.bs.pojo.Employee;
import com.shy.bs.service.EmployeeService;
import com.shy.bs.util.JwtHelper;
import com.shy.bs.util.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author night
 * @date 2022/10/22 13:18
 */
@Api(tags = "用户控制器")
@Slf4j
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private EmployeeService employeeService;

    @ApiOperation("登录")
    @PostMapping("login")
    public ServerResponse login(String employeeId, String password) {
        ServerResponse response = employeeService.login(Integer.valueOf(employeeId), password);
        if (response.isSuccess()) {
            Map<String, String> map = new HashMap<>(1);
            map.put("token", JwtHelper.createToken(Long.valueOf(employeeId)));
            response = ServerResponse.createBySuccess(map);
        }
        log.info("userId:{}, password:{}, data:{}", employeeId, password, response.getData());
        return response;
    }

    @GetMapping("logout")
    public ServerResponse logout() {
        log.info("data:{}", "用户退出");
        return ServerResponse.createBySuccess();
    }
    @ApiOperation("获取登录信息")
    @PostMapping("info")
    public ServerResponse info(String token) {
        boolean isEX = JwtHelper.isExpiration(token);
        if (isEX) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
        }

        Long userId = JwtHelper.getUserId(token);
        Employee Employee = employeeService.getById(userId.intValue());
        return ServerResponse.createBySuccess(Employee);
    }

    @ApiOperation("更新雇员信息")
    @PostMapping("updateMessage")
    public ServerResponse updateMessage(Employee employee) {
        return employeeService.updateEmployee(employee);
    }

    @ApiOperation("验证密码")
    @PostMapping("validPassword")
    public ServerResponse validPassword(String token, String validPass) {
        Long employeeId = JwtHelper.getUserId(token);
        return employeeService.validPassword(employeeId, validPass);
    }

    @ApiOperation("修改密码")
    @PostMapping("updatePassword")
    public ServerResponse updatePassword(String token, String oldPass, String newPass) {
        Long id =JwtHelper.getUserId(token);
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id).eq("password", oldPass);
        Employee employee = employeeService.getOne(queryWrapper);
        if (null != employee) {
            employee.setPassword(newPass);
            employeeService.saveOrUpdate(employee);
        } else {
            return ServerResponse.createByErrorMessage("更新失败");
        }
        return ServerResponse.createBySuccess();
    }


}
