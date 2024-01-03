package com.moon.mapper;

import com.moon.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);


    /**
     * 添加新用户
     *
     * @param employee
     */
    @Insert("""
            INSERT INTO employee
            (name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user)
            VALUES (#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})
            """)
    void addEmployee(Employee employee);


    /**
     * 根据name查询用户
     * @param name
     * @return
     */
//    @Select("""
//            SELECT * FROM employee WHERE name LIKE '%${name}%'
//            """)
    List<Employee> getEmployeeByName(@Param("name") String name);

}
