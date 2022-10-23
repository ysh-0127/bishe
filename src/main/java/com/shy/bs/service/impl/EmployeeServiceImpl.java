package com.shy.bs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.bs.mapper.EmployeeMapper;
import com.shy.bs.pojo.Employee;
import com.shy.bs.service.EmployeeService;
import com.shy.bs.util.Const;
import com.shy.bs.util.ServerResponse;
import com.shy.bs.vo.EmployeeQuery;
import com.shy.bs.vo.ListVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author night
 * @date 2022/10/21 20:34
 */
@Slf4j
@Transactional
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    @Override
    public ServerResponse<Employee> login(Integer userId, String password) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("id", userId);
        queryWrapper.eq("password", password);
        Employee employee = baseMapper.selectOne(queryWrapper);
        if (null == employee) {
            return ServerResponse.createByErrorMessage("用户名或密码错误");
        }
        employee.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", employee);

    }

    @Override
    public ServerResponse updateEmployee(Employee employee) {
        UpdateWrapper<Employee> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", employee.getId());
        int resultCount = baseMapper.update(employee, updateWrapper);
        if (resultCount > 0) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("更新失败");
    }

    @Override
    public ServerResponse getList(EmployeeQuery employeeQuery) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .like(employeeQuery.getId() != null, "id", employeeQuery.getId())
                .like(employeeQuery.getName() != null, "name", employeeQuery.getName())
                .like(employeeQuery.getIdCard() != null, "id_card", employeeQuery.getIdCard())
                .like(employeeQuery.getPhone() != null, "phone", employeeQuery.getPhone())
                .eq(employeeQuery.getStatus() != null, "status", employeeQuery.getStatus());

        // 设置排序规则
        queryWrapper.orderByAsc("id");
        Page<Employee> pageParam = new Page<>(employeeQuery.getPage(), employeeQuery.getLimit());

        IPage<Employee> pageRs = baseMapper.selectPage(pageParam, queryWrapper);

        List<Employee> list = pageRs.getRecords();
        if (list != null) {
            ListVo listVo = new ListVo();
            listVo.setItems(list);
            listVo.setTotal(pageRs.getTotal());
            return ServerResponse.createBySuccess(listVo);
        }
        return ServerResponse.createByErrorMessage("获取客户列表失败");
    }

    @Override
    public ServerResponse addEmployee(Employee employee) {
        employee.setId(createEmployeeId());
        employee.setRole(Const.Number.ONE);
        int resultCount = baseMapper.insert(employee);
        if (resultCount != 0) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("添加失败");
    }

    @Override
    public ServerResponse validPassword(Long id, String validPass) {
        Employee employee = baseMapper.selectById(id);
        if (employee.getPassword().equals(validPass)) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    /**
     * 客户编号
     * 格式为：yyMM 加 三位递增的数字，数字每月重置为1
     *
     * @return
     */
    private Integer createEmployeeId() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM");
        String format = dateFormat.format(new Date()) + "000";
        return Integer.valueOf(format) + (num++);
    }

    private int num = 1;

    @Scheduled(cron = "0 0 0 0 * ?")
    private void clearNum() {
        num = 1;
    }
}


