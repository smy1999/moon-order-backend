package com.moon.service;

import com.moon.dto.EmployeeDTO;
import com.moon.dto.EmployeeLoginDTO;
import com.moon.dto.EmployeePageQueryDTO;
import com.moon.entity.Employee;
import com.moon.result.PageResult;

import java.util.Map;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 添加员工
     * @param employeeDTO
     */
    void addEmployee(EmployeeDTO employeeDTO);

    /**
     * 分页查询员工数据
     * @param employeePageQueryDTO
     * @return
     */
    PageResult getEmployeePage(EmployeePageQueryDTO employeePageQueryDTO);
}
