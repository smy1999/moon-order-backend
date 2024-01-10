package com.moon.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.moon.constant.MessageConstant;
import com.moon.constant.PasswordConstant;
import com.moon.constant.StatusConstant;
import com.moon.context.BaseContext;
import com.moon.dto.EmployeeDTO;
import com.moon.dto.EmployeeLoginDTO;
import com.moon.dto.EmployeePageQueryDTO;
import com.moon.dto.PasswordEditDTO;
import com.moon.entity.Employee;
import com.moon.exception.AccountLockedException;
import com.moon.exception.AccountNotFoundException;
import com.moon.exception.PasswordEditFailedException;
import com.moon.exception.PasswordErrorException;
import com.moon.mapper.EmployeeMapper;
import com.moon.result.PageResult;
import com.moon.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    @Override
    public void addEmployee(EmployeeDTO employeeDTO) {

        Employee employee = new Employee();

        BeanUtils.copyProperties(employeeDTO, employee);

        employee.setStatus(StatusConstant.ENABLE);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        Long empId = BaseContext.getCurrentId();
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        employeeMapper.addEmployee(employee);
    }

    @Override
    public PageResult getEmployeePage(EmployeePageQueryDTO employeePageQueryDTO) {

        //TODO: 如果在第二页查询第一页中含有的结果,会无法显示,因为是查询第二页,但人数少于一页,第二页为空
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        List<Employee> employees = employeeMapper.getEmployeeByName(employeePageQueryDTO.getName());
        PageInfo<Employee> pageInfo = new PageInfo<>(employees);

        PageResult data = new PageResult();
        data.setTotal(pageInfo.getTotal());
        data.setRecords(pageInfo.getList());

        return data;
    }

    @Override
    public void changeStatus(Long id, Integer status) {
        Employee employee = Employee.builder()
                .id(id)
                .status(status)
                .build();

        employeeMapper.updateEmployee(employee);

    }

    @Override
    public Employee findEmployeeById(Long id) {
        Employee employee = employeeMapper.getById(id);
        employee.setPassword("");
        return employee;
    }

    @Override
    public void editEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        employeeMapper.updateEmployee(employee);
    }

    @Override
    public void editPassword(PasswordEditDTO passwordEditDTO) {
        Long EmpId = BaseContext.getCurrentId();
        Employee employeeDB = employeeMapper.getById(EmpId);
        if (employeeDB == null) {
            throw new PasswordEditFailedException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        if (!employeeDB.getPassword().equals(DigestUtils.md5DigestAsHex(passwordEditDTO.getOldPassword().getBytes()))) {
            throw new PasswordEditFailedException(MessageConstant.PASSWORD_ERROR);
        }
        Employee employee = Employee.builder()
                .id(EmpId)
                .password(DigestUtils.md5DigestAsHex(passwordEditDTO.getNewPassword().getBytes()))
                .build();
        employeeMapper.updateEmployee(employee);
    }

}
