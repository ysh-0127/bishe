package com.shy.bs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.bs.pojo.Employee;
import com.shy.bs.util.ServerResponse;
import com.shy.bs.vo.EmployeeQuery;


/**
 * @author night
 * @date 2022/10/21 20:09
 */

public interface EmployeeService extends IService<Employee> {
    ServerResponse<Employee> login(Integer userId, String password);

    ServerResponse updateEmployee(Employee employee);

    ServerResponse validPassword(Long id, String validPass);

    ServerResponse getList(EmployeeQuery employeeQuery);

    ServerResponse addEmployee(Employee employee);


}
