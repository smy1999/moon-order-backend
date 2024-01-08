package com.moon.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.moon.constant.MessageConstant;
import com.moon.constant.StatusConstant;
import com.moon.dto.SetmealDTO;
import com.moon.dto.SetmealPageQueryDTO;
import com.moon.entity.Dish;
import com.moon.entity.Setmeal;
import com.moon.entity.SetmealDish;
import com.moon.exception.DeletionNotAllowedException;
import com.moon.exception.SetmealEnableFailedException;
import com.moon.mapper.DishMapper;
import com.moon.mapper.SetmealDishMapper;
import com.moon.mapper.SetmealMapper;
import com.moon.result.PageResult;
import com.moon.service.SetmealService;
import com.moon.vo.DishItemVO;
import com.moon.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional
    public void add(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.add(setmeal);

        Long setmealId = setmeal.getId();
        List<SetmealDish> dishes = setmealDTO.getSetmealDishes();
        dishes.forEach(dish -> dish.setSetmealId(setmealId));
        setmealDishMapper.addBatch(dishes);

    }

    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        List<SetmealVO> setmeals = setmealMapper.findAllWithCategoryName(setmealPageQueryDTO);

        PageInfo<SetmealVO> pageInfo = new PageInfo<>(setmeals);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DELETE_NULL);
        }
        List<Integer> statuses = setmealMapper.getStatusesByIds(ids);
        if (statuses.contains(StatusConstant.ENABLE)) {
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }
        setmealMapper.deleteBatch(ids);
        setmealDishMapper.deleteBatchBySetmealId(ids);
    }

    @Override
    public SetmealVO getById(Long id) {
        SetmealVO setmealVO = new SetmealVO();

        Setmeal setmeal = setmealMapper.getById(id);
        BeanUtils.copyProperties(setmeal, setmealVO);

        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);

        Long setmealId = setmeal.getId();
        List<Long> setmealIds = new ArrayList<>();
        setmealIds.add(setmealId);
        setmealDishMapper.deleteBatchBySetmealId(setmealIds);

        List<SetmealDish> dishes = setmealDTO.getSetmealDishes();
        dishes.forEach(dish -> dish.setSetmealId(setmealId));
        setmealDishMapper.addBatch(setmealDTO.getSetmealDishes());
    }

    @Override
    public void changeStatus(Long id, Integer status) {
        if (status == StatusConstant.ENABLE) {
            List<Dish> dishes = dishMapper.findBySetmealId(id);
            if (dishes != null && !dishes.isEmpty()) {
                dishes.forEach(dish -> {
                    if (dish.getStatus() == StatusConstant.DISABLE) {
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }

    @Override
    public List<Setmeal> findList(Long categoryId) {
        String key = "dish_" + categoryId;
        List<Setmeal> setmeals = (List<Setmeal>) redisTemplate.opsForValue().get(key);
        if (setmeals != null) {
            return setmeals;
        }
        setmeals = setmealMapper.findByCategoryId(categoryId);
        redisTemplate.opsForValue().set(key, setmeals);

        return setmeals;

    }

    @Override
    public List<DishItemVO> findDishItems(Long setmealId) {
        return setmealDishMapper.findBySetmealId(setmealId);
    }
}
