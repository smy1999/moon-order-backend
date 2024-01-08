package com.moon.controller.admin;

import com.moon.dto.SetmealDTO;
import com.moon.dto.SetmealPageQueryDTO;
import com.moon.result.PageResult;
import com.moon.result.Result;
import com.moon.service.SetmealService;
import com.moon.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminSetmealController")
@Slf4j
@RequestMapping("/admin/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 修改状态
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> changeStatus(@PathVariable Integer status, @RequestParam Long id) {
        log.info("修改状态 {}: {}", status, id);
        setmealService.changeStatus(id, status);
        return Result.success();
    }

    /**
     * 修改菜品
     * @param setmealDTO
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改菜品 : {}", setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }


    /**
     * 根据 id 查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        log.info("根据id查询: {}", id);
        SetmealVO data = setmealService.getById(id);
        return Result.success(data);
    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteBatch(@RequestParam List<Long> ids) {
        log.info("删除套餐 : {}", ids);
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页查询套餐: {}", setmealPageQueryDTO);
        PageResult data = setmealService.page(setmealPageQueryDTO);
        return Result.success(data);
    }

    /**
     * 添加套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    public Result<String> add(@RequestBody SetmealDTO setmealDTO) {
        log.info("添加套餐: {}", setmealDTO);
        setmealService.add(setmealDTO);
        return Result.success();
    }


}
