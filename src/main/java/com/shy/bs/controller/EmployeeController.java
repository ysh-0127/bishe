package com.shy.bs.controller;


import com.shy.bs.pojo.Employee;
import com.shy.bs.service.EmployeeService;
import com.shy.bs.util.ServerResponse;
import com.shy.bs.vo.EmployeeQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author night
 * @date 2022/10/22 18:29
 */
@Api(tags = "员工控制器")
@Slf4j
@RestController
@RequestMapping("employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @ApiOperation("添加雇员")
    @PostMapping("addEmployee")
    public ServerResponse addEmployee(Employee employee) {
        return employeeService.addEmployee(employee);
    }

    @ApiOperation("获取员工列表")
    @RequestMapping(value = "getList", method = RequestMethod.GET)
    public ServerResponse getList(EmployeeQuery employeeQuery) {

        return employeeService.getList(employeeQuery);
    }

    @ApiOperation("更新员工信息")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ServerResponse update(Employee employee) {
        return employeeService.updateEmployee(employee);
    }
}
