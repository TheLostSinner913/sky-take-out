package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.Result;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    Result add(EmployeeDTO employeeDTO);

    Result show(Integer page, Integer pageSize, String name);

    Result check(Long id);

    Result status(Long id, Integer status);

    void update(EmployeeDTO employeeDTO);

    Result resetPassword(PasswordEditDTO passwordEditDTO);
}
