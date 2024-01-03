package com.moon.service;

import com.moon.dto.EmployeeDTO;
import com.moon.dto.EmployeeLoginDTO;
import com.moon.entity.Employee;

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

}
