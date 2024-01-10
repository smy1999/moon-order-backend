package com.moon.mapper;

import com.moon.dto.GoodsSalesDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReportMapper {

    /**
     * 根据日期,状态计算当日营业额
     * @param param
     * @return
     */
    Double sumTurnoverByMap(Map<String, Object> param);

    /**
     * 根据日期计算用户数
     * @param param
     * @return
     */
    Integer sumUserByMap(Map<String, Object> param);

    /**
     * 根据日期,状态查订单
     * @param param
     * @return
     */
    Integer sumOrderByMap(Map<String, Object> param);

    /**
     * 查询销量top10
     * @param param
     * @return
     */
    List<GoodsSalesDTO> findTopSales(Map<String, Object> param);
}
