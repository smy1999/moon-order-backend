package com.moon.service;

import com.moon.dto.EmployeeLoginDTO;
import com.moon.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

}
