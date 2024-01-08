package com.moon.mapper;


import com.moon.dto.ShoppingCartDTO;
import com.moon.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {


    /**
     * 添加到购物车
     * @param shoppingCart
     */
    @Insert("""
            INSERT INTO shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time)
            VALUES (#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{createTime})
            """)
    void add(ShoppingCart shoppingCart);

    /**
     * 更新购物车
     * @param shoppingCart
     */
    @Update("UPDATE shopping_cart SET amount = #{amount}, number = #{number} WHERE id = #{id}")
    void updateById(ShoppingCart shoppingCart);

    /**
     * 根据 ShoppingCart 中不为空的条件查询对应的内容
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> find(ShoppingCart shoppingCart);

    /**
     * 根据id删除
     * @param id
     */
    @Delete("DELETE FROM shopping_cart WHERE id = #{id}")
    void deleteById(Long id);

    /**
     * 根据 User Id 删除(清空购物车)
     * @param userId
     */
    @Delete("DELETE FROM shopping_cart WHERE user_id = #{userId}")
    void deleteByUserId(Long userId);
}
