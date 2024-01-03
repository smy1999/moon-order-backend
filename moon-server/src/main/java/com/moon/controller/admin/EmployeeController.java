package com.moon.controller.admin;

import com.moon.constant.JwtClaimsConstant;
import com.moon.dto.EmployeeDTO;
import com.moon.dto.EmployeeLoginDTO;
import com.moon.dto.EmployeePageQueryDTO;
import com.moon.entity.Employee;
import com.moon.properties.JwtProperties;
import com.moon.result.PageResult;
import com.moon.result.Result;
import com.moon.service.EmployeeService;
import com.moon.utils.JwtUtil;
import com.moon.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;


    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Employee> findEmployeeById(@PathVariable Long id) {
        log.info("查询员工 id: {} ", id);
        Employee employee = employeeService.findEmployeeById(id);
        return Result.success(employee);
    }

    /**
     * 编辑员工信息
     * @param employeeDTO
     * @return
     */
    @PutMapping
    public Result<String> editEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("修改员工: {}", employeeDTO);
        employeeService.editEmployee(employeeDTO);

        return Result.success();
    }

    /**
     * 修改用户状态
     *
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> changeStatus(
            @RequestParam Long id,
            @PathVariable Integer status

    ) {
        log.info("修改员工 {} 状态 {}", id, status);

        employeeService.changeStatus(id, status);

        return Result.success();
    }


    /**
     * 分页查询员工数据
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> getEmployeePage(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询: {}", employeePageQueryDTO);

        PageResult pageResult = employeeService.getEmployeePage(employeePageQueryDTO);

        return Result.success(pageResult);
    }


    /**
     * 添加用户
     * @param employeeDTO
     * @return
     */
    @PostMapping
    public Result<String> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("添加员工: {}", employeeDTO);
        employeeService.addEmployee(employeeDTO);

        return Result.success();
    }


    /**
     * 登录
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

}
